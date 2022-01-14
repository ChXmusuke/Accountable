package Main;

import Accounts.Account;
import Transactions.*;

import java.util.ArrayList;

public class Main {

    static boolean stop = false;
    static Account defaultAccount = new Account("Default account");

    public static void main(String[] args) {
        System.out.println("Welcome to Accountable\nMade by Purrp Inc.");

        while (!stop) {
            System.out.println("Enter the type of action:\n1. Transaction input\n2. Browse (WIP)\n0. Quit");

            byte choice = Methods.byteInput((byte) 0, (byte) 2);
            switch (choice) {
                case 1:
                    // Transaction input
                    enterTransaction(defaultAccount);
                    break;
                case 2:
                    // TODO: Browse
                    System.out.println("Sorry, this feature is in development. Tune in for updates!");
                    System.out.println("--------------------");
                    break;
                default:
                    // Quit
                    System.out.println("It was good to have you!");
                    stop = true;
                    break;
            }

        }

    }

    public static void enterTransaction(Account account) {
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
                    System.out.println("Sorry, this feature is in development. Tune in for updates!");
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

        account.addTransactions(pendingTransactions);
    }

}
