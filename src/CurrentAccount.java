public class CurrentAccount extends Account{

    private static final long STANDARD_OVERDRAFT_ALLOWED = 1_000_000; //10.000 kr
    private static final int STANDARD_INTEREST_RATE = 10;
    private static final long SERVICE_CHARGE = 10_000; //100 kr
    
    /**
     * Constructor for the CurrentAccount Class.
     * @param accountNo
     */
    public CurrentAccount(String accountNo) {
        super(accountNo);
        this.balance = 0;
        this.interestRate = STANDARD_INTEREST_RATE;
        this.overdraftAllowed = STANDARD_OVERDRAFT_ALLOWED;
    }
    
    @Override
    public void withdraw(long amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
