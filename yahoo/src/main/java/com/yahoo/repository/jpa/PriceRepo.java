package com.yahoo.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.yahoo.dto.MarketPriceDTO;
import com.yahoo.entity.TransactionEntity;

@Repository
public interface PriceRepo extends JpaRepository<TransactionEntity, Long>{
  @Query("SELECT new com.yahoo.dto.MarketPriceDTO(mp.marketPrice, mp.marketDateTime)" +
           "FROM TransactionEntity mp WHERE mp.symbol = :symbol " +
           "ORDER BY mp.marketDateTime DESC")
    List<MarketPriceDTO> findBySymbol(String symbol);
}
