
# Wallet Service

A microservice developed as part of a technical challenge to manage user wallets and perform money operations such as deposits, withdrawals, transfers, and balance queries (current and historical). The system ensures full traceability and is designed with production-level concerns in mind.

## 🚀 Features

- ✅ Create new wallets for users
- 💰 Deposit funds into a wallet
- 💸 Withdraw funds from a wallet
- 🔁 Transfer funds between wallets
- 📊 Retrieve current wallet balance
- 🕓 Retrieve historical wallet balance (based on timestamp)
- 🔎 Full traceability of transactions
- ❗ Robust error handling and validation

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database (in-memory for local testing)
- Flyway
- Swagger / OpenAPI
- JUnit 5 + Mockito
- Docker/Docker Compose

---

## 📦 Project Structure

```
br.com.recargapay.walletservice
│
├── controller           → REST endpoints
├── dto                 → Request/response models
├── entity              → JPA entities (Wallet, Transaction)
├── enumeration         → TransactionType, Direction
├── exception           → Custom exceptions
├── handler             → Global error handling
├── mapper              → Converts entities to DTOs
├── repository          → JPA Repositories
├── service
│   ├── command         → Commands (create, deposit, withdraw, transfer)
│   └── query           → Queries (balance, history)
├── factory             → Transaction object creation
└── config              → Swagger/OpenAPI config
```

---

## ▶️ How to Run

### Prerequisites

- Java 21+
- Docker
- Docker compose

---

### 🐳 Running with Docker Compose (preferred)

You can run the entire application with a single script:

#### On Unix/Linux/macOS (bash):
```bash
./run.sh
```

#### On Windows (cmd):
```cmd
run.cmd
```

These scripts will:

1. Clean and package the application (skipping tests)
2. Build the Docker image
3. Start the application using Docker Compose

After successful startup, access:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

> ℹ️ If you're using Git Bash or WSL on Windows, prefer `run.sh`. For regular Command Prompt or PowerShell, use `run.cmd`.

---
#### 🧰 Running with Maven (manual)

```bash
git clone https://github.com/franciscoguinarte/wallet-ms.git
cd wallet-ms/wallet-service
./mvnw spring-boot:run
```


### Swagger UI

Access [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for the interactive API documentation.

---

## 🧪 Running Tests

```bash
./mvnw test
```

---

## 📂 API Endpoints

| Method | Endpoint                       | Description                         |
|--------|--------------------------------|-------------------------------------|
| POST   | `/wallets`                     | Create a new wallet                 |
| GET    | `/wallets/{id}/balance`        | Get current balance of wallet       |
| GET    | `/wallets/{id}/balance/history`| Get historical balance by timestamp |
| POST   | `/transactions/deposit`        | Deposit funds                       |
| POST   | `/transactions/withdraw`       | Withdraw funds                      |
| POST   | `/transactions/transfer`       | Transfer funds to another wallet    |

---

## 📬 Postman Collection

A Postman collection is available in the src/main/resources/ folder to facilitate local testing of the API.

You can import this collection into Postman and run the pre-configured requests to test the service endpoints.

---
## 🧠 Design Decisions

- **CQS Pattern**: The services are split into `command` and `query` packages for clear separation of write and read concerns.
- **Immutability of Transactions**: Transactions are persisted with the resulting balance (`balanceAfter`) to support accurate historical queries and auditing.
- **Exception Handling**: Custom exceptions and a global handler provide meaningful responses to clients.
- **Validation & Safety**: Checks for insufficient balance, invalid transfers, and wallet existence are in place.
- **DTO Isolation**: Entities are never exposed directly — only via mapped DTOs.

---

## ⚖️ Trade-offs & Time Constraints

- Chose an in-memory H2 database to speed up local development and avoid complex Docker setup.
- Focused on correctness, test coverage, and clean architecture over advanced optimizations.
- Transaction isolation levels and concurrency controls are assumed ideal due to time constraints, but could be improved in a production-grade version.

---

## ⏱️ Time Spent

**Approximate Time Invested**: _~9 hours_

---

## 📃 License

This project is provided as part of a confidential technical assessment.
