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

    static private int nextId=0;

    private Transaction(){
        this.timestamp=new Date();
        this.idNo=nextId++;
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
     * @param fromAccountNumber the account to move money from
     * @param toAccountNumber the account to move money to
     * @param amount the amount to move expressed in 'øre'
     */
    public Transaction(Bank bank, String fromAccountNumber, String toAccountNumber, long amount){
        this();

        // Error on: amount is negative
        if(amount<0)
            throw new NumberFormatException();
        this.amount=amount;

        // fromAccount
        // Error on: fromAccount is in an invalid format
        if(!AccountNumber.isValidFormat(fromAccountNumber))
            throw new NumberFormatException();
        // Error on: fromAccount is not local
        if(!AccountNumber.isLocal(bank,fromAccountNumber))
            throw new NumberFormatException();
        this.fromAccount=AccountNumber.getAccount(bank,fromAccountNumber);

        // toAccount
        // Error on: toAccount is in an invalid format
        if(!AccountNumber.isValidFormat(toAccountNumber))
            throw new NumberFormatException();

        // Local toAccount
        if(AccountNumber.isLocal(bank,toAccountNumber)) {
            this.toAccount = AccountNumber.getAccount(bank, toAccountNumber);
        } else { // External toAccount
            if (BankRegister.accountExists(toAccountNumber)) {
                this.bankReference = toAccountNumber;
                this.toAccount=AccountNumber.getAccount(bank,bank.getInterBankAccountNumber());
            } else {
                throw new NumberFormatException();
            }
        }
    }

    public String toString(){
        return fromAccount.accountNo + " " + toAccount.accountNo + " " + amount;
    }
}
