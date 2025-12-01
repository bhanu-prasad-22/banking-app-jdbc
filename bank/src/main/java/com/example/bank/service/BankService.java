package com.example.bank.service;

import com.example.bank.dao.AccountDao;
import com.example.bank.dao.TransactionDao;
import com.example.bank.model.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;

public class BankService {
    private final AccountDao accountDao;
    private final TransactionDao txnDao;

    public BankService(AccountDao accountDao, TransactionDao txnDao) {
        this.accountDao = accountDao;
        this.txnDao = txnDao;
    }

    // deposit (single-statement pattern, logs txn)
    public void deposit(String acctNo, BigDecimal amount) throws SQLException {
        try (Connection conn = accountDao.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account a = accountDao.findByAccountNumber(conn, acctNo);
                if (a == null) throw new SQLException("Account not found: " + acctNo);
                BigDecimal newBal = a.getBalance().add(amount);
                boolean ok = accountDao.updateBalance(conn, acctNo, newBal);
                if (!ok) throw new SQLException("Failed to update balance");
                txnDao.logTxn(conn, null, acctNo, amount, "DEPOSIT");
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // withdraw
    public void withdraw(String acctNo, BigDecimal amount) throws SQLException {
        try (Connection conn = accountDao.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account a = accountDao.findByAccountNumber(conn, acctNo);
                if (a == null) throw new SQLException("Account not found: " + acctNo);
                if (a.getBalance().compareTo(amount) < 0) throw new SQLException("Insufficient funds");
                BigDecimal newBal = a.getBalance().subtract(amount);
                boolean ok = accountDao.updateBalance(conn, acctNo, newBal);
                if (!ok) throw new SQLException("Failed to update balance");
                txnDao.logTxn(conn, acctNo, null, amount, "WITHDRAW");
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // transfer funds using single DB transaction (ACID)
    public void transfer(String fromAcct, String toAcct, BigDecimal amount) throws SQLException {
        try (Connection conn = accountDao.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account from = accountDao.findByAccountNumber(conn, fromAcct);
                Account to = accountDao.findByAccountNumber(conn, toAcct);
                if (from == null) throw new SQLException("From account not found: " + fromAcct);
                if (to == null) throw new SQLException("To account not found: " + toAcct);
                if (from.getBalance().compareTo(amount) < 0) throw new SQLException("Insufficient funds in from account");

                BigDecimal newFrom = from.getBalance().subtract(amount);
                BigDecimal newTo   = to.getBalance().add(amount);

                boolean u1 = accountDao.updateBalance(conn, fromAcct, newFrom);
                boolean u2 = accountDao.updateBalance(conn, toAcct, newTo);
                if (!u1 || !u2) throw new SQLException("Failed to update balances");

                txnDao.logTxn(conn, fromAcct, toAcct, amount, "TRANSFER");
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // get balance
    public BigDecimal getBalance(String acctNo) throws SQLException {
        try (Connection conn = accountDao.getConnection()) {
            Account a = accountDao.findByAccountNumber(conn, acctNo);
            if (a == null) throw new SQLException("Account not found: " + acctNo);
            return a.getBalance();
        }
    }
}