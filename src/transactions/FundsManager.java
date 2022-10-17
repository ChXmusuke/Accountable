package transactions;

import java.util.*;
import util.*;

/**
 * This class is the main part of the program.
 * Its purpose is to manage the transaction data and accounts.
 */
public class FundsManager {

    final private List<Set<Transaction>> transactions;
    final private Map<Byte, Float> balances;
    final private Map<String, Byte> accountNames;

    /**
     * Constructor for the FundsManager class.
     * It automatically creates an account at address 0,
     * which represents the "outside world".
     */
    public FundsManager() {

        transactions = new ArrayList<>(List.of(new TreeSet<>()));
        balances = new HashMap<>();
        accountNames = new HashMap<>();

        // Reserved account considered as "the outside world"
        // Its balance never changes, therefore its initial value doesn't matter
        createAccount((byte) 0, "Out", 0f);
    }

    /**
     * Checks for the existence of an account at the given address.
     * 
     * @param address
     *                the address that the user wants to check
     * 
     * @return true if the address is mapped to an account, else false
     */
    public boolean accountExists(byte address) {
        return balances.containsKey(address);
    }

    /**
     * Checks for the existence of an account at the address corresponding to the
     * given name.
     * 
     * @param name
     *             the name of the account that the user wants to check
     * 
     * @return true if the name is mapped to an account, else false
     */
    public boolean accountExists(String name) {
        return accountExists(accountNames.get(name));
    }

    /**
     * Creates an account with specified address, name and initial value.
     * 
     * @param address
     *                  an address, as byte
     * @param name
     *                  a name, as String
     * @param initValue
     *                  an initial value, as float
     * 
     * @return the address of the newly created acccount
     */
    private byte createAccount(byte address, String name, float initValue) {
        CheckConditions.checkArgument(!accountExists(address));

        balances.put(address, initValue);
        accountNames.put(name, address);

        return address;
    }

    /**
     * Creates an account with specified name and initial value.
     * the address is chosen randomly among the ones available.
     * 
     * @param name
     *                  a name, as String
     * @param initValue
     *                  an initial value, as float
     * 
     * @return the address of the newly created acccount
     */
    public byte createAccount(String name, float initValue) {
        byte address;
        do
            address = (byte) new Random().nextInt();
        while (balances.containsKey(address));

        return createAccount(address, name, initValue);
    }

    /**
     * Checks the balance of an account.
     * 
     * @param address
     *                an address, as byte
     * 
     * @return the balance of the address
     */
    public float getBalance(byte address) {

        return balances.get(address);
    }

    /**
     * Adds a transaction to the ledger and updates all account's balances.
     * 
     * @param t
     *          a transaction
     * 
     * @return the added transaction
     */
    public Transaction addTransaction(Transaction t) {

        transactions.get(DateUtil.extractYear(t.date()) - DateUtil.FIRST_YEAR)
                .add(t);

        updateBalances(t);

        return t;
    }

    /**
     * Removes a transaction from the ledger and updates all account's balances.
     * 
     * @param t
     *          a transaction
     * 
     * @return the removed transaction
     */
    public Transaction removeTransaction(Transaction t) {

        transactions.get(DateUtil.extractYear(t.date()) - DateUtil.FIRST_YEAR)
                .remove(t);

        updateBalances(t.reverse());

        return t;
    }

    /**
     * Replaces transaction t with transaction newT.
     * 
     * @param t
     *             the transaction to replace
     * @param newT
     *             the transaction replacing t
     * 
     * @return newT
     */
    public Transaction modifyTransaction(Transaction t, Transaction newT) {
        removeTransaction(t);

        addTransaction(newT);

        return newT;
    }

    /**
     * Updates the balances of the accounts mentioned in transaction t.
     * 
     * @param t
     *          a transaction
     */
    private void updateBalances(Transaction t) {
        byte from = t.from();
        byte to = t.to();
        float value = t.value();

        // The balance of the account 0 is never changed
        if (from != 0)
            balances.put(from, balances.get(from) - value);
        if (to != 0)
            balances.put(to, balances.get(to) + value);
    }

    /**
     * !!! TO BE DEPRECATED !!!
     * 
     * Dumps all the transaction data
     * 
     * @return all the transaction data, as a list of sets
     */
    public List<Set<Transaction>> dumpTransactions() {
        return transactions;
    }
}