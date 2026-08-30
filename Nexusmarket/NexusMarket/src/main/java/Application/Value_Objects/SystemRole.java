package Application.Value_Objects;

public final class SystemRole extends DomainCatalog {

    public static final SystemRole BUYER =
            new SystemRole(
                    "BUYER","Buyer","Participant who acquires products published in the Marketplace."
            );

    public static final SystemRole SELLER =
            new SystemRole(
                    "SELLER","Seller","Participant responsible for registering and managing products."
            );

    public static final SystemRole LOGISTICS_OPERATOR =
            new SystemRole(
                    "LOGISTICS_OPERATOR","Logistics Operator","Participant responsible for warehouse and dispatch operations."
            );

    public static final SystemRole ADMINISTRATOR =
            new SystemRole(
                    "ADMINISTRATOR","Administrator","Participant responsible for managing sellers and warehouses."
            );

    public static final SystemRole SUPERVISOR =
            new SystemRole(
                    "SUPERVISOR","Supervisor","Participant responsible for consultation and operational monitoring."
            );

    private SystemRole(
            String code,
            String name,
            String description) {

        super(code, name, description);
    }
}