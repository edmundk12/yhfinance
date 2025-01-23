package com.yahoo.util;

import java.util.List;

public class ApiResp<T> {
  private String message;
  private String symbol;
  private List<T> data;

  private ApiResp(Builder<T> builder) {
    this.message = builder.message;
    this.symbol = builder.symbol;
    this.data = builder.data;
}

  public String getMessage() {
    return this.message;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public List<T> getData() {
    return this.data;
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  public static class Builder<T> {
    private String message;
    private String symbol;
    private List<T> data;

        public Builder<T> message(String message) {
        this.message = message;
        return this;
    }

    public Builder<T> symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Builder<T> data(List<T> data) {
      this.data = data;
      return this;
    }

    public ApiResp<T> build() {
      return new ApiResp<>(this);
    }
  }
}
