public class Main {

    private static final String DB_HOST= "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_DATABASE = "bank";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "1234";

    private static final String REGISTRATION_NUMBER = "9800";
    private static final String OWN_ACCOUNT_NUMBER = "0000000001";
    private static final String CASH_ACCOUNT_NUMBER = "0000000002";
    private static final String INTER_BANK_ACCOUNT_NUMBER = "0000000003";


    public static void main(String[] args) throws DuplicateAccountException {
        Persistence persistence = new MySQLPersistence(DB_HOST,DB_PORT,DB_DATABASE,DB_USER,DB_PASSWORD);
        Bank bank = persistence.load(REGISTRATION_NUMBER);
        if (bank == null) {
            bank = new Bank("Britta og Steins Bank", REGISTRATION_NUMBER
                    , REGISTRATION_NUMBER + OWN_ACCOUNT_NUMBER
                    , REGISTRATION_NUMBER + CASH_ACCOUNT_NUMBER
                    , REGISTRATION_NUMBER + INTER_BANK_ACCOUNT_NUMBER);
            if(bank==null){
                System.out.println("***ERROR: Could not create bank***");
                return;
            }
            persistence.addBank(bank);
        }

        new GUI(bank, persistence);
        System.out.println("Thank you, come again!");
    }
}
