package com.yahoo.config.dataloader;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.yahoo.entity.StockEntity;
import com.yahoo.repository.jpa.StockJpaRepo;
import com.yahoo.repository.jpa.TransactionRepo;
import com.yahoo.service.StockService;
import java.util.stream.Collectors;


@Component
public class DataLoader implements CommandLineRunner {

  @Autowired
  private StockJpaRepo stockJpaRepo;

  @Autowired
  private StockService stockService;

  @Autowired
  private TransactionRepo transactionRepo;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Override
  public void run(String... args) throws Exception {
    this.stockJpaRepo.deleteAll();
    this.transactionRepo.deleteAll();

    String[] symbols = new String[] {"0388.HK", "0700.HK", "0005.HK", "AAPL"};
    List<StockEntity> entities = Arrays.stream(symbols) //
        .map(e -> StockEntity.builder().stockSymbol(e).build()) //
        .collect(Collectors.toList());
    stockJpaRepo.saveAll(entities);
    System.out.println("Insert Stock Symbols Completed.");
  }
}
