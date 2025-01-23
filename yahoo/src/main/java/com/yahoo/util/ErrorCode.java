package com.yahoo.util;

public enum ErrorCode {
  Entity_NOT_FOUND_EX(1, "Entity not found."), //
  INV_INPUT_EX(2, "Invalid Input."), //
  REST_CLIENT_EX(3, "Rest Client Exception."),;

  private int code;
  private String message;

  private ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return this.code;
  }

  public String getMessage() {
    return this.message;
  }
}

