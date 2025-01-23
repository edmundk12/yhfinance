package com.yahoo.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yahoo.entity.TransactionEntity;
import com.yahoo.line.Candle;
import com.yahoo.line.PriceData;
import com.yahoo.line.PriceLine;
import com.yahoo.line.PriceLineFactory;
import com.yahoo.service.StockService;
import com.yahoo.util.ApiResp;

@RestController
public class PriceLineController {
  @Autowired
  private StockService stockService;

  @Autowired
  private PriceLineFactory priceLineFactory;

  @GetMapping("/priceline/candle/{symbol}")
  public ApiResp<PriceLine<Candle>> getCandlePrices(@PathVariable String symbol)
      throws JsonProcessingException {
    PriceLine<Candle> priceline = stockService.get5MinCandle(symbol);
    List<PriceLine<Candle>> priceLineList = new ArrayList<>();
    priceLineList.add(priceline);
    return ApiResp.<PriceLine<Candle>>builder() //
        .message("ok") //
        .data(priceLineList) //
        .build();
  }
}
