package transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.CheckConditions;

public class Transaction implements Comparable<Transaction> {

    private final int id;

    private String name;
    private int date;
    private byte from;
    private byte to;
    private float value;
    private byte category;
    private List<Byte> tags = new ArrayList<>();

    public Transaction(String name, int date, byte from, byte to, float value, byte category, List<Byte> tags) {
        CheckConditions.checkArgument(from != to);

        this.id = new Random().nextInt();

        this.name = name;
        this.date = date;
        this.from = from;
        this.to = to;
        this.value = value;
        this.category = category;
        this.tags.addAll(tags);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public byte getFrom() {
        return from;
    }

    public byte getTo() {
        return to;
    }

    public float getValue() {
        return value;
    }

    public byte getCategory() {
        return category;
    }

    @Override
    public int compareTo(Transaction that) {
        return Integer.compare(this.date, that.date);
    }
}
