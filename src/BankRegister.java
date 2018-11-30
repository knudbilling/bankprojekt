public class BankRegister {
    static final String[] externalBankAccountNumbers = {"99000000000001", "99000000000002"};

    static public boolean accountExists(String accountNumber) {

        for (int i = 0; i < externalBankAccountNumbers.length; i++)
            if (externalBankAccountNumbers[i].equals(accountNumber))
                return true;
        return false;
    }
}
