package cardapp.card;

import cardapp.common.Uri;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CardController {
    public static final String COLOR_PATTERN = "[0-9a-fA-F]{6}";
    private final CardService cardService;

    @GetMapping(Uri.CARDS)
    public List<CardResponseDto> getCards() {
        return cardService.findAll().stream()
                .map(MapUtil::toCardResponseDto).toList();
    }

    @GetMapping(Uri.CARDS + "/{cardName}")
    public CardResponseDto getCard(@PathVariable String cardName) {
        return MapUtil.toCardResponseDto(cardService.getCardByUserAndByNameOrThrow(cardName));
    }

    @PostMapping(Uri.CARDS)
    public CardResponseDto createCard(@RequestBody @Valid CreateCardDto card) {
        return MapUtil.toCardResponseDto(
                cardService.createCard(card.name(), card.color(), card.description())
        );
    }

    @PutMapping(Uri.CARDS + "/{cardName}")
    public CardResponseDto updateCard(@PathVariable String cardName, @RequestBody @Valid UpdateCardDto card) {
        return MapUtil.toCardResponseDto(
                cardService.updateCard(cardName, card.name(), card.color(), card.description(),
                        MapUtil.toStatusEnumElseThrow(card.status()))
        );
    }

    @GetMapping(Uri.CARDS + "/search")
    List<CardResponseDto> searchCards(@RequestParam(required = false) String name,
                                      @Pattern(regexp = COLOR_PATTERN)
                                      @RequestParam(required = false) String color,
                                      @RequestParam(required = false) String status,
                                      @RequestParam(required = false) LocalDate creationDate,
                                      @RequestParam(required = false) Integer pageNumber,
                                      @RequestParam(required = false) Integer pageSize,
                                      @RequestParam(required = false) String sortBy) {

        return cardService.searchCards(
                        name, color, creationDate,
                        MapUtil.toStatusEnumElseThrow(status),
                        MapUtil.toPage(pageNumber, pageSize, sortBy)
                )
                .stream().map(MapUtil::toCardResponseDto)
                .toList();
    }

    @DeleteMapping(Uri.CARDS + "/{cardName}")
    public void deleteCard(@PathVariable String cardName) {
        cardService.deleteCardByName(cardName);
    }

    @Builder
    public record CreateCardDto(@NotBlank String name, @Pattern(regexp = COLOR_PATTERN) String color,
                                String description) {
    }

    @Builder
    public record UpdateCardDto(@NotBlank String name, @Pattern(regexp = COLOR_PATTERN) String color,
                                String description, String status) {
    }

    public record CardResponseDto(String cardName, String description, String color, String status) {
    }
}
