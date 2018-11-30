import java.util.ArrayList;
import java.util.List;

public class Bank {

    //Fields
    private String name;
    private final String REG_NO;
    private String ACC_BANK; // <-- Skal det være en String? Jeg ville umiddelbart gøre det til et "Account" objekt?
    private String cashAccount;
    private ArrayList<Customer> customerList; //Det er en liste af "Customer" objekter. Det er en god idé lige at specificere.
    private ArrayList<Transaction> transactionList; //Samme logik her.
    private ArrayList<Account> accountList;

    //Constructor
    public Bank(String name, String REG_NO, String accBank, String cashAccount) {
        this.name = name;
        this.REG_NO = REG_NO;
        this.ACC_BANK = accBank;
        this.cashAccount=cashAccount;
        this.customerList = new ArrayList<Customer>();
        this.transactionList = new ArrayList<Transaction>();
        this.accountList = new ArrayList<Account>();
        Account account = new CurrentAccount(accBank);
        account.overdraftAllowed=Long.MAX_VALUE;
        account.interestRate=0;
        addAccount(account);
        account = new CurrentAccount(cashAccount);
        account.overdraftAllowed=Long.MAX_VALUE;
        account.interestRate=0;
        addAccount(account);

    }

    public void addCustomer(Customer customer) {
        //Her bør man måske checke om kunden findes i forvejen, og f.eks kaste en exception hvis dette er tilfældet.
        if (customerList.contains(customer)) {
            // throw new Exception();
            //System.out.println("The customer already exists.");
        } else {
            customerList.add(customer);
        }
    }

    public void deleteCustomer(Customer customer) {
        //Her bør nok være et check for om kunden findes, og evt blive kastet en exception hvis ikke.
        customerList.remove(customer);
    }

    //Det er nok en god idé at have metoder der kan tilgå "transactions"-listen også.
    //Efter samme logik som ved customer.
    public void addTransaction(Transaction transaction) {
        if(transactionList.contains(transaction))
            return;
        transaction.fromAccount.withdraw(transaction.amount);
        transaction.toAccount.deposit(transaction.amount);
        transactionList.add(transaction);
    }

    public void deleteTransaction(Transaction transaction) {
    }

    public void addAccount(Account account) {
        if (accountList.contains(account)) {

        } else {
            accountList.add(account);
        }
    }

    public String getName() {
        return name;
    }

    public String getRegNo() {
        return REG_NO;
    }

    public String getAccBank() {
        return ACC_BANK;
    }

    public ArrayList getCustomerList() {
        return customerList;
    }

    public ArrayList getTransaction() {
        return transactionList;
    }

    public List<Account> getAccountList(){
        return accountList;
    }


    public void setAccBank(String accBank) {
        this.ACC_BANK = accBank;
    }

    public void setPersonList(ArrayList personList) {
        this.customerList = personList;
    }

    public String getCashAccount() {
        return cashAccount;
    }


    public void setTransaction(ArrayList<Transaction> transaction) {
        this.transactionList = transaction;
    }

    public List<Transaction> getTransactionList(Account account) {
        List<Transaction> tl = new ArrayList<Transaction>();
        for(Transaction t:transactionList){
            if(t.fromAccount==account || t.toAccount==account){
                tl.add(t);
            }
        }
        return tl;
    }


}