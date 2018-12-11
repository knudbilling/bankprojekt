import java.sql.*;

public class MySQLPersistence implements Persistence {

    private final String DB_HOSTNAME;
    private final int DB_PORT;
    private final String DB_USERNAME;
    private final String DB_PASSWORD;
    private final String DB_DATABASE;

    private Connection connection;


    /**
     * Constructor for connecting to any MySQL server
     *
     * @param hostname the hostname of the server e.g. "localhost" or "127.0.0.1"
     * @param port     port to connect to. Usually 3306
     * @param database database to use
     * @param userName username to log into MySQL
     * @param password password for the user
     */
    MySQLPersistence(String hostname, int port, String database, String userName, String password) {

        DB_HOSTNAME = hostname;
        DB_PORT = port;
        DB_DATABASE = database;
        DB_USERNAME = userName;
        DB_PASSWORD = password;

        String DB_Url = "jdbc:mysql://" + DB_HOSTNAME + ":" + DB_PORT + "/" + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET";

        // Check that correct driver exists
        // Only for telling the user what's wrong if we cannot connect to MySQL
        try {
            DriverManager.getDriver(DB_Url);
        } catch (SQLException e) {
            System.out.println("***ERROR: JDBC driver for MySQL not found***");
            e.printStackTrace();
            System.exit(3);
        }

        // Connect to MySQL
        try {
            connection = DriverManager.getConnection(DB_Url, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not connect to MySQL***");
            e.printStackTrace();
            System.exit(4);
        }

        makeDatabaseIfNotExists();
        //executeUpdate("create database if not exists " + DB_DATABASE + ";");
        //executeUpdate("use " + DB_DATABASE + ";");
    }

    @Override
    public void addBank(Bank bank) {
        String query = "insert into banks (RegistrationNumber, Name, OwnAccountNumber, CashAccountNumber, InterBankAccountNumber) values (?,?,?,?,?);";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, bank.getRegNo());
            pst.setString(2, bank.getName());
            pst.setString(3, AccountNumber.getShortNumber(bank.getOwnAccountNumber()));
            pst.setString(4, AccountNumber.getShortNumber(bank.getCashAccountNumber()));
            pst.setString(5, AccountNumber.getShortNumber(bank.getInterBankAccountNumber()));
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not add bank to database***");
            e.printStackTrace();
            System.exit(5);
        }
    }

    @Override
    public void updateBank(Bank bank) {
        String query = "update banks set Name=?, OwnAccountNumber=?, CashAccountNumber=?, InterBankAccountNumber=? where RegistrationNumber = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, bank.getName());
            pst.setString(2, bank.getOwnAccountNumber());
            pst.setString(3, bank.getCashAccountNumber());
            pst.setString(4, bank.getInterBankAccountNumber());
            pst.setString(5, bank.getRegNo());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not update bank in database***");
            e.printStackTrace();
            System.exit(6);
        }
    }

    @Override
    public void addCustomer(Bank bank, Customer customer) {
        String query = "insert into customers (ID, BankRegistrationNumber, FirstName, LastName, Address, PhoneNumber) values (?,?,?,?,?,?);";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, customer.idNo);
            pst.setString(2, bank.getRegNo());
            pst.setString(3, customer.firstName);
            pst.setString(4, customer.lastName);
            pst.setString(5, customer.address);
            pst.setString(6, customer.phoneNo);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not add customer to database***");
            e.printStackTrace();
            System.exit(7);
        }
    }

    @Override
    public void updateCustomer(Bank bank, Customer customer) {
        String query = "update customers set FirstName = ?, LastName = ?, Address = ?, PhoneNumber = ? where ID=? and BankRegistrationNumber=?;";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, customer.firstName);
            pst.setString(2, customer.lastName);
            pst.setString(3, customer.address);
            pst.setString(4, customer.phoneNo);
            pst.setInt(5, customer.idNo);
            pst.setString(6, bank.getRegNo());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not update customer in database***");
            e.printStackTrace();
            System.exit(8);
        }
    }

    @Override
    public void addAccount(Bank bank, Account account) {
        String query = "insert into accounts (AccountNumber, BankRegistrationNumber, AccountType, AllowedOverdraft, InterestRate, CustomerID ) values (?,?,?,?,?,?);";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, AccountNumber.getShortNumber(account.accountNumber));
            pst.setString(2, bank.getRegNo());
            pst.setString(3, account instanceof CurrentAccount ? "C" : "S");
            pst.setLong(4, account.allowedOverdraft);
            pst.setInt(5, account.interestRate);
            pst.setInt(6, bank.getCustomerNumber(account));
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not add account to database***");
            e.printStackTrace();
            System.exit(9);
        }
    }

    @Override
    public void updateAccount(Bank bank, Account account) {
        String query = "update accounts set AllowedOverdraft = ?, InterestRate = ?, CustomerID = ? where AccountNumber = ? and BankRegistrationNumber = ?;";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setLong(1, account.allowedOverdraft);
            pst.setInt(2, account.interestRate);
            pst.setInt(3, bank.getCustomerNumber(account));
            pst.setString(4, AccountNumber.getShortNumber(account.accountNumber));
            pst.setString(5, bank.getRegNo());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not update account in database***");
            e.printStackTrace();
            System.exit(10);
        }
    }

    @Override
    public void addTransaction(Bank bank, Transaction transaction) {
        String query = "insert into transactions (BankRegistrationNumber, FromAccount, ToAccount, Amount, TimeStamp, Reference) values (?,?,?,?,?,?);";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, bank.getRegNo());
            pst.setString(2, AccountNumber.getShortNumber(transaction.fromAccount.accountNumber));
            pst.setString(3, AccountNumber.getShortNumber(transaction.toAccount.accountNumber));
            pst.setLong(4, transaction.amount);
            pst.setDate(5, new java.sql.Date(transaction.timestamp.getTime()));
            pst.setString(6, transaction.bankReference);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("***ERROR: Could not add transaction to database***");
            e.printStackTrace();
            System.exit(11);
        }
    }

    @Override
    public void save(Bank bank) {
        resetBank(bank.getRegNo());
        addBank(bank);
        for (int i = 0; i < bank.getCustomerList().size(); i++) {
            addCustomer(bank, bank.getCustomerList().get(i));
        }
        for (int i = 0; i < bank.getAccountList().size(); i++) {
            addAccount(bank, bank.getAccountList().get(i));
        }
        for (int i = 0; i < bank.getTransactionList().size(); i++) {
            addTransaction(bank, bank.getTransactionList().get(i));
        }
    }

    @Override
    public Bank load(String registrationNumber) {
        Bank bank;

        if ((bank = loadBank(registrationNumber)) == null)
            return null;

        loadCustomers(registrationNumber, bank);
        loadAccounts(registrationNumber, bank);
        loadTransactions(registrationNumber, bank);

        return bank;
    }

    private void loadTransactions(String registrationNumber, Bank bank) {
        Transaction transaction = null;
        ResultSet resultSet = queryRegistrationNumber("select * from transactions where BankRegistrationNumber = ?;", registrationNumber);

        try {
            while (resultSet.next()) {
                try {
                    transaction = new Transaction(bank, bank.getRegNo() + resultSet.getString("FromAccount"), bank.getRegNo() + resultSet.getString("ToAccount"), resultSet.getLong("Amount"));
                } catch (NegativeAmountException e) {
                    System.out.println("***ERROR: Negative amount error***");
                    e.printStackTrace();
                    System.exit(12);
                } catch (IllegalAccountException e) {
                    System.out.println("***ERROR: Trying to transfer to a non-existing external account***");
                    e.printStackTrace();
                    System.exit(13);
                }
                transaction.timestamp = resultSet.getDate("TimeStamp");
                transaction.bankReference = resultSet.getString("Reference");

                try {
                    bank.addTransaction(transaction);
                } catch (NegativeAmountException e) {
                    System.out.println("***ERROR: Trying to transfer a negative amount***");
                    e.printStackTrace();
                    System.exit(14);
                } catch (NoOverdraftAllowedException e) {
                    System.out.println("***ERROR: Trying to transfer more than allowed***");
                    e.printStackTrace();
                    System.exit(15);
                } catch (NotEnoughCashException e) {
                    System.out.println("***ERROR: Cash holding negative***");
                    e.printStackTrace();
                    System.exit(16);
                } catch (IllegalAccountException e) {
                    System.out.println("***ERROR: Account error***");
                    e.printStackTrace();
                    System.exit(17);
                }

            }
        } catch (SQLException e) {
            System.out.println("***ERROR: Failed getting transaction data from database***");
            e.printStackTrace();
            System.exit(18);
        }
    }

    private void loadAccounts(String registrationNumber, Bank bank) {
        Account account;
        Customer customer = null;

        ResultSet resultSet = queryRegistrationNumber("select * from accounts where BankRegistrationNumber = ?;", registrationNumber);

        try {
            while (resultSet.next()) {

                customer = bank.getCustomer(resultSet.getInt("CustomerID"));

                if (resultSet.getString("AccountType").equals("C"))
                    account = new CurrentAccount(bank.getRegNo() + resultSet.getString("AccountNumber"));
                else // Assume AccountType is "S"
                    account = new SavingsAccount(bank.getRegNo() + resultSet.getString("AccountNumber"));

                try {
                    bank.addAccount(customer, account);
                } catch (DuplicateAccountException e) {
                    System.out.println("***ERROR: Duplicate account number***");
                    e.printStackTrace();
                    System.exit(19);
                } catch (DuplicateCustomerException e) {
                    System.out.println("***ERROR: Duplicate customer number***");
                    e.printStackTrace();
                    System.exit(20);
                }
            }
        } catch (SQLException e) {
            System.out.println("***ERROR: Failed getting data from database***");
            e.printStackTrace();
            System.exit(21);
        }
    }

    private void loadCustomers(String registrationNumber, Bank bank) {
        Customer customer;
        ResultSet resultSet = queryRegistrationNumber("select * from customers where BankRegistrationNumber = ?;", registrationNumber);

        try {
            while (resultSet.next()) {

                customer = new Customer(resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Address"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getInt("ID"));
                bank.addCustomer(customer);
            }
        } catch (SQLException e) {
            System.out.println("***ERROR: Failed getting data from database***");
            e.printStackTrace();
            System.exit(22);
        } catch (DuplicateCustomerException e) {
            System.out.println("***ERROR: Duplicate customer***");
            e.printStackTrace();
            System.exit(23);
        }
    }

    private ResultSet queryRegistrationNumber(String query, String registrationNumber) {
        ResultSet resultSet = null;
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, registrationNumber);
            resultSet = pst.executeQuery();
        } catch (SQLException e) {
            System.out.println("***ERROR: Failed to query database***");
            e.printStackTrace();
            System.exit(24);
        }
        return resultSet;
    }

    private Bank loadBank(String registrationNumber) {
        Bank bank = null;
        ResultSet resultSet = queryRegistrationNumber("select * from banks where RegistrationNumber=?;", registrationNumber);

        try {
            if (!resultSet.next())
                return null;
            bank = new Bank(resultSet.getString("name"),
                    resultSet.getString("registrationnumber"),
                    registrationNumber + resultSet.getString("ownaccountnumber"),
                    registrationNumber + resultSet.getString("cashaccountnumber"),
                    registrationNumber + resultSet.getString("interbankaccountnumber"));
        } catch (SQLException e) {
            System.out.println("***ERROR: Failed getting data from database***");
            e.printStackTrace();
            System.exit(25);
        } catch (DuplicateAccountException e) {
            System.out.println("***ERROR: Duplicate account***");
            e.printStackTrace();
            System.exit(26);
        }
        return bank;
    }

    @Override
    public void resetBank(String registrationNumber) {
        executeUpdate("delete from transactions where BankRegistrationNumber=" + registrationNumber + ";");
        executeUpdate("delete from accounts where BankRegistrationNumber=" + registrationNumber + ";");
        executeUpdate("delete from customers where BankRegistrationNumber=" + registrationNumber + ";");
        executeUpdate("delete from banks where RegistrationNumber=" + registrationNumber + ";");
    }

    private void makeDatabaseIfNotExists(){
        executeUpdate("create database if not exists " + DB_DATABASE + ";");
        executeUpdate("use " + DB_DATABASE + ";");
        executeUpdate("create table if not exists accounts (" +
                "AccountNumber varchar(10), " +
                "BankRegistrationNumber varchar(4), " +
                "CustomerID int, " +
                "InterestRate int, " +
                "AllowedOverdraft bigint, " +
                "AccountType varchar(1), " +
                "primary key (AccountNumber) " +
                ");");
        executeUpdate("create table if not exists customers (" +
                "ID int, " +
                "BankRegistrationNumber varchar(4), " +
                "FirstName varchar(45), " +
                "LastName varchar(45), " +
                "Address varchar(45), " +
                "PhoneNumber varchar(45), " +
                "primary key (ID) " +
                ");");
        executeUpdate("create table if not exists transactions (" +
                "ID int auto_increment, " +
                "BankRegistrationNumber varchar(4), " +
                "FromAccount varchar(10), " +
                "ToAccount varchar(10), " +
                "Amount bigint, " +
                "Reference varchar(45), " +
                "TimeStamp date, " +
                "primary key (ID) " +
                ");");
        executeUpdate("create table if not exists banks (" +
                "RegistrationNumber varchar(4), " +
                "Name varchar(45), " +
                "OwnAccountNumber varchar(10), " +
                "CashAccountNumber varchar(10), " +
                "InterBankAccountNumber varchar(10), " +
                "primary key (registrationNumber) " +
                ");");
    }

    @Override
    public void resetPersistence() {
        executeUpdate("drop database if exists " + DB_DATABASE + ";");
        makeDatabaseIfNotExists();
    }

    private ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("***ERROR: MySQL query did not execute***");
            ex.printStackTrace();
            System.exit(27);
        }
        return resultSet;
    }

    private void executeUpdate(String query) {
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("***ERROR: MySQL update did not execute***");
            ex.printStackTrace();
            System.exit(28);
        }
    }

}
