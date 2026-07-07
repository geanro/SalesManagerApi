package com.portfolio.salesmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "saleDetail")
public class SaleDetail {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long idSaleDetail;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "sale_id",nullable = false)
   private Sale sale;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "product_id", nullable = false)
   private Product product;

   @Column(nullable = false)
   private Integer quantity;

   @Column(nullable = false)
   private BigDecimal unitPrice;

   @Column(nullable = false)
   private BigDecimal subtotal;
}
