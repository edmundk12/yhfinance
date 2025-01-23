package com.yahoo.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;
import com.yahoo.entity.TransactionEntity;
import com.yahoo.entity.YahooQuoteDTO;

@Component
public class TransactionMapper {
  public TransactionEntity map(YahooQuoteDTO.QuoteBody.QuoteResult quotePrice) {
    LocalDateTime quoteDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(quotePrice.getRegularMarketTime()),
        ZoneId.systemDefault());
    return TransactionEntity.builder().symbol(quotePrice.getSymbol())
        .tranDatetime(LocalDateTime.now()) //
        .marketUnixTime(quotePrice.getRegularMarketTime()) //
        .marketDateTime(quoteDateTime) //
        .marketPrice(quotePrice.getRegularMarketPrice()) //
        // .marketPriceChangePercent(quotePrice.getRegularMarketChangePercent()) //
        // .ask(quotePrice.getAsk()) //
        // .askSize(quotePrice.getAskSize()) //
        // .bid(quotePrice.getBid()) //
        // .bidSize(quotePrice.getBidSize()) //
        .build();
  }
}
