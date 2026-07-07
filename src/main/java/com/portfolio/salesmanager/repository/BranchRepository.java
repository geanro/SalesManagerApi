package com.portfolio.salesmanager.repository;

import com.portfolio.salesmanager.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {
    List<Branch> findByNameContainingIgnoreCase(String name);
    List<Branch> findByActiveTrue();
    boolean existsByPhone(String phone);
}
