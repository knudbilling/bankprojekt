public abstract class Account {
    protected final String accountNo;
    protected long balance;
    protected long overdraftAllowed;
    protected int interestRate;

    /**
     * Minimal Constructor for the Account Class.
     * @param accountNo The account number.
     */
    public Account(String accountNo) {
        this.accountNo = accountNo;
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
    public abstract void withdraw(long amount);

    public String getAccountNo() { return this.accountNo; }
    public long getBalance() { return this.balance; }
    public long getOverdraftAllowed() { return this.overdraftAllowed; }
    public int getInterestRate() { return this.interestRate; }


    public void setOverdraftAllowed(long overdraftAllowed) {
        this.overdraftAllowed = overdraftAllowed;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    public String getAccountStatus(){
        return ""+getBalance();
    }

}
