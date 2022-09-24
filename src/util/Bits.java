package util;

/**
 * This class provides methods to work on bit vectors.
 */
public class Bits {

    /**
     * Don't let anyone instantiate this class
     */
    private Bits() {}

    /**
     * Extracts a vector of given length from a 16-bit short
     *
     * @param  vector
     *         A 16-bit vector
     * @param  start
     *         The first bit of the extracted vector
     * @param  length
     *         The length of the extracted vector
     * @return The trimmed 16-bit vector
     */
    public static short extractUnsigned(int vector, int start, int length) {
        CheckConditions.checkArgument(validExtraction(start, length));

        vector = vector << Integer.SIZE - (start + length);
        vector = vector >>> Integer.SIZE - length;

        vector &= 0xFF;

        return (short) vector;
    }

    /**
     * Helper method used to check the validity of a bit extraction.
     *
     * @param  start
     *         The start of the bit vector
     * @param  length
     *         The length of the bit vector
     * @return Validity of the extraction (bool)
     */
    private static boolean validExtraction(int start, int length){

        return 0<=start && start+length<=Integer.SIZE && length>=0 && length<Integer.SIZE;
    }
}