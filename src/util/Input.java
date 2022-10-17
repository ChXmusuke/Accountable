package util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Allows for basic I/O in the command prompt.
 */
public final class Input {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Don't let anyone instantiate this class.
     */
    private Input() {
    }

    /**
     * Prompts the user for a floating point number.
     * The bounds are included in the interval.
     *
     * @param lowerBound
     *                   for the value given by the user
     * @param upperBound
     *                   for the value given by the user
     *
     * @return the number entered
     *
     * @throws NumberFormatException
     *                               when the value entered is not valid
     */
    public static float floatInput(float lowerBound, float upperBound) throws NumberFormatException {
        float number;

        while (true) {
            try {
                number = Float.parseFloat(scanner.nextLine());
                if (number < lowerBound || number > upperBound)
                    throw new NumberFormatException();
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(Locales.messages.getString("INVALID_FLOAT"));
            }
        }
        return number;
    }

    /**
     * Prompts the user for a byte-sized integer.
     * The bounds are included in the interval.
     *
     * @param lowerBound
     *                   for the value given by the user
     * @param upperBound
     *                   for the value given by the user
     *
     * @return the number entered
     *
     * @throws NumberFormatException
     *                               when the value entered is not valid
     */
    public static byte byteInput(byte lowerBound, byte upperBound) throws NumberFormatException {
        byte number;

        while (true) {
            try {
                number = Byte.parseByte(scanner.nextLine());
                if (number < lowerBound || number > upperBound)
                    throw new NumberFormatException();
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(Locales.messages.getString("INVALID_NUMBER"));
            }
        }
        return number;
    }

    /**
     * Prompts the user for an int-sized integer.
     * The bounds are included in the interval.
     *
     * @param lowerBound
     *                   for the value given by the user
     * @param upperBound
     *                   for the value given by the user
     *
     * @return the number entered
     *
     * @throws NumberFormatException
     *                               when the value entered is not valid
     */
    public static int intInput(int lowerBound, int upperBound) {
        int number;

        while (true) {
            try {
                number = Integer.parseInt(scanner.nextLine());
                if (number < lowerBound || number > upperBound)
                    throw new NumberFormatException();
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(Locales.messages.getString("INVALID_NUMBER"));
            }
        }
        return number;
    }

    /**
     * Prompts the user for a textual input.
     *
     * @return the string entered
     */
    public static String stringInput() {
        return scanner.nextLine();
    }
}
