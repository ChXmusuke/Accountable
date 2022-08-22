import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AccountableUtilTest {

    @Test
    public void extractWorksProperly() {
        short a = 0b00011100;
        short b = util.Bits.extract(a, 2, 4);

        Assertions.assertEquals((short)0b111, b);
    }
}
