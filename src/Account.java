public class Account {
    private String name;
    private float balance;

    public Account(String name, float initBalance) {
        this.name = name;
        this.balance = initBalance;

    }

    // Constructor when no initial balance is provided
    public Account(String name) {
        this(name, 0);

    }

}
