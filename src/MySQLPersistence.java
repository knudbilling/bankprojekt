import java.sql.*;

public class MySQLPersistence implements Persistence {

    private static String DB_HOSTNAME;
    private static int DB_PORT;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_SCHEMA;

    private static final String MYSQL_BACKUP = "backup.sql";

    private static Connection connection;
    private static ResultSet resultSet;

    /**
     * Constructor using the default values
     */
    public MySQLPersistence() {
        this("localhost",3306,"bank","user","1234");
    }

    /**
     * Constructor for connecting to any MySQL server
     * @param hostname the hostname of the server e.g. "localhost" or "127.0.0.1"
     * @param port port to connect to. Usually 3306
     * @param schema schema to use
     * @param userName username to log into MySQL
     * @param password password for the user
     */
    public MySQLPersistence(String hostname, int port, String schema, String userName, String password) {
        DB_HOSTNAME=hostname;
        DB_PORT=port;
        DB_SCHEMA=schema;
        DB_USERNAME=userName;
        DB_PASSWORD=password;

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

            executeUpdate("create database if not exists "+DB_SCHEMA+";");
            // Connect to the right schema
            executeUpdate("use " + DB_SCHEMA + ";");
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
        for (Account a : customer.accountList) {
            query = "insert into customerAccounts (c_id, a_id) values (?,?);";
            try {
                PreparedStatement pst = connection.prepareStatement(query);
                pst.setInt(1, customer.idNo);
                pst.setString(2, a.accountNo);
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
    public Bank reload() {
        return null;
    }

    @Override
    public boolean resetPersistance() {
        executeUpdate("drop database if exists " + DB_SCHEMA + ";");
        executeUpdate("create database if not exists " + DB_SCHEMA + ";");
        executeUpdate("use " + DB_SCHEMA + ";");
        executeUpdate("create table accounts (" +
                "accountNumber char(20), " +
                "balance bigint, " +
                "interestRate int, " +
                "allowedOverdraft bigint, " +
                "accountType char(1), " +
                "primary key (accountNumber) " +
                ");");
        executeUpdate("create table customers (" +
                "customerID varchar(45), " +
                "firstName varchar(45), " +
                "lastName varchar(45), " +
                "phone varchar(45), " +
                "primary key (customerID) " +
                ");");
        executeUpdate("create table transactions (" +
                "transactionID int, " +
                "fromAccount varchar(14), " +
                "toAccount varchar(14), " +
                "amount bigint, " +
                "primary key (transactionID) " +
                ")");
        executeUpdate("create table bank (" +
                "thekey varchar(45), " +
                "thevalue varchar(45), " +
                "primary key (thekey) " +
                ");");

        return true;
    }

    private void executeQuery(String query) {
        try {
            resultSet = connection.createStatement().executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("***ERROR: MySQL query did not execute***");
            ex.printStackTrace();
        }
    }

    private void executeUpdate(String query) {
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("***ERROR: MySQL update did not execute***");
            ex.printStackTrace();
        }
    }

    /*
    // mysqldump er for system-specifikt til at kunne lave i Java.... samme med restore
    // er der mon en SQL kommando der kan udføre det samme?


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
    */
}
