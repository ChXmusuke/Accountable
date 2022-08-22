package util;

/**
 * This class allows to check a boolean parameter
 */
public class CheckConditions {

    /**
     * Don't let anyone instantiate this class
     */
    private CheckConditions() {}

    /**
     * Checks if the given boolean argument is correct
     *
     * @param  conditions
     *         The conditions on the arguments
     *
     * @throws IllegalArgumentException
     *         if the conditions are not met
     */
    public static void checkArgument(boolean conditions) throws IllegalArgumentException {
        if (!conditions)
            throw new IllegalArgumentException();
    }
}
