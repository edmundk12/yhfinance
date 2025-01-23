package com.yahoo.exception;

import com.yahoo.util.ErrorCode;

public class BusinessException extends RuntimeException {
  private int code;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.code = errorCode.getCode();
  }

  public BusinessException(ErrorCode error, String overrideMessage) {
    super(overrideMessage);
    this.code = error.getCode();
  }

  public int getCode() {
    return this.code;
  }
}
