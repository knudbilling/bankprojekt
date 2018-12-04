public class SavingsAccount extends Account
{
    private static final long STANDARD_OVERDRAFT_ALLOWED = 0; //0 kr
    private static final int STANDARD_INTEREST_RATE = 10; //0.10%

    public SavingsAccount (String accountNo)
    {
        super(accountNo);
        this.balance=0;
        this.interestRate = STANDARD_INTEREST_RATE;
        this.overdraftAllowed = STANDARD_OVERDRAFT_ALLOWED;
    }

    public void withdraw(long amount) throws NoOverdraftAllowedException, NegativeAmountException
    {
        if(amount<0)
            throw new NegativeAmountException();

        if(this.balance - amount < this.overdraftAllowed)
            throw new NoOverdraftAllowedException();
        else
            this.balance -= amount;
    }
}
