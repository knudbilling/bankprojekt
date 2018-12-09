import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KnudGUI {

    Scanner scanner = new Scanner(System.in);
    Bank bank;
    Persistence persistence;
    int customerNumber;
    String accountNumber;
    String toAccountNumber;
    long amount;

    /*
    public static void main(String[] args) throws OperationNotSupportedException, DuplicateAccountException {
        Bank theBank = new Bank("Roskilde Bank", "9800", "0000000001", "0000000002", "0000000003");
        Persistence thePersistence = new MySQLPersistence("192.168.1.102", 3306, "bank", "user", "1234");

        KnudGUI kg = new KnudGUI(theBank, thePersistence);
        kg.mainFlow();
        System.out.println("Goodbye, come again!");
    }
    */

    public KnudGUI(Bank newBank, Persistence newPersistence) {
        this.bank = newBank;
        this.persistence = newPersistence;
    }

    private void mainFlow() throws OperationNotSupportedException {
        String result;

        do {
            result = mainGUI();

            switch (result) {
                case "1":
                    result = customerAccessFlow();
                    break;
                case "2":
                    result = employeeFlow();
                    break;
                case "3":
                    result = adminFlow();
                    break;
            }
        } while (!result.equals("Q"));
    }

    private String customerAccessFlow() {
        String result;

        while (true) {
            result = customerAccessGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            result = customerFlow();
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String customerFlow() {
        String result;

        while (true) {
            result = customerGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "1":
                    result = customerAccountsFlow();
                    break;
                case "2":
                    result = customerTransactionFromFlow();
                    break;
            }
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String customerAccountsFlow() {
        String result;

        while (true) {
            result = customerAccountsGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            result = customerAccountInfoFlow();
            switch (result) {
                case "Q":
                case "M":
                    return result;
            }
        }
    }

    private String customerAccountInfoFlow() {
        return (customerAccountInfoGUI());
    }

    private String customerTransactionFromFlow() {
        String result;

        while (true) {
            result = customerTransactionFromGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            if (this.bank.getAccount(this.accountNumber) instanceof SavingsAccount) {
                result = customerTransactionFromSavingsFlow();
            } else {
                result = customerTransactionToFlow();
            }
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String customerTransactionFromSavingsFlow() {
        String result;

        while (true) {
            result = customerTransactionFromSavingsGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            result = customerTransactionAmountFlow();
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String customerTransactionToFlow() {
        String result;
        while (true) {
            result = customerTransactionToGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            result = customerTransactionAmountFlow();
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String customerTransactionAmountFlow() {
        String result;
        Transaction transaction = null;

        while (true) {
            result = customerTransactionAmountGUI();

            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            try {
                transaction = new Transaction(bank, accountNumber, toAccountNumber, amount);
            } catch (Exception e) { // Should never execute
                e.printStackTrace();
            }
            try {
                bank.addTransaction(transaction);
                customerTransactionSuccessDisplay();
                return "M";
            } catch (NoOverdraftAllowedException e) {
                customerTransactionIllegalOverDraftDisplay();
            } catch (Exception e) { // Should never execute
                e.printStackTrace();
            }
        }
    }

    private void customerTransactionIllegalOverDraftDisplay() {
        System.out.println("Fejl i beløb. For stort?");
    }

    private void customerTransactionSuccessDisplay() {
        System.out.println("Overførsel gennemført");
    }

    private String mainGUI() {
        String result;

        while (true) {
            mainDisplay();
            result = this.scanner.next();
            this.scanner.nextLine();
            switch (result) {
                case "1":
                case "2":
                case "3":
                case "Q":
                    return result;
                case "q":
                    return "Q";
            }
        }
    }

    private void mainDisplay() {
        System.out.println("Main Screen");
        System.out.println("Vælg:");
        System.out.println("1 for kunde");
        System.out.println("2 for medarbejder");
        System.out.println("3 for administrator");
        System.out.println("Q for at afslutte programmet");
    }


    private String customerAccessGUI() {
        String result;
        int customerNumber;
        boolean customerFound = true;

        while (true) {
            customerAccessDisplay(customerFound);
            result = this.scanner.next();
            this.scanner.nextLine();
            switch (result) {
                case "B":
                case "b":
                    return "B";
                case "Q":
                case "q":
                    return "Q";
                case "m":
                case "M":
                    return "M";
            }
            try {
                customerNumber = Integer.parseInt(result);
                if (bank.getCustomer(customerNumber) != null) {
                    this.customerNumber = customerNumber;
                    return "" + customerNumber;
                }
            } catch (NumberFormatException e) {
            }
            customerFound = false;

        }
    }

    private void customerAccessDisplay(boolean customerFound) {
        if (!customerFound)
            System.out.println("NOT FOUND! TRY AGAIN");
        System.out.println("customerScreen\nkundenummer:\nBack\nMain\nQuit");

    }

    private String customerGUI() {
        String result;

        while (true) {
            System.out.println("Kunde\n1-kontooversigt\n2-overfør\nBack\nMain\nQuit");
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "1":
                case "2":
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
        }
    }

    private String customerAccountsGUI() {
        //TODO
        return "";
    }

    private String customerAccountInfoGUI() {
        //TODO
        return "";
    }

    private String customerTransactionFromGUI() {
        // return one of: accountNumber, B, M, Q
        //TODO
        return "";
    }

    private String customerTransactionFromSavingsGUI() {
        //TODO
        return "";
    }

    private String customerTransactionToGUI() {
        // Check it's not the cash account!
        // Check the user does not input the interbankaccount!
        //TODO
        return "";
    }

    private String customerTransactionAmountGUI() {
        // check amount is not negative
        // TODO
        return "";
    }

    private String employeeFlow() {
        String result;
        while (true) {
            result = employeeGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "1":
                    result = employeeNewCustomerFlow();
                    break;
                case "2":
                    result = employeeSearchFlow();
                    break;
            }
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String employeeNewCustomerFlow() {
        // TODO
        return "";
    }

    private String employeeSearchFlow() {
        String result;

        while (true) {
            result = employeeSearchGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "1":
                    result = employeeSearchNameFlow();
                    break;
                case "2":
                    result = employeeSearchCustomerNumberFlow();
                    break;
                case "3":
                    result = employeeSearchAccountNumberFlow();
                    break;
            }
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String employeeSearchGUI() {
        String result;

        while (true) {
            employeeSearchDisplay();
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "1":
                case "2":
                case "3":
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
        }
    }

    private void employeeSearchDisplay() {
        System.out.println("1 navn");
        System.out.println("2 kundenummer");
        System.out.println("3 kontonummer");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeGUI() {
        String result;

        while (true) {
            employeeDisplay();
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "1":
                case "2":
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
        }
    }

    private void employeeDisplay() {
        System.out.println("1 opret kunde");
        System.out.println("2 søg kunde");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q quit");
    }

    private String employeeSearchNameFlow() {
        String result;
        String customerName;
        List<Customer> customerList;

        while(true) {
            customerList = new ArrayList<>();
            result = employeeSearchNameGUI();

            for (int i = 0; i < bank.getCustomerList().size(); i++) {
                customerName = bank.getCustomerList().get(i).firstName + " " + bank.getCustomerList().get(i).lastName;
                if (customerName.equals(result)) { // TODO
                    customerList.add(bank.getCustomerList().get(i));
                }
            }

            if (customerList.size() == 0) {
                result=employeeSearchNoMatchFlow();
            } else if (customerList.size() == 1) {
                result = employeeCustomerFlow();
            } else { // >1
                result = employeeSearchMatchesFlow();
            }
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String employeeSearchNameGUI() {
        String result;
        employeeSearchNameDisplay();
        result = scanner.nextLine();
        return result;
    }

    private void employeeSearchNameDisplay() {
        System.out.println("Indtast navn");
    }

    private String employeeSearchNoMatchFlow(){
        String result;
        result=employeeSearchNoMatchGUI();
        return result;
        //TODO
    }

    private String employeeSearchNoMatchGUI(){
        String result;
        employeeSearchNoMatchDisplay();
        return "";
        //TODO
    }

    private void employeeSearchNoMatchDisplay(){
        //TODO
    }

    private String employeeSearchMatchesFlow(){
        String result;
        result=employeeSearchMatchesGUI();
        return result;
        //TODO
    }

    private String employeeSearchMatchesGUI(){
        String result;
        employeeSearchMatchesDisplay();
        return "";
        //TODO
    }

    private void employeeSearchMatchesDisplay(){
        //TODO
    }

    private String employeeSearchCustomerNumberFlow() {
        String result;

        while (true) {
            result = employeeSearchCustomerNumberGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            result = employeeCustomerFlow();
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String employeeSearchCustomerNumberGUI() {
        String result;
        int customerNumber;

        while (true) {
            employeeSearchCustomerNumberDisplay();
            result = scanner.next();
            scanner.nextLine();

            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }

            try {
                customerNumber = Integer.parseInt(result);
                if (bank.getCustomer(customerNumber) != null) {
                    this.customerNumber = customerNumber;
                    return "" + customerNumber;
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    private void employeeSearchCustomerNumberDisplay() {
        System.out.println("Indtast kundenummer");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeSearchAccountNumberFlow() {
        String result;
        result = employeeSearchAccountNumberGUI();
        //TODO
        return "";
    }

    private String employeeSearchAccountNumberGUI() {
        employeeSearchAccountNumberDisplay();
        //TODO
        return "";
    }

    private void employeeSearchAccountNumberDisplay() {
        //TODO
    }

    private String employeeCustomerFlow() {
        String result;
        while (true) {
            result = employeeCustomerGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            result = employeeAccountFlow();
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String employeeCustomerGUI() {
        String result;
        while (true) {
            employeeCustomerDisplay();
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                case "N":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
                case "n":
                    return "N";
            }
            if (bank.getAccount(result) != null)
                return result;
        }
    }

    private void employeeCustomerDisplay() {
        Customer customer = bank.getCustomer(customerNumber);
        List<Account> accountList = customer.accountList;

        System.out.println(customer.firstName + " " + customer.lastName);
        for (int i = 0; i < accountList.size(); i++) {
            System.out.println(accountList.get(i).accountNumber + " " + accountList.get(i).getBalance());
        }
        System.out.println("N Ny konto");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeAccountFlow() {
        String result;
        while (true) {
            result = employeeAccountGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "1":
                    result = employeeInterestRateFlow();
                    break;
                case "2":
                    result = employeeOverdraftFlow();
                    break;
                case "3":
                    result = employeeWithdrawFlow();
                    break;
                case "4":
                    result = employeeDepositFlow();
                    break;
            }
            switch (result) {
                case "M":
                case "Q":
                    return result;
            }

        }
    }

    private String employeeAccountGUI() {
        String result;
        while (true) {
            employeeAccountDisplay();
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
        }
    }

    private void employeeAccountDisplay() {
        System.out.println("OPlysninger om konto her");
        System.out.println("1 rentesats");
        System.out.println("2 overtræk");
        System.out.println("3 udbetal");
        System.out.println("4 indbetal");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeInterestRateFlow() {
        String result;
        double interestRate;
        Account account;

        result = employeeInterestRateGUI();
        switch (result) {
            case "B":
            case "M":
            case "Q":
                return result;
        }
        interestRate = Double.parseDouble(result);
        account = bank.getAccount(accountNumber);
        account.setInterestRate((int) (interestRate * 100));
        persistence.updateAccount(bank, account);

        return "B";
    }

    private String employeeInterestRateGUI() {
        String result;

        while (true) {
            employeeInterestRateDisplay();
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
            try {
                Double.parseDouble(result);
                return result;
            } catch (NumberFormatException e) {
            }
        }
    }

    private void employeeInterestRateDisplay() {
        Account account = bank.getAccount(accountNumber);
        System.out.println("Nuværende: " + account.getInterestRate() / 100.0);
        System.out.println("Indtast ny");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeOverdraftFlow() {
        String result;
        Account account;
        long allowedOverdraft;

        result = employeeOverDraftGUI();
        switch (result) {
            case "B":
            case "M":
            case "Q":
                return result;
        }
        allowedOverdraft = (long) Double.parseDouble(result);
        account = bank.getAccount(accountNumber);
        account.setAllowedOverdraft(allowedOverdraft);
        persistence.updateAccount(bank, account);

        return "B";
    }

    private String employeeOverDraftGUI() {
        String result;
        double doubleResult;

        while (true) {
            employeeOverdraftDisplay();
            result = scanner.next();
            scanner.nextLine();

            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
            try {
                doubleResult = Double.parseDouble(result) * 100.0;
                return "" + ((long) doubleResult);
            } catch (NumberFormatException e) {
            }
        }
    }

    private void employeeOverdraftDisplay() {
        Account account = bank.getAccount(accountNumber);
        System.out.println("Nuværende: " + account.getAllowedOverdraft() / 100.0);
        System.out.println("Indtast ny");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeWithdrawFlow() {
        String result;
        Transaction transaction = null;
        int error;

        while(true) {
            error = 0;
            result = employeeWithdrawGUI();
            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
            }
            amount = Long.parseLong(result);

            try {
                transaction = new Transaction(bank, accountNumber, toAccountNumber, amount);
            } catch (Exception e) {  // Should never happen
                e.printStackTrace();
            }

            try {
                bank.addTransaction(transaction);
            } catch (NoOverdraftAllowedException e) {
                error = 1;
            } catch (NotEnoughCashException e) {
                error = 2;
            } catch (Exception e) { // Should never happen
                e.printStackTrace();
            }
            switch (error) {
                case 0:
                    persistence.addTransaction(bank, transaction);
                    return "B";
                case 1:
                    result = employeeNoOverdraftAllowedFlow();
                    break;
                case 2:
                    result = employeeNotEnoughCashFlow();
                    break;
            }
            switch(result){
                case "M":
                case "Q":
                    return result;
            }
        }
    }

    private String employeeWithdrawGUI() {
        String result;
        double doubleResult;

        while (true) {
            employeeWithdrawDisplay();
            result = scanner.next();
            scanner.nextLine();

            switch (result) {
                case "B":
                case "M":
                case "Q":
                    return result;
                case "b":
                    return "B";
                case "m":
                    return "M";
                case "q":
                    return "Q";
            }
            try {
                doubleResult = Double.parseDouble(result) * 100.0;
                if (doubleResult > 0)
                    return "" + ((long) doubleResult);
            } catch (NumberFormatException e) {
            }
        }
    }

    private void employeeWithdrawDisplay() {
        Account account = bank.getAccount(accountNumber);
        System.out.println("Nuværende balance: " + account.getBalance() / 100.0);
        System.out.println("Indtast beløb at hæve");
        System.out.println("B Back");
        System.out.println("M Main");
        System.out.println("Q Quit");
    }

    private String employeeNoOverdraftAllowedFlow(){
        String result;
        result=employeeNoOverdraftAllowedGUI();
        return result;
        //TODO
    }

    private String employeeNoOverdraftAllowedGUI(){
        String result;
        employeeNoOverdraftAllowedDisplay();
        return "";
        //TODO
    }

    private void employeeNoOverdraftAllowedDisplay(){
        //TODO
    }

    private String employeeNotEnoughCashFlow(){
        String result;
        result=employeeNotEnoughCashGUI();
        return result;
    }

    private String employeeNotEnoughCashGUI(){
        String result;
        employeeNotEnoughCashDisplay();
        return "";
        //TODO
    }

    private void employeeNotEnoughCashDisplay(){
        //TODO
    }

    private String employeeDepositFlow() {
        String result;
        result = employeeDepositGUI();
        return result;
        //TODO
    }

    private String employeeDepositGUI() {
        String result;
        employeeDepositDisplay();
        return "";
        //TODO
    }

    private void employeeDepositDisplay() {
        //TODO
    }

    String adminFlow() throws OperationNotSupportedException {
        adminGUI();
        throw new OperationNotSupportedException();
    }

    String adminGUI() {
        //TODO
        return "";
    }

}
