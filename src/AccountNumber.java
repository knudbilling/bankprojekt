public class AccountNumber {

    public static String getRegistrationNumber(String account){
        if(!AccountNumber.isValid(account))
            return null;
        return account.substring(0,3);
    }

    public static String getAccountNumber(String account){
        if(!AccountNumber.isValid(account))
            return null;
        return account.substring(4);
    }

    public static boolean isValid(String account){
        if(account.length()!=14)
            return false;
        for(int i=0;i<account.length();i++){
            if(account.charAt(i)<48 || (account.charAt(i)>58))
                return false;
        }
        return true;
    }

    public static boolean isLocal(Bank bank, String account){
        String reg=AccountNumber.getRegistrationNumber(account);
        if(reg==null)
            return false;
        if(!reg.equals(bank.regNumber))
            return false;
        return true;
    }

    public static boolean exists(Bank bank, String account){
        String accountNumber=AccountNumber.getAccountNumber(account);
        for(Account a:bank.accountList){
            if(a.accountNumber==accountNumber)
                return true;
        }
        return false;
    }
    public static Account getAccount(Bank bank, String account){
        String accountNumber=AccountNumber.getAccountNumber(account);
        for(Account a:bank.accountList){
            if(a.accountNumber.equals(accountNumber));
                return a;
        }
        return null;
    }
}
