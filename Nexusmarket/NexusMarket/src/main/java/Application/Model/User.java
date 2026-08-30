package Application.Model;

import Application.Value_Objects.SystemRole;
import Application.Value_Objects.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class User {

    private String identification;
    private String fullName;
    private String email;
    private SystemRole role;
    private UserStatus status;
}