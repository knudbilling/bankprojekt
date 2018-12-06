import java.util.Date;

public class Transaction {
    final Account fromAccount;
    final Account toAccount;
    final long amount;
    final Date timestamp;
    String bankReference;

    {
        this.timestamp=new Date();
    }

    /**
     * Constructs a transaction from two account objects
     * @param fromAccount
     * @param toAccount
     * @param amount
     */
    public Transaction(Account fromAccount, Account toAccount, long amount){
        this.fromAccount=fromAccount;
        this.toAccount=toAccount;
        this.amount=amount;
    }

    /**
     * Constructs a transaction from two account numbers
     * @param bank the local bank
     * @param fromAccountNumber the account number to move money from
     * @param toAccountNumber the account number to move money to
     * @param amount the amount to move expressed in 'øre'
     */
    public Transaction(Bank bank, String fromAccountNumber, String toAccountNumber, long amount)
            throws NegativeAmountException, IllegalAccountException {

        // Error on: amount is negative
        if(amount<0)
            throw new NegativeAmountException();
        this.amount=amount;

        // fromAccount
        // Error on: fromAccount is in an invalid format
        if(!AccountNumber.isValidFormat(fromAccountNumber)) {
            System.out.println("***ERROR: Account number not in a valid format***");
            throw new NumberFormatException();
        }
        // Error on: fromAccount is not local
        if(!AccountNumber.isLocal(bank,fromAccountNumber)) {
            System.out.println("***ERROR: Trying to transfer from an external account***");
            throw new IllegalArgumentException();
        }
        this.fromAccount=AccountNumber.getAccount(bank,fromAccountNumber);

        // toAccount
        // Error on: toAccount is in an invalid format
        if(!AccountNumber.isValidFormat(toAccountNumber)) {
            System.out.println("ERROR: Account number is not in a valid format***");
            throw new NumberFormatException();
        }

        // Local toAccount
        if(AccountNumber.isLocal(bank,toAccountNumber)) {
            this.toAccount = AccountNumber.getAccount(bank, toAccountNumber);
            // If transferring from savings account
            if (this.fromAccount instanceof SavingsAccount){
                // the 'to' account must be owned by the same customer
                if( bank.getCustomerNumber(this.toAccount)!=bank.getCustomerNumber(this.fromAccount)){
                    // except if it's the cash account
                    if( this.toAccount.getAccountNumber() != bank.getCashAccountNumber() ) {
                        throw new IllegalAccountException();
                    }
                }
            }
        } else { // External toAccount
            if (BankRegister.accountExists(toAccountNumber)) {
                this.bankReference = toAccountNumber;
                this.toAccount=AccountNumber.getAccount(bank,bank.getInterBankAccountNumber());
            } else {
                System.out.println("***ERROR: Trying to transfer to a non-existing external account***");
                throw new IllegalArgumentException();
            }
        }
    }

    public String toString(){
        return fromAccount.accountNumber + " " + toAccount.accountNumber + " " + amount;
    }
}
