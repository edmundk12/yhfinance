package com.yahoo.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yahoo.URLManager;
import com.yahoo.dto.MarketPriceDTO;
import com.yahoo.entity.StockEntity;
import com.yahoo.entity.TransactionEntity;
import com.yahoo.exception.BusinessException;
import com.yahoo.line.Candle;
import com.yahoo.line.PriceLine;
import com.yahoo.line.PriceLineFactory;
import com.yahoo.line.PriceLine.LineType;
import com.yahoo.mapper.StockEntityMapper;
import com.yahoo.repository.jpa.PriceRepo;
import com.yahoo.repository.jpa.StockJpaRepo;
import com.yahoo.repository.jpa.TransactionRepo;
import com.yahoo.util.ApiResp;
import com.yahoo.util.CookieManager;
import com.yahoo.util.CrumbManager;
import com.yahoo.util.RedisHelper;

@Service
public class StockService {

  private final RedisTemplate<String, String> redisTemplate;

  private final RestTemplate restTemplate;

  public StockService(RedisTemplate<String, String> redisTemplate,
      RestTemplate restTemplate) {
    this.redisTemplate = redisTemplate;
    this.restTemplate = restTemplate;
  }

  @Autowired
  private RedisHelper redisHelper;

  @Autowired
  private StockJpaRepo stockJpaRepo;

  @Autowired
  private TransactionRepo transactionRepo;

  @Autowired
  private CrumbManager crumbManager;

  @Autowired
  private CookieManager cookieManager;

  @Autowired
  private StockEntityMapper stockEntityMapper;

  @Autowired
  private PriceRepo priceRepo;

  @Autowired
  private PriceLineFactory priceLineFactory;

  public void saveStockSymbols(List<String> stockSymbols) {
    List<String> stocksRedis =
        redisTemplate.opsForList().range("STOCK-LIST", 0, -1);

    if (stocksRedis == null) {
      stocksRedis = new ArrayList<>();
    }

    for (String symbol : stockSymbols) {
      if (!stocksRedis.contains(symbol)) {
        redisTemplate.opsForList().rightPush("STOCK-LIST", symbol);
      }
    }
    redisTemplate.expire("STOCK-LIST", 24, TimeUnit.HOURS);
  }

  public List<String> getStockSymbols() {
    saveStockSymbols(stockJpaRepo.findAll().stream()
        .map(stock -> stock.getStockSymbol()).collect(Collectors.toList()));
    return redisTemplate.opsForList().range("STOCK-LIST", 0, -1);
  }

  public String getCrumb() {
    return crumbManager.getCrumb();
  }

  public String getCookie() {
    return cookieManager.getCookie().toString();
  }

  public List<StockEntity> findAll() throws JsonProcessingException {
    List<StockEntity> redisStocks =
        getStockSymbols().stream().map(s -> stockEntityMapper.map(s)) //
            .collect(Collectors.toList());
    if (redisStocks == null) {
      List<StockEntity> dbStocks = stockJpaRepo.findAll();
      redisTemplate.opsForList().rightPushAll("STOCK-LIST", dbStocks.stream()
          .map(StockEntity::getStockSymbol).collect(Collectors.toList()));
      redisTemplate.expire("STOCK-LIST", Duration.ofMinutes(10));
      return dbStocks;
    }
    return redisStocks;
  }

  public StockEntity findBySymbol(String symbol) {
    return this.stockJpaRepo.findByStockSymbol(symbol)
        .orElseThrow(() -> new NoSuchElementException(
            "Stock not found with symbol: " + symbol));
  }

  public ApiResp<MarketPriceDTO> getPriceData(String symbol) {
    List<MarketPriceDTO> prices = priceRepo.findBySymbol(symbol);
    LocalTime now = LocalTime.now();
    LocalTime start = LocalTime.of(9, 30);
    LocalTime end = LocalTime.of(23, 59);
    LocalDate today = LocalDate.now();
    LocalDate yesterday = LocalDate.now().minusDays(1);
    if (now.isAfter(start) && now.isBefore(end)) {
      prices.stream()
          .filter(
              price -> price.getMarketDateTime().toLocalDate().isEqual(today))
          .collect(Collectors.toList());
    } else {
      prices.stream().filter(
          price -> price.getMarketDateTime().toLocalDate().isEqual(yesterday))
          .collect(Collectors.toList());
    }
    return ApiResp.<MarketPriceDTO>builder().message("success").symbol(symbol)
        .data(prices).build();
  }

  public LocalDate getSysDate(String symbol) {
    String redisKey = "SYSDATE-".concat(symbol);
    String redisSysDate = redisTemplate.opsForValue().get(redisKey);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
    if (redisSysDate == null) {
      Long lastMarketTime = transactionRepo.getMaxMarketTime(symbol);
      LocalDate sysDate = Instant.ofEpochSecond(lastMarketTime.longValue())
          .atZone(ZoneId.systemDefault()).toLocalDate();
      redisTemplate.opsForValue().set(redisKey, sysDate.format(format), 5,
          TimeUnit.MINUTES);
      return sysDate;
    }
    return LocalDate.parse(redisSysDate, format);
  }

  public List<MarketPriceDTO> getTransactionOnSysDate(String symbol) {
    LocalDate sysDate = getSysDate(symbol);
    long startEpoch = sysDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    long endEpoch = sysDate.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
    return transactionRepo.findBySymbol(symbol, startEpoch, endEpoch) //
    .stream().map(transaction -> new MarketPriceDTO(transaction.getMarketPrice(), transaction.getMarketDateTime())).collect(Collectors.toList());
  }

  public List<TransactionEntity> getTransactionOnSysDate1(String symbol) {
    LocalDate sysDate = getSysDate(symbol);
    long startEpoch = sysDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    long endEpoch = sysDate.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
    // return transactionRepo.findBySymbol(symbol, startEpoch, endEpoch);
    return transactionRepo.findBySymbolOnly(symbol);
  }

    public PriceLine<Candle> get5MinCandle(String symbol)
      throws JsonProcessingException {
    List<TransactionEntity> transactions = getTransactionOnSysDate1(symbol);
    return priceLineFactory.getCandleLine(transactions, 5);
  }
}
