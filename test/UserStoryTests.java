public class UserStoryTests {

    public static void main(String[] args) {
        Bank bank=new Bank("12341234567890",null,null);
        Persistence persistence= new MySQLPersistence();

        Customer bankCustomer = new Customer("Golden","Bullion","Financial Plaza 1","98765432");
        Account cashAccount = new CurrentAccount("12340000000001");
        cashAccount.interestRate=0;
        cashAccount.overdraftAllowed=Long.MAX_VALUE;
        bankCustomer.addAccount(cashAccount);

        Account interBankAccount = new CurrentAccount("12340000000002");
        interBankAccount.interestRate=0;
        interBankAccount.overdraftAllowed=Long.MAX_VALUE;
        bankCustomer.addAccount(interBankAccount);

        bank.addCustomer(bankCustomer); // Should automagically add new accounts

        // user story 1:
        // As a bank employee, I want to add a new customer to the system.
        Customer customer = new Customer("Douglas","Beaver","HisHome","12345678");
        bank.addCustomer(customer);

        // user story 2:
        // As a bank employee, I want to create a savings account for a customer.
        SavingsAccount savingsAccount = new SavingsAccount("12340000000003");
        bank.addAccount(customer, savingsAccount);

        // user story 3:
        //As a bank employee, I want to create a current account for a customer.
        CurrentAccount currentAccount = new CurrentAccount("12340000000004");
        bank.addAccount(customer, currentAccount);

        // user story 4:
        // As a bank employee, I want to allow compound interest to a savings account for a customer.
        customer.accountList.get(1).interestRate=20;

        // user story 5:
        // As a bank administrator, I want to create a facility report of any given account’s status.
        for(Account a: Bank.accountList)
            a.printReport();

        // user story 6:
        // As a customer, I want to deposit money into my savings account.
        Transaction transaction = new Transaction(bank,"12340000000001","12340000000003",10000);
        bank.addTransaction(transaction);

        // user story 7:
        // As a customer, I want to deposit money into my current account.
        transaction = new Transaction(bank,"12340000000001","12340000000004",20000);
        bank.addTransaction(transaction);

        // user story 8:
        // As a customer, I want to withdraw money from my savings account.
        transaction = new Transaction(bank,"12340000000003","12340000000001",500);
        bank.addTransaction(transaction);

        // user story 9:
        // As a customer, I want to withdraw money from my current account.
        transaction = new Transaction(bank,"12340000000004","12340000000001",600);
        bank.addTransaction(transaction);

        // user story 10:
        // As a customer, I want to make transfers with my savings account.
        transaction = new Transaction(bank,"12340000000003","98000000000001",100);
        bank.addTransaction(transaction);

        // user story 11:
        // As a customer, I want to make transfers with my current account.
        transaction = new Transaction(bank,"12340000000004","98000000000001",100);
        bank.addTransaction(transaction);

    }
}
