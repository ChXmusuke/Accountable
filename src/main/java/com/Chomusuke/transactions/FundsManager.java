/*  Accountable: a personal spending monitoring program
    Copyright (C) 2022  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.chomusuke.transactions;

import java.util.*;

import com.chomusuke.util.*;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

/**
 * Instances of this class bind transaction data with stateful balance info and
 * account names.
 */
public class FundsManager {
    private final SimpleMapProperty<Byte, Float> balancesProperty;
    final private SimpleMapProperty<String, Byte> accountNamesProperty;
    final private Storage storage;

    /**
     * Constructor for the FundsManager class.
     * It automatically creates an account at address 0,
     * which represents the "outside world".
     */
    public FundsManager() {

        balancesProperty = new SimpleMapProperty<>(FXCollections.observableHashMap());
        accountNamesProperty = new SimpleMapProperty<>(FXCollections.observableHashMap());
        storage = new Storage();

        // Reserved account considered as "the outside world"
        // Its balance never changes, therefore its initial value doesn't matter
        createAccount((byte) 0, "Out", 0f);
    }

    public MapProperty<Byte, Float> balancesProperty() {
        return balancesProperty;
    }

    public MapProperty<String, Byte> accountNamesProperty() {
        return accountNamesProperty;
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
        return balancesProperty.containsKey(address);
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
        return accountNamesProperty.containsKey(name);
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
        // The account should not exist yet
        if (accountExists(address) || accountExists(name))
            return (accountExists(address) ? address : accountNamesProperty.get(name));

        // Creates entries for the account's name and balance
        balancesProperty.put(address, initValue);
        accountNamesProperty.put(name, address);

        System.out.println(this);  // TODO: Remove

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
        while (balancesProperty.containsKey(address));

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

        return balancesProperty.get(address);
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

        storage.addTransaction(t);

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

        storage.removeTransaction(t);

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
            balancesProperty.put(from, balancesProperty.get(from) - value);
        if (to != 0)
            balancesProperty.put(to, balancesProperty.get(to) + value);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (String n : accountNamesProperty.keySet()) {
            b.append(java.lang.String.format("%s(%s) : %s$\n",
                    n,
                    java.lang.Byte.toUnsignedInt(accountNamesProperty.get(n)),
                    balancesProperty.get(accountNamesProperty.get(n))));
        }

        return b.toString();
    }
}