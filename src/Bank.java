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

    public Bank(String name, String registrationNumber, String ownAccountNumber, String cashAccountNumber, String interBankAccountNumber) throws DuplicateAccountException {
        this.name = name;
        this.REGISTRATION_NUMBER = registrationNumber;
        this.ownAccountNumber = ownAccountNumber;
        this.cashAccountNumber = cashAccountNumber;
        this.interBankAccountNumber = interBankAccountNumber;
        this.customerList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
        this.accountList = new ArrayList<>();

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

    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        for(int i=0;i<customerList.size();i++){
            if(customerList.get(i).getidNo()==customer.getidNo()){
                if(customerList.get(i)!=customer) {
                    throw new DuplicateCustomerException();
                }
            }
        }
        if (!customerList.contains(customer))
            customerList.add(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerList.remove(customer);
    }

    public void addTransaction(Transaction transaction)
            throws NegativeAmountException, NoOverdraftAllowedException, NotEnoughCashException, IllegalAccountException {
        if(transactionList.contains(transaction))
            return;

        // If it's from a current account
        if(transaction.fromAccount instanceof CurrentAccount){
            // and it's not to the banks own account
            if(transaction.toAccount != getAccount(this.ownAccountNumber)){
                // and the balance gets below the allowed overdraft
                if(transaction.fromAccount.getBalance()-transaction.amount<-transaction.fromAccount.allowedOverdraft){
                    // then make a transaction with a fee payable to the banks own account
                    Transaction overdraftFeeTransaction = new Transaction(this,transaction.fromAccount.getAccountNumber(),this.getOwnAccountNumber(),SERVICE_CHARGE);
                    this.addTransaction(overdraftFeeTransaction);
                }
                // or it's to another bank
                if(transaction.toAccount==this.getAccount(this.getInterBankAccountNumber())){
                    // then make a transaction with a fee payable to the banks own account
                    Transaction interBankFeeTransaction= new Transaction(this,transaction.fromAccount.getAccountNumber(),this.getOwnAccountNumber(),SERVICE_CHARGE);
                    this.addTransaction(interBankFeeTransaction);
                }
            }
        }

        // If it's from a savings account
        if(transaction.fromAccount instanceof SavingsAccount){
            // and it's not to the customers own account
            if(getCustomerNumber(transaction.fromAccount) != getCustomerNumber(transaction.toAccount)){
                // or the banks cash account
                if(transaction.toAccount.getAccountNumber() != this.getCashAccountNumber()) {
                    throw new IllegalAccountException();
                }
            }
        }

        // If it's the cashAccount
        if(transaction.toAccount == getAccount(this.cashAccountNumber)){
            // It must never be positive (A positive amount of cash in the bank equals a negative amount on this account)
            if(transaction.toAccount.getBalance()+transaction.amount>0){
                throw new NotEnoughCashException();
            }
        }
        // Order is important. First withdraw, then deposit.
        transaction.fromAccount.withdraw(transaction.amount);
        transaction.toAccount.deposit(transaction.amount);
        transactionList.add(transaction);
    }

    private void addAccount(Account account) throws DuplicateAccountException {
        for(int i=0;i<accountList.size();i++){
            // If the new account has the same account number as an existing one
            if(accountList.get(i).getAccountNumber().equals(account.getAccountNumber())) {
                if(accountList.get(i)!=account) {
                    throw new DuplicateAccountException();
                }
            }
        }
        if (!accountList.contains(account))
            accountList.add(account);
    }

    public void addAccount(Customer customer, Account account) throws DuplicateAccountException, DuplicateCustomerException {
        // If customer==null then make no customer checks, just add account
        if(customer==null){
            try {
                this.addAccount(account);
            } catch (DuplicateAccountException ignore){}
        } else {
            this.addCustomer(customer);
            customer.addAccount(account);
            this.addAccount(account);
        }
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

    public Customer getCustomer(int customerNumber){
        for(int i=0;i<customerList.size();i++){
            if(customerList.get(i).getidNo()==customerNumber)
                return customerList.get(i);
        }
        return null;
    }

    public Account getAccount(String accountNumber){
        for(int i=0;i<this.getAccountList().size();i++){
            if(this.getAccountList().get(i).accountNumber.equals(accountNumber))
                return this.getAccountList().get(i);
        }
        return null;
    }

    boolean booksAreBalancing(){
        long sum=0;

        for(int i=0;i<accountList.size();i++)
            sum+=accountList.get(i).getBalance();

        return sum==0;
    }

}