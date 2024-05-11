package cardapp.card;

import cardapp.auth.controller.AuthController;
import cardapp.auth.model.dto.TokenDto;
import cardapp.card.controller.CardController;
import cardapp.card.model.dto.CardResponseDto;
import cardapp.card.model.dto.CreateCardDto;
import cardapp.card.model.dto.UpdateCardDto;
import cardapp.common.constant.TestConstants;
import cardapp.common.constant.Uri;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import static cardapp.common.constant.TestConstants.MEMBER1_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardAppIT {

    @Autowired
    AuthController authController;
    @Autowired
    CardController cardController;
    @Autowired
    private TestRestTemplate restTemplate;

    // TODO: Add proper IT tests with separated use cases and unit tests for each app layer.
    @Test
    public void testOperationSequence() {
        // given member1 token and 2 new cards
        String jwtTokenMember1 = createJwtTokenForUser(MEMBER1_USERNAME, TestConstants.MEMBER1_PASSWORD);
        var card1Member1 = createCard(CreateCardDto.builder().name("card1-name-member1").build(), jwtTokenMember1);
        var card2Member1 = createCard(CreateCardDto.builder().name("card2-name-member1").color("AA11BB").build(),
                jwtTokenMember1);

        // given member2 token and 2 new cards
        String jwtTokenMember2 = createJwtTokenForUser(TestConstants.MEMBER2_USERNAME, TestConstants.MEMBER2_PASSWORD);
        var card1Member2 = createCard(CreateCardDto.builder().name("card1-name-member2").build(), jwtTokenMember2);
        var card2Member2 = createCard(CreateCardDto.builder().name("card2-name-member2")
                .description("card2-description-member2").build(), jwtTokenMember2);

        // expect only 2 cards for member1
        assertThat(getAllCardsForUser(jwtTokenMember1)).hasSize(2);

        // given admin1 token and 2 new cards
        String jwtTokenAdmin1 = createJwtTokenForUser(TestConstants.ADMIN1_USERNAME, TestConstants.ADMIN1_PASSWORD);
        var card1Admin1 = createCard(CreateCardDto.builder().name("card1-name-admin1").build(), jwtTokenAdmin1);
        var card2Admin1 = createCard(CreateCardDto.builder().name("card2-name-admin1")
                .description("card2-description-admin1").build(), jwtTokenAdmin1);

        // expect 6 cards for admin1
        assertThat(getAllCardsForUser(jwtTokenAdmin1)).hasSize(6);

        // when member1 updates card1 to Done
        updateStatus(card1Member1.getBody().cardName(), "Done", jwtTokenMember1);
        // expect 5 'To Do' cards for admin1
        assertThat(findCardsByStatus(jwtTokenAdmin1, "To Do")).hasSize(5);
        // expect 1 'To Do' card for member1
        assertThat(findCardsByStatus(jwtTokenMember1, "To Do")).hasSize(1);
    }

    public CardResponseDto updateStatus(String cardName, String status, String jwtToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        UpdateCardDto body = UpdateCardDto.builder().name(cardName).status(status).build();
        var request = new HttpEntity<>(body, headers);
        var resp = restTemplate.exchange(Uri.CARDS + "/" + cardName, HttpMethod.PUT, request, CardResponseDto.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        return resp.getBody();
    }

    private CardResponseDto[] findCardsByStatus(String jwtToken, String status) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        var uriBuilder = UriComponentsBuilder.fromUriString(Uri.CARDS + "/search")
                .queryParam("status", status)
                .build();
        var request = new HttpEntity<>(headers);

        var resp = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, CardResponseDto[].class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        return resp.getBody();
    }

    private CardResponseDto[] getAllCardsForUser(String jwtToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        var request = new HttpEntity<>(headers);
        var resp = restTemplate.exchange(Uri.CARDS, HttpMethod.GET, request, CardResponseDto[].class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        return resp.getBody();
    }

    private ResponseEntity<CardResponseDto> createCard(CreateCardDto dto, String jwtToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        var createRequest = new HttpEntity<>(dto, headers);
        return restTemplate.postForEntity(Uri.CARDS, createRequest, CardResponseDto.class);
    }

    private String createJwtTokenForUser(String username, String password) {
        var authApiHeaders = new HttpHeaders();
        authApiHeaders.setBasicAuth(username, password);
        var tokenRequest = new HttpEntity<>(authApiHeaders);
        var tokenResponse = restTemplate.postForEntity(Uri.TOKEN, tokenRequest, TokenDto.class);
        assertThat(tokenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        return tokenResponse.getBody().token();
    }

}
