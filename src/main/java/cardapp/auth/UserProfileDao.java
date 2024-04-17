package cardapp.auth;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileDao extends JpaRepository<UserProfileEntity, Long> {
    UserProfileEntity findByUsername(String username);
}
