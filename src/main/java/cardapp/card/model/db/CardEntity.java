package cardapp.card.model.db;

import cardapp.card.repo.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_profile_id", "card_name"})})
@Entity
@Getter
@Setter
@ToString // TODO: exclude lazy loaded fields
@NoArgsConstructor
public class CardEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    private String cardName;
    private String description;
    private String color;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public enum StatusEnum {
        TODO,
        IN_PROGRESS,
        DONE
    }
}
