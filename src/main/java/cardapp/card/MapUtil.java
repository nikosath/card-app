package cardapp.card;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static cardapp.card.CardEntity.StatusEnum.*;

public class MapUtil {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static CardEntity.StatusEnum toStatusEnumElseThrow(String status) {
        return toStatusEnum(status).orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
    }

    public static Pageable toPage(Integer pageNumber, Integer pageSize, String sortBy) {
        return PageRequest.of(pageNumber == null ? 0 : pageNumber,
                pageSize == null ? DEFAULT_PAGE_SIZE : pageSize,
                sortBy == null ? Sort.unsorted() : Sort.by(Sort.Direction.ASC, sortBy));
    }

    public static CardController.CardResponseDto toCardResponseDto(CardEntity card) {
        return new CardController.CardResponseDto(card.getCardName(), card.getDescription(), card.getColor(),
                toStatus(card.getStatus()));
    }

    private static Optional<CardEntity.StatusEnum> toStatusEnum(String status) {
        if (status == null) {
            return Optional.empty();
        }
        var statusEnum = switch (status) {
            case "To Do" -> TODO;
            case "In Progress" -> IN_PROGRESS;
            case "Done" -> DONE;
            default -> null;
        };
        return Optional.ofNullable(statusEnum);
    }

    public static String toStatus(CardEntity.StatusEnum statusEnum) {
        return switch (statusEnum) {
            case TODO -> "To Do";
            case IN_PROGRESS -> "In Progress";
            case DONE -> "Done";
        };
    }
}
