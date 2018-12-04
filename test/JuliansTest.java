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
    public void canTransferFromCurrentToExternalAccount() throws NoOverdraftAllowedException {
        Bank bank = new Bank("MinBank", "9800", "98005678901234", "98000000000002", "98000000000001");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98005678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, bank.getCashAccountNumber(), "98005678901234", 5000);
            bank.addTransaction(transaction);
            transaction = new Transaction(bank, "98005678901234", "99000000000001", 5000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e) {
            System.out.println("Task 10: ErrorTest");
            fail();
        }
        assertEquals(0, account.getBalance());
        assertEquals(5000, AccountNumber.getAccount(bank, bank.getInterBankAccountNumber()).getBalance());
    }

    @Test
    public void canTransferWithOverdraftFromCurrentToExternalAccount() throws NoOverdraftAllowedException {
        Bank bank = new Bank("MinBank", "9800", "98005678901234", "98000000000002", "98000000000001");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98005678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, "98000000000002", "98005678901234", 5000);
            bank.addTransaction(transaction);
            transaction = new Transaction(bank, "98005678901234", "99000000000001", 10000);
            account.getOverdraftAllowed();
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e) {
            System.out.println("Task 11: ErrorTest");
            fail();
        }
        assertEquals(-5000, account.getBalance()); //Fejl her, hvorfor? Manglende tilladelse til overtræk?
        assertEquals(10000, AccountNumber.getAccount(bank, bank.getInterBankAccountNumber()).getBalance());
    }

    @Test
    public void canTransferFromNegativeCurrentToExternalAccount() throws NoOverdraftAllowedException {
        Bank bank = new Bank("MinBank", "9800", "98005678901234", "98000000000002", "98000000000001");
        Customer customer = new Customer("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98005678901234");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            Transaction transaction = new Transaction(bank, "98000000000002", "98005678901234", 1000); //Hvordan sætter man kontoen på minus?
            bank.addTransaction(transaction);
            transaction = new Transaction(bank, "98005678901234", "99000000000001", 2000);
            bank.addTransaction(transaction);
        } catch (NegativeAmountException e) {
            System.out.println("Task 12: A transaction from negative account is not allowed!");
            fail();
        }
        assertEquals(-1000, account.getBalance());
        assertEquals(1000, AccountNumber.getAccount(bank, bank.getInterBankAccountNumber()).getBalance());
    }
}