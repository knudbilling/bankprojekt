import java.util.ArrayList;
import java.util.List;

public class Bank {

    private static final long SERVICE_CHARGE = 10_000; //100 kr

    private String name;
    private final String REGISTRATION_NUMBER;
    private String ownAccountNumber;
    private String cashAccountNumber;
    private String interBankAccountNumber;
    private ArrayList<Customer> customerList;
    private ArrayList<Transaction> transactionList;
    private ArrayList<Account> accountList;

    public Bank(String name, String registrationNumber, String ownAccountNumber, String cashAccountNumber, String interBankAccountNumber) {
        this.name = name;
        this.REGISTRATION_NUMBER = registrationNumber;
        this.ownAccountNumber = ownAccountNumber;
        this.cashAccountNumber = cashAccountNumber;
        this.interBankAccountNumber = interBankAccountNumber;
        this.customerList = new ArrayList<Customer>();
        this.transactionList = new ArrayList<Transaction>();
        this.accountList = new ArrayList<Account>();

        Account account = new CurrentAccount(ownAccountNumber);
        account.allowedOverdraft =Long.MAX_VALUE;
        account.interestRate=0;
        addAccount(account);

        account = new CurrentAccount(cashAccountNumber);
        account.allowedOverdraft =Long.MAX_VALUE;
        account.interestRate=0;
        addAccount(account);

        account = new CurrentAccount(interBankAccountNumber);
        account.allowedOverdraft =Long.MAX_VALUE;
        account.interestRate=0;
        addAccount(account);
    }

    public void addCustomer(Customer customer) {
        if (!customerList.contains(customer))
            customerList.add(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerList.remove(customer);
    }

    public void addTransaction(Transaction transaction)
            throws NegativeAmountException, NoOverdraftAllowedException, NotEnoughCashException{
        if(transactionList.contains(transaction))
            return;

        // If it's from a current account
        if(transaction.fromAccount instanceof CurrentAccount){
            // and it's not to the banks own account
            if(transaction.toAccount != AccountNumber.getAccount(this,this.ownAccountNumber)){
                // and the balance gets below the allowed overdraft
                if(transaction.fromAccount.getBalance()-transaction.amount<-transaction.fromAccount.allowedOverdraft){
                    // then make a transaction with a fee payable to the banks own account
                    Transaction feeTransaction = new Transaction(this,transaction.fromAccount.getAccountNumber(),this.getOwnAccountNumber(),SERVICE_CHARGE);
                    this.addTransaction(feeTransaction);
                }
            }
        }

        // If it's the cashAccount
        if(transaction.toAccount == AccountNumber.getAccount(this,this.cashAccountNumber)){
            // It must never be positive (A positive amount of cash in the bank equals a negative amount on this account)
            if(transaction.toAccount.getBalance()+transaction.amount>0){
                throw new NotEnoughCashException();
            }
        }
        transaction.fromAccount.withdraw(transaction.amount);
        transaction.toAccount.deposit(transaction.amount);
        transactionList.add(transaction);
    }

    public void addAccount(Account account) {
        if (!accountList.contains(account))
            accountList.add(account);
    }

    public String getName() {
        return name;
    }

    public String getRegNo() {
        return REGISTRATION_NUMBER;
    }

    public String getOwnAccountNumber() {
        return ownAccountNumber;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<Account> getAccountList(){
        return accountList;
    }

    public void setOwnAccountNumber(String accBank) {
        this.ownAccountNumber = accBank;
    }

    public String getCashAccountNumber() {
        return cashAccountNumber;
    }

    public String getInterBankAccountNumber() {
        return interBankAccountNumber;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public List<Transaction> getTransactionList(Account account) {
        List<Transaction> tl = new ArrayList<Transaction>();
        for(int i=0;i<transactionList.size();i++)
            if(transactionList.get(i).fromAccount==account || transactionList.get(i).toAccount==account)
                tl.add(transactionList.get(i));
        return tl;
    }

    public int getCustomerNumber(Account account){
        for(int i=0;i<customerList.size();i++){
            for(int j=0;j<customerList.get(i).accountList.size();j++){
                if(account==customerList.get(i).accountList.get(j))
                    return customerList.get(i).idNo;
            }
        }
        return 0;
    }

    boolean booksAreBalancing(){
        long sum=0;

        for(int i=0;i<accountList.size();i++)
            sum+=accountList.get(i).getBalance();

        return sum==0;
    }

}