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

    private final String name;
    private double balance;
    private final double objective;

    /**
     * 2-parameter constructor
     *
     * @param name a name
     * @param balance a value
     */
    public Account(String name, double balance) {

        this(name, balance, 0);
    }

    /**
     * Constructor.
     *
     * @param name a name
     * @param balance a value
     */
    public Account(String name, double balance, double objective) {
        this.name = name;
        this.balance = balance;
        this.objective = objective;
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
     * Updates the balance with the specified amount.
     *
     * @param amount a value
     */
    public void update(double amount) {

        balance += amount;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the balance of this account
     */
    public double getBalance() {

        return balance;
    }

    /**
     * Returns the savings goal of the account.
     * 0 if no objective has been set.
     *
     * @return the savings objective
     */
    public double getObjective() {

        return objective;
    }

    /**
     * Returns the progress of the savings goal.
     * The value is negative is no goal has been set.
     *
     * @return a value between 0 and 1
     */
    public double getProgress() {

        return objective == 0 ? -1 : balance/objective;
    }

    @Override
    public String toString() {

        return String.format("Account {name:%s, balance:%s, objective:%s}", name, balance, objective);
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
                this.balance == ((Account) that).balance &&
                this.objective == ((Account) that).objective;
    }


    /**
     * Provides recording of modifications made to all existing accounts.
     */
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

        /**
         * Reverses the sign of the change values.
         *
         * @return the reversed ModMap
         */
        public ModMap reverse() {
            modMap.replaceAll((b, v) -> -v);
            return this;
        }

        /**
         * Applies the ModMap to the given account mapping.
         *
         * @param balances an account map
         */
        public void apply(Map<Byte, Account> balances) {
            Account a;
            for (byte b : modMap.keySet()) {
                a = balances.get(b);
                if (a.getBalance() == -1)
                    a.update(1);
                a.update(modMap.get(b));
                if (a.getBalance() == 0)
                    a.update(-1);
            }
        }

        public double sum() {
            double sum = modMap.values().stream()
                    .mapToDouble(Number::doubleValue)
                    .filter(d -> d != -1)
                    .sum();
            return Math.round(sum*100)/100d;
        }

        @Override
        public String toString() {

            return modMap.toString();
        }
    }
}
