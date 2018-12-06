import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ThomasTest{

    @Test
    public void canTransferLegalAmountNoOverdraftFromACurrentAccountToASavingsAccountInternallyInBank() throws Exception {

        //Setting up the bank and the customers.
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customerSender = new Customer ("firstname", "lastname", "address", "phone");
        Customer customerRecipient = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customerSender);
        bank.addCustomer(customerRecipient);

        //Setting up sender's account.
        Account accountSender = new CurrentAccount("98000000000001");
        customerSender.addAccount(accountSender);
        bank.addAccount(accountSender);

        //Setting up recipients account.
        Account accountRecipient = new SavingsAccount ("98000000000002");
        customerSender.addAccount(accountRecipient);
        bank.addAccount(accountRecipient);

        //Deposit DKK 10.000,00 into sender's account.
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 10000_00);
        bank.addTransaction(transaction);

        //Transfering DKK 5.000,00 to recipient's account.
        transaction = new Transaction(bank,"98000000000001", "98000000000002", 5000_00);
        bank.addTransaction(transaction);

        //Checking that the amounts on the accounts are as predicted.
        assertEquals(5000_00, AccountNumber.getAccount(bank,"98000000000001").getBalance());
        assertEquals(5000_00, AccountNumber.getAccount(bank,"98000000000002").getBalance());
        assertEquals(0, AccountNumber.getAccount(bank, "98001234567890").getBalance());
    }

    public void canTransferLegalAmountWithOverdraftFromACurrentAccountToASavingsAccountInternallyInBank() throws Exception {

        //Setting up the bank and the customers.
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customerSender = new Customer ("firstname", "lastname", "address", "phone");
        Customer customerRecipient = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customerSender);
        bank.addCustomer(customerRecipient);

        //Setting up sender's account.
        Account accountSender = new CurrentAccount("98000000000001");
        customerSender.addAccount(accountSender);
        bank.addAccount(accountSender);

        //Setting up recipients account.
        Account accountRecipient = new SavingsAccount ("98000000000002");
        customerSender.addAccount(accountRecipient);
        bank.addAccount(accountRecipient);

        //Deposit DKK 10.000,00 into sender's account.
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 10000_00);
        bank.addTransaction(transaction);

        //Transfering DKK 15.000,00 to recipient's account.
        transaction = new Transaction(bank,"98000000000001", "98000000000002", 15000_00);
        bank.addTransaction(transaction);

        //Checking that the amounts on the accounts are as predicted.
        assertEquals(-5100_00, AccountNumber.getAccount(bank,"98000000000001").getBalance());
        assertEquals(15000_00, AccountNumber.getAccount(bank,"98000000000002").getBalance());
        assertEquals(100_00, AccountNumber.getAccount(bank, "98001234567890").getBalance());
    }

    @Test(expected = NegativeAmountException.class)
    public void canTransferIllegalAmountFromACurrentAccountToASavingsAccountInternallyInBank() throws Exception {

        //Setting up the bank and the customers.
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customerSender = new Customer ("firstname", "lastname", "address", "phone");
        Customer customerRecipient = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customerSender);
        bank.addCustomer(customerRecipient);

        //Setting up sender's account.
        Account accountSender = new CurrentAccount("98000000000001");
        customerSender.addAccount(accountSender);
        bank.addAccount(accountSender);

        //Setting up recipients account.
        Account accountRecipient = new SavingsAccount ("98000000000002");
        customerSender.addAccount(accountRecipient);
        bank.addAccount(accountRecipient);

        //Deposit DKK 10.000,00 into sender's account.
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 10000_00);
        bank.addTransaction(transaction);

        //Transfering DKK -5.000,00 to recipient's account.
        transaction = new Transaction(bank,"98000000000001", "98000000000002", -5000_00);
        bank.addTransaction(transaction);

    }

}
