package Application.Model;

import Application.Value_Objects.SystemRole;
import Application.Value_Objects.UserStatus;
import lombok.Getter;

@Getter
public class LogisticsOperator extends User {

    public LogisticsOperator(
            String identification,
            String fullName,
            String email,
            UserStatus status) {

        super(identification, fullName, email, status);
    }

    @Override
    public SystemRole getRole() {
        return SystemRole.LOGISTICS_OPERATOR;
    }
}
