public abstract class Account {
    protected final String accountNumber;
    protected long balance;
    protected long allowedOverdraft;
    protected int interestRate;

    /**
     * Minimal Constructor for the Account Class.
     * @param accountNo The account number.
     */
    public Account(String accountNo) {
        this.accountNumber = accountNo;
    }

    /**
     * Handling the depositing of money into the account.
     * @param amount The amount of money to be deposited.
     */
    public void deposit(long amount) {
        if(amount > 0) {
            this.balance += amount;
        } else {
            //DO STUFF?
        }
    }
    
    /**
     * Abstract method to be implemented by subclasses.
     * Handling withdrawal of money from accounts.
     * @param amount The amount of money to be withdrawn.
     */
    public abstract void withdraw(long amount) throws NoOverdraftAllowedException, NegativeAmountException;

    public String getAccountNumber() { return this.accountNumber; }
    public long getBalance() { return this.balance; }
    public long getAllowedOverdraft() { return this.allowedOverdraft; }
    public int getInterestRate() { return this.interestRate; }


    public void setAllowedOverdraft(long allowedOverdraft) {
        this.allowedOverdraft = allowedOverdraft;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    public String getAccountStatus(){
        return ""+getBalance();
    }

}
