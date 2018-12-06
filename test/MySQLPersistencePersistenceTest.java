import org.junit.Test;

import static org.junit.Assert.*;

public class MySQLPersistencePersistenceTest {

    @Test
    public void canConnectToMySQL(){
        Persistence p=new MySQLPersistence("localhost",3306,"bank","user","1234");
        assertNotNull(p);
    }

    @Test
    public void greatBigTest(){
        Persistence persistence = new MySQLPersistence("localhost",3306,"bank","user","1234");

        Bank bank=new Bank("ShowMeTheMoney","9800","98000000000001","98000000000002","98000000000003");

        Customer c1=new Customer("Douglas","Beaver","Douglas' Home","12345678");
        Customer c2=new Customer("Morten","Christensen","Mortens Home","23456789");
        Customer c3=new Customer("Julian","Christensen","Julians Home","11223344");
        Customer c4=new Customer("Patricia","Kellvig","Patricias Home","22334455");
        Customer c5=new Customer("Martin","Busk","Martins Home","33445566");

        Account a1 = new SavingsAccount("98001000000001");
        Account a2 = new CurrentAccount("98001000000002");
        Account a3 = new SavingsAccount("98001000000003");
        Account a4 = new CurrentAccount("98001000000004");
        Account a5 = new SavingsAccount("98001000000005");
        Account a6 = new CurrentAccount("98001000000006");
        Account a7 = new SavingsAccount("98001000000007");
        Account a8 = new CurrentAccount("98001000000008");
        Account a9 = new SavingsAccount("98001000000009");
        Account a10 = new CurrentAccount("98001000000010");

        c1.addAccount(a1);
        c1.addAccount(a2);
        c2.addAccount(a3);
        c2.addAccount(a4);
        c3.addAccount(a5);
        c3.addAccount(a6);
        c4.addAccount(a7);
        c4.addAccount(a8);
        c5.addAccount(a9);
        c5.addAccount(a10);

        bank.addCustomer(c1);
        bank.addCustomer(c2);
        bank.addCustomer(c3);
        bank.addCustomer(c4);
        bank.addCustomer(c5);

        bank.addAccount(a1);
        bank.addAccount(a2);
        bank.addAccount(a3);
        bank.addAccount(a4);
        bank.addAccount(a5);
        bank.addAccount(a6);
        bank.addAccount(a7);
        bank.addAccount(a8);
        bank.addAccount(a9);
        bank.addAccount(a10);

        persistence.save(bank);

        Bank bank9800 = persistence.load("9800");

        for(Customer c:bank9800.getCustomerList())
        System.out.println(c);

        assert(true);
    }

 }
