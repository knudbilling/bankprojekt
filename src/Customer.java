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
        this(firstName,lastName,address,phoneNo,nextidNo);
    }

    public Customer(String firstName, String lastName, String address, String phoneNo,int idNo){
        // default constructor.
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNo = phoneNo;
        this.idNo=idNo;
        if(idNo>=nextidNo) nextidNo=idNo+1;
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

    public void addAccount(Account account) throws DuplicateAccountException {
        for(int i=0;i<this.accountList.size();i++){
            if(this.accountList.get(i).getAccountNumber()==account.getAccountNumber()){
                if(this.accountList.get(i)!=account) {
                    throw new DuplicateAccountException();
                }
            }
        }
        if(!this.accountList.contains(account))
            accountList.add(account);
    }

    public void deleteAccount(String accountNo){
        if (accountList.size() <=0) {
            System.out.println("accountList is empty");
        }else{
            for (int i = accountList.size() - 1; i >= 0; i--) {
                // Looking for the accoutNo in accountList
                if (accountList.get(i).getAccountNumber() == accountNo) {
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