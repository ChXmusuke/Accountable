package transactions;

import java.util.*;

import util.DateUtil;

public class TxManager {

    private final List<Set<Transaction>> transactions;

    public TxManager(List<Set<Transaction>> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(
            String name,
            int date,
            byte from,
            byte to,
            float value,
            byte category,
            List<Byte> tags) {

        transactions.get(DateUtil.extractYear(date)-DateUtil.FIRST_YEAR)
                .add(new Transaction(
                        name,
                        date,
                        from,
                        to,
                        value,
                        category,
                        tags));
    }
}
