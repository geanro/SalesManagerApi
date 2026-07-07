package com.portfolio.salesmanager.repository;

import com.portfolio.salesmanager.entity.Sale;
import com.portfolio.salesmanager.util.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {
    List<Sale> findByUser_IdUser(Long idUser);
    List<Sale> findByBranch_IdBranch(Long idBranch);
    List<Sale> findByStatus(SaleStatus status);
    List<Sale> findBySaleDateBetween(LocalDateTime initial,
                                     LocalDateTime end);
    List<Sale> findTop5ByOrderByTotalDesc();
    
}
