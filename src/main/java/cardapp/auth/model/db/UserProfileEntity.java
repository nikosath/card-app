package cardapp.auth.model.db;

import cardapp.common.model.db.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

// TODO: Add bean validation annotations to all Entities
@Entity
@Getter
@Setter
@ToString // exclude lazy loaded fields
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UserProfileEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    @Transient
    private transient Set<GrantedAuthority> authorities;

    public UserProfileEntity(String username, String password, RoleEnum role) {
        this.username = username;
        this.password = password;
        setRole(role);
    }

    private void setRole(RoleEnum role) {
        this.role = role;
        setAuthoritiesFromRole(role);
    }

    private void setAuthoritiesFromRole(RoleEnum role) {
        this.authorities = Set.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    public Set<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            setAuthoritiesFromRole(role);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
