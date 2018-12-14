import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JuliansTest {

/*
10. Overførsel af gyldigt beløb uden overtræk fra lønkonto til anden kundes lønkonto i andet pengeinstitut.
11. Overførsel af gyldigt beløb med overtræk fra lønkonto til anden kundes lønkonto i andet pengeinstitut.
12. Overførsel af negativt beløb fra lønkonto til anden kundes lønkonto I andet pengeinstitut.
*/

    @Test
    public void canTransferFromCurrentToExternalAccount() throws Exception {
        Bank bank = new Bank("MinBank", "9800", "98005678900001", "98000000000002", "98000000000001");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98005678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        try {
            Transaction transaction = new Transaction(bank, bank.getCashAccountNumber(), "98005678901234", 5000);
            bank.addTransaction(transaction);
            transaction = new Transaction(bank, "98005678901234", "99000000000001", 5000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e) {
            System.out.println("Task 10: ErrorTest");
            fail();
        }
        assertEquals(-10000, account.getBalance()); // transfer fee
        assertEquals(5000, bank.getAccount(bank.getInterBankAccountNumber()).getBalance());
    }

    @Test
    public void canTransferWithOverdraftFromCurrentToExternalAccount() throws Exception  {
        Bank bank = new Bank("MinBank", "9800", "98005678900001", "98000000000002", "98000000000001");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98005678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        try {
            Transaction transaction = new Transaction(bank, "98000000000002", "98005678901234", 5000);
            bank.addTransaction(transaction);
            transaction = new Transaction(bank, "98005678901234", "99000000000001", 10000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e) {
            System.out.println("Task 11: ErrorTest");
            fail();
        }
        assertEquals(-15000, account.getBalance()); // including transfer fee
        assertEquals(10000, bank.getAccount(bank.getInterBankAccountNumber()).getBalance());
    }

    @Test (expected = NegativeAmountException.class)
    public void canTransferFromNegativeCurrentToExternalAccount() throws Exception {
        Bank bank = new Bank("MinBank", "9800", "98005678900001", "98000000000002", "98000000000001");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Transaction transaction;
        Account account = new CurrentAccount("98005678901234");
        customer.addAccount(account);
        bank.addAccount(customer,account);

        try {
            transaction = new Transaction(bank, "98000000000002", "98005678901234", 10000); //Hvordan sætter man kontoen på minus?
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e) {
            System.out.println("Task 12: A transaction from negative account is not allowed!");
            fail();
        }
        transaction = new Transaction(bank, "98005678901234", "99000000000001", -20000);
        bank.addTransaction(transaction);
    }
}