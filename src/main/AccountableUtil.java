package main;

import transactions.Transaction;

import java.util.*;

public class AccountableUtil {

    public static ResourceBundle messages = ResourceBundle.getBundle("resources.messages.messages", Locale.ENGLISH);

    /**
     * @param transactionList a list with transactions. (Thanks Sherlock.)
     * @return the sum of all transactions in transactionList
     */
    public static float computeTransactionSum(ArrayList<Transaction> transactionList) {
        float newBalance = 0;
        for (Transaction t : transactionList) {
            newBalance += t.getAmount();
        }

        return newBalance;
    }

    public static void printTransactionListInfo(ArrayList<Transaction> list) {
        for (int i = 0 ; i < list.size() ; i++) {
            System.out.printf("%d) %s%n", (i+1), list.get(i));
        }
        System.out.println(AccountableUtil.messages.getString("SEPARATOR"));
        System.out.printf(AccountableUtil.messages.getString("TOTAL") + "%n", computeTransactionSum(list));

    }


    // A sub-class of general methods allowing user input
    public static class Input {

        private static final Scanner scanner = new Scanner(System.in);

        /**
         * Method used to prompt the user for a floating point number
         * @param lowerBound for the value given by the user
         * @param upperBound for the value given by the user
         * @return the number entered
         * @throws NumberFormatException when the value entered is not valid
         */
        public static float floatInput(float lowerBound, float upperBound) throws NumberFormatException {
            float number;

            while (true) {
                try {
                    number = Float.parseFloat(scanner.nextLine());
                    if (number < lowerBound || number > upperBound) throw new NumberFormatException();
                    break;
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println(AccountableUtil.messages.getString("INVALID_FLOAT"));
                }
            }
            return number;
        }

        /**
         * Method used to prompt the user for a byte-sized integer.
         * The bounds are included in the interval.
         * @param lowerBound for the value given by the user
         * @param upperBound for the value given by the user
         * @return the number entered
         * @throws NumberFormatException when the value entered is not valid
         */
        public static byte byteInput(byte lowerBound, byte upperBound) throws NumberFormatException {
            byte number;

            while (true) {
                try {
                    number = Byte.parseByte(scanner.nextLine());
                    if (number < lowerBound || number > upperBound) throw new NumberFormatException();
                    break;
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println(AccountableUtil.messages.getString("INVALID_NUMBER"));
                }
            }
            return number;
        }

        public static int intInput(int lowerBound, int upperBound) {
            int number;

            while (true) {
                try {
                    number = Integer.parseInt(scanner.nextLine());
                    if (number < lowerBound || number > upperBound) throw new NumberFormatException();
                    break;
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println(AccountableUtil.messages.getString("INVALID_NUMBER"));
                }
            }
            return number;
        }

        public static String stringInput() {
            return scanner.nextLine();
        }

    }

}
