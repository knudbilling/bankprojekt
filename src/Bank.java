import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Bank {

    //Fields
    private static final String NAME = "BANKNAME"; //Navnet på vores bank ændres aldrig - så det kan ligeså godt være final.
    private static final String REG_NO = "1234"; //Samme logik her.
    private static Account ACC_BANK; // <-- Skal det være en String? Jeg ville umiddelbart gøre det til et "Account" objekt?
    private static ArrayList<Customer> customerList; //Det er en liste af "Customer" objekter. Det er en god idé lige at specificere.
    private static ArrayList<Transaction> transactions; //Samme logik her.

    //Constructor
    public Bank(Account accBank, ArrayList<Customer> customerList, ArrayList<Transaction> transactions) {
        this.ACC_BANK = accBank;
        this.customerList = customerList;
        this.transactions = transactions;
    }

    public void createCustomer(String firstName, String lastName, String address, String phoneNo) {
        //Do we check that the customer does not already exist here?
        //Or is this checked prior to arriving here?
        Customer customer = new Customer(firstName,lastName,address,phoneNo);
        customerList.add(customer);
        //TODO - Add customer to database.
    }

    public void deleteCustomer(Customer customer) throws NoSuchElementException {
        //Her bør nok være et check for om kunden findes, og evt blive kastet en exception hvis ikke.
        if(customerList.contains(customer)) {
            customerList.remove(customer);
        } else {
            throw new NoSuchElementException("Cannot delete non-existing Customer.");
        }

    }

    public static void addTransaction(Transaction transaction) {

        //TODO:
        //Executing transaction.
            //When to connect this action to the database?
            //Withdraw from "fromAccount".
            //Deposit to Bank's account?
            //Withdraw from Bank's account?
            //Deposit to "toAccount".

        //Storing transaction in list of transactions.
            //Add to "transactions" ArrayList.
            //Add to database?

    }

    public static String getName() {
        return NAME; }

    public static String getRegNo() {
        return REG_NO; }

    public static Account getAccBank() {
        return ACC_BANK; }

    public static ArrayList getCustomerList() {
        return customerList; }

    public static ArrayList getTransactions() {
        return transactions; }


    public void setAccBank(Account accBank) {
        this.ACC_BANK = accBank;
    }

    public void setCustomerList(ArrayList customerList) {
        this.customerList = customerList;
    }

    public void setTransaction(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }


}