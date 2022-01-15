package accounts;

import main.AccountableUtil;
import transactions.*;

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


    /**
     * Used to enter new transactions. The user can choose either an income, an expense
     * or a transfer between 2 accounts (the latter is a WIP).
     * The method creates a buffer in which the new transactions are stored, and
     * adds all its content to the overall transaction list.
     */
    public void enterTransactions() {
        // Buffer
        ArrayList<Transaction> pendingTransactions = new ArrayList<>();

        byte choice = -1;

        while (choice != 0) {
            System.out.println(AccountableUtil.messages.getString("TRANSACTION_PROMPT"));
            // Input of the type of transaction
            choice = AccountableUtil.Input.byteInput((byte) 0, (byte) 3);

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
                    System.out.println(AccountableUtil.messages.getString("INDEV_FEATURE"));
                    System.out.println(AccountableUtil.messages.getString("SEPARATOR"));
                    break;
            }

        }

        // Going back
        System.out.println(AccountableUtil.messages.getString("ENTER_TRANSACTION_EXIT"));
        System.out.println(AccountableUtil.messages.getString("SEPARATOR"));
        AccountableUtil.printTransactionListInfo(pendingTransactions);
        System.out.println("\n" + AccountableUtil.messages.getString("BACK") + "\n");

        // Add the sum of all new transactions to the already registered ones
        this.balance += AccountableUtil.computeTransactionSum(pendingTransactions);
        this.transactionList.addAll(pendingTransactions);
    }

    public void browse() {
        // Choice is an integer instead of a byte to prevent problems with large transaction lists.
        // TODO: transactions separated by year/month hub
        int choice = -1;

        while (choice != 0) {
            if (this.transactionList.size() > 0) {
                // Displays the transactions and the total sum
                System.out.println(AccountableUtil.messages.getString("TRANSACTIONS_LIST"));
                AccountableUtil.printTransactionListInfo(this.transactionList);
                System.out.printf(AccountableUtil.messages.getString("TRANSACTION_SELECT_PROMPT") + "%n", this.transactionList.size());
                choice = AccountableUtil.Input.intInput(0, this.transactionList.size());
            } else {
                System.out.printf(AccountableUtil.messages.getString("NO_TRANSACTIONS") + "%n", this.name);
                choice = 0;
            }
        }

        System.out.println(AccountableUtil.messages.getString("BACK"));
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
