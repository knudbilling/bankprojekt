import java.util.*;
public class CustomerMainTestJohannesA {



    public static void main(String[] args) throws Exception {
        Customer firstCustomer = new Customer("Johannes", "Linde-Hansen", "My home", "12345678");

        System.out.println(firstCustomer.toString());

        Account firstAccountFirstCustomer = new SavingsAccount("First Account");
        firstCustomer.addAccount(firstAccountFirstCustomer);

        firstCustomer.printAccountList();







        //Customer secondCustomer = new Customer("Johannes", "Linde-Hansen", "My home", "12345678");

       // System.out.println(secondCustomer.toString());

        //Customer thirdCustomer = new Customer("Johannes", "Linde-Hansen", "My home", "12345678");

        //System.out.println(thirdCustomer.toString());

    }
}
