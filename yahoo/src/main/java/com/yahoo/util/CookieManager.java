package com.yahoo.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
public class CookieManager {
  private final RestTemplate restTemplate;

  public CookieManager(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
  }

  // public Cookie getCookie() {
  //   String url = "https://fc.yahoo.com"; 
  //   CloseableHttpClient httpClient = HttpClients.createDefault();
  //   HttpGet httpGet = new HttpGet(url);
  //   try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
      
  //     Header[] cookiesHeader = response.getHeaders("Set-Cookie");
  //     List<String> cookies = new ArrayList<>();
  //     for(Header header : cookiesHeader) {
  //       cookies.add(header.getValue().split(";")[0]);
  //       }
  //       return cookies.get(0);
  //   }        catch (IOException e) {
  //     e.printStackTrace();
  //   } return null;
  // }

  public String getCookie() {
    try {
      String cookieUrl = "https://fc.yahoo.com";
      ResponseEntity<String> entity =
          restTemplate.getForEntity(cookieUrl, String.class);
      // If no exception thrown, you can find the header from ResponseEntity.
      List<String> cookies = entity.getHeaders().get("Set-Cookie");
      return cookies != null ? cookies.get(0).split(";")[0] : null;
    } catch (RestClientException e) {
      // Able to get the response headers, even the restTemplate throws error.
      if (e instanceof HttpStatusCodeException) {
        HttpHeaders headers =
            ((HttpStatusCodeException) e).getResponseHeaders();
        if (headers != null) {
          List<String> cookies = headers.get("Set-Cookie");
          return cookies != null ? cookies.get(0).split(";")[0] : null;
        }
      }
      return null;
    }
  }
}