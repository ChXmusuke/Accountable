/*  Accountable: a personal spending monitoring program
    Copyright (C) 2023  Artur Yukhanov

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

package com.chomusuke.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides memory storage for an account.
 */
public class Account {

    private String name;
    private double balance;

    /**
     * Constructor.
     *
     * @param name a name
     * @param balance a value
     */
    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    /**
     * Sets the name of the account to the specified value.
     *
     * @param newName a new name for this account
     */
    public void setName(String newName) {

        name = newName;
    }

    /**
     * Gets the name of the account.
     *
     * @return the name of this account
     */
    public String getName() {

        return name;
    }

    /**
     * Set the balance to the specified value.
     *
     * @param newBalance new value for the balance
     */
    public void setBalance(double newBalance) {

        balance = newBalance;
    }

    /**
     * Adds the specified amount to the balance.
     *
     * @param amount the amount to add
     */
    public void add(double amount) {

        balance += amount;
    }

    /**
     * Subtracts the specified amount from the balance.
     *
     * @param amount the amount to subtract
     */
    public void subtract(double amount) {

        balance -= amount;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the balance of this account
     */
    public double getBalance() {

        return balance;
    }

    @Override
    public String toString() {
        return String.format("Account {name:%s, balance:%s}", name, balance);
    }

    @Override
    public boolean equals(Object that) {
        if (that == null)
            return false;
        if (this == that)
            return true;
        if (this.getClass() != that.getClass())
            return false;

        return this.name.equals(((Account) that).name) &&
                this.balance == ((Account) that).balance;
    }



    public static class ModMap {

        Map<Byte, Float> modMap = new HashMap<>();

        /**
         * Don't let anyone instantiate this class
         */
        private ModMap() {
        }

        /**
         * Returns a mapping with the modifications to all accounts mentioned by the transactions'
         * "to" fields.
         *
         * @param txs a transaction list
         *
         * @return the corresponding ModMap
         */
        public static ModMap of(List<Transaction> txs) {
            ModMap m = new ModMap();

            if (txs != null) {
                float[] values = TransactionList.getValues(txs);

                for (int i = 0; i < txs.size(); i++) {
                    if (txs.get(i).transactionType().equals(Transaction.TransactionType.SAVINGS)) {
                        byte to = txs.get(i).to();
                        float oldValue = 0;
                        if (m.modMap.containsKey(to))
                            oldValue = m.modMap.get(to);

                        m.modMap.put(to, oldValue-values[i]);
                    }
                }
            }

            return m;
        }

        public ModMap reverse() {
            modMap.replaceAll((b, v) -> -v);
            return this;
        }

        public void apply(Map<Byte, Account> balances) {
            for (byte b : modMap.keySet()) {
                balances.get(b).add(modMap.get(b));
            }
        }

        @Override
        public String toString() {

            return modMap.toString();
        }
    }
}
