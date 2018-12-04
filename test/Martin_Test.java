import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Martin_Test {
    @Test
    public void canTransferLegalAmountFromCurrentAccountToSavingsAccount() {
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customer = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98000000000001");
        customer.addAccount(account);
        bank.addAccount(account);

        account = new SavingsAccount ("98000000000002");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"98000987654321", "98000000000001", 50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"98000000000001", "98000000000002", 30000);
        bank.addTransaction(transaction);

        assertEquals(20000, AccountNumber.getAccount(bank,"98000000000001").getBalance());
        assertEquals(30000, AccountNumber.getAccount(bank,"98000000000002").getBalance());

    }

    @Test
    public void canTransferLegalAmountWithServiceChargeFromCurrentAccountToSavingsAccount() {
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customer = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98000000000001");
        customer.addAccount(account);
        bank.addAccount(account);

        account = new SavingsAccount ("98000000000002");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"98000987654321", "98000000000001", 50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"98000000000001", "98000000000002", 60000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank, "98000000000001", "98001234567890", 5000);
        bank.addTransaction(transaction);

        assertEquals(-15000, AccountNumber.getAccount(bank, "98000000000001").getBalance());
        assertEquals(60000, AccountNumber.getAccount(bank, "98000000000002").getBalance());
        assertEquals(5000, AccountNumber.getAccount(bank, "98001234567890").getBalance());


    }
    @Test
    public void canTransferNegativeAmountFromCurrentAccountToSavingsAccount() {
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customer = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customer);

        Account account = new CurrentAccount("98000000000001");
        customer.addAccount(account);
        bank.addAccount(account);

        account = new SavingsAccount ("98000000000002");
        customer.addAccount(account);
        bank.addAccount(account);

        Transaction transaction = new Transaction(bank,"98000987654321", "98000000000001", 50000);
        bank.addTransaction(transaction);

        transaction = new Transaction(bank,"98000000000001", "98000000000002", -60000);
        bank.addTransaction(transaction);

        assertEquals(-10000, AccountNumber.getAccount(bank, "98000000000001").getBalance());
        assertEquals(-60000, AccountNumber.getAccount(bank, "98000000000002").getBalance());

    }

}
