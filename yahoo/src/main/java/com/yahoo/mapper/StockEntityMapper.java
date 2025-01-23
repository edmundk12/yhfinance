package com.yahoo.mapper;

import org.springframework.stereotype.Component;
import com.yahoo.entity.StockEntity;

@Component
public class StockEntityMapper {

  public StockEntity map(String symbol) {
    return StockEntity.builder() //
      .stockSymbol(symbol).build();
  }
}
