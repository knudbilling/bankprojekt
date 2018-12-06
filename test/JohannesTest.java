import org.junit.Test;

import java.util.ArrayList;
//import java.util.List;
import java.util.*;

import static org.junit.Assert.assertEquals;
public class JohannesTest {

    //Test 1 - Overførsel af gyldigt beløb fra opsparingskonto til egen lønkonto
    @Test
    public void canMakeATransferFromSavingsToCurrentAccount() throws Exception{
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account firstAccount = new SavingsAccount("12345678901234");
        customer.addAccount(firstAccount);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(firstAccount, c.accountList.get(0));


        Account secondAccount = new CurrentAccount("12345678904321");
        customer.addAccount(secondAccount);


        assertEquals(secondAccount, c.accountList.get(1));


        firstAccount.deposit(100000);

        Transaction testTransaction = new Transaction(bank,firstAccount.getAccountNumber(),secondAccount.getAccountNumber(),40000);
        bank.addTransaction(testTransaction);

        assertEquals(60000, firstAccount.getBalance());

        assertEquals(40000,secondAccount.getBalance());



    }
    // Test 2 - Overførsel af gyldigt beløb fra opsparingskonto til egen opsparingskonto
    @Test
    public void canMakeATransferFromSavingsToSavingsAccount() throws Exception {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account firstAccount = new SavingsAccount("12345678901234");
        customer.addAccount(firstAccount);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(firstAccount, c.accountList.get(0));


        Account secondAccount = new SavingsAccount("12345678904321");
        customer.addAccount(secondAccount);


        assertEquals(secondAccount, c.accountList.get(1));


        firstAccount.deposit(120000);

        Transaction testTransaction = new Transaction(bank,firstAccount.getAccountNumber(), secondAccount.getAccountNumber(), 70000);
        bank.addTransaction(testTransaction);

        assertEquals(50000, firstAccount.getBalance());

        assertEquals(70000,secondAccount.getBalance());

    }

    //Test 3 - Overførsel af for stort beløb fra opsparingskonto til egen lønkonto
    @Test (expected = NoOverdraftAllowedException.class)
    public void can_Not_Make_A_Too_Large_Transfer_From_Savings_To_Current_Account() throws Exception {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account firstAccount = new SavingsAccount("12345678901234");
        customer.addAccount(firstAccount);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(firstAccount, c.accountList.get(0));

        Account secondAccount = new CurrentAccount("12345678904321");
        customer.addAccount(secondAccount);

        assertEquals(secondAccount, c.accountList.get(1));


        Transaction putMoneyInAccount = new Transaction(bank, bank.getInterBankAccountNumber(),firstAccount.getAccountNumber(),3000);
        bank.addTransaction(putMoneyInAccount);


        Transaction testTransaction = new Transaction(bank,firstAccount.getAccountNumber(), secondAccount.getAccountNumber(), 4000);
        bank.addTransaction(testTransaction);
    }
}
