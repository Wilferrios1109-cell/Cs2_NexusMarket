package Application.Value_Objects;

public final class WarehouseType extends DomainCatalog {

    public static final WarehouseType MARKETPLACE =
            new WarehouseType(
                    "MARKETPLACE","Marketplace Warehouse","Warehouse operated by the Marketplace."
            );

    public static final WarehouseType SELLER =
            new WarehouseType(
                    "SELLER","Seller Warehouse","Warehouse belonging to a Seller."
            );

    private WarehouseType(
            String code,
            String name,
            String description) {

        super(code, name, description);
    }
}