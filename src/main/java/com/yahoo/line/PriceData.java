package com.yahoo.line;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PriceData {
  private LocalDateTime time;
  private double open;
  private double close;
  private double high;
  private double low;

  public PriceData(LocalDateTime time, double open, double close, double high,
      double low) {
    this.time = time;
    this.open = open;
    this.close = close;
    this.high = high;
    this.low = low;
  }
}
