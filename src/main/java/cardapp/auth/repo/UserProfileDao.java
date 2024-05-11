package cardapp.auth.repo;

import cardapp.auth.model.db.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileDao extends JpaRepository<UserProfileEntity, Long> {
    UserProfileEntity findByUsername(String username);
}
