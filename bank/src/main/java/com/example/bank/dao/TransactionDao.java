package com.example.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;

public class TransactionDao {
    public boolean logTxn(Connection conn, String fromAcct, String toAcct, BigDecimal amount, String type) throws SQLException {
        String sql = "INSERT INTO txns (from_account, to_account, amount, txn_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fromAcct);
            ps.setString(2, toAcct);
            ps.setBigDecimal(3, amount);
            ps.setString(4, type);
            return ps.executeUpdate() == 1;
        }
    }
}