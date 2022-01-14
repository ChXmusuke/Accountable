package Accounts;

import Transactions.*;

import java.util.ArrayList;

public class Account {
    private final String name;
    private float balance;
    private final ArrayList<Transaction> transactionList = new ArrayList<>();

    /**
     * Constructor
     * @param name of the account/wallet
     * @param initBalance initial balance of the account/wallet
     */
    public Account(String name, float initBalance) {
        this.name = name;
        this.balance = initBalance;
    }

    // Constructor when no initial balance is provided
    public Account(String name) {
        this(name, 0);
    }

    public void addTransactions(ArrayList<Transaction> transactionList) {
        this.balance += computeTransactionSum(transactionList);
        this.transactionList.addAll(transactionList);
    }

    private float computeTransactionSum(ArrayList<Transaction> transactionList) {
        float newBalance = 0;
        for (Transaction t : transactionList) {
            newBalance += t.getAmount();
        }

        return newBalance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", transactionList=" + transactionList +
                '}';
    }
}
