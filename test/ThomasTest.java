import org.junit.Test;
import static org.junit.Assert.assertEquals;

//Test 13 - Overførsel af gyldigt beløb uden overtræk fra lønkonto til anden kundes lønkonto i samme pengeinstitut.
//Test 14 - Overførsel af gyldigt beløb med overtræk fra lønkonto til anden kundes lønkonto i samme pengeinstitut.
//Test 15 - Overførsel af negativt beløb fra lønkonto til anden kundes lønkonto i samme pengeinstitut.

public class ThomasTest{

    //Test 13 - Overførsel af gyldigt beløb uden overtræk fra lønkonto til anden kundes lønkonto i samme pengeinstitut.
    @Test
    public void canTransferLegalAmountNoOverdraftFromACurrentAccountToACurrentAccountInternallyInBank() throws Exception {

        //Setting up the bank and the customers.
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customerSender = new Customer ("firstname", "lastname", "address", "phone");
        Customer customerRecipient = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customerSender);
        bank.addCustomer(customerRecipient);

        //Setting up sender's account.
        Account accountSender = new CurrentAccount("98000000000001");
        customerSender.addAccount(accountSender);
        bank.addAccount(customerSender,accountSender);

        //Setting up recipients account.
        Account accountRecipient = new CurrentAccount ("98000000000002");
        customerSender.addAccount(accountRecipient);
        bank.addAccount(customerRecipient,accountRecipient);

        //Deposit DKK 10.000,00 into sender's account.
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 10000_00);
        bank.addTransaction(transaction);

        //Transfering DKK 5.000,00 to recipient's account.
        transaction = new Transaction(bank,"98000000000001", "98000000000002", 5000_00);
        bank.addTransaction(transaction);

        //Checking that the amounts on the accounts are as predicted.
        assertEquals(5000_00, bank.getAccount("98000000000001").getBalance());
        assertEquals(5000_00, bank.getAccount("98000000000002").getBalance());
        assertEquals(0, bank.getAccount("98001234567890").getBalance());
    }

    //Test 14 - Overførsel af gyldigt beløb med overtræk fra lønkonto til anden kundes lønkonto i samme pengeinstitut.
    @Test
    public void canTransferLegalAmountWithOverdraftFromACurrentAccountToACurrentAccountInternallyInBank() throws Exception {

        //Setting up the bank and the customers.
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customerSender = new Customer ("firstname", "lastname", "address", "phone");
        Customer customerRecipient = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customerSender);
        bank.addCustomer(customerRecipient);

        //Setting up sender's account.
        Account accountSender = new CurrentAccount("98000000000001");
        customerSender.addAccount(accountSender);
        bank.addAccount(customerSender,accountSender);

        //Setting up recipients account.
        Account accountRecipient = new CurrentAccount ("98000000000002");
        customerSender.addAccount(accountRecipient);
        bank.addAccount(customerSender,accountRecipient);

        //Deposit DKK 10.000,00 into sender's account.
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 10000_00);
        bank.addTransaction(transaction);

        //Transfering DKK 25.000,00 to recipient's account.
        transaction = new Transaction(bank,"98000000000001", "98000000000002", 25000_00);
        bank.addTransaction(transaction);

        //Checking that the amounts on the accounts are as predicted.
        assertEquals(-15100_00, bank.getAccount("98000000000001").getBalance());
        assertEquals(25000_00, bank.getAccount("98000000000002").getBalance());
        assertEquals(100_00, bank.getAccount("98001234567890").getBalance());
    }

    //Test 15 - Overførsel af negativt beløb fra lønkonto til anden kundes lønkonto i samme pengeinstitut.
    @Test(expected = NegativeAmountException.class)
    public void canNotTransferIllegalAmountFromACurrentAccountToACurrentAccountInternallyInBank() throws Exception {

        //Setting up the bank and the customers.
        Bank bank = new Bank("MinBank", "9800", "98001234567890", "98000987654321", "98000000000009");
        Customer customerSender = new Customer ("firstname", "lastname", "address", "phone");
        Customer customerRecipient = new Customer ("firstname", "lastname", "address", "phone");
        bank.addCustomer(customerSender);
        bank.addCustomer(customerRecipient);

        //Setting up sender's account.
        Account accountSender = new CurrentAccount("98000000000001");
        customerSender.addAccount(accountSender);
        bank.addAccount(customerSender,accountSender);

        //Setting up recipients account.
        Account accountRecipient = new CurrentAccount ("98000000000002");
        customerSender.addAccount(accountRecipient);
        bank.addAccount(customerSender,accountRecipient);

        //Deposit DKK 10.000,00 into sender's account.
        Transaction transaction = new Transaction(bank,bank.getCashAccountNumber(), "98000000000001", 10000_00);
        bank.addTransaction(transaction);

        //Transfering DKK -5.000,00 to recipient's account.
        transaction = new Transaction(bank,"98000000000001", "98000000000002", -5000_00);
        bank.addTransaction(transaction);

    }

}
