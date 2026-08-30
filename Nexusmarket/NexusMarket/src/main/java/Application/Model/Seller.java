package Application.Model;

import Application.Value_Objects.SystemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Seller extends User {

    {
        setRole(SystemRole.SELLER);
    }
}