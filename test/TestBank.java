import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestBank {

    @Test
    public void canMakeNewCustomer() {
        Bank bank=new Bank("MinBank","1234","12345678901234", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        assertEquals(customer, bank.getCustomerList().get(0));
    }

    @Test
    public void canMakeNewSavingsAccount() {
        Bank bank=new Bank("MinBank","1234","12345678901234", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);


        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(account, c.accountList.get(0));
    }
    @Test
    public void canMakeNewCurrentAccount() {
        Bank bank=new Bank("MinBank","1234","12345678901234", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);


        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(account, c.accountList.get(0));
    }
    @Test
    public void canSetInterestRateOnSavingsAccount() {
        Bank bank=new Bank("MinBank","1234","12345678901234", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);

        customer.accountList.get(0).interestRate=20;

        Customer c = (Customer) bank.getCustomerList().get(0);
        Account a = (SavingsAccount)c.accountList.get(0);

        assertEquals(20, a.interestRate);
    }
    @Test
    public void canPrintAccountStatus() {
        Bank bank=new Bank("MinBank","1234","12345678901234", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);

        customer.accountList.get(0).interestRate=20;

        Customer c = (Customer) bank.getCustomerList().get(0);
        Account a = (SavingsAccount)c.accountList.get(0);

        assertEquals("0", a.getAccountStatus());

        a.deposit(10000);
        assertEquals("10000", a.getAccountStatus());

        a.withdraw(4000);
        assertEquals("6000", a.getAccountStatus());



        Transaction testTransaction = new Transaction(account, account,1000);

        bank.addTransaction(testTransaction);

        List<Transaction> tl = bank.getTransactionList(account);

        assertEquals("12345678901234 12345678901234 1000",tl.get(0).toString());


    }

    @Test
    public void canDepositToSavingsAccount() {
        Bank bank=new Bank("MinBank","1234","12340000000001", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"12340000000002","12345678901234",50000);

        assertEquals(account, transaction.toAccount);

        bank.addTransaction(transaction);

        assertEquals(50000,account.getBalance());
    }
    @Test
    public void canDepositToCurrentAccount() {
        Bank bank=new Bank("MinBank","1234","12340000000001", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"12340000000002","12345678901234",50000);

        assertEquals(account, transaction.toAccount);

        bank.addTransaction(transaction);

        assertEquals(50000,account.getBalance());
    }
    @Test
    public void canWithdrawFromSavingsAccount() {
        Bank bank=new Bank("MinBank","1234","12340000000001", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"12345678901234","12340000000002",50000);

       // assertEquals(account, transaction.toAccount);

        bank.addTransaction(transaction);

        assertEquals(50000, AccountNumber.getAccount(bank,bank.getCashAccount()).getBalance());
    }
    @Test
    public void canWithdrawFromCurrentAccount() {
        Bank bank=new Bank("MinBank","1234","12340000000001", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"12345678901234","12340000000002",50000);

        // assertEquals(account, transaction.toAccount);

        bank.addTransaction(transaction);

        assertEquals(50000, AccountNumber.getAccount(bank,bank.getCashAccount()).getBalance());
    }
    @Test
    public void canTransferWithSavingsAccount() {
        Bank bank=new Bank("MinBank","1234","12340000000001", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);
        account = new SavingsAccount("12345678904321");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"12340000000002","12345678904321",50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"12345678904321","12345678901234",30000);
        bank.addTransaction(transaction);

        assertEquals(20000, AccountNumber.getAccount(bank,"12345678904321").getBalance());
        assertEquals(30000, AccountNumber.getAccount(bank,"12345678901234").getBalance());
    }

    @Test
    public void canTransferWithCurrentAccount() {
        Bank bank=new Bank("MinBank","1234","12340000000001", "12340000000002");
        Customer customer=new Customer("firstname","lastname","address","phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);
        account = new SavingsAccount("12345678904321");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"12340000000002","12345678901234",50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"12345678901234","12345678904321",30000);
        bank.addTransaction(transaction);

        assertEquals(20000, AccountNumber.getAccount(bank,"12345678901234").getBalance());
        assertEquals(30000, AccountNumber.getAccount(bank,"12345678904321").getBalance());
    }

}
