package com.yahoo.controller;

import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import org.apache.hc.client5.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yahoo.dto.MarketPriceDTO;
import com.yahoo.entity.YahooQuoteDTO;
import com.yahoo.service.StockService;
import com.yahoo.util.ApiResp;
import com.yahoo.util.CookieManager;
import com.yahoo.util.YHRestClient;

@RestController
public class StockController {

  @Autowired
  private RestTemplate restTemplate;
  
  @Autowired
  private StockService stockService;

  @Autowired
  private YHRestClient yhRestClient;

  @Autowired
  private CookieManager cookieManager;

  @GetMapping("/stocklist")
  public List<String> fetchStockSymbols() {
    return stockService.getStockSymbols();
  }

  @GetMapping("/quote")
  public YahooQuoteDTO fetchQuote() throws JsonProcessingException {
    return yhRestClient.getQuote(stockService.getStockSymbols());
  }

  @GetMapping("/price/{symbol}")
  public ApiResp<MarketPriceDTO> fetchPriceData(@PathVariable String symbol) {
    return stockService.getPriceData(symbol);
  }
}
