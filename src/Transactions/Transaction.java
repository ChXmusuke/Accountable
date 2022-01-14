package Transactions;

import Main.Input;

public abstract class Transaction {
    private float amount;
    private String name;

    public Transaction() {
        System.out.println("Enter a name to identify the transaction:");
        this.name = Input.stringInput();

        System.out.println("Enter the amount of the transaction:");
        this.amount = Input.floatInput(0, Float.MAX_VALUE);

        System.out.println("--------------------\n" + this + "\n--------------------");
    }

    /**
     * Used to edit a transaction's amount
     * @param newAmount self-explanatory name
     */
    public void setAmount(float newAmount) {
        this.amount = newAmount;
    }

    /**
     * Used to edit a transaction's name
     * @param newName self-explanatory name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    public float getAmount() {
        return this.getMultiplier() * this.amount;
    }

    protected abstract byte getMultiplier();

    /**
     * toString override
     * @return name and amount of the transaction
     */
    @Override
    public String toString() {
        return this.name + ": " + this.getMultiplier() * this.amount;
    }
}
