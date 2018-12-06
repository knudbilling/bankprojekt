import org.junit.Test;

import static org.junit.Assert.*;

public class MySQLPersistencePersistenceTest {

    @Test
    public void canConnectToMySQL(){
        Persistence p=new MySQLPersistence("192.168.1.102",3306,"bank","user","1234");
        assertNotNull(p);
    }

    @Test
    public void greatBigTest() throws Exception {
        Persistence persistence = new MySQLPersistence("192.168.1.102",3306,"bank","user","1234");

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


        bank.addCustomer(c1);
        bank.addCustomer(c2);
        bank.addCustomer(c3);
        bank.addCustomer(c4);
        bank.addCustomer(c5);

        bank.addAccount(c1,a1);
        bank.addAccount(c1,a2);
        bank.addAccount(c2,a3);
        bank.addAccount(c2,a4);
        bank.addAccount(c3,a5);
        bank.addAccount(c3,a6);
        bank.addAccount(c4,a7);
        bank.addAccount(c4,a8);
        bank.addAccount(c5,a9);
        bank.addAccount(c5,a10);

        persistence.save(bank);

        Bank bank9800 = persistence.load("9800");

        for(Customer c:bank9800.getCustomerList())
        System.out.println(c);

        assert(true);
    }

 }
