package transactions;

import util.CheckConditions;

import java.util.Map;
import java.util.Random;

public class AccountManager {

    private final Map<Byte, Float> balances;

    public AccountManager(Map<Byte, Float> balances) {
        this.balances = balances;
    }

    public boolean accountExists(byte address) {
        return balances.containsKey(address);
    }

    public void createAccount(byte address, float initValue) {
        CheckConditions.checkArgument(!accountExists(address));

        byte a;
        do
            a = (byte) new Random().nextInt();
        while (balances.containsKey(a));

        balances.put(a, initValue);
    }

    public void updateBalances(Transaction transaction) {
        byte from = transaction.getFrom();
        byte to = transaction.getTo();
        float value = transaction.getValue();

        balances.put(from, balances.get(from)-value);
        balances.put(to, balances.get(to)+value);
    }
}
