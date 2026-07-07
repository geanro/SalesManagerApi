package com.portfolio.salesmanager.repository;

import com.portfolio.salesmanager.entity.BranchInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchInventoryRepository extends JpaRepository<BranchInventory,Long> {
    List<BranchInventory> findByBranch_IdBranch(Long idBranch);
    List<BranchInventory> findByProduct_IdProduct(Long idProduct);
    Optional<BranchInventory> findByBranch_IdBranchAndProduct_IdProduct(Long idBranch, Long idProduct);
    List<BranchInventory> findByStockLessThan(Integer stock);

    @Query("SELECT COALESCE(SUM(i.stock), 0) FROM BranchInventory i WHERE i.product.idProduct = :idProduct")
    Integer sumStockByProductId(@Param("idProduct") Long idProduct);
}
