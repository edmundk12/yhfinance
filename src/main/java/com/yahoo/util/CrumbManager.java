package com.yahoo.util;

import java.io.IOException;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CrumbManager {
  private RestTemplate restTemplate;
  private CookieManager cookieManager;


  public CrumbManager(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.cookieManager = new CookieManager(restTemplate);
  }

  public String getCrumb() {
    String cookie = cookieManager.getCookie();
    HttpHeaders header = new HttpHeaders();
    header.set("Cookie", cookie);
    HttpEntity<Void> entity = new HttpEntity<>(header);
    String crumbUrl = "https://query1.finance.yahoo.com/v1/test/getcrumb";
    return restTemplate.exchange(crumbUrl, HttpMethod.GET, entity, String.class)
        .getBody();
  }
}
