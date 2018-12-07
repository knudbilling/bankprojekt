import java.util.Date;

public class Transaction {
    final Account fromAccount;
    final Account toAccount;
    final long amount;
    Date timestamp;
    String bankReference;

    // Instance initializer
    {
        this.timestamp=new Date();
    }

    /**
     * Constructs a transaction from two account numbers
     * @param bank the local bank
     * @param fromAccountNumber the account number to move money from
     * @param toAccountNumber the account number to move money to
     * @param amount the amount to move expressed in 'øre'
     */
    public Transaction(Bank bank, String fromAccountNumber, String toAccountNumber, long amount) throws NegativeAmountException, IllegalAccountException {

        // Error on: amount is negative
        if(amount<0)
            throw new NegativeAmountException();
        this.amount=amount;

        // fromAccount
        // Error on: fromAccount is in an invalid format
        if(!AccountNumber.isValidFormat(fromAccountNumber)) {
            throw new NumberFormatException();
        }
        // Error on: fromAccount is not local
        if(!AccountNumber.isLocal(bank,fromAccountNumber)) {
            throw new IllegalAccountException();
        }
        this.fromAccount=bank.getAccount(fromAccountNumber);

        // toAccount
        // Error on: toAccount is in an invalid format
        if(!AccountNumber.isValidFormat(toAccountNumber)) {
            throw new NumberFormatException();
        }

        // Local toAccount
        if(AccountNumber.isLocal(bank,toAccountNumber)) {
            this.toAccount = bank.getAccount(toAccountNumber);
        // External toAccount
        } else {
            if (BankRegister.accountExists(toAccountNumber)) {
                this.bankReference = toAccountNumber;
                this.toAccount=bank.getAccount(bank.getInterBankAccountNumber());
            } else {
               throw new IllegalAccountException();
            }
        }
    }

    public String toString(){
        return fromAccount.accountNumber + " " + toAccount.accountNumber + " " + amount;
    }
}
