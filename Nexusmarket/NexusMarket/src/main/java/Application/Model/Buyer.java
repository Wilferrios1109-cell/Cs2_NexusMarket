package Application.Model;

import Application.Value_Objects.CommercialStatus;
import Application.Value_Objects.SystemRole;
import Application.Value_Objects.UserStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Buyer extends User {

    private final String primaryAddress;
    private final List<String> additionalAddresses;
    private final CommercialStatus commercialStatus;

    public Buyer(
            String identification,
            String fullName,
            String email,
            UserStatus status,
            String primaryAddress,
            CommercialStatus commercialStatus) {

        super(identification, fullName, email, status);

        this.primaryAddress = primaryAddress;
        this.additionalAddresses = new ArrayList<>();
        this.commercialStatus = commercialStatus;
    }

    @Override
    public SystemRole getRole() {
        return SystemRole.BUYER;
    }

    public List<String> getAdditionalAddresses() {
        return Collections.unmodifiableList(additionalAddresses);
    }

    public void addAddress(String address) {
        additionalAddresses.add(address);
    }

    public void removeAddress(String address) {
        additionalAddresses.remove(address);
    }
}
