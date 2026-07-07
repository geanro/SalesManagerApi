package com.portfolio.salesmanager.service;


import com.portfolio.salesmanager.dto.request.SaleRequest;
import com.portfolio.salesmanager.dto.response.SaleResponse;
import com.portfolio.salesmanager.entity.*;
import com.portfolio.salesmanager.exception.*;
import com.portfolio.salesmanager.mapper.SaleMapper;
import com.portfolio.salesmanager.repository.*;
import com.portfolio.salesmanager.util.Role;
import com.portfolio.salesmanager.util.SaleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SaleService {

    private final SaleMapper saleMapper;
    private final SaleRepository saleRepository;
    private final BranchRepository branchRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final UserRepository userRepository;

    public List<SaleResponse> findAll(){

        return saleRepository.findAll()
                .stream()
                .map(saleMapper::toResponse)
                .toList();
    }

    public SaleResponse findSale(Long idSale){

        return saleRepository.findById(idSale)
                .map(saleMapper::toResponse)
                .orElseThrow(()->new SaleNotFoundException("Venta no encontrada"));
    }

    public  List<SaleResponse> findSalesByUser(Long idUser){

        userRepository.findById(idUser)
                .orElseThrow(()->new UserNotFoundException("Usuario no encontrado"));

        List<Sale> salesUser= saleRepository.findByUser_IdUser(idUser);

        return saleMapper.toResponseList(salesUser);
    }

    public List<SaleResponse> findSalesByBranch(Long idBranch) {

        branchRepository.findById(idBranch)
                .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));

        List<Sale> salesBranch= saleRepository.findByBranch_IdBranch(idBranch);

        return saleMapper.toResponseList(salesBranch);
    }

    public List<SaleResponse> findSalesByStatus(SaleStatus status){

        List<Sale> salesStatus= saleRepository.findByStatus(status);

        return saleMapper.toResponseList(salesStatus);
    }

    public List<SaleResponse> findSalesByDateBetween(LocalDateTime start,
                                                     LocalDateTime end){

        List<Sale> sales= saleRepository.findBySaleDateBetween(start,end);

        return saleMapper.toResponseList(sales);
    }

    public List<SaleResponse> findTopSales(){

        List<Sale> sales= saleRepository.findTop5ByOrderByTotalDesc();

        return saleMapper.toResponseList(sales);
    }

    public List<SaleResponse> findMySales(){

        User user = userService.getAuthenticatedUser();

        List<Sale> sales = saleRepository.findByUser_IdUser(user.getIdUser());

        return saleMapper.toResponseList(sales);
    }

    public List<SaleResponse> findSalesByMyBranch(){

        User user = userService.getAuthenticatedUser();

        if (user.getBranch() == null) {
            throw new BranchNotFoundException("El usuario no tiene una sucursal asignada");
        }

        List<Sale> sales = saleRepository.findByBranch_IdBranch(
                user.getBranch().getIdBranch());

        return saleMapper.toResponseList(sales);
    }

    @Transactional
    public SaleResponse saveSale(SaleRequest saleRequest){

        User authenticatedUser = userService.getAuthenticatedUser();

        Branch branch= branchRepository.findById(saleRequest.getBranchId())
                .orElseThrow(()->new BranchNotFoundException("Sucursal no encontrada"));

        validateSaleBranchAccess(authenticatedUser, branch.getIdBranch());

        LocalDateTime now = LocalDateTime.now();

        //seteamos valores por defecto para la venta
        Sale sale= new Sale();
        sale.setSaleDate(now);
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setUser(authenticatedUser);
        sale.setBranch(branch);

        //crear lista details
        List<SaleDetail> details= saleRequest.getDetails()
                .stream()
                .map(detailsList->{

                    Product product= productRepository.findById(detailsList.getProductId())
                            .orElseThrow(()->new ProductNotFoundException("Producto no encontrado"));

                    BranchInventory branchInventory= branchInventoryRepository.findByBranch_IdBranchAndProduct_IdProduct(
                            branch.getIdBranch(),
                            product.getIdProduct())
                            .orElseThrow(()-> new InventoryNotFoundException("Inventario no encontrado para este producto"));

                    if (branchInventory.getStock()<detailsList.getQuantity()){
                        throw new StockInsufficientException("Stock insuficiente para el producto: " + product.getName());
                    }

                    branchInventory.setStock(branchInventory.getStock()-detailsList.getQuantity());

                    BigDecimal unitPrice= product.getPrice();
                    BigDecimal subTotal= unitPrice.multiply(BigDecimal.valueOf(detailsList.getQuantity()));


                    return SaleDetail.builder()
                            .sale(sale)
                            .product(product)
                            .quantity(detailsList.getQuantity())
                            .unitPrice(unitPrice)
                            .subtotal(subTotal)
                            .build();
                })
                .collect(Collectors.toList());

        //Seteamos details
        sale.setDetails(details);

        //Calculamos el Total
        BigDecimal total= details.stream()
                .map(SaleDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Seteamos total
        sale.setTotal(total);

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponse cancelSale(Long idSale){

        Sale sale= saleRepository.findById(idSale)
                .orElseThrow(()-> new SaleNotFoundException("Venta no encontrada"));

        if (sale.getStatus()==SaleStatus.CANCELLED){
            throw new SaleAlreadyCancelledException("La venta ya se encuentra cancelada");
        }

        validateCancelSaleAccess(userService.getAuthenticatedUser(), sale);

        sale.getDetails().forEach(saleDetail -> {

            BranchInventory branchInventory= branchInventoryRepository.findByBranch_IdBranchAndProduct_IdProduct(
                    sale.getBranch().getIdBranch(),
                    saleDetail.getProduct().getIdProduct())
                    .orElseThrow(()-> new InventoryNotFoundException("Inventario no encontrado"));

            branchInventory.setStock(branchInventory.getStock()+saleDetail.getQuantity());
        });

        sale.setStatus(SaleStatus.CANCELLED);

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    private void validateSaleBranchAccess(User user, Long branchId){

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.CUSTOMER) {
            return;
        }

        if ((user.getRole() == Role.SUPERVISOR || user.getRole() == Role.SELLER)
                && user.getBranch() != null
                && user.getBranch().getIdBranch().equals(branchId)) {
            return;
        }

        throw new AccessDeniedException("No puedes registrar ventas en otra sucursal");
    }

    private void validateCancelSaleAccess(User user, Sale sale){

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() == Role.SUPERVISOR
                && user.getBranch() != null
                && user.getBranch().getIdBranch().equals(sale.getBranch().getIdBranch())) {
            return;
        }

        throw new AccessDeniedException("No puedes cancelar ventas de otra sucursal");
    }
}
