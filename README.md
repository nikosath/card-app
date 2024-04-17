## Description

A demo app that allows users to create and manage tasks in the form of cards:

- Application users are identified uniquely by their mail address, have a role (Member or Admin) and use a password to
  authenticate themselves before accessing cards
    - Members have access to cards they created
    - Admins have access to all cards

## Review

https://github.dev/nikosath/card-app

## How to Run

1. ./gradlew bootJar
2. docker compose up

## REST APIs

The server port is set to 28852.
Thanks to SpringDoc OpenAPI, the API JSON is available at http://localhost:28852/api-docs .
A basic UI for trying out the API will be available later on at http://localhost:28852/swagger-ui/index.html .

### AuthController

- `POST /token`: Use Basic Auth to retrieve a JWT token needed for using the CardController operations below.
  For testing purposes the [username / password] pairs for three dummy users have been hardcoded
  in method 'userDetailsService' from [SecurityConfig](src/main/java/cardapp/auth/SecurityConfig.java). These are:
    - `member1@cards.io / memberPass1`
    - `member2@cards.io / memberPass2`
    - `admin1@cards.io / adminPass1`

### CardController

Use the token from AuthController as a bearer token to access the following operations.
Example header: `Authorization: Bearer {token}`.
With the current application.properties, no data is kept between app restarts.

- `GET /cards`: Retrieves all cards.
- `GET /cards/search`: Searches for cards based on given parameters.
- `POST /cards`: Creates a new card.
- `GET /cards/{cardName}`: Retrieves a card by its name.
- `PUT /cards/{cardName}`: Updates a card by its name.
- `DELETE /cards/{cardName}`: Deletes a card by its name.

## TODOs

- Improve integration with SpringDoc OpenAPI.
- Add proper IT tests with separated use cases and unit tests for each app layer.
- Don't use email as username.
- Consider making @Entity retrieval lazy.
- Exclude lazy loaded fields from Lombok.
- Add bean validation annotations to all @Entity.
- Consider making UserContext a spring bean.
