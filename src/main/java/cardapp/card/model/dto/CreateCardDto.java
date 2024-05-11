package cardapp.card.model.dto;

import cardapp.card.controller.CardController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateCardDto(@NotBlank String name, @Pattern(regexp = CardController.COLOR_PATTERN) String color,
                            String description) {
}
