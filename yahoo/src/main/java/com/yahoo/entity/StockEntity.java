package com.yahoo.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StockEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String stockSymbol;
  private int regularMarketUnix;
  private double regularMarketPrice;
  private double regularMarketChangePercent;
  private double bid;
  private double bidSize;
  private double ask;
  private double askSize;

  public StockEntity(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }
}
