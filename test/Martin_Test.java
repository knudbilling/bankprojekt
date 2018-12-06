import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Martin_Test {
    @Test
    public void canTransferLegalAmountFromCurrentAccountToSavingsAccount() throws Exception {
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customer = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98000000000001");
        customer.addAccount(account);
        bank.addAccount(account);

        account = new SavingsAccount ("98000000000002");
        customer.addAccount(account);
        bank.addAccount(account);

        //Sæt penge ind på kundes konto
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"98000000000001", "98000000000002", 30000);
        bank.addTransaction(transaction);

        assertEquals(20000, AccountNumber.getAccount(bank,"98000000000001").getBalance());
        assertEquals(30000, AccountNumber.getAccount(bank,"98000000000002").getBalance());

    }

    @Test
    public void canTransferLegalAmountWithServiceChargeFromCurrentAccountToSavingsAccount() throws Exception {
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customer = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98000000000001");
        customer.addAccount(account);
        bank.addAccount(account);

        account = new SavingsAccount ("98000000000002");
        customer.addAccount(account);
        bank.addAccount(account);

        // Sæt penge ind på kundens konto
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 0);
        bank.addTransaction(transaction);

        // Overfør 11000,00 fra lønkonto til opsparingskonto
        transaction = new Transaction(bank,"98000000000001", "98000000000002", 1100000);
        bank.addTransaction(transaction);

        assertEquals(-1110000, AccountNumber.getAccount(bank, "98000000000001").getBalance());
        assertEquals(1100000, AccountNumber.getAccount(bank, "98000000000002").getBalance());
        assertEquals(10000, AccountNumber.getAccount(bank, "98001234567890").getBalance());


    }
    @Test(expected = NegativeAmountException.class)
    public void canTransferNegativeAmountFromCurrentAccountToSavingsAccount() throws Exception{
        Transaction transaction = null;
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customer = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98000000000001");
        customer.addAccount(account);
        bank.addAccount(account);

        account = new SavingsAccount ("98000000000002");
        customer.addAccount(account);
        bank.addAccount(account);

        try {
            // Sæt penge ind på kundens konto
            transaction = new Transaction(bank, bank.getCashAccountNumber(), "98000000000001", 50000);
            bank.addTransaction(transaction);
        } catch(NegativeAmountException e)
        {
            fail();
        }
        transaction = new Transaction(bank, "98000000000001", "98000000000002", -60000);
        bank.addTransaction(transaction);

    }

}
