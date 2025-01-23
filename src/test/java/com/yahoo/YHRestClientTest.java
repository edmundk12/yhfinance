package com.yahoo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import com.yahoo.entity.YahooQuoteDTO;
import com.yahoo.util.YHRestClient;

@ExtendWith(MockitoExtension.class)
public class YHRestClientTest {

  private YHRestClient yhRestClient;

  @BeforeEach
  public void setUp() {
    yhRestClient = new YHRestClient();
  }

  @Test
  public void testInitialization() {
    assertNotNull(yhRestClient.getRestTemplate());
    assertNotNull(yhRestClient.getCrumbManager());
    assertNotNull(yhRestClient.getCookieStore());
  }

  @Test
  public void testGetQuote() throws Exception {
    String symbol = "AAPL";
    YahooQuoteDTO actualQuote = yhRestClient.getQuote(List.of(symbol));
    assertNotNull(actualQuote);
    assertEquals("AAPL", actualQuote.getBody().getResults().get(0).getSymbol());
  }
}
