import java.util.*;


public class Customer {
    // fields
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNo;
    private int idNo;

    static int nextidNo = 0;


   // private list[] accounts;

    // constructor
    public Customer(String firstName, String lastName, String address, String phoneNo){
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNo = phoneNo;


    }


    public Customer() {
        idNo = nextidNo++;
    }



    public String toString() {
        return "first name: " + this.firstName
                + " last name: " + this.lastName + " address : "
                + this.address + "phone number: " + this.phoneNo + " idNo: " + this.idNo;
    }





}
