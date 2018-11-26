public class CurrentAccount extends Account{

    private static final long STANDARD_OVERDRAFT_ALLOWED = 1_000_000; //10.000 kr
    private static final int STANDARD_INTEREST_RATE = 10; //0.10%
    private static final long SERVICE_CHARGE = 10_000; //100 kr

    /**
     * Constructor for the CurrentAccount Class.
     * @param accountNo The account number.
     */
    public CurrentAccount(String accountNo) {
        super(accountNo);
        this.balance = 0;
        this.interestRate = STANDARD_INTEREST_RATE;
        this.overdraftAllowed = STANDARD_OVERDRAFT_ALLOWED;
    }

    /**
     * Handling withdrawal of money from accounts.
     * @param amount The amount of money to be withdrawn.
     */
    @Override
    public void withdraw(long amount) {
        if(this.balance - amount < -this.overdraftAllowed) {
            handleServiceCharge();
        }
        this.balance -= amount;
    }

    /**
     * Handling the Service Charge when allowed overdraft is surpassed.
     */
    private void handleServiceCharge() {
        //TODO - Make a transaction of amount "SERVICE_CHARGE" to the Bank's own account?
    }

}