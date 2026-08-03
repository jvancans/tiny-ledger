# tiny-ledger

## Running the app (multiple options)
- Using bash and gradle wrapper: ```./gradlew bootRun```
- Using gradle: ```gradle bootRun``` (requires gradle)
- Using IDE: Run the main class ```LedgerApplication``` as Java/Spring Boot application
- Building the JAR artifact with ```gradle build``` and then running standalone JAR with ```java -jar build/libs/tiny-ledger-0.0.1-SNAPSHOT.jar```

## Running tests
- Using gradle: ```./gradlew test```
- Report can be found here: ```build/reports/tests/test/index.html```

## Overview
- There are two domain entities - `Account` and `Transaction`
- Pre-requisite for making a `Transaction` is creation of an `Account`
- During `Account` creation, `currency` is specified, all transactions and balance reflected use this currency
  - Only EUR (iso number = 978) is supported 
- `Transaction` can have positive or negative amount
  - positive amount (> 0) reflects deposit
  - negative amount (< 0) reflects withdrawal
- `Transaction` updates account balance

## Endpoints (supports application/json media types)
- Swagger UI - http://localhost:8080/swagger-ui/index.html
- API docs - http://localhost:8080/v3/api-docs (if you prefer your own Swagger UI)

## Getting started

1. Get default account
```bash
curl -X GET 'http://localhost:8080/api/v1/accounts/ce58d887-2a59-4dc1-a83a-0d74ea642a71' -H 'accept: application/json'
```
2. Get default account transactions
```bash
curl -X GET 'http://localhost:8080/api/v1/accounts/ce58d887-2a59-4dc1-a83a-0d74ea642a71/transactions' -H 'accept: application/json'
```
3. Create deposit transaction
```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/accounts/ce58d887-2a59-4dc1-a83a-0d74ea642a71/transactions' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{"amount": 1000}'
```

Full set of APIs available in Swagger UI

## Requirement completion
- "Ability to record money movements" is supported via Transaction API `POST /api/v1/accounts/{accountId}/transactions`
- "View current balance" is supported via Account API `GET /api/v1/accounts/{accountId}`
- "View transaction history" is supported via Transaction API `GET /api/v1/accounts/{accountId}/transactions`

## Additional notes
- For IDE support, Lombok plugin is required
- In-memory database is wiped between application restarts
*default* account is created during startup (id=`ce58d887-2a59-4dc1-a83a-0d74ea642a71`), it has few associated transactions

## Known limitations
- Only EUR currency is supported
- Only deposits and withdrawals to specific accounts are supported
- Missing validation
- Missing pagination (there can be N accounts/transactions in response)
- Missing transactions/atomic operations (concurrent requests can cause incorrect application behavior)
- Missing error handling