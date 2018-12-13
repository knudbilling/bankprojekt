/**
 * A mock class to emulate a central register of account numbers
 */
public class BankRegister {
    // Array of valid account numbers
    private static final String[] externalBankAccountNumbers = {"99000000000001", "99000000000002"};

    /**
     * Checks is an account number exists in a foreign bank
     * @param accountNumber is the number to check
     * @return true if the account number exists in a foreign bank, false otherwise
     */
    static public boolean accountExists(String accountNumber) {

        for (int i = 0; i < externalBankAccountNumbers.length; i++)
            if (externalBankAccountNumbers[i].equals(accountNumber))
                return true;
        return false;
    }
}
