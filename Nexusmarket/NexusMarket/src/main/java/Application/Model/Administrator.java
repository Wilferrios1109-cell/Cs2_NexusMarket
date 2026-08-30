package Application.Model;

import Application.Value_Objects.SystemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Administrator extends User {

    {
        setRole(SystemRole.ADMINISTRATOR);
    }
}