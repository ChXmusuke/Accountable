package transactions;

public class Income extends Transaction {

    @Override
    protected byte getMultiplier() {
        return 1;
    }
}