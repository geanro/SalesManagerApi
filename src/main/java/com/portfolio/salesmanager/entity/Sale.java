package com.portfolio.salesmanager.entity;

import com.portfolio.salesmanager.util.SaleStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSale;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id",nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "sale",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<SaleDetail> details= new ArrayList<>();
}
