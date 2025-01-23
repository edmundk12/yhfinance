package com.yahoo.exception;

import com.yahoo.entity.YahooErrorDTO;
import com.yahoo.util.ErrorCode;

public class StockExceptionHandler extends BusinessException {

  public StockExceptionHandler(YahooErrorDTO quoteErrorDTO) {
    super(ErrorCode.REST_CLIENT_EX,
        quoteErrorDTO.getBody().getError().getMessage());
  }
}
