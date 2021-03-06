import org.junit.Test;

import static org.junit.Assert.*;

public class AccountNumberTest {

    @Test
    public void validAccountNumberFormat(){
        assertTrue(AccountNumber.isValidFormat("00000000000000"));
        assertTrue(AccountNumber.isValidFormat("12345678901234"));
    }

    @Test
    public void invalidLength() {
        assertFalse(AccountNumber.isValidFormat(""));
        assertFalse(AccountNumber.isValidFormat("1234567890123"));
        assertFalse(AccountNumber.isValidFormat("123456789012345"));
    }

    @Test
    public void invalidCharacters() {
        assertFalse(AccountNumber.isValidFormat("1234567890123X"));
        assertFalse(AccountNumber.isValidFormat("X2345678901234"));
        assertFalse(AccountNumber.isValidFormat("1234567X901234"));
    }

    @Test
    public void getValidRegistrationNumber() {
        assertEquals("1234",AccountNumber.getRegistrationNumber("12340000000000"));
    }

    @Test
    public void getInvalidRegistrationNumber() {
        assertNull(AccountNumber.getRegistrationNumber(""));
        assertNull(AccountNumber.getRegistrationNumber("123456789012345"));
        assertNull(AccountNumber.getRegistrationNumber("abcdefghijklmn"));
    }

    @Test
    public void getValidAccountNumber(){
        assertEquals("1234567890",AccountNumber.getShortNumber("00001234567890"));
    }

    @Test
    public void getInvalidAccountNumber(){
        assertNull(AccountNumber.getShortNumber(""));
        assertNull(AccountNumber.getShortNumber("123456789012345"));
        assertNull(AccountNumber.getShortNumber("abcdefghijklmn"));
    }

    @Test
    public void validLocalAccounts() throws Exception {
        Bank bank = new Bank("MinBank","1234","12340000000001", "12349999999999","12340000000009");
        assertTrue(AccountNumber.isLocal(bank,"12340000000000"));
        assertTrue(AccountNumber.isLocal(bank,"12349876598765"));
    }

    @Test
    public void invalidLocalAccounts() throws Exception {
        Bank bank = new Bank("MinBank","1234","12340000000000", "12349999999999","12340000000009");
        String regNo=bank.getRegNo();
        String testNo="1234";
        if(testNo.equals(regNo)) testNo="4321";

        assertFalse(AccountNumber.isLocal(bank,""));
        assertFalse(AccountNumber.isLocal(bank,testNo+"123456789"));
        assertFalse(AccountNumber.isLocal(bank,"abcdefghijklmn"));
        assertFalse(AccountNumber.isLocal(bank,testNo+"1234567890"));
        assertFalse(AccountNumber.isLocal(bank,regNo+"123456"));
        assertFalse(AccountNumber.isLocal(bank,regNo.toString()));
    }
}
