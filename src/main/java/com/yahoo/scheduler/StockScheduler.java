package com.yahoo.scheduler;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yahoo.entity.StockEntity;
import com.yahoo.entity.TransactionEntity;
import com.yahoo.entity.YahooQuoteDTO;
import com.yahoo.mapper.StockEntityMapper;
import com.yahoo.mapper.TransactionMapper;
import com.yahoo.repository.jpa.TransactionRepo;
import com.yahoo.service.StockService;
import com.yahoo.util.RedisHelper;
import com.yahoo.util.YHRestClient;

@Component
public class StockScheduler {

  @Autowired
  private StockEntityMapper stockEntityMapper;

  @Autowired
  private RedisHelper redisHelper;

  @Autowired
  private StockService stockService;

  @Autowired
  private YHRestClient yhRestClient;

  @Autowired
  private TransactionMapper transactionMapper;

  @Autowired
  private TransactionRepo transactionRepo;
  
  // @Scheduled(cron = "0 */5 9-15 * * MON-FRI")
  // @Scheduled(cron = "0 0 16 * * MON-FRI")
  @Scheduled(fixedRate = 25000)
  public void quoteScheduler() throws JsonProcessingException {
    List<StockEntity> stocks = stockService.findAll();
    System.out.println("stocks = " + stocks);
    if (stocks == null || stocks.size() == 0)
    return;
    List<String> symbols = stocks.stream() //
    .map(s -> s.getStockSymbol()) //
    .collect(Collectors.toList());
    
    YahooQuoteDTO quoteDTO = yhRestClient.getQuote(symbols);
    quoteDTO.getBody().getResults().forEach(s -> {
      StockEntity stockEntity = this.stockService.findBySymbol(s.getSymbol());
      TransactionEntity transactionEntity = this.transactionMapper.map(s);
      transactionEntity.setStock(stockEntity);
      transactionEntity.setSymbol(stockEntity.getStockSymbol());
      transactionEntity.setTranType("5MIN");
      this.transactionRepo.save(transactionEntity);
    });
  }
}
