package com.example.bank.dao;

import com.example.bank.model.Account;

import java.sql.*;
import java.math.BigDecimal;

public class AccountDao {
    private final String url;
    private final String user;
    private final String pass;

    public AccountDao(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    // get connection
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    // find by account number (uses provided connection for tx support)
    public Account findByAccountNumber(Connection conn, String acctNo) throws SQLException {
        String sql = "SELECT id, account_number, holder_name, balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acctNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account a = new Account();
                    a.setId(rs.getInt("id"));
                    a.setAccountNumber(rs.getString("account_number"));
                    a.setHolderName(rs.getString("holder_name"));
                    a.setBalance(rs.getBigDecimal("balance"));
                    return a;
                } else return null;
            }
        }
    }

    // update balance (part of transaction)
    public boolean updateBalance(Connection conn, String acctNo, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.setString(2, acctNo);
            int affected = ps.executeUpdate();
            return affected == 1;
        }
    }

    // create account (standalone)
    public boolean createAccount(String acctNo, String holder, BigDecimal initial) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, holder_name, balance) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acctNo);
            ps.setString(2, holder);
            ps.setBigDecimal(3, initial);
            return ps.executeUpdate() == 1;
        }
    }
}