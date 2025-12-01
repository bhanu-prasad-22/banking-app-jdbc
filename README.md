# banking-app-jdbc

A simple, modular Banking Application built using Java, JDBC, and MySQL.
Supports account creation, deposits, withdrawals, balance checks, and secure money transfers using ACID transactions.


---

📌 Features

Create new bank accounts

Check account balance

Deposit money

Withdraw money (with insufficient funds validation)

Transfer money between accounts

JDBC transaction management (commit & rollback)

DAO-layer architecture (clean separation of concerns)

PreparedStatement used everywhere (SQL injection safe)

Transaction logs stored in txns table

Console-based interactive menu



---

🛠 Tech Stack

Java 17+

MySQL 8+

JDBC (MySQL Connector/J)

Maven

DAO + Service Architecture



---

📂 Project Structure

banking-app-jdbc
│
├── src/main/java/com/example/bank
│   ├── model
│   │   └── Account.java
│   ├── dao
│   │   ├── AccountDao.java
│   │   └── TransactionDao.java
│   ├── service
│   │   └── BankService.java
│   └── App.java
│
└── pom.xml


---

🗄 Database Setup (MySQL)

Run the following SQL:

CREATE DATABASE IF NOT EXISTS bankdb;
USE bankdb;

CREATE TABLE accounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_number VARCHAR(20) NOT NULL UNIQUE,
  holder_name VARCHAR(100) NOT NULL,
  balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE txns (
  id INT AUTO_INCREMENT PRIMARY KEY,
  from_account VARCHAR(20),
  to_account VARCHAR(20),
  amount DECIMAL(15,2),
  txn_type ENUM('DEPOSIT', 'WITHDRAW', 'TRANSFER'),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Create DB User:

CREATE USER 'bankuser'@'localhost' IDENTIFIED BY 'bankpass';
GRANT ALL PRIVILEGES ON bankdb.* TO 'bankuser'@'localhost';
FLUSH PRIVILEGES;


---

⚙ Configure Database Connection

Inside App.java:

String url = "jdbc:mysql://localhost:3306/bankdb?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
String user = "bankuser";
String pass = "bankpass";


---

🚀 How to Run

1. Build the project

mvn clean package

2. Run the app

mvn exec:java -Dexec.mainClass="com.example.bank.App"

Or run from IntelliJ by executing BankApplication.java.


---

💻 CLI Preview

--- Simple Bank CLI ---
1) Check Balance
2) Deposit
3) Withdraw
4) Transfer
5) Create Account
0) Exit
Choose:

Example run:

Choose: 5
New account number: ACC1003
Holder name: BhanuPrasad
Initial deposit: 5000
Account created.


---

🔒 Transaction Management (ACID)

All money operations (deposit, withdraw, transfer) are wrapped inside:

conn.setAutoCommit(false);

try {
    // SQL operations
    conn.commit();
} catch (Exception ex) {
    conn.rollback();
}

This guarantees:

No partial transfers

Balance consistency

Safe rollback on error



---

📦 Key Classes

AccountDao.java

Handles all account DB operations (find, create, update).

TransactionDao.java

Logs transaction history for auditing.

BankService.java

Implements business logic like deposit, withdraw, transfer using transactions.

App.java

Console UI for interacting with the system.


---

🚀 Future Enhancements (optional)

View transaction history

Delete account

Add concurrency safety with SELECT FOR UPDATE

Add connection pooling (HikariCP)

Convert to Spring Boot REST API

Add unit tests (JUnit + Testcontainers)



---

📜 License

This project is open-source and can be used for learning or personal development.


---

