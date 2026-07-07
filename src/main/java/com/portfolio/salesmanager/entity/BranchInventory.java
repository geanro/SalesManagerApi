package com.portfolio.salesmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "branchInventory")
public class BranchInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id",nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer stock;
}
