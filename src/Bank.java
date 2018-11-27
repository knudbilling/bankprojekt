import java.util.ArrayList;

public class Bank {

    //Fields
    private static final String NAME = "BANKNAME"; //Navnet på vores bank ændres aldrig - så det kan ligeså godt være final.
    private static final String REG_NO = "1234"; //Samme logik her.
    private static String ACC_BANK; // <-- Skal det være en String? Jeg ville umiddelbart gøre det til et "Account" objekt?
    private static ArrayList<Customer> customerList; //Det er en liste af "Customer" objekter. Det er en god idé lige at specificere.
    private static ArrayList<Transaction> transactions; //Samme logik her.

    //Constructor
    public Bank(String accBank, ArrayList<Customer> personList, ArrayList<Transaction> transactions) {
        this.ACC_BANK = accBank;
        this.customerList = personList;
        this.transactions = transactions;
    }

    public void addCustomer(Customer customer) {
        //Her bør man måske checke om kunden findes i forvejen, og f.eks kaste en exception hvis dette er tilfældet.
        customerList.add(customer);
        if(customerList.contains(customer)) {
            throw new Exception();
            System.out.println("The customer already exists.");
        }
    }

    public void deleteCustomer(Customer customer) {
        //Her bør nok være et check for om kunden findes, og evt blive kastet en exception hvis ikke.
        customerList.remove(customer);
    }

    //Det er nok en god idé at have metoder der kan tilgå "transactions"-listen også.
    //Efter samme logik som ved customer.
    public void addTransaction(Transaction transaction) {}
    public void deleteTransaction(Transaction transaction) {}


    public String getName() {
        return NAME; }

    public String getRegNo() {
        return REG_NO; }

    public String getAccBank() {
        return ACC_BANK; }

    public ArrayList getPersonList() {
        return customerList; }

    public static ArrayList getTransaction() {
        return transactions; }


    public void setAccBank(String accBank) {
        this.ACC_BANK = accBank;
    }

    public void setPersonList(ArrayList personList) {
        this.customerList = personList;
    }

    public void setTransaction(ArrayList<Transaction> transaction) {
        this.transactions = transaction;
    }


}