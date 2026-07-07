package com.portfolio.salesmanager.service;

import com.portfolio.salesmanager.dto.request.BranchInventoryRequest;
import com.portfolio.salesmanager.dto.request.UpdateStockRequest;
import com.portfolio.salesmanager.dto.response.BranchInventoryResponse;
import com.portfolio.salesmanager.entity.Branch;
import com.portfolio.salesmanager.entity.BranchInventory;
import com.portfolio.salesmanager.entity.Product;
import com.portfolio.salesmanager.entity.User;
import com.portfolio.salesmanager.exception.BranchNotFoundException;
import com.portfolio.salesmanager.exception.InventoryAlreadyExistsException;
import com.portfolio.salesmanager.exception.InventoryNotFoundException;
import com.portfolio.salesmanager.exception.ProductNotFoundException;
import com.portfolio.salesmanager.mapper.BranchInventoryMapper;
import com.portfolio.salesmanager.repository.BranchInventoryRepository;
import com.portfolio.salesmanager.repository.BranchRepository;
import com.portfolio.salesmanager.repository.ProductRepository;
import com.portfolio.salesmanager.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BranchInventoryService {

    private final BranchInventoryRepository invRepo;
    private final BranchInventoryMapper invMapper;
    private final BranchRepository branchRepo;
    private final ProductRepository productRepo;
    private final UserService userService;

    public List<BranchInventoryResponse> findAll(){

        return invRepo.findAll()
                .stream()
                .map(invMapper::toResponse)
                .toList();
    }

    public BranchInventoryResponse findInventory(Long idInventory){

        BranchInventory inventory= invRepo.findById(idInventory)
                .orElseThrow(()-> new InventoryNotFoundException("Inventario no encontrado"));

        return invMapper.toResponse(inventory);
    }

    public List<BranchInventoryResponse> findByBranch(Long idBranch){

        branchRepo.findById(idBranch)
                .orElseThrow(()-> new BranchNotFoundException("Sucursal no encontrada"));

        List<BranchInventory> inventories= invRepo.findByBranch_IdBranch(idBranch);

        return invMapper.toResponseList(inventories);
    }

    public List<BranchInventoryResponse> findInventoryByMyBranch(){

        User user = userService.getAuthenticatedUser();

        if (user.getBranch() == null) {
            throw new BranchNotFoundException("El usuario no tiene una sucursal asignada");
        }

        List<BranchInventory> inventories = invRepo.findByBranch_IdBranch(user.getBranch().getIdBranch());

        return invMapper.toResponseList(inventories);
    }

    public List<BranchInventoryResponse>  findByProduct(Long idProduct){

        productRepo.findById(idProduct)
                .orElseThrow(()-> new ProductNotFoundException("Producto no encontrado"));

        List<BranchInventory> producBranches= invRepo.findByProduct_IdProduct(idProduct);

        return invMapper.toResponseList(producBranches);
    }

    public BranchInventoryResponse findByBranchAndProduct(Long idBranch,
                                                                Long idProduct){

        BranchInventory inventory= invRepo.findByBranch_IdBranchAndProduct_IdProduct(idBranch, idProduct)
                .orElseThrow(()-> new InventoryNotFoundException("Inventario no encontrado"));

        return invMapper.toResponse(inventory);
    }

    public List<BranchInventoryResponse> findLowStock(Integer stock){

        List<BranchInventory> inventoriesLowStock= invRepo.findByStockLessThan(stock);

        return invMapper.toResponseList(inventoriesLowStock);
    }

    @Transactional
    public BranchInventoryResponse saveInventory(BranchInventoryRequest branchInventoryRequest){

        validateBranchOperation(branchInventoryRequest.getBranchId());

        if (invRepo.findByBranch_IdBranchAndProduct_IdProduct(branchInventoryRequest.getBranchId(),
                branchInventoryRequest.getProductId()).isPresent()){
            throw new InventoryAlreadyExistsException("Ya existe un inventario para este producto en la sucursal");
        }

        Branch branch= branchRepo.findById(branchInventoryRequest.getBranchId())
                .orElseThrow(()-> new BranchNotFoundException("Sucursal no encontrada"));

        Product product= productRepo.findById(branchInventoryRequest.getProductId())
                .orElseThrow(()->new ProductNotFoundException("Producto no encontrado"));

        BranchInventory branchInventory= BranchInventory.builder()
                .branch(branch)
                .product(product)
                .stock(branchInventoryRequest.getStock())
                .build();

        return invMapper.toResponse(invRepo.save(branchInventory));
    }

    @Transactional
    public BranchInventoryResponse updateStock(Long idInventory,
                                               UpdateStockRequest updateStockRequest){

        BranchInventory inventory= invRepo.findById(idInventory)
                .orElseThrow(()-> new InventoryNotFoundException("Inventario no encontrado"));

        validateBranchOperation(inventory.getBranch().getIdBranch());

        inventory.setStock(updateStockRequest.getStock());

        return invMapper.toResponse(invRepo.save(inventory));
    }

    @Transactional
    public void deleteInventory(Long idInventory){

        BranchInventory inventory= invRepo.findById(idInventory)
                .orElseThrow(()-> new InventoryNotFoundException("Inventario no encontrado"));

        invRepo.delete(inventory);
    }

    private void validateBranchOperation(Long branchId){

        User user = userService.getAuthenticatedUser();

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() == Role.SUPERVISOR) {
            if (user.getBranch() == null || !user.getBranch().getIdBranch().equals(branchId)) {
                throw new AccessDeniedException("No puedes modificar inventario de otra sucursal");
            }
            return;
        }

        throw new AccessDeniedException("No tienes permisos para modificar inventario");
    }
}
