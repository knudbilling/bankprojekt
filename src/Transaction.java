import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Knud Billing
 */
public class Transaction {
    int idNo;
    Account fromAccount;
    Account toAccount;
    long amount;
    Date timestamp;
    String note;

    private Transaction(){
        timestamp=new Date();
    }

    // Local transactions
    public Transaction(Account fromAccount, Account toAccount, long amount, String note){
        this();
        this.fromAccount=fromAccount;
        this.toAccount=toAccount;
        this.amount=amount;
        this.note=note;
    }

    // Any transactions
    public Transaction(Bank bank, String fromAccountString, String toAccountString, long amount){
        this();

        // Error on: amount is negative
        if(amount<0)
            throw new NumberFormatException();
        this.amount=amount;

        // fromAccount
        // Error on: fromAccount is in an invalid format
        if(!AccountNumber.isValid(fromAccountString))
            throw new NumberFormatException();
        // Error on: fromAccount is not local
        if(!AccountNumber.isLocal(bank,fromAccountString))
            throw new NumberFormatException();
        this.fromAccount=AccountNumber.getAccount(bank,fromAccountString);

        // toAccount
        // Error on: toAccount is in an invalid format
        if(!AccountNumber.isValid(toAccountString))
            throw new NumberFormatException();

        // Local or external toAccount
        if(AccountNumber.isLocal(bank,toAccountString)) {
            this.toAccount = AccountNumber.getAccount(bank, toAccountString);
        } else {
            // TODO: check if account exists in foreign bank
            this.note=toAccountString;
 // TODO           toAccount=bank.interBankAccount;
        }
    }

}
