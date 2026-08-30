package Application.Value_Objects;

public final class ProductStatus extends DomainCatalog {

    public static final ProductStatus PUBLISHED =
            new ProductStatus(
                    "PUBLISHED","Published","Product is visible and available in the public catalog."
            );

    public static final ProductStatus SUSPENDED =
            new ProductStatus(
                    "SUSPENDED","Suspended","Product is temporarily unavailable in the public catalog."
            );

    public static final ProductStatus DISCONTINUED =
            new ProductStatus(
                    "DISCONTINUED","Discontinued","Product is no longer offered in the Marketplace."
            );

    private ProductStatus(
            String code,
            String name,
            String description) {

        super(code, name, description);
    }
}