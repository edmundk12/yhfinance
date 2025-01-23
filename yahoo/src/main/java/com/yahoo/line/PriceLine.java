package com.yahoo.line;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PriceLine<T extends PriceType> {
  private LineType lineType;
  private String symbol;
  private Long stockId;
  @Builder.Default
  private Set<PricePoint<T>> points = new HashSet<>();

  public PriceLine(LineType lineType, String symbol, Long stockId) {
    this.lineType = lineType;
    this.symbol = symbol;
    this.stockId = stockId;
  }

  public boolean add(PricePoint<T> pricePoint) {
    return this.points.add(pricePoint);
  }

  public void clear() {
    this.points.clear();
  }

  public static enum LineType {
    // MIN1, //
    MIN5, //
    // MIN15, //
    // MIN30, //
    // HOUR1, //
    DAY, //
    WEEK, //
    MONTH1, //
    // MONTH3, //
    // YEAR, //
    ;
  }
}
