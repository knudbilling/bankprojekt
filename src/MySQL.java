import java.io.IOException;
import java.sql.*;
import java.util.List;

public class MySQL implements Persistance {

    private static final String DB_HOSTNAME = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_USERNAME = "user";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_SCHEMA = "bank";

    private static final String MYSQL_BACKUP = "backup.sql";

    private static Connection connection;
    private static ResultSet resultSet;


    public MySQL() {
        // Make connection if it does not already exists
        if (connection == null) {
            String DB_Url = "jdbc:mysql://" + DB_HOSTNAME + ":" + DB_PORT + "/" + "?serverTimeZone=CET";

            // Check that correct driver exists
            // Only for telling the user whats wrong if we cannot connect to MySQL
            try {
                DriverManager.getDriver(DB_Url);
            } catch (SQLException e) {
                System.out.println("***ERROR: JDBC driver for MySQL not found***");
                e.printStackTrace();
            }

            // Connect to MySQL
            try {
                connection = DriverManager.getConnection(DB_Url, DB_USERNAME, DB_PASSWORD);
            } catch (SQLException e) {
                System.out.println("***ERROR: Could not connect to MySQL***");
                e.printStackTrace();
            }

            // Connect to the right schema
            update("use " + DB_SCHEMA + ";");
        }
    }

    @Override
    public boolean updateBank(Bank bank) {
        return true;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        // Update customer
        String query = "update customers set firstname = ? ,lastname = ?, address = ?, phone = ? where customerID=?;";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, customer.firstName);
            pst.setString(2, customer.lastName);
            pst.setString(3, customer.address);
            pst.setString(4, customer.phoneNo);
            pst.setInt(5, customer.idNo);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // TODO: delete all customer account refs
        // TODO: add back customer account refs
        return true;
    }

    @Override
    public boolean addCustomer(Customer customer) {

        // Add customer
        String query = "insert into customers (customerID,firstname,lastname,address,phone) values (?,?,?,?,?);";
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, customer.idNo);
            pst.setString(2, customer.firstName);
            pst.setString(3, customer.lastName);
            pst.setString(4, customer.address);
            pst.setString(5, customer.phoneNo);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add account connections
        for(Account a:customer.accountList){
            query = "insert into customerAccounts (c_id, a_id) values (?,?);";
            try {
                PreparedStatement pst = connection.prepareStatement(query);
                pst.setInt(1, customer.idNo);
//                pst.setString(2, a.accountnr);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    @Override
    public boolean updateAccount(Account account) {
        return true;
    }

    @Override
    public boolean addAccount(Account account) {
        return true;
    }

    @Override
    public boolean addTransaction(Transaction transaction) {
        return true;
    }

    @Override
    public boolean saveAll(Bank bank) {
        return true;
    }

    @Override
    public Bank loadBank() {
        return null;
    }

    @Override
    public Bank loadAll() {
        return null;
    }

    @Override
    public boolean resetPersistance() {
        update("drop database if exists " + DB_SCHEMA + ";");
        update("create database if not exists " + DB_SCHEMA + ";");
        update("use " + DB_SCHEMA + ";");
        update("create table accounts (" +
                "accountNumber char(20), " +
                "balance bigint, " +
                "interestRate int, " +
                "allowedOverdraft bigint, " +
                "accountType char(1), " +
                "primary key (accountNumber) " +
                ");");
        update("create table customers (" +
                "customerID varchar(45), " +
                "firstName varchar(45), " +
                "lastName varchar(45), " +
                "phone varchar(45), " +
                "primary key (customerID) " +
                ");");
        update("create table transactions (" +
                "transactionID int, " +
                "fromAccount varchar(14), " +
                "toAccount varchar(14), " +
                "amount bigint, " +
                "primary key (transactionID) " +
                ")");
        update("create table bank (" +
                "thekey varchar(45), " +
                "thevalue varchar(45), " +
                "primary key (thekey) " +
                ");");

        return true;
    }

    private void query(String query) {
        try {
            resultSet = connection.createStatement().executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("***ERROR: MySQL query did not execute***");
            ex.printStackTrace();
        }
    }

    private void update(String query) {
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("***ERROR: MySQL update did not execute***");
            ex.printStackTrace();
        }
    }

    public boolean backup() {

        int processComplete = 0;
        String command = "mysqldump -u " + DB_USERNAME + " -p" + DB_PASSWORD + " " + DB_SCHEMA + " -r " + MYSQL_BACKUP;

        Process runtimeProcess = null;
        try {
            runtimeProcess = Runtime.getRuntime().exec(command);
            processComplete = runtimeProcess.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (processComplete == 0) {
            System.out.println("BMySQL backup successfull");
            return true;
        }
        System.out.println("MySQL backup failed");
        return false;
    }

    public boolean restore() {

        int processComplete = 0;
        String command[] = new String[]
                {"mysql",
                        DB_SCHEMA,
                        "-u" + DB_USERNAME,
                        "-p" + DB_PASSWORD,
                        "-e",
                        "source " + MYSQL_BACKUP};
        try {
            Process runtimeProcess = Runtime.getRuntime().exec(command);
            processComplete = runtimeProcess.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (processComplete == 0) {
            System.out.println("MySQL restore successfull");
            return true;
        }
        System.out.println("MySQL restore failed");
        return false;
    }

}
