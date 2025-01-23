package com.yahoo.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.yahoo.entity.TransactionEntity;
import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface TransactionRepo
    extends JpaRepository<TransactionEntity, Long> {

  @Query(
      value = "SELECT p FROM TransactionEntity p WHERE p.symbol = :symbol AND p.marketUnixTime BETWEEN :startEpoch AND :endEpoch")
  List<TransactionEntity> findBySymbol(String symbol, long startEpoch,
      long endEpoch);

      @Query(
        value = "SELECT p FROM TransactionEntity p WHERE p.symbol = :symbol")
    List<TransactionEntity> findBySymbolOnly(String symbol);

  @Query(
      value = "SELECT max(p.marketUnixTime) FROM TransactionEntity p WHERE p.symbol = :symbol")
  Long getMaxMarketTime(@Param("symbol") String symbol);
}
