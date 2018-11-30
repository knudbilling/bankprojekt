/**
 * Helper class for dealing with account numbers
 */

public class AccountNumber {

    /**
     * Get the 4 digit registration number from a 14 digit account number.
     * @param account Full account number (14 digits)
     * @return Registration number (4 digits). Null is account number is malformed.
     */
    public static String getRegistrationNumber(String account){
        if(!AccountNumber.isValidFormat(account))
            return null;
        return account.substring(0,4);
    }

    /**
     * Get the 10 digit account number from a 14 digit account number.
     * @param account Full account number (14 digits)
     * @return The account number (10 digits). Null if the account number is malformed.
     */
    public static String getAccountNumber(String account){
        if(!AccountNumber.isValidFormat(account))
            return null;
        return account.substring(4);
    }

    /**
     * Tests that an account number has exactly 14 numeric digits.
     * @param account Account number to test.
     * @return True is account number is valid. Otherwise false.
     */
    public static boolean isValidFormat(String account){
        if(account.length()!=14)
            return false;
        for(int i=0;i<account.length();i++){
            if(account.charAt(i)<48 || (account.charAt(i)>58))
                return false;
        }
        return true;
    }

    /**
     * Test if an account has the same registration number as a specific bank instance
     * @param bank The bank to check against
     * @param account Full account number (14 digits)
     * @return True if the account has the same registration number af the supplied bank. Otherwise false.
     */
    public static boolean isLocal(Bank bank, String account){
        String reg=AccountNumber.getRegistrationNumber(account);
        if(reg==null)
            return false;
        if(!reg.equals(bank.getRegNo()))
            return false;
        return true;
    }

    /**
     * Tests whether an account exists in a specific bank instance
     * @param bank The bank to check for the account
     * @param account The account to check
     * @return True is the account exists in the supplied bank. Otherwise false.
     */
    public static boolean existsInBank(Bank bank, String account){
        String accountNumber=AccountNumber.getAccountNumber(account);
       for(Account a:bank.getAccountList()){
            if(a.accountNo.equals(accountNumber))
                return true;
        }
        return false;
    }

    /**
     * Get an Account object identified by the supplied account from a bank intance
     * @param bank The bank to get the Account object from
     * @param account The full account number (14 digits)
     * @return The account object if it exists. Otherwise null.
     */
    public static Account getAccount(Bank bank, String account){
        for(int i=0;i<bank.getAccountList().size();i++){
            if(bank.getAccountList().get(i).accountNo.equals(account))
                return bank.getAccountList().get(i);
        }
        System.out.println("not found");
        return null;
    }
}
