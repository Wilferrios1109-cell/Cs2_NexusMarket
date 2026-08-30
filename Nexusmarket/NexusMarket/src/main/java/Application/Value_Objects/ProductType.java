package Application.Value_Objects;

public final class ProductType extends DomainCatalog {

    public static final ProductType PHYSICAL =
            new ProductType(
                    "PHYSICAL","Physical","Physical product that requires inventory and dispatch."
            );

    public static final ProductType DIGITAL =
            new ProductType(
                    "DIGITAL","Digital","Digital product delivered immediately after payment."
            );

    private ProductType(
            String code,
            String name,
            String description) {

        super(code, name, description);
    }
}