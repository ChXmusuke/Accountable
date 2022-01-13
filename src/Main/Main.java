package Main;

import Transactions.Expense;
import Transactions.Income;
import Transactions.Transaction;

import java.util.ArrayList;

public class Main {

    static boolean stop = false;

    public static void main(String[] args) {
        System.out.println("Welcome to Accountable\nMade by Purrp Inc.");
        ArrayList<Transaction> transactionList = new ArrayList<>();

        while (!stop) {
            System.out.println("Enter the type of action:\n1. Transaction input\n2. Browse (WIP)\n0. Quit");

            byte choice = Methods.byteInput((byte) 0, (byte) 2);
            switch (choice) {
                case 1:
                    // Transaction input
                    enterTransaction(transactionList);
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

    public static void enterTransaction(ArrayList<Transaction> transactionList) {
        byte choice = -1;

        while (choice != 0) {
            System.out.println("Enter the type of transaction:\n1. Income\n2. Expense\n3. Transfer (WIP)\n0. Back");
            // Input of the type of transaction
            choice = Methods.byteInput((byte) 0, (byte) 3);

            switch (choice) {
                case 1:
                    // Income
                    transactionList.add(new Income());
                    break;
                case 2:
                    // Expense
                    transactionList.add(new Expense());
                    break;
                case 3:
                    // TODO: Transfers
                    System.out.println("Sorry, this feature is in development. Tune in for updates!");
                    System.out.println("--------------------");
                    break;
                default:
                    // Going back
                    System.out.println("Thanks! I'll process that for you...");
                    // TODO: better listing of entered transactions
                    System.out.println("Here is the list of the transactions you have entered:");
                    for (Transaction t:transactionList) {
                        System.out.print(t + " - ");
                    }
                    System.out.println("\n<-- Going back --");
                    break;
            }

        }

    }

}
