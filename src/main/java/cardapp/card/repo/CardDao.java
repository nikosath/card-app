package cardapp.card.repo;

import cardapp.card.model.db.CardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardDao extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByUserProfileUsernameAndCardName(String username, String cardName);

    default List<CardEntity> searchCards(String name, String color, CardEntity.StatusEnum status,
                                         LocalDate creationDate, Pageable pageable) {
        return searchCards(null, name, color, status, creationDate, pageable);
    }

    @Query("SELECT c FROM CardEntity c WHERE " +
            "(:userProfileId IS NULL OR c.userProfile.id = :userProfileId) " +
            "AND (:cardName IS NULL OR c.cardName = :cardName) " +
            "AND (:color IS NULL OR UPPER(c.color) = UPPER(:color)) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:creationDate IS NULL OR c.creationDate = :creationDate)")
    List<CardEntity> searchCards(
            @Param("userProfileId") Long userProfileId,
            @Param("cardName") String cardName,
            @Param("color") String color,
            @Param("status") CardEntity.StatusEnum status,
            @Param("creationDate") LocalDate creationDate,
            Pageable pageable
    );

    Optional<CardEntity> findByCardName(String cardName);

    @Query("SELECT c FROM CardEntity c WHERE c.userProfile.id = :userProfileId")
    List<CardEntity> findAllByUser(@Param("userProfileId") Long userProfileId);

}
