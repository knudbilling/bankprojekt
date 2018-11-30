/**
 * Helper class for dealing with account numbers
 */

public class AccountNumber {

    /**
     * Get the 4 digit registration number from a 14 digit account number.
     * @param accountNumber Full account number (14 digits)
     * @return Registration number (4 digits). Null is account number is malformed.
     */
    public static String getRegistrationNumber(String accountNumber){
        if(!AccountNumber.isValidFormat(accountNumber))
            return null;
        return accountNumber.substring(0,4);
    }

    /**
     * Get the 10 digit account number from a 14 digit account number.
     * @param accountNumber Full account number (14 digits)
     * @return The account number (10 digits). Null if the account number is malformed.
     */
    public static String getAccountNumber(String accountNumber){
        if(!AccountNumber.isValidFormat(accountNumber))
            return null;
        return accountNumber.substring(4);
    }

    /**
     * Tests that an account number has exactly 14 numeric digits.
     * @param accountNumber Account number to test.
     * @return True is account number is valid. Otherwise false.
     */
    public static boolean isValidFormat(String accountNumber){
        if(accountNumber.length()!=14)
            return false;
        for(int i=0;i<accountNumber.length();i++){
            if(accountNumber.charAt(i)<48 || (accountNumber.charAt(i)>58))
                return false;
        }
        return true;
    }

    /**
     * Test if an account has the same registration number as a specific bank instance
     * @param bank The bank to check against
     * @param accountNumber Full account number (14 digits)
     * @return True if the account has the same registration number af the supplied bank. Otherwise false.
     */
    public static boolean isLocal(Bank bank, String accountNumber){
        String registrationNumber=AccountNumber.getRegistrationNumber(accountNumber);
        if(registrationNumber==null)
            return false;
        if(!registrationNumber.equals(bank.getRegNo()))
            return false;
        return true;
    }

    /**
     * Get an Account object identified by the supplied account from a bank intance
     * @param bank The bank to get the Account object from
     * @param accountNumber The full account number (14 digits)
     * @return The account object if it exists. Otherwise null.
     */
    public static Account getAccount(Bank bank, String accountNumber){
        for(int i=0;i<bank.getAccountList().size();i++){
            if(bank.getAccountList().get(i).accountNo.equals(accountNumber))
                return bank.getAccountList().get(i);
        }
        System.out.println("not found");
        return null;
    }
}
