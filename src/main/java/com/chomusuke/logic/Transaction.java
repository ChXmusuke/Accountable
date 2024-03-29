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

import com.chomusuke.util.Preconditions;

/**
 * This record provides memory storage of transactions.
 */
public record Transaction(String name,
                          byte to,
                          TransactionType transactionType,
                          ValueType valueType,
                          float value) {

    /**
     * Canonical constructor.
     *
     * @param name the name of the tx
     * @param to the destination account if tx type is SAVINGS TODO: not used yet
     * @param transactionType the type of tx
     * @param valueType the type of value of the tx
     * @param value The absolute value of the tx
     */
    public Transaction {
        Preconditions.checkArgument(transactionType != TransactionType.REVENUE || valueType == ValueType.ABSOLUTE);
    }

    /**
     * Packs the enum values of {@code transactionType}
     * into a single byte.
     *
     * @return the packed byte value
     */
    public byte packTypes() {
        return (byte) ((this.transactionType().ordinal() << 2) | this.valueType().ordinal());
    }

    /**
     * Returns the value computed from the transaction's type.
     *
     * @param total The total value of the session
     * @param used The value of the session that's already
     *             been used
     *
     * @return the value of the transaction, context-aware
     */
    public float value(float total, float used) {
        switch (valueType) {
            case ABSOLUTE -> {
                return value;
            }
            case TOTAL -> {
                float v = Math.max(value/100*total, 0);
                return Math.round(v*100)/100f;
            }
            case REMAINDER -> {
                float v = Math.max((total-used) * value/100, 0);
                return Math.round(v*100)/100f;
            }
            case ALL -> {
                float v = Math.max(total-used, 0);
                return Math.round(v*100)/100f;
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null)
            return false;
        else if (this == that)
            return true;
        else if (getClass() != that.getClass())
            return false;

        Transaction o = (Transaction) that;

        return name.equals(o.name)
                && to == o.to()
                && transactionType == o.transactionType()
                && valueType == o.valueType()
                && value == o.value();
    }


    /**
     * This enum provides constants for transaction types.
     */
    public enum TransactionType {
        REVENUE,
        BUDGET,
        BILL,
        SAVINGS;

        /**
         * Unpacks the byte {@code b} into a {@code TransactionType}.
         *
         * @param b a byte
         *
         * @return a {@code TransactionType}
         */
        public static TransactionType of(byte b) {
            b &= 0b11;

            return values()[b];
        }
    }

    /**
     * This enum provides constants for value computation types.
     */
    public enum ValueType {
        ABSOLUTE,
        TOTAL,
        REMAINDER,
        ALL;

        /**
         * Unpacks the byte {@code b} into a {@code ValueType}.
         *
         * @param b a byte
         *
         * @return a {@code ValueType}
         */
        public static ValueType of(byte b) {
            b &= 0b11;

            return values()[b];
        }
    }
}