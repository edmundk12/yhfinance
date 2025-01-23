package com.yahoo.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.yahoo.entity.StockEntity;

@Repository
public interface StockJpaRepo extends JpaRepository<StockEntity, Long> {
  
  Optional<StockEntity> findByStockSymbol(String symbol);
}
