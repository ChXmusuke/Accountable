package transactions;

import main.AccountableUtil;

public abstract class Transaction {
    private float amount;
    private String name;

    public Transaction() {
        System.out.println(AccountableUtil.messages.getString("TRANSACTION_NAME_PROMPT"));
        this.name = AccountableUtil.Input.stringInput();

        System.out.println(AccountableUtil.messages.getString("TRANSACTION_AMOUNT_PROMPT"));
        this.amount = AccountableUtil.Input.floatInput(0, Float.MAX_VALUE);

        System.out.println(AccountableUtil.messages.getString("SEPARATOR") + "\n" +
                this +
                "\n" + AccountableUtil.messages.getString("SEPARATOR"));
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

    // Multiplier is 1 when the transaction is an income, -1 if it is an expense
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
