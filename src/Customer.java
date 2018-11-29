import java.util.*;


public class Customer {
    // fields
    public String firstName;
    public String lastName;
    public String address;
    public String phoneNo;
    public int idNo;
    public List<Account> accountList = new ArrayList<>();

    static int nextidNo = 0;


    // constructor
    public Customer(String firstName, String lastName, String address, String phoneNo){
        // default constructor.
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNo = phoneNo;
    }
    //default constructor.
    public Customer() {
        idNo = nextidNo++;
    }


    //Methods
    public int getidNo(){
        return this.idNo;
    }
    public void setidNo(){
        this.idNo = idNo;
    }

    public String toString() {
        return "first name: " + this.firstName
               + " last name: " + this.lastName + " address : "
               + this.address + "phone number: " + this.phoneNo + " idNo: " + this.idNo;
    }

    public void addAccount(Account ac){
        // this.Account
        accountList.add(ac);
    }
    public void printAccountList(){
        for(int i = 0; i < accountList.size(); i++) {
            System.out.println(accountList.get(i).toString() + "   ");
        }
    }


}
