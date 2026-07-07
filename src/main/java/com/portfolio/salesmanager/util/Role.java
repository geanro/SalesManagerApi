package com.portfolio.salesmanager.util;

import java.util.Arrays;
import java.util.List;

public enum Role {

    // Administrador general: controla todo el sistema y no pertenece a una sucursal fija.
    ADMIN(Arrays.asList(
            Permission.READ_ALL_USERS,
            Permission.SAVE_ONE_USER,
            Permission.UPDATE_ONE_USER,
            Permission.DELETE_ONE_USER,

            Permission.READ_ALL_BRANCHES,
            Permission.SAVE_ONE_BRANCH,
            Permission.UPDATE_ONE_BRANCH,
            Permission.DELETE_ONE_BRANCH,

            Permission.READ_ALL_CATEGORIES,
            Permission.SAVE_ONE_CATEGORY,
            Permission.UPDATE_ONE_CATEGORY,
            Permission.DELETE_ONE_CATEGORY,

            Permission.READ_ALL_PRODUCTS,
            Permission.SAVE_ONE_PRODUCT,
            Permission.UPDATE_ONE_PRODUCT,
            Permission.DELETE_ONE_PRODUCT,

            Permission.READ_INVENTORY,
            Permission.READ_BRANCH_INVENTORY,
            Permission.SAVE_INVENTORY,
            Permission.UPDATE_INVENTORY,
            Permission.DELETE_INVENTORY,

            Permission.CREATE_ONE_SALE,
            Permission.READ_ALL_SALES,
            Permission.READ_USER_SALES,
            Permission.READ_ANY_BRANCH_SALES,
            Permission.READ_BRANCH_SALES,
            Permission.READ_MY_SALES,
            Permission.CANCEL_ONE_SALE
    )),

    // Supervisor de sucursal: trabaja en una sucursal fija y supervisa ventas/inventario de esa sucursal.
    SUPERVISOR(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_ALL_CATEGORIES,
            Permission.READ_ALL_BRANCHES,

            Permission.READ_BRANCH_INVENTORY,
            Permission.SAVE_INVENTORY,
            Permission.UPDATE_INVENTORY,

            Permission.CREATE_ONE_SALE,
            Permission.READ_BRANCH_SALES,
            Permission.READ_MY_SALES,
            Permission.CANCEL_ONE_SALE
    )),

    // Vendedor: vende y consulta información básica de su sucursal.
    SELLER(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_ALL_CATEGORIES,
            Permission.READ_BRANCH_INVENTORY,

            Permission.CREATE_ONE_SALE,
            Permission.READ_BRANCH_SALES,
            Permission.READ_MY_SALES
    )),

    // Cliente: compra y consulta sus propias ventas.
    CUSTOMER(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_ALL_CATEGORIES,
            Permission.CREATE_ONE_SALE,
            Permission.READ_MY_SALES
    ));

    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
}
