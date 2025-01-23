package com.yahoo.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketPriceDTO {
  private Double marketPrice;
  private LocalDateTime marketDateTime;
  public MarketPriceDTO(Double marketPrice, LocalDateTime marketDateTime) {
    this.marketPrice = marketPrice;
    this.marketDateTime = marketDateTime;
}

}
