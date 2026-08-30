package Application.Value_Objects;

public final class CommercialStatus extends DomainCatalog {

    private CommercialStatus(
            String code,
            String name,
            String description) {

        super(code, name, description);
    }

    public static CommercialStatus of(
            String code,
            String name,
            String description) {

        return new CommercialStatus(
                code,
                name,
                description
        );
    }
}