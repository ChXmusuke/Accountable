package transactions;

public class Expense extends Transaction {

    @Override
    protected byte getMultiplier() {
        return -1;
    }
}
