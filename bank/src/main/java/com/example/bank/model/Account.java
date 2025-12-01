package com.example.bank.model;

import java.math.BigDecimal;

public class Account {
    private int id;
    private String accountNumber;
    private String holderName;
    private BigDecimal balance;

    public Account() {}

    public Account(String accountNumber, String holderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    @Override
    public String toString() {
        return String.format("Account[%s, %s, balance=%s]", accountNumber, holderName, balance);
    }
}