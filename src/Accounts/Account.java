package Accounts;

import Main.Methods;
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

    /**
     * Constructor when no initial balance is provided
     * @param name of the account/wallet
     */
    public Account(String name) {
        this(name, 0);
    }


    public void enterTransactions() {
        ArrayList<Transaction> pendingTransactions = new ArrayList<>();

        byte choice = -1;

        while (choice != 0) {
            System.out.println("Enter the type of transaction:\n1. Income\n2. Expense\n3. Transfer (WIP)\n0. Back");
            // Input of the type of transaction
            choice = Methods.byteInput((byte) 0, (byte) 3);

            switch (choice) {
                case 1:
                    // Income
                    pendingTransactions.add(new Income());
                    break;
                case 2:
                    // Expense
                    pendingTransactions.add(new Expense());
                    break;
                case 3:
                    // TODO: Transfers
                    System.out.println("Sorry, this feature is still in development. Tune in for updates!");
                    System.out.println("--------------------");
                    break;
                default:
                    // Going back
                    System.out.println("Thanks! I'll process that for you...");
                    System.out.println("Here is the list of the transactions you have entered:");
                    for (int i = 0 ; i < pendingTransactions.size() ; i++) {
                        System.out.print(pendingTransactions.get(i));
                        if (i < pendingTransactions.size()-1) System.out.print(" - ");
                    }
                    System.out.println("\n<-- Going back --");
                    break;
            }

        }

        this.balance += computeTransactionSum(pendingTransactions);
        this.transactionList.addAll(pendingTransactions);
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
