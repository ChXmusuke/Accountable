package Main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Methods {

    private static Scanner scanner = new Scanner(System.in);

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
                System.out.println("Please enter a valid number, or use a dot (.) instead of a comma (,) as a decimal separator.");
            }
        }
        return number;
    }

    /**
     * Method used to prompt the user for a byte-sized integer
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
                System.out.println("Please enter a valid number.");
            }
        }
        return number;
    }
}
