package cardapp.card.service;

import cardapp.auth.model.db.UserProfileEntity;
import cardapp.card.error.CardAppException;
import cardapp.card.model.db.CardEntity;
import cardapp.card.model.db.CardEntity.StatusEnum;
import cardapp.card.repo.CardDao;
import cardapp.common.controller.UserContext;
import cardapp.common.model.db.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static cardapp.card.model.db.CardEntity.StatusEnum.TODO;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardDao cardDao;

    private static boolean isAdmin(UserProfileEntity currentUser) {
        return RoleEnum.ADMIN.equals(currentUser.getRole());
    }

    public Optional<CardEntity> getCardByUserAndByName(String cardName) {
        UserProfileEntity currentUser = UserContext.getCurrentUser();
        if (isAdmin(currentUser)) {
            return cardDao.findByCardName(cardName);
        }
        return cardDao.findByUserProfileUsernameAndCardName(currentUser.getUsername(), cardName);
    }

    public CardEntity getCardByUserAndByNameOrThrow(String cardName) {
        return getCardByUserAndByName(cardName)
                .orElseThrow(() -> CardAppException.notFound("Card not found"));
    }

    public List<CardEntity> findAll() {
        UserProfileEntity currentUser = UserContext.getCurrentUser();
        if (isAdmin(currentUser)) {
            return cardDao.findAll();
        }
        List<CardEntity> allUserCards = cardDao.findAllByUser(currentUser.getUserProfileId());
        log.debug("allUserCards: {}", allUserCards);
        return allUserCards;
    }

    public CardEntity createCard(String name, String color, String description) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardName(name);
        cardEntity.setColor(color);
        cardEntity.setDescription(description);
        cardEntity.setStatus(TODO);
        return cardDao.save(cardEntity);
    }


    public List<CardEntity> searchCards(String name, String color, LocalDate creationDate,
                                 StatusEnum statusEnum, Pageable page) {

        UserProfileEntity currentUser = UserContext.getCurrentUser();
        if (isAdmin(currentUser)) {
            return cardDao.searchCards(name, color, statusEnum, creationDate, page);
        }
        return cardDao.searchCards(currentUser.getUserProfileId(), name, color, statusEnum, creationDate, page);
    }

    public CardEntity updateCard(String currentName, String newName, String newColor, String newDescription,
                          StatusEnum newStatus) {

        CardEntity cardEntity = getCardByUserAndByNameOrThrow(currentName);
        cardEntity.setCardName(newName);
        cardEntity.setColor(newColor);
        cardEntity.setDescription(newDescription);
        cardEntity.setStatus(newStatus);
        return cardDao.save(cardEntity);
    }

    public void deleteCardByName(String cardName) {
        CardEntity cardEntity = getCardByUserAndByNameOrThrow(cardName);
        cardDao.delete(cardEntity);
    }
}
