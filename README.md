## Review

https://github.dev/nikosath/card-app

## How to Run
1. ./gardlew bootJar
2. docker compose up

## List of TODOs
- Add proper IT tests with separated use cases and unit tests for each app layer.
- Consider making @Entity retrieval lazy.
- Exclude lazy loaded fields from Lombok.
- Add bean validation annotations to all @Entity.
- Make UserContext a spring bean.
- Don't use email as username.
- Use Swagger for API documentation.
