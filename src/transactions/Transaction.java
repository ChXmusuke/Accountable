package transactions;

import main.AccountableUtil;

public abstract class Transaction {
    private float amount;
    private String name;

    public Transaction() {
        System.out.println(AccountableUtil.messages.getString("TRANSACTION_NAME_PROMPT"));
        setName(AccountableUtil.Input.stringInput());

        System.out.println(AccountableUtil.messages.getString("TRANSACTION_AMOUNT_PROMPT"));
        setAmount(AccountableUtil.Input.floatInput(0, Float.MAX_VALUE));

        System.out.println(AccountableUtil.messages.getString("SEPARATOR") + "\n" +
                this +
                "\n" + AccountableUtil.messages.getString("SEPARATOR"));
    }

    /**
     * Used to edit a transaction's amount
     * @param newAmount self-explanatory name
     */
    private void setAmount(float newAmount) {
        this.amount = newAmount;
    }

    /**
     * Used to edit a transaction's name
     * @param newName self-explanatory name
     */
    private void setName(String newName) {
        this.name = newName;
    }

    /**
     * Method called when the user wants to modify a transaction
     * @return true if the transaction is deleted
     */
    public boolean modifyTransaction() {
        byte choice = -1;

        while (choice != 0) {
            System.out.println(AccountableUtil.messages.getString("TRANSACTION_MODIFY_PROMPT"));
            // Input of the data the user wants to modify
            choice = AccountableUtil.Input.byteInput((byte) 0, (byte) 3);

            switch (choice) {
                case 1:
                    // Name
                    System.out.println(AccountableUtil.messages.getString("TRANSACTION_NAME_PROMPT"));
                    String oldName = this.getName();
                    String newName = AccountableUtil.Input.stringInput();
                    this.setName(newName);
                    // Displays the old and new names
                    System.out.printf(AccountableUtil.messages.getString("TRANSACTION_NEW_NAME") + "%n", oldName, this.getName());
                    break;

                case 2:
                    // Amount
                    System.out.println(AccountableUtil.messages.getString("TRANSACTION_AMOUNT_PROMPT"));
                    float oldAmount = this.getAmount();
                    float newAmount = AccountableUtil.Input.floatInput(0, Float.MAX_VALUE);
                    this.setAmount(newAmount);
                    // Displays the old and new amounts
                    System.out.printf(AccountableUtil.messages.getString("TRANSACTION_NEW_AMOUNT") + "%n", oldAmount, this.getAmount());
                    break;

                case 3:
                    // Prompt to confirm the deletion of the transaction
                    System.out.printf(AccountableUtil.messages.getString("TRANSACTION_DELETE_CONFIRM") + "%n", this.getName());
                    choice = AccountableUtil.Input.byteInput((byte) 0, (byte) 1);
                    if (choice == 1) {
                        // Option to cancel the deletion
                        choice = -1;
                        break;
                    }

                    return true;
            }
            System.out.println(AccountableUtil.messages.getString("SEPARATOR"));

        }

        return false;
    }

    public String getName() {
        return this.name;
    }

    public float getAmount() {
        return this.getMultiplier() * this.amount;
    }

    // Multiplier is 1 if the transaction is an income, -1 if it is an expense
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
