import java.util.Date;

/**
 * @author Knud Billing
 */
public class Transaction {
    int idNo;
    Account fromAccount;
    Account toAccount;
    long amount;
    Date timestamp;
    String bankReference;

    private Transaction(){
        timestamp=new Date();
    }

    /**
     * Constructor to use if both Account objects are known
     * @param fromAccount
     * @param toAccount
     * @param amount
     */
    public Transaction(Account fromAccount, Account toAccount, long amount){
        this();
        this.fromAccount=fromAccount;
        this.toAccount=toAccount;
        this.amount=amount;
    }

    /**
     * Constructor to use is accounts are expressed with strings
     * @param bank the local bank
     * @param fromAccountString the account to move money from
     * @param toAccountString the account to move money to
     * @param amount the amount to move expressed in 'øre'
     */
    public Transaction(Bank bank, String fromAccountString, String toAccountString, long amount){
        this();

        // Error on: amount is negative
        if(amount<0)
            throw new NumberFormatException();
        this.amount=amount;

        // fromAccount
        // Error on: fromAccount is in an invalid format
        if(!AccountNumber.isValidFormat(fromAccountString))
            throw new NumberFormatException();
        // Error on: fromAccount is not local
        if(!AccountNumber.isLocal(bank,fromAccountString))
            throw new NumberFormatException();
        this.fromAccount=AccountNumber.getAccount(bank,fromAccountString);

        // toAccount
        // Error on: toAccount is in an invalid format
        if(!AccountNumber.isValidFormat(toAccountString))
            throw new NumberFormatException();

        // Local toAccount
        if(AccountNumber.isLocal(bank,toAccountString)) {
            this.toAccount = AccountNumber.getAccount(bank, toAccountString);
        } else { // External toAccount
            // TODO: check if account exists in foreign bank
            this.bankReference =toAccountString;
            // TODO: definer en interbankkonto så:  toAccount=bank.interBankAccount;
        }
    }

}
