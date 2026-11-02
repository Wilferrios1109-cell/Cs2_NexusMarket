package Application.Model;

import Application.Value_Objects.SystemRole;
import Application.Value_Objects.UserStatus;
import lombok.Getter;

@Getter
public abstract class User {

    private final String identification;
    private final String fullName;
    private final String email;
    private final UserStatus status;

    protected User(
            String identification,
            String fullName,
            String email,
            UserStatus status) {

        this.identification = identification;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
    }

    public abstract SystemRole getRole();
}
