package Application.Model;

import Application.Value_Objects.SystemRole;
import Application.Value_Objects.UserStatus;
import lombok.Getter;

@Getter
public class Seller extends User {

    public Seller(
            String identification,
            String fullName,
            String email,
            UserStatus status) {

        super(identification, fullName, email, status);
    }

    @Override
    public SystemRole getRole() {
        return SystemRole.SELLER;
    }
}
