package com.yahoo.line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.yahoo.entity.TransactionEntity;
import com.yahoo.line.PriceData;
@Component
public class PriceLineFactory {

  public List<PriceData> groupTransactions(
      List<TransactionEntity> transactions, int interval) {
        List<TransactionEntity> sortedTransactions = transactions.stream()
        .sorted(Comparator.comparing(TransactionEntity::getMarketDateTime))
        .collect(Collectors.toList());

    Map<LocalDateTime, List<TransactionEntity>> group =
        sortedTransactions.stream()
        // .filter(transaction -> {
        //   LocalTime time = transaction.getMarketDateTime().toLocalTime();
        //   return !time.isBefore(LocalTime.of(9, 0))
        //       && !time.isAfter(LocalTime.of(16, 0));
        // })
        .collect(Collectors.groupingBy(t -> {
          LocalDateTime dateTime = t.getMarketDateTime();
          // Round down the minute to the nearest interval
          int minutes = dateTime.getMinute() / interval * interval;
          return dateTime.withMinute(minutes).withSecond(0).withNano(0);
        }));

    List<PriceData> result = new ArrayList<>();
    for (Map.Entry<LocalDateTime, List<TransactionEntity>> entry : group
        .entrySet()) {
      List<TransactionEntity> transactionList = entry.getValue();
      double open = transactionList.get(0).getMarketPrice(); // Assuming price is obtained via getPrice()
      double close = transactionList.get(transactionList.size() - 1).getMarketPrice();
      double high = transactionList.stream().mapToDouble(TransactionEntity::getMarketPrice).max().orElse(0);
      double low = transactionList.stream().mapToDouble(TransactionEntity::getMarketPrice).min().orElse(0);
      PriceData priceData = new PriceData(entry.getKey(), open, close, high, low);
            result.add(priceData);
  }
  return result;
}

  public PriceLine<Candle> getCandleLine(List<TransactionEntity> transaction,
      int minutePerInterval) {
    List<PriceData> transactions =
        groupTransactions(transaction, minutePerInterval);
    Set<PricePoint<Candle>> points = transactions.stream() //
        .map(t -> new PricePoint<>(t.getTime(),
            new Candle(t.getOpen(), t.getClose(), t.getHigh(), t.getLow())))
        .collect(Collectors.toSet());

    return PriceLine.<Candle>builder() //
        .lineType(PriceLine.LineType.MIN5) //
        .symbol(transaction.get(0).getSymbol()) //
        .stockId(transaction.get(0).getId()) //
        .points(points) //
        .build();
  }
}

