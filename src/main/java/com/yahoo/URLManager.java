package com.yahoo;

public class URLManager {
  private final String base = "https://query1.finance.yahoo.com";
  private final String path = "/v7/finance/quote";
  private String symbol;
  private String crumb;

  private URLManager(Builder builder) {
    this.symbol = builder.symbol;
    this.crumb = builder.crumb;
  }

  public static URLManager.Builder builder() {
    return new Builder();
  }

  public String buildUrl() {
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(base);
    urlBuilder.append(path);
    urlBuilder.append("?symbols=").append(symbol);
    urlBuilder.append("&crumb=").append(crumb);
    return urlBuilder.toString();
}

  public static class Builder {
    private final String base = "https://query1.finance.yahoo.com";
    private final String path = "/v7/finance/quote";
    private String symbol;
    private String crumb;

    public Builder symbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    public Builder crumb(String crumb) {
      this.crumb = crumb;
      return this;
    }

    public URLManager build() {
      return new URLManager(this);
    }
  }
}
