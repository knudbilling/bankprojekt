import java.util.ArrayList;

public class Bank {

    //Fields
    private static final String NAME = "BANKNAME";
    private static final String REG_NO = "1234";
    private static String ACC_BANK; // <-- Skal det være en String? Jeg ville umiddelbart gøre det til et "Account" objekt?
    private static ArrayList<Customer> customerList;
    private static ArrayList<Transaction> transactions;

    //Constructor
    public Bank(String accBank, ArrayList<Customer> personList, ArrayList<Transaction> transactions) {
        this.ACC_BANK = accBank;
        this.customerList = personList;
        this.transactions = transactions;
    }

    public void addCustomer(Customer customer) throws Exception {
        customerList.add(customer);
        if(customerList.contains(customer)) {
            System.out.println("The customer already exists.");
            throw new Exception();
        }
    }

    public void deleteCustomer(Customer customer) {
        if(customerList.contains(customer)) {
            customerList.remove(customer);
            System.out.println("The customer has been successfully deleted.");
        } else {
            System.out.println("The customer does not exist or has already been deleted.");
        }
    }

    //Mangler der mere her?
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    //Dette skal nok ikke være med, da man ikke skal redigere i transaktionernek
    //public void deleteTransaction(Transaction transaction) {
    //    transactions.add(transaction);
    //}

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