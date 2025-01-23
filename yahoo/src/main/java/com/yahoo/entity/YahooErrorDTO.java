package com.yahoo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class YahooErrorDTO {
  @JsonProperty("finance")
  private YahooQuoteDTO.QuoteBody body;
}
