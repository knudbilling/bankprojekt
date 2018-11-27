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

    public Transaction(){
        timestamp=new Date();
    }

    // Local transactions
    public Transaction(Account fromAccount, Account toAccount, long amount, String note){
        this.fromAccount=fromAccount;
        this.toAccount=toAccount;
        this.amount=amount;
        this.note=note;
    }

    // Any transactions
    public Transaction(Bank bank, String fromAccount, String toAccount, long amount){
        // from skal være lokal
        // to kan være:
        // lokal: ok
        // extern: check gyldig - lav den om til egen interbank-konto og lav note
        if(!AccountNumber.isValid(fromAccount))
            throw new NumberFormatException();
        if(!AccountNumber.isValid(toAccount))
            throw new NumberFormatException();
        if(amount<0)
            throw new NumberFormatException();
        if(!AccountNumber.isLocal(bank,fromAccount))
            throw new NumberFormatException();

    }


}
