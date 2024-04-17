## Description

A demo app that allows users to create and manage tasks in the form of cards:

- Application users are identified uniquely by their mail address, have a role (Member or Admin) and use a password to
  authenticate themselves before accessing cards
  - Members have access to cards they created
  - Admins have access to all cards

## Review

https://github.dev/nikosath/card-app

## How to Run

1. ./gardlew bootJar
2. docker compose up

## REST APIs
The server port is set to 28852.

### AuthController

- `POST /token`: Use Basic Auth to retrieve a JWT token needed for using the CardController operations below.
  For testing purposes the [username / password] pairs for three dummy users have been hardcoded
  in method 'userDetailsService' from [SecurityConfig](src/main/java/cardapp/auth/SecurityConfig.java). These are:
    - `member1@cards.io / memberPass1`
    - `member2@cards.io / memberPass2`
    - `admin1@cards.io / adminPass1`

### CardController
Use the token from AuthController as a bearer token to access the following operations.
Example header: `Authorization: Bearer {token}` 
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
