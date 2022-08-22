package util;

public class CheckConditions {

    /**
     * Don't let anyone instantiate this class
     */
    private CheckConditions() {}

    public static void checkArgument(boolean args) {
        if (!args)
            throw new IllegalArgumentException();
    }
}
