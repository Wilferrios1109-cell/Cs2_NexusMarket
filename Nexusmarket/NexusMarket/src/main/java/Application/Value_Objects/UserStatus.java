package Application.Value_Objects;

public final class UserStatus extends DomainCatalog {

    public static final UserStatus ACTIVE =
            new UserStatus(
                    "ACTIVE","Active","User is active and can operate within the Marketplace."
            );

    public static final UserStatus BLOCKED =
            new UserStatus(
                    "BLOCKED","Blocked","User is blocked from operating within the Marketplace."
            );

    private UserStatus(
            String code,
            String name,
            String description) {

        super(code, name, description);
    }
}