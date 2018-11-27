import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Transaction {
    public int idNo;
    public Account fromAccount;
    public Account toAccount;
    public long amount;
    public Date timestamp;
    public String note;

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

        // Error on: Is the amount negative
        if(amount<0)
            throw new NumberFormatException();
        this.amount=amount;

        // fromAccount
        // Error on: Is fromAccount in an invalid format
        if(!AccountNumber.isValid(fromAccountString))
            throw new NumberFormatException();
        // Error on: Is the fromAccount non-local
        if(!AccountNumber.isLocal(bank,fromAccountString))
            throw new NumberFormatException();
        this.fromAccount=AccountNumber.getAccount(bank,fromAccountString);

        // toAccount
        // Error on: Is toAccount in an invalid format
        if(!AccountNumber.isValid(toAccountString))
            throw new NumberFormatException();
        // Local or external toAccount
        if(AccountNumber.isLocal(bank,toAccountString)) {
            this.toAccount = AccountNumber.getAccount(bank, toAccountString);
        } else {
            this.note=toAccountString;
 // TODO           toAccount=bank.interBankAccount;
        }
    }

}
