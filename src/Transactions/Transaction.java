package Transactions;

import Main.Methods;

public abstract class Transaction {
    private float amount;

    public Transaction() {
        System.out.println("Enter the amount of the transaction:");

        this.amount = Methods.floatInput(0, Float.MAX_VALUE);
    }

    public void setAmount(float newAmount) {
        this.amount = newAmount;
    }
    public float getAmount() {
        return this.amount;
    }
    public abstract float add();
}
