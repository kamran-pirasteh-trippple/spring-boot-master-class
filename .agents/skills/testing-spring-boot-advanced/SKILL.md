---
name: testing-spring-boot-advanced
description: Test the 05.Spring-Boot-Advanced Spring Boot REST API end-to-end. Use when verifying changes to the survey API, security config, JPA entities, or Spring Boot version upgrades.
---

# Testing 05.Spring-Boot-Advanced

## Prerequisites

- Java 17+ installed
- Maven installed (`sudo apt-get install -y maven` if missing)

## Running Tests

### Unit + Integration Tests
```bash
cd 05.Spring-Boot-Advanced
mvn test -Dtest="SurveyControllerTest,SurveyControllerIT"
```

Note: `SurveyControllerIT` uses `*IT.java` naming but is picked up by surefire when explicitly specified. No failsafe plugin is configured.

### Starting the App
```bash
cd 05.Spring-Boot-Advanced
mvn spring-boot:run
```
App runs on port 8080.

## Auth Credentials

Credentials are defined in `SecurityConfig.java` using `{noop}` password encoding (no encryption). Check the `userDetailsManager()` bean for current usernames, passwords, and roles. Typically two users are configured: one with USER role and one with USER+ADMIN roles.

## Endpoint Test Matrix

| Endpoint | Method | Auth Required | Expected Role | Expected Status |
|----------|--------|---------------|---------------|----------------|
| /surveys/Survey1/questions | GET | Yes | USER | 200 |
| /surveys/Survey1/questions/Question1 | GET | Yes | USER | 200 |
| /surveys/Survey1/questions | POST | Yes | USER | 201 (+ Location header) |
| /welcome | GET | Yes | ADMIN | 200 |
| /dynamic-configuration | GET | Yes | ADMIN | 200 |
| /users | GET | Yes | USER | 200 (HAL JSON) |
| /actuator | GET | Yes | ADMIN | 200 |
| Any endpoint | GET | No | - | 401 |

## Security Testing

- Unauthenticated requests should return 401
- USER role accessing ADMIN-only paths (e.g. /actuator) should return 403
- ADMIN role can access all paths

## Key Data

- 4 survey questions (Question1-Question4) are hardcoded in `SurveyService`
- 4 JPA users (Ranga, Ravi, Satish, Raghu) are seeded by `UserCommandLineRunner` into H2 in-memory DB
- H2 console might be available at `/h2-console` (datasource: `jdbc:h2:mem:testdb`)

## Spring Boot 3.x Migration Notes

- `javax.persistence` -> `jakarta.persistence`
- `WebSecurityConfigurerAdapter` removed; use `@Bean SecurityFilterChain` + `@Bean InMemoryUserDetailsManager`
- `antMatchers` -> `requestMatchers`
- `authorizeRequests` -> `authorizeHttpRequests`
- `LocalServerPort` moved to `org.springframework.boot.test.web.server`
- `@WebMvcTest` POST requests need `with(csrf())` since CSRF is enabled by default in the test slice
