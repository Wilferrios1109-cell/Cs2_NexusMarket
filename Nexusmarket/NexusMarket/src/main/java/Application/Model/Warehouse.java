package Application.Model;

import Application.Value_Objects.WarehouseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Warehouse {

    private String id;
    private String name;
    private WarehouseType type;
}