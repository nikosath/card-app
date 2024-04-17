## Review

https://github.dev/nikosath/card-app

## How to Run

1. ./gardlew bootJar
2. docker compose up

## REST APIs

### AuthController
- `POST /auth/token`: Use Basic Auth to retrieve a JWT token needed for using the CardController operations below.
  For testing purposes the [username / password] pairs for three dummy users have been hardcoded
  in method 'userDetailsService' from [SecurityConfig](src/main/java/cardapp/auth/SecurityConfig.java). These are:
    - `member1@cards.io / memberPass1`
    - `member2@cards.io / memberPass2`
    - `admin1@cards.io / adminPass1`

### CardController

- `GET /cards`: Retrieves all cards.
- `GET /cards/search`: Searches for cards based on given parameters.
- `POST /cards`: Creates a new card.
- `GET /cards/{cardName}`: Retrieves a card by its name.
- `PUT /cards/{cardName}`: Updates a card by its name.
- `DELETE /cards/{cardName}`: Deletes a card by its name.

## TODOs
- Add proper IT tests with separated use cases and unit tests for each app layer.
- Consider making @Entity retrieval lazy.
- Exclude lazy loaded fields from Lombok.
- Add bean validation annotations to all @Entity.
- Make UserContext a spring bean.
- Don't use email as username.
- Use Swagger for API documentation.
