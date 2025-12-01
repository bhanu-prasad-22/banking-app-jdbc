package com.example.bank;

import com.example.bank.dao.AccountDao;
import com.example.bank.dao.TransactionDao;
import com.example.bank.service.BankService;

import java.math.BigDecimal;
import java.util.Scanner;

public class BankApplication {
    public static void main(String[] args) {
        // configure DB connection URL & creds (change as necessary)
        String url = "jdbc:mysql://127.0.0.1:3306/bankdb?useSSL=false&serverTimezone=UTC";
        String user = "bankuser";
        String pass = "bankpass";

        AccountDao accountDao = new AccountDao(url, user, pass);
        TransactionDao txnDao = new TransactionDao();
        BankService bankService = new BankService(accountDao, txnDao);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Simple Bank CLI ---");
            System.out.println("1) Check Balance");
            System.out.println("2) Deposit");
            System.out.println("3) Withdraw");
            System.out.println("4) Transfer");
            System.out.println("5) Create Account");
            System.out.println("6) Ex1it");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Account number: ");
                        System.out.println("Balance: " + bankService.getBalance(sc.nextLine().trim()));
                        break;
                    case "2":
                        System.out.print("Account number: ");
                        String a1 = sc.nextLine().trim();
                        System.out.print("Amount: ");
                        BigDecimal damt = new BigDecimal(sc.nextLine().trim());
                        bankService.deposit(a1, damt);
                        System.out.println("Deposited.");
                        break;
                    case "3":
                        System.out.print("Account number: ");
                        String a2 = sc.nextLine().trim();
                        System.out.print("Amount: ");
                        BigDecimal wamt = new BigDecimal(sc.nextLine().trim());
                        bankService.withdraw(a2, wamt);
                        System.out.println("Withdrawn.");
                        break;
                    case "4":
                        System.out.print("From account: ");
                        String from = sc.nextLine().trim();
                        System.out.print("To account: ");
                        String to = sc.nextLine().trim();
                        System.out.print("Amount: ");
                        BigDecimal tamt = new BigDecimal(sc.nextLine().trim());
                        bankService.transfer(from, to, tamt);
                        System.out.println("Transfer completed.");
                        break;
                    case "5":
                        System.out.print("New account number: ");
                        String acct = sc.nextLine().trim();
                        System.out.print("Holder name: ");
                        String holder = sc.nextLine().trim();
                        System.out.print("Initial deposit: ");
                        BigDecimal init = new BigDecimal(sc.nextLine().trim());
                        boolean ok = accountDao.createAccount(acct, holder, init);
                        System.out.println(ok ? "Account created." : "Failed to create account.");
                        break;
                    case "0":
                        System.out.println("Bye.");
                        sc.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
}