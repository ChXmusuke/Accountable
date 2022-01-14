package Main;

import Transactions.Transaction;

import java.util.List;

public class Methods {

    /**
     * Prints all the elements of a list in a single line.
     * @param list the list we want to print
     */
    public static void printList(List<Transaction> list) {
        for (int i = 0 ; i < list.size() ; i++) {
            System.out.print(list.get(i));
            if (i < list.size()-1) System.out.print(" | ");
        }
    }
    public static void printListVertically(List<Transaction> list) {
        for (Transaction transaction : list) System.out.println(transaction);
    }
}
