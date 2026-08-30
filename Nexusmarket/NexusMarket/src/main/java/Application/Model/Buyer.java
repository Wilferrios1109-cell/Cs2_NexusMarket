package Application.Model;

import Application.Value_Objects.CommercialStatus;
import Application.Value_Objects.SystemRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Buyer extends User {

    private String primaryAddress;

    private List<String> additionalAddresses = new ArrayList<>();

    private CommercialStatus commercialStatus;

    {
        setRole(SystemRole.BUYER);
    }
}