package Main;

import Accounts.Account;

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
                    defaultAccount.enterTransactions();
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


}
