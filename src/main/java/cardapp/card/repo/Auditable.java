package cardapp.card.repo;

import cardapp.auth.model.db.UserProfileEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Auditable {
    // TODO: make retrieval lazy
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false, updatable = false)
    protected UserProfileEntity userProfile;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDate creationDate;
}
