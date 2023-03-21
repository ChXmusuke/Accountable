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

/**
 * Provides memory storage of transactions.
 *
 * @param name the name of the tx
 * @param to the destination account if tx type is SAVINGS
 * @param transactionType the type of tx
 * @param valueType the type of value of the tx
 * @param value The absolute value of the tx
 */
public record Transaction(String name,
                          byte to,
                          TransactionType transactionType,
                          ValueType valueType,
                          float value) {

    /**
     * Packs the enum values of {@code transactionType}
     * into a single byte.
     *
     * @return the packed byte value
     */
    public byte packTypes() {
        return (byte) ((TransactionType.convert(this.transactionType()) << 2) | ValueType.convert(this.valueType()));
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
                return value*total;
            }
            case REMAINDER -> {
                return (total-used) * value;
            }
            default -> {
                return total-used;
            }
        }
    }

    public enum TransactionType {
        REVENUE,
        BUDGET,
        BILL,
        SAVINGS;

        public static byte convert(TransactionType t) {
            switch (t) {
                case REVENUE -> {
                    return 0;
                }
                case BUDGET -> {
                    return 1;
                }
                case BILL -> {
                    return 2;
                }
                default -> {
                    return 3;
                }
            }
        }
    }

    public enum ValueType {
        ABSOLUTE,
        TOTAL,
        REMAINDER,
        ALL;

        public static byte convert(ValueType v) {
            switch (v) {
                case ABSOLUTE -> {
                    return 0;
                }
                case TOTAL -> {
                    return 1;
                }
                case REMAINDER -> {
                    return 2;
                }
                default -> {
                    return 3;
                }
            }
        }
    }
}