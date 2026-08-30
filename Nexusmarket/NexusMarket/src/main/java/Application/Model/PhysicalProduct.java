package Application.Model;

import Application.Value_Objects.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhysicalProduct extends Product {

    public ProductType getProductType() {
        return ProductType.PHYSICAL;
    }
}