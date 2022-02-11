package main;

import accounts.Account;

public class Main {

    static boolean stop = false;
    static Account defaultAccount = new Account(AccountableUtil.messages.getString("DEFAULT_ACCOUNT"));

    public static void main(String[] args) {
        System.out.println(AccountableUtil.messages.getString("WELCOME"));

        while (!stop) {
            System.out.println(AccountableUtil.messages.getString("ACCOUNT_MENU"));

            byte choice = AccountableUtil.Input.byteInput((byte) 0, (byte) 2);
            switch (choice) {
                case 1:
                    // Transaction input
                    defaultAccount.enterTransactions();
                    break;
                case 2:
                    System.out.println(AccountableUtil.messages.getString("SEPARATOR"));
                    defaultAccount.browse();
                    break;
                default:
                    // Quit
                    System.out.println(AccountableUtil.messages.getString("GOODBYE"));
                    stop = true;
                    break;
            }

        }

    }

}
