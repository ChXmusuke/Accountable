package transactions;

import java.util.*;
import util.*;

public class FundsManager {

    final List<Set<Transaction>> transactions;
    final Map<Byte, Float> balances;

    public FundsManager() {

        transactions = new ArrayList<>(List.of(new TreeSet<>()));
        balances = new HashMap<>();

        // Reserved account considered as "the outside world"
        // Its balance never changes, therefore its initial value doesn't matter
        createAccount((byte) 0, 0f);
    }

    public boolean accountExists(byte address) {
        return balances.containsKey(address);
    }

    public boolean createAccount(byte address, float initValue) {
        CheckConditions.checkArgument(!accountExists(address));

        balances.put(address, initValue);
        return true;
    }

    public boolean createAccount(float initValue) {
        byte a;
        do
            a = (byte) new Random().nextInt();
        while (balances.containsKey(a));

        return createAccount(a, initValue);
    }

    public float getBalance(byte address) {

        return balances.get(address);
    }

    public Transaction addTransaction(
            String name,
            int date,
            byte from,
            byte to,
            float value,
            byte category,
            List<Byte> tags) {
        Transaction t = new Transaction(
                name,
                date,
                from,
                to,
                value,
                category,
                tags);

        transactions.get(DateUtil.extractYear(date) - DateUtil.FIRST_YEAR)
                .add(t);

        updateBalances(t);

        return t;
    }

    private void updateBalances(Transaction transaction) {
        byte from = transaction.getFrom();
        byte to = transaction.getTo();
        float value = transaction.getValue();

        if (from != 0)
            balances.put(from, balances.get(from) - value);
        if (to != 0)
            balances.put(to, balances.get(to) + value);
    }
}