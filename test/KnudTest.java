import org.junit.Test;

import static org.junit.Assert.assertEquals;

//Test 16 - Overførsel af gyldigt beløb uden overtræk fra lønkonto til anden kundes opsparingskonto i samme pengeinstitut
//Test 17 - Overførsel af gyldigt beløb med overtræk fra lønkonto til anden kundes opsparingskonto i samme pengeinstitut
//Test 18 - Overførsel af negativt beløb fra lønkonto til anden kundes opsparingskonto i samme pengeinstitut


public class KnudTest {

    //Test 16 - Overførsel af gyldigt beløb uden overtræk fra lønkonto til anden kundes opsparingskonto i samme pengeinstitut
    @Test
    public void canTransferValidAmountFromCurrentAccountWithNoOverdraftToOtherCustomersSavingsAccount() throws Exception{
        Customer customer=null;
        Account account=null;
        Transaction transaction=null;

        Bank bank=new Bank("MyBank","9800","98000000000001","98000000000002","98000000000003");

        customer = new Customer("John","Doe","Address","12345678");
        account=new CurrentAccount("98001000000001");
        bank.addAccount(customer,account);

        customer = new Customer("Jane","Doe","Address","87654321");
        account=new SavingsAccount("98001000000002");
        bank.addAccount(customer,account);

        // Indsæt 1000 kr på kundens lønkonto
        transaction=new Transaction(bank,bank.getCashAccountNumber(),"98001000000001",100000);
        bank.addTransaction(transaction);

        // Overfør 200 kr fra egen lønkonto til anden kundes opsparingskonto
        transaction=new Transaction(bank,"98001000000001","98001000000002",20000);
        bank.addTransaction(transaction);

        // Kontroller at den anden kunde har 200 kr på kontoen
        assertEquals(20000,AccountNumber.getAccount(bank,"98001000000002").getBalance());
        // Kontroller at første kunde har 800 kr på kontoen
        assertEquals(80000,AccountNumber.getAccount(bank,"98001000000001").getBalance());
    }

    //Test 17 - Overførsel af gyldigt beløb med overtræk fra lønkonto til anden kundes opsparingskonto i samme pengeinstitut
    @Test
    public void canTransferValidAmountFromCurrentAccountWithOverdraftToOtherCustomersSavingsAccount() throws Exception{
        Customer customer=null;
        Account account=null;
        Transaction transaction=null;

        Bank bank=new Bank("MyBank","9800","98000000000001","98000000000002","98000000000003");

        customer = new Customer("John","Doe","Address","12345678");
        account=new CurrentAccount("98001000000001");
        bank.addAccount(customer,account);

        customer = new Customer("Jane","Doe","Address","87654321");
        account=new SavingsAccount("98001000000002");
        bank.addAccount(customer,account);

        // Indsæt 100 kr på kundens lønkonto
        transaction=new Transaction(bank,bank.getCashAccountNumber(),"98001000000001",10000);
        bank.addTransaction(transaction);

        // Overfør 300 kr fra egen lønkonto til bankens konto (Så der kommer overtræk på kundens konto)
        transaction=new Transaction(bank,"98001000000001",bank.getOwnAccountNumber(),30000);
        bank.addTransaction(transaction);

        // Overfør 50 kr fra egen lønkonto til anden kundes opsparingskonto
        transaction=new Transaction(bank,"98001000000001","98001000000002",5000);
        bank.addTransaction(transaction);

        // Kontroller at den anden kunde har 50 kr på kontoen
        assertEquals(5000,AccountNumber.getAccount(bank,"98001000000002").getBalance());
        // Kontroller at første kunde har -250 kr på kontoen
        assertEquals(-25000,AccountNumber.getAccount(bank,"98001000000001").getBalance());
    }

    //Test 18 - Overførsel af negativt beløb fra lønkonto til anden kundes opsparingskonto i samme pengeinstitut
    @Test(expected = NegativeAmountException.class)
    public void canNotTransferNegativeAmountFromCurrentAccountToOtherCustomersSavingsAccount() throws Exception{
        Customer customer=null;
        Account account=null;
        Transaction transaction=null;

        Bank bank=new Bank("MyBank","9800","98000000000001","98000000000002","98000000000003");

        customer = new Customer("John","Doe","Address","12345678");
        account=new CurrentAccount("98001000000001");
        bank.addAccount(customer,account);

        customer = new Customer("Jane","Doe","Address","87654321");
        account=new SavingsAccount("98001000000002");
        bank.addAccount(customer,account);

        // Indsæt 1000 kr på kundens lønkonto
        transaction=new Transaction(bank,bank.getCashAccountNumber(),"98001000000001",100000);
        bank.addTransaction(transaction);

        // Overfør -300 kr fra egen lønkonto til anden kundes opsparingskonto
        transaction=new Transaction(bank,"98001000000001","98001000000002",-30000); // Throws NegativeAmountException
        bank.addTransaction(transaction);

    }
}
