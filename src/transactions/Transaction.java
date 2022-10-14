package transactions;

import java.util.*;

import util.CheckConditions;

public record Transaction(String name, int date, byte from, byte to, float value, byte category, List<Byte> tags)
        implements Comparable<Transaction> {

    public Transaction {
        CheckConditions.checkArgument(from != to);
    }

    public Transaction reverse() {
        byte _from = this.to;
        byte _to = this.from;

        return new Transaction(this.name, this.date, _from, _to, this.value, this.category, this.tags);
    }

    @Override
    public int compareTo(Transaction that) {
        return Integer.compare(this.date, that.date);
    }

    public static class TransactionBuilder {

        private String name;
        private int date;
        private byte from;
        private byte to;
        private float value;
        private byte category;
        private List<Byte> tags;

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

        public TransactionBuilder() {
            this.tags = new ArrayList<>();
        }

        public TransactionBuilder setName(String name) {
            this.name = name;

            return this;
        }

        public TransactionBuilder setDate(int date) {
            this.date = date;

            return this;
        }

        public TransactionBuilder setFrom(byte from) {
            CheckConditions.checkArgument(from != this.to);

            this.from = from;

            return this;
        }

        public TransactionBuilder setTo(byte to) {
            CheckConditions.checkArgument(to != this.from);

            this.to = to;

            return this;
        }

        public TransactionBuilder setValue(float value) {
            this.value = value;

            return this;
        }

        public TransactionBuilder setCategory(byte category) {
            this.category = category;

            return this;
        }

        public TransactionBuilder addTag(byte tag) {
            tags.add(tag);

            return this;
        }

        public TransactionBuilder addTagList(List<Byte> tags) {
            this.tags.addAll(tags);

            return this;
        }

        public TransactionBuilder removeTag(byte tag) {
            tags.remove(tag);

            return this;
        }

        public Transaction build() {
            return new Transaction(name, date, from, to, value, category, tags);
        }
    }
}
