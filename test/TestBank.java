import org.junit.Test;

//import java.util.List;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestBank {

    @Test
    public void canMakeNewCustomer() {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        assertEquals(customer, bank.getCustomerList().get(0));
    }

    @Test
    public void canMakeNewSavingsAccount() {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(account, c.accountList.get(0));
    }

    @Test
    public void canMakeNewCurrentAccount() {
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);

        Customer c = (Customer) bank.getCustomerList().get(0);
        assertEquals(account, c.accountList.get(0));
    }

    @Test
    public void canSetInterestRateOnSavingsAccount() {
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
    public void canPrintAccountStatus() throws NoOverdraftAllowedException, NegativeAmountException{
        Bank bank = new Bank("MinBank", "1234", "12345678901234", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);

        customer.accountList.get(0).interestRate = 20;

        Customer c = (Customer) bank.getCustomerList().get(0);
        Account a = (SavingsAccount) c.accountList.get(0);

        assertEquals("0", a.getAccountStatus());

        a.deposit(10000);
        assertEquals("10000", a.getAccountStatus());

        a.withdraw(4000);
        assertEquals("6000", a.getAccountStatus());

        Transaction testTransaction = new Transaction(account, account, 1000);
        bank.addTransaction(testTransaction);

        List<Transaction> tl = bank.getTransactionList(account);

        assertEquals("12345678901234 12345678901234 1000", tl.get(0).toString());
    }

    @Test
    public void canDepositToSavingsAccount() throws NoOverdraftAllowedException{
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, "12340000000002", "12345678901234", 50000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }

        assertEquals(50000, account.getBalance());
    }

    @Test
    public void canDepositToCurrentAccount() throws NoOverdraftAllowedException{
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, "12340000000002", "12345678901234", 50000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }
        assertEquals(50000, account.getBalance());
    }

    @Test
    public void canWithdrawFromSavingsAccount() throws NoOverdraftAllowedException{
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new SavingsAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, "12345678901234", "12340000000002", 50000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }

        assertEquals(50000, AccountNumber.getAccount(bank, bank.getCashAccountNumber()).getBalance());
    }

    @Test
    public void canWithdrawFromCurrentAccount() throws NoOverdraftAllowedException {
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, "12345678901234", "12340000000002", 50000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }
        assertEquals(50000, AccountNumber.getAccount(bank, bank.getCashAccountNumber()).getBalance());
    }

    @Test
    public void canTransferWithSavingsAccount() throws NoOverdraftAllowedException{
        Transaction transaction=null;
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);
        account = new SavingsAccount("12345678904321");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            transaction = new Transaction(bank, "12340000000002", "12345678904321", 50000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }

        try {
            transaction = new Transaction(bank, "12345678904321", "12345678901234", 30000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }

        assertEquals(20000, AccountNumber.getAccount(bank, "12345678904321").getBalance());
        assertEquals(30000, AccountNumber.getAccount(bank, "12345678901234").getBalance());
    }

    @Test
    public void canTransferWithCurrentAccount() throws NoOverdraftAllowedException{
        Transaction transaction=null;
        Bank bank = new Bank("MinBank", "1234", "12340000000001", "12340000000002", "12340000000009");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("12345678901234");
        customer.addAccount(account);
        bank.addAccount(account);
        account = new SavingsAccount("12345678904321");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            transaction = new Transaction(bank, "12340000000002", "12345678901234", 50000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }

        try {
            transaction = new Transaction(bank, "12345678901234", "12345678904321", 30000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e){
            System.out.println("Hov du! Man kan ikke overføre et negativt beløb!");
            fail();
        }
        assertEquals(20000, AccountNumber.getAccount(bank, "12345678901234").getBalance());
        assertEquals(30000, AccountNumber.getAccount(bank, "12345678904321").getBalance());
    }

    @Test
    public void canDeleteAccountFromCustomersAccountList() {
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
