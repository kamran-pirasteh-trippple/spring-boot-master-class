---
name: testing-jpa-intro
description: Test the 06.JPA-Introduction-In-10-Steps project end-to-end. Use when verifying JPA, H2, or Spring Boot migration changes in this subproject.
---

# Testing 06.JPA-Introduction-In-10-Steps

## Prerequisites
- Java 17+ installed
- Maven installed (system `mvn` or use `./mvnw` wrapper)
- No secrets or credentials needed — H2 in-memory DB with default `sa` / empty password

## Build & Unit Tests
```bash
cd 06.JPA-Introduction-In-10-Steps
mvn clean package
```
Expect: `BUILD SUCCESS`, `Tests run: 1, Failures: 0, Errors: 0`

## Start the App
```bash
cd 06.JPA-Introduction-In-10-Steps
mvn spring-boot:run
```
The app runs on port 8080.

## Verify Startup Data Initialization
Check startup logs for these lines (in order):
1. `Hibernate: create table user (id bigint not null, name varchar(255), role varchar(255), primary key (id))` — DDL auto-generation
2. `Hibernate: insert into user (name,role,id) values (?,?,?)` — EntityManager insert
3. `New User is created : User [id=1, name=Jack, role=Admin]` — UserDaoServiceCommandLineRunner
4. `Hibernate: insert into user (name,role,id) values (?,?,?)` — Spring Data JPA insert
5. `New User is created : User [id=2, name=Jill, role=Admin]` — UserRepositoryCommandLineRunner
6. `User is retrieved : Optional[User [id=1, name=Jack, role=Admin]]` — findById
7. `All Users : [User [id=1, name=Jack, role=Admin], User [id=2, name=Jill, role=Admin]]` — findAll

Also confirm NO `ClassNotFoundException`, `NoClassDefFoundError`, or `javax.persistence` references in logs.

## Verify H2 Console
1. Open browser to `http://localhost:8080/h2-console`
2. Set JDBC URL to `jdbc:h2:mem:testdb;NON_KEYWORDS=USER`
3. User Name: `sa`, Password: (empty)
4. Click Connect
5. Verify `USER` table is visible in the left schema tree
6. Run `SELECT * FROM USER;` — expect 2 rows: Jack/Admin (id=1), Jill/Admin (id=2)

## Notes
- The `USER` table name is a reserved keyword in H2. The `NON_KEYWORDS=USER` parameter in the JDBC URL handles this.
- The Maven wrapper (`./mvnw`) might try to download an old Maven 3.5.0 which can hit rate limits. Use system `mvn` as a fallback.
- Hibernate dialect is auto-detected — no explicit dialect config is needed for H2.
- The app has two CommandLineRunners: one uses EntityManager directly (`UserDAOService`), the other uses Spring Data JPA (`UserRepository`). Both should insert data on startup.
