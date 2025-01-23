package com.yahoo.line;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class PricePoint<T extends PriceType> {
  private LocalDateTime dateTime;
  private T price;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof PricePoint))
      return false;
    PricePoint<?> pricePoint = (PricePoint<?>) obj;
    return Objects.equals(this.dateTime, pricePoint.getDateTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.dateTime);
  }
}