import java.util.*;


public class Customer {
    // fields
    public String firstName;
    public String lastName;
    public String address;
    public String phoneNo;
    public int idNo;
    public List<Account> accountList = new ArrayList<>();

    static int nextidNo = 1;


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
    //public List<Account> getAccountList(){return this.accountList;}

    public String toString() {
        return "first name: " + this.firstName
               + " last name: " + this.lastName + " address : "
               + this.address + "phone number: " + this.phoneNo + " idNo: " + this.idNo;
    }

    public void addAccount(Account account){
        // this.Account
        accountList.add(account);
    }
    public void deleteAccount(String accountNo){
        if (accountList.size() <=0) {
            System.out.println("accountList is empty");
        }else{
            for (int i = accountList.size() - 1; i >= 0; i--) {
                // Looking for the accoutNo in accountList
                if (accountList.get(i).getAccountNo() == accountNo) {
                    if (accountList.get(i).getBalance() == 0) { // account balance needs to be zero in order to delete the account
                        accountList.remove(i);
                    } else {
                        System.out.println("accountBalance needs to be zero in order to deleteAccount");
                    }
                }
            }
        }
    }
    public void printAccountList(){

        for(int i = 0; i < accountList.size(); i++) {
            System.out.println(accountList.get(i).toString() + "   ");
        }
    }


}
