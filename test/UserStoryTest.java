import org.junit.Test;

//import java.util.List;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserStoryTest {

    @Test
    public void canMakeNewCustomer() throws Exception {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        assertEquals(customer, bank.getCustomerList().get(0));
    }

    @Test
    public void canMakeNewSavingsAccount() throws Exception{
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(account, c.accountList.get(0));
    }

    @Test
    public void canMakeNewCurrentAccount() throws Exception{
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(account, c.accountList.get(0));
    }

    @Test
    public void canSetInterestRateOnSavingsAccount() throws  Exception{
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);

        customer.accountList.get(0).interestRate = 20;

        Customer c = (Customer) bank.getCustomerList().get(0);
        Account a = (SavingsAccount) c.accountList.get(0);

        assertEquals(20, a.interestRate);
    }

    @Test
    public void canPrintAccountStatus() throws Exception {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");

        Account account = new SavingsAccount("12345678901235");
        account.interestRate = 20;
        bank.addAccount(customer,account);

        assertEquals("0", account.getAccountStatus());

        account.deposit(10000);
        assertEquals("10000", account.getAccountStatus());

        account.withdraw(4000);
        assertEquals("6000", account.getAccountStatus());
    }

    @Test
    public void canDepositToSavingsAccount() throws Exception {
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        Transaction transaction = new Transaction(bank, "12340000000002", "12345678901234", 50000);
        bank.addTransaction(transaction);

        assertEquals(50000, account.getBalance());
    }

    @Test
    public void canDepositToCurrentAccount() throws Exception{
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        Transaction transaction = new Transaction(bank, "12340000000002", "12345678901234", 50000);
        bank.addTransaction(transaction);

        assertEquals(50000, account.getBalance());
    }

    @Test
    public void canWithdrawFromSavingsAccount() throws Exception{
        Transaction transaction=null;
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        transaction = new Transaction(bank,bank.getCashAccountNumber(),"12345678901234",50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank, "12345678901234", "12340000000002", 50000);
        bank.addTransaction(transaction);

        assertEquals(0, bank.getAccount(bank.getCashAccountNumber()).getBalance());
        assertEquals(0, account.getBalance());
    }

    @Test
    public void canWithdrawFromCurrentAccount() throws Exception {
        Transaction transaction=null;
        long equity=10000000;
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        transaction=new Transaction(bank,bank.getCashAccountNumber(),bank.getOwnAccountNumber(),equity);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank, "12345678901234", "12340000000002", 50000);
        bank.addTransaction(transaction);

        assertEquals(50000-equity, bank.getAccount(bank.getCashAccountNumber()).getBalance());
    }

    @Test
    public void canTransferWithSavingsAccount() throws Exception {
        Transaction transaction=null;
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);
        account = new SavingsAccount("12345678904321");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        transaction = new Transaction(bank, "12340000000002", "12345678904321", 50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank, "12345678904321", "12345678901234", 30000);
        bank.addTransaction(transaction);

        assertEquals(20000, bank.getAccount("12345678904321").getBalance());
        assertEquals(30000, bank.getAccount("12345678901234").getBalance());
    }

    @Test
    public void canTransferWithCurrentAccount() throws Exception {
        Transaction transaction=null;
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);
        account = new SavingsAccount("12345678904321");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        transaction = new Transaction(bank, "12340000000002", "12345678901234", 50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank, "12345678901234", "12345678904321", 30000);
        bank.addTransaction(transaction);

        assertEquals(20000, bank.getAccount("12345678901234").getBalance());
        assertEquals(30000, bank.getAccount("12345678904321").getBalance());
    }

    @Test
    public void canDeleteAccountFromCustomersAccountList() throws Exception {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        String acNo = "12345678901234";
        Account account = new SavingsAccount(acNo);
        customer.addAccount(account);

        String acNoAlt = "12345678901235";
        account = new SavingsAccount(acNoAlt);
        customer.addAccount(account);

        account = new SavingsAccount("12345678901236");
        customer.addAccount(account);

        account = new SavingsAccount("12345678901237");
        customer.addAccount(account);

        assertEquals(customer.accountList.size(), 4);

        customer.deleteAccount(acNo);

        assertEquals(customer.accountList.size(), 3);

        customer.deleteAccount(acNoAlt);

        assertEquals(customer.accountList.size(), 2);
    }


}
