import transactions.AccountManager;
import transactions.Transaction;
import transactions.TxManager;

import java.util.*;

import util.DateUtil;

public class Main {

    public static void main(String[] args) {

        // TODO: files
        List<Set<Transaction>> transactions = new ArrayList<>(List.of(new TreeSet<>()));
        Map<Byte, Float> balances = new HashMap<>();
        DateUtil.FIRST_YEAR = 2022;

        TxManager transactionManager = new TxManager(transactions);
        AccountManager accountManager = new AccountManager(balances);
    }
}
