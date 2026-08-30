package Application.Value_Objects;

import java.util.Objects;

public abstract class DomainCatalog {

    private final String code;
    private final String name;
    private final String description;

    protected DomainCatalog(
            String code,
            String name,
            String description) {

        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        DomainCatalog other = (DomainCatalog) object;

        return Objects.equals(code, other.code)
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, description);
    }

    @Override
    public String toString() {
        return code;
    }
}