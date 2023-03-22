package com.chomusuke.transactions;

import java.util.*;

import static com.chomusuke.transactions.Transaction.TransactionType;

public class TransactionList {

    List<Transaction> txs = new ArrayList<>();
    private int nextRevenueIndex = 0;

    public float[] computeValues() {
        float[] values = new float[txs.size()];
        float valueP = 0;
        float valueN = 0;

        for (int i = 0 ; i < txs.size() ; i++) {
            Transaction t = txs.get(i);
            values[i] = t.value(valueP, valueN);

            if (t.transactionType().equals(TransactionType.REVENUE)) {
                valueP += values[i];
            } else {
                valueN += values[i];
                values[i] *= -1;
            }
        }

        return values;
    }

    public void add(Transaction t) {
        if (t.transactionType().equals(TransactionType.REVENUE))
            txs.add(nextRevenueIndex++, t);
        else txs.add(t);
    }
    public int size() {
        return txs.size();
    }
}
