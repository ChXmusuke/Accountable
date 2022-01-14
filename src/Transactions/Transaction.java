package Transactions;

import Main.Methods;

public abstract class Transaction {
    private float amount;
    private String name;

    public Transaction() {
        System.out.println("Enter a name to identify the transaction:");
        this.name = Methods.stringInput();

        System.out.println("Enter the amount of the transaction:");
        this.amount = Methods.floatInput(0, Float.MAX_VALUE);

        System.out.println("--------------------\n" + this + "\n--------------------");
    }

    /**
     * Used to edit a transaction's amount
     * @param newAmount self-explanatory name
     */
    public void setAmount(float newAmount) {
        this.amount = newAmount;
    }

    public float getAmount() {
        return this.amount;
    }

    public abstract float add();

    /**
     * toString override
     * @return name and amount of the transaction
     */
    @Override
    public String toString() {
        return this.name + ": " + this.amount;
    }
}
