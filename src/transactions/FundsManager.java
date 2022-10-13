package transactions;

import java.util.*;
import util.*;

public class FundsManager {

    final private List<Set<Transaction>> transactions;
    final private Map<Byte, Float> balances;
    final private Map<String, Byte> accountNames;

    public FundsManager() {

        transactions = new ArrayList<>(List.of(new TreeSet<>()));
        balances = new HashMap<>();
        accountNames = new HashMap<>();

        // Reserved account considered as "the outside world"
        // Its balance never changes, therefore its initial value doesn't matter
        createAccount((byte) 0, "Out", 0f);
    }

    public boolean accountExists(byte address) {
        return balances.containsKey(address);
    }

    public boolean accountExists(String name) {
        return accountExists(accountNames.get(name));
    }

    private byte createAccount(byte address, String name, float initValue) {
        CheckConditions.checkArgument(!accountExists(address));

        balances.put(address, initValue);
        accountNames.put(name, address);

        return address;
    }

    public byte createAccount(String name, float initValue) {
        byte address;
        do
            address = (byte) new Random().nextInt();
        while (balances.containsKey(address));

        return createAccount(address, name, initValue);
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