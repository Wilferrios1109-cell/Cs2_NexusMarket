package Application.Model;

import Application.Value_Objects.ProductStatus;
import Application.Value_Objects.ProductType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Product {

    private String id;
    private String name;

    @Setter(AccessLevel.NONE)
    private ProductType productType;

    private ProductStatus status;

    protected void setProductType(ProductType productType) {
        this.productType = productType;
    }
}