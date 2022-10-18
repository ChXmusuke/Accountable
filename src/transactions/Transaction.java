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

package transactions;

import java.util.*;

import util.CheckConditions;

/**
 * Provides storage of transactions.
 */
public record Transaction(String name, int date, byte from, byte to, float value, byte category, List<Byte> tags)
        implements Comparable<Transaction> {

    /**
     * Constructor for Transaction
     * 
     * @param name
     *                 a name, as String
     * @param date
     *                 a date, as int
     *                 Must be packed in 23-4-5 format
     * @param from
     *                 the address of the sender, as byte
     * @param to
     *                 the address of the receiver, as byte
     * @param value
     *                 the value of the transaction, as float
     * @param category
     *                 the category of the transaction, as byte
     * @param tags
     *                 the list of tags assigned to the transaction, as a List of
     *                 bytes
     */
    public Transaction {
        CheckConditions.checkArgument(from != to);
    }

    /**
     * Reverses the current transaction.
     * 
     * @return
     *         the current transaction with sender and receiver swapped
     */
    public Transaction reverse() {
        byte _from = this.to;
        byte _to = this.from;

        return new Transaction(this.name, this.date, _from, _to, this.value, this.category, this.tags);
    }

    /**
     * Comparison implementation.
     * 
     * Compares two transactions according to their dates.
     * 
     * @param that
     *             another transaction
     * 
     * @return -1, 0 or 1 if the current transaction is smaller, equal or greater
     *         then the other transaction according to their dates
     */
    @Override
    public int compareTo(Transaction that) {
        return Integer.compare(this.date, that.date);
    }

    /**
     * Builds a Transaction object.
     */
    public static class TransactionBuilder {

        private String name;
        private int date;
        private byte from;
        private byte to;
        private float value;
        private byte category;
        private List<Byte> tags;

        /**
         * Constructor for a TransactionBuilder.
         * 
         * @param name
         *                 a name, as String
         * @param date
         *                 a date, as int (must be packed in 23-4-5 format)
         * @param from
         *                 the address of the sender, as byte
         * @param to
         *                 the address of the receiver, as byte
         * @param value
         *                 the value of the transaction, as float
         * @param category
         *                 the category of the transaction, as byte
         * @param tags
         *                 the list of tags assigned to the transaction, as a List of
         *                 bytes
         */
        public TransactionBuilder(String name, int date, byte from, byte to, float value, byte category,
                List<Byte> tags) {
            CheckConditions.checkArgument(from != to);

            this.name = name;
            this.date = date;
            this.from = from;
            this.to = to;
            this.value = value;
            this.category = category;
            this.tags = List.copyOf(tags);
        }

        /**
         * Creates a TransactionBuilder by copying transaction t's data.
         * 
         * @param t
         *          the transaction to copy
         */
        public TransactionBuilder(Transaction t) {
            this.tags = new ArrayList<>();

            setName(t.name());
            setDate(t.date());
            setFrom(t.from());
            setTo(t.to());
            setValue(t.value());
            setCategory(t.category());
            addTagList(t.tags());
        }

        /**
         * Constructor for an empty TransactionBuilder.
         */
        public TransactionBuilder() {
            this.tags = new ArrayList<>();
        }

        /**
         * Sets the name of the TransactionBuilder.
         * 
         * @param name
         *             a name, as String
         * 
         * @return the transaction with the new name
         */
        public TransactionBuilder setName(String name) {
            this.name = name;

            return this;
        }

        /**
         * Sets the date of the TransactionBuilder.
         * 
         * @param date
         *             a date, as int (must be packed in 23-4-5 format)
         * 
         * @return the transaction with the new date
         */
        public TransactionBuilder setDate(int date) {
            this.date = date;

            return this;
        }

        /**
         * Sets the sender of the TransactionBuilder.
         * 
         * @param from
         *             a sender, as byte
         * 
         * @return the transaction with the new sender
         */
        public TransactionBuilder setFrom(byte from) {
            CheckConditions.checkArgument(from != this.to);

            this.from = from;

            return this;
        }

        /**
         * Sets the receiver of the TransactionBuilder.
         * 
         * @param to
         *           a receiver, as byte
         * 
         * @return the transaction with the new receiver
         */
        public TransactionBuilder setTo(byte to) {
            CheckConditions.checkArgument(to != this.from);

            this.to = to;

            return this;
        }

        /**
         * Sets the value of the TransactionBuilder.
         * 
         * @param value
         *              a value, as float
         * 
         * @return the transaction with the new value
         */
        public TransactionBuilder setValue(float value) {
            this.value = value;

            return this;
        }

        /**
         * Sets the category of the TransactionBuilder.
         * 
         * @param from
         *             a category, as byte
         * 
         * @return the transaction with the new category
         */
        public TransactionBuilder setCategory(byte category) {
            this.category = category;

            return this;
        }

        /**
         * Adds a tag to the TransactionBuilder.
         * 
         * @param tag
         *            a tag, as byte
         * 
         * @return the transaction with the new tag added
         */
        public TransactionBuilder addTag(byte tag) {
            tags.add(tag);

            return this;
        }

        /**
         * Adds all the tags in a list to the TransactionBuilder.
         * 
         * @param tags
         *             a list of tags, as a List of bytes
         * 
         * @return the transaction with the new tags added
         */
        public TransactionBuilder addTagList(List<Byte> tags) {
            this.tags.addAll(tags);

            return this;
        }

        /**
         * Removes a tag from the TransactionBuilder.
         * 
         * @param tag
         *            a tag, as byte
         * 
         * @return the transaction with the tag removed
         */
        public TransactionBuilder removeTag(byte tag) {
            tags.remove(tag);

            return this;
        }

        /**
         * builds the transaction as an immutable object.
         * 
         * @return the transaction built from the builder's data
         */
        public Transaction build() {
            return new Transaction(name, date, from, to, value, category, tags);
        }
    }
}
