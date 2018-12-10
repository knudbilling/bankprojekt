public class Main {

    static final String DB_HOST = "localhost";
    static final int DB_PORT = 3006;
    static final String DB_DATABASE = "bank";
    static final String DB_USER = "user";
    static final String DB_PASSWORD = "1234";

    static final String BANK_NAME = "Boddum-Ydby Sparekasse";
    static final String BANK_REGISTRATION_NUMBER = "9121";
    static final String BANK_OWN_ACCOUNT_NUMBER = BANK_REGISTRATION_NUMBER + "0000000001";
    static final String BANK_CASH_ACCOUNT_NUMBER = BANK_REGISTRATION_NUMBER + "0000000002";
    static final String BANK_INTER_BANK_ACCOUNT_NUMBER = BANK_REGISTRATION_NUMBER + "0000000003";

    static GUIConstructor guiConstructor;

    public static void main(String[] args) {
        Persistence persistence = new MySQLPersistence(DB_HOST, DB_PORT, DB_DATABASE, DB_USER, DB_PASSWORD);
        Bank bank = persistence.load(BANK_REGISTRATION_NUMBER);
        if (bank == null) {
            try {
                bank = new Bank(BANK_NAME, BANK_REGISTRATION_NUMBER, BANK_OWN_ACCOUNT_NUMBER, BANK_CASH_ACCOUNT_NUMBER, BANK_INTER_BANK_ACCOUNT_NUMBER);
                persistence.addBank(bank);
            } catch (DuplicateAccountException e) {
                System.out.println("***ERROR: Banks account numbers not all different***");
                e.printStackTrace();
            }
        }
        guiConstructor = new GUIConstructor(BANK_NAME);
        mainScreen();
        System.out.println("Goodbye, come again!");
    }

    private static void mainScreen() {
        String result="";
        boolean success = true;

        while (!result.equals("Q")) {
            guiConstructor.mainScreen(success);
            result = ""; // TODO: get input
            success = true;
            switch (result) {
                case ("1"):
                    result=customerAccessScreen();
                    break;
                case ("2"):
                    result=employeeScreen();
                    break;
                case ("3"):
                    result=adminScreen();
                    break;
                case ("Q"):
                    return;
                default:
                    success = false;
            }
        }
    }

    private static String customerAccessScreen() {
        boolean success = true;
        String result="";

        while (!result.equals("Q")) {
            guiConstructor.customerAccessScreen(success);
            result = ""; // TODO: get input
            success = true;
            switch (result) {
                case ("B"):
                    return "";
                case ("Q"):
                    return "Q";
                default:
                    success = false;
            }
        }
        return "";
    }

    private static String employeeScreen() {
        return "";
    }

    private static String adminScreen() {
        return "";
    }
}
