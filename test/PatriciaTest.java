import org.junit.Test;
//import java.util.List;
import java.util.*;
import static org.junit.Assert.*;


//Test 4-6

/*
 * 4. Overførsel af negativt beløb fra opsparingskonto til egen lønkonto.
 * 5. Overførsel af gyldigt beløb fra opsparingskonto til anden kundes lønkonto.
 * 6. Overførsel af gyldigt beløb fra opsparingskonto til ugyldig konto.
 */

public class PatriciaTest {


    /*Test 4
    * Overførsel af negativt beløb fra opsparingskonto til egen lønkonto.
    * */
    @Test(expected = NegativeAmountException.class)
    public void cantTransferNegativeAmounts() throws Exception{
       // Transaction transaction = null;
        Bank bank=new Bank("MinBank","1234","12340000000001",
                "12340000000002","12340000000009");
        Customer customer=new Customer("firstname","lastname","address","phone");

        Account account = new SavingsAccount("12345678901234");
        bank.addAccount(customer, account);

            Transaction transaction = new Transaction(bank,"12345678901234",
                    "12340000000002",-50000);
            bank.addTransaction(transaction);

    }



    /* Test 5
     * Overførsel af gyldigt beløb fra opsparingskonto til anden kundes lønkonto.
      * */
    @Test (expected = IllegalAccountException.class)
    public void cantTransferWithSavingsAccountToAnotherCustomer() throws Exception {
        Bank bank=new Bank("MinBank","1234","12340000000001",
                "12340000000002","12340000000009");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        bank.addAccount(customer, account);

        customer = new Customer("AndenKunde", "Lastname", "Norhavn", "mobil");
        bank.addCustomer(customer);

        account = new SavingsAccount("12345678904321");
        bank.addAccount(customer, account);

        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(),
                "12345678904321",50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"12345678904321",
                "12345678901234", 30000);
        bank.addTransaction(transaction);

    }


    /* Test 6*
    6. Overførsel af gyldigt beløb fra opsparingskonto til ugyldig konto.
     */

    @Test (expected = IllegalAccountException.class)
    public void cantTransferToInvalidAccountFromSavingsAccount() throws Exception {
        Bank bank=new Bank("MinBank","1234","12340000000001",
                "12340000000002","12340000000009");
        Customer customer=new Customer("førstekunde","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("02039485761245");
        bank.addAccount(customer, account);

        customer = new Customer("AndenKunde", "Lastname", "Norhavn", "mobil");
        bank.addCustomer(customer);

        account = new SavingsAccount("12345678904321");
        bank.addAccount(customer, account);

        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(),
                "12345678904321",50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"12345678904321",
                "02039485761245", 30000);
        bank.addTransaction(transaction);

    }


}
