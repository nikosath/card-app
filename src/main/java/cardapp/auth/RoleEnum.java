package cardapp.auth;

import lombok.Getter;

@Getter
public enum RoleEnum {
    MEMBER("ROLE_MEMBER"), ADMIN("ROLE_ADMIN");

    private final String authority;

    RoleEnum(String authority) {
        this.authority = authority;
    }
}
