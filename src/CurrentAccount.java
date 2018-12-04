public class CurrentAccount extends Account{

    private static final long STANDARD_OVERDRAFT_ALLOWED = 1_000_000; //10.000 kr
    private static final int STANDARD_INTEREST_RATE = 10;

    /**
     * Constructor for the CurrentAccount Class.
     * @param accountNo
     */
    public CurrentAccount(String accountNo) {
        super(accountNo);
        this.balance = 0;
        this.interestRate = STANDARD_INTEREST_RATE;
        this.allowedOverdraft = STANDARD_OVERDRAFT_ALLOWED;
    }

    @Override
    public void withdraw(long amount) throws NegativeAmountException {
        if (amount < 0 )
            throw new NegativeAmountException();

        balance -= amount;

    }

}
