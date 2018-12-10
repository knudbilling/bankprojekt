import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GUI {

    Scanner scanner = new Scanner(System.in);
    Bank bank;
    Persistence persistence;
    int customerNumber;
    String accountNumber;
    String toAccountNumber;
    long amount;
    public String headerBlock = "";
    public static String backLine =
            "|                                                                          |\n" +
                    "|    Tast \"B\" for at gå tilbage.                                           |\n";

    public static String endLine =
            "|    Tast \"Q\" for at afslutte.                                             |\n";

    public static String mainLine =
            "|    Tast \"M\" for at gå til  hovedmenu.                                    |\n";



    public static String bottom =
            "|                                                                          |\n" +
                    "|    Godkend med \"Enter\".                                                  |\n" +
                    "|                                                                          |\n" +
                    "+--------------------------------------------------------------------------+\n\n";


    private String generateHeader(String bankName) {

        String header = "+--------------------------------------------------------------------------+\n";
        header += "|";

        int numSpaces = (74 - bankName.length()) / 2;

        for (int i = 0; i < numSpaces; i++) {
            header += " ";
        }

        header += bankName;

        for (int i = 0; i < numSpaces; i++) {
            header += " ";
        }

        if (!(bankName.length() % 2 == 0)) {
            header += " ";
        }

        header +=
                "|\n" +
                        "+--------------------------------------------------------------------------+\n" +
                        "|                                                                          |\n";

        return header;
    }

    public static void main(String[] args) throws DuplicateAccountException {
        Bank theBank;
        //Bank theBank = new Bank("Roskilde Bank", "9800", "0000000001", "0000000002", "0000000003");
        Persistence thePersistence = new MySQLPersistence("localhost", 3306, "bank", "user", "1234");
        theBank=thePersistence.load("9800");

        GUI kg = new GUI(theBank, thePersistence);
        kg.mainFlow();
        System.out.println("Thank you, come again!");
    }


    public GUI(Bank newBank, Persistence newPersistence) {
        this.bank = newBank;
        this.persistence = newPersistence;
        headerBlock = generateHeader(bank.getName());
    }

    /**
     * Examine if a string is "M" or "Q"
     *
     * @param string the string to examine
     * @return true if string is "M" or "Q"
     */
    private static boolean isMQ(String string) {
        switch (string) {
            case "M":
            case "Q":
                return true;
        }
        return false;
    }

    /**
     * Examine if a string is "B", "M" or "Q"
     *
     * @param string the string to examine
     * @return true is string is "B", "M" or "Q"
     */
    private static boolean isBMQ(String string) {
        switch (string) {
            case "B":
            case "M":
            case "Q":
                return true;
        }
        return false;
    }

    /**
     * Transform lowercase "b", "m" and "q" to uppercase
     *
     * @param result the string to transform
     * @return "B", "M" or "Q" if input is "b", "m" or "q", otherwise the original string is returned
     */
    private static String cleanBMQ(String result) {
        switch (result) {
            case "b":
                return "B";
            case "m":
                return "M";
            case "q":
                return "Q";
        }
        return result;
    }

    private void mainFlow() {
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

    private String mainGUI() {
        String result;
        while (true) {
            mainDisplay();
            result = scanner.next();
            scanner.nextLine();
            switch (result) {
                case "1":
                case "2":
                case "3":
                    return result;
                case "Q":
                case "q":
                    return "Q";
            }
        }
    }

    private void mainDisplay() {
        String screen = headerBlock;
        screen +=
                "|    Tast \"1\" hvis du er kunde.                                            |\n" +
                        "|    Tast \"2\" hvis du er medarbejder i banken.                             |\n" +
                        "|    Tast \"3\" hvis du er administrator.                                    |\n" +
                        "|                                                                          |\n";
        screen += endLine;
        screen += bottom;
        System.out.println(screen);
    }

    private String customerAccessFlow() {
        String result;

        while (true) {
            result = customerAccessGUI();
            if (isBMQ(result)) return result;

            result = customerFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerAccessGUI() {
        String result;
        int customerNumber;
        boolean customerFound = true;

        while (true) {
            customerAccessDisplay(customerFound);
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;

            try {
                customerNumber = Integer.parseInt(result);
                if (bank.getCustomer(customerNumber) != null) {
                    this.customerNumber = customerNumber;
                    return "" + customerNumber;
                }
            } catch (NumberFormatException ignore) {
            }
            customerFound = false;

        }
    }

    private void customerAccessDisplay(boolean customerFound) {
        String screen = headerBlock;
        if (!customerFound) {
            screen += "|    Kundenummer ikke fundet. Prøv igen.                                   |\n" +
                    "|                                                                          |\n";
        }
        screen += "|    Indtast kundenummer: ________                                         |\n";
        screen += backLine;
        screen += mainLine;
        screen += endLine;
        screen += bottom;
        System.out.println(screen);
    }

    private String customerFlow() {
        String result;
        while (true) {
            result = customerGUI();
            if (isBMQ(result)) return result;
            switch (result) {
                case "1":
                    result = customerAccountsFlow();
                    break;
                case "2":
                    result = customerTransactionFromFlow();
                    break;
            }
            if (isMQ(result)) return result;
        }
    }

    private String customerGUI() {
        String result;
        boolean customerCanTransfer = false;
        Customer customer = bank.getCustomer(customerNumber);

        if (customer.accountList.size() > 1) {
            customerCanTransfer = true;
        } else if (customer.accountList.size() == 1) {
            if (customer.accountList.get(0) instanceof CurrentAccount)
                customerCanTransfer = true;
        }

        while (true) {
            customerDisplay(customerCanTransfer);
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            switch (result) {
                case "1":
                    return result;
                case "2":
                    if (customerCanTransfer) return result;
            }
        }
    }

    private void customerDisplay(boolean customerCanTransfer) {
        Customer customer = bank.getCustomer(customerNumber);
        String screen = headerBlock;

        screen += "|    Velkommen " + customer.firstName + " " + customer.lastName + ",";

        int nameLen = customer.firstName.length() + customer.lastName.length();
        for(int i = 0; i < 58-nameLen;i++ ) {
            screen += " ";
        }
        screen += "|\n";
        screen += "|                                                                          |\n" +
                "|    Tast \"1\" for at se kontooversigt.                                     |\n";
        if(customerCanTransfer)
                screen+="|    Tast \"2\" for at overføre penge.                                       |\n";
        screen += backLine;
        screen+=mainLine;
        screen+=endLine;
        screen += bottom;
        System.out.println(screen);
    }

    private String customerAccountInfoFlow() {
        return (customerAccountInfoGUI());
    }

    private String customerAccountInfoGUI() {
        String result;
        while (true) {
            customerAccountInfoDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
        }
    }

    private void customerAccountInfoDisplay() {
        Account account=bank.getAccount(accountNumber);
        String accountType;
        if(account instanceof CurrentAccount)
            accountType="Lønkonto       ";
        else
            accountType="Opsparingskonto";

        System.out.print(headerBlock);
        System.out.println("|    Reg. nummer:       "+AccountNumber.getRegistrationNumber(accountNumber)+"|");
        System.out.println("|    Kontonummer:       "+AccountNumber.getShortNumber(accountNumber)+"|");
        System.out.println("|    Type:              "+accountType+"|");
        System.out.printf("|    Indestående:       %20.2f   |%n",account.getBalance()/100.0);
        System.out.printf("|    Rentesats:         %20.2f   |%n",account.getInterestRate()/100.0);
        System.out.printf("|    Tilladt overtræk:  %20.2f   |%n",account.getAllowedOverdraft()/100.0);
        System.out.println("|                                                                          |");
        System.out.println(backLine+mainLine+endLine+bottom);
    }

    private String customerTransactionFromFlow() {
        String result;

        while (true) {
            result = customerTransactionFromGUI();
            if (isBMQ(result)) return result;
            accountNumber=result;
            if (this.bank.getAccount(this.accountNumber) instanceof SavingsAccount) {
                result = customerTransactionFromSavingsFlow();
            } else {
                result = customerTransactionToFlow();
            }
            if (isMQ(result)) return result;
        }
    }

    private String customerTransactionFromGUI() {
        String result;
        List<Account> accountList = bank.getCustomer(customerNumber).accountList;
        while (true) {
            customerTransactionFromDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            if (AccountNumber.isValidFormat(result) && AccountNumber.isLocal(bank, result) && bank.getAccount(result) != null) {
                for (int i = 0; i < accountList.size(); i++) {
                    if (accountList.get(i).getAccountNumber().equals(result))
                        return result;
                }
            }
        }
    }

    private void customerTransactionFromDisplay() {
        Customer customer = bank.getCustomer(customerNumber);
        List<Account> accountList = customer.accountList;

        for (int i = 0; i < accountList.size(); i++)
            System.out.println(accountList.get(i).getAccountNumber() + " : " + accountList.get(i).getBalance());
        System.out.println("kontonummer:");
        System.out.println("BMQ");
    }

    private String customerTransactionFromSavingsFlow() {
        String result;

        while (true) {
            result = customerTransactionFromSavingsGUI();
            if (isBMQ(result)) return result;
            toAccountNumber=result;
            result = customerTransactionAmountFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerTransactionFromSavingsGUI() {
        String result;
        List<Account> accountList=bank.getCustomer(customerNumber).accountList;
        //TODO
        while (true) {
            customerTransactionFromSavingsDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            if(AccountNumber.isValidFormat(result)){
                for(int i=0;i<accountList.size();i++){
                    if(accountList.get(i).getAccountNumber().equals(result))
                        return result;
                }
            }
        }
    }

    private void customerTransactionFromSavingsDisplay() {
        List<Account> accountList = bank.getCustomer(customerNumber).accountList;

        for (int i = 0; i < accountList.size(); i++) {
            if (!accountList.get(i).getAccountNumber().equals(accountNumber))
                System.out.println(accountList.get(i).getAccountNumber() + " " + accountList.get(i).getBalance());
        }
        System.out.println("BMQ");
    }

    private String customerTransactionToFlow() {
        String result;
        while (true) {
            result = customerTransactionToGUI();
            if (isBMQ(result)) return result;
            toAccountNumber=result;
            result = customerTransactionAmountFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerTransactionToGUI() {
        String result;
        while(true) {
            customerTransactionToDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            if (AccountNumber.isValidFormat(result)) {
                if (!result.equals(bank.getCashAccountNumber())) {
                    if (!result.equals(bank.getInterBankAccountNumber())) {
                        if (!result.equals(accountNumber)) {
                            if (AccountNumber.exists(bank, result)) {
                                return result;
                            }
                        }
                    }
                }
            }
        }
    }

    private void customerTransactionToDisplay() {
        System.out.println("modtager reg+kontonummer");
        System.out.println("BMQ");
        //TODO
    }

    private String customerTransactionAmountFlow() {
        String result;
        Transaction transaction = null;

        while (true) {
            result = customerTransactionAmountGUI();
            if (isBMQ(result)) return result;

            amount=Long.parseLong(result);

            try {
                transaction = new Transaction(bank, accountNumber, toAccountNumber, amount);
            } catch (Exception e) { // Should never execute
                e.printStackTrace();
                System.out.println(accountNumber+" "+toAccountNumber+" "+amount);
                System.exit(1);
            }
            try {
                bank.addTransaction(transaction);
                persistence.addTransaction(bank,transaction);
                customerTransactionSuccessDisplay();
                return "M";
            } catch (NoOverdraftAllowedException e) {
                customerTransactionIllegalOverDraftDisplay();
            } catch (Exception e) { // Should never execute
                e.printStackTrace();
                System.exit(2);
            }
        }
    }

    private String customerTransactionAmountGUI() {
        String result;
        double amount;

        while(true) {
            customerTransactionAmountDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            try {
                 amount = Double.parseDouble(result);
                if(amount>0)
                    return ""+(long)(amount*100.0);
            } catch (NumberFormatException ignore) {
            }
        }
    }

    private void customerTransactionAmountDisplay() {
        System.out.println("Beløb");
        System.out.println("BMQ");
        //TODO
    }

    private void customerTransactionIllegalOverDraftDisplay() {
        System.out.println("Fejl i beløb. For stort?");
    }

    private void customerTransactionSuccessDisplay() {
        System.out.println("Overførsel gennemført");
    }


    private String customerAccountsFlow() {
        String result;

        while (true) {
            result = customerAccountsGUI();
            if (isBMQ(result)) return result;
            accountNumber=result;
            result = customerAccountInfoFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerAccountsGUI() {
        String result;
        List<Account> accountList=bank.getCustomer(customerNumber).accountList;
        boolean validAccount;

        while(true) {
            validAccount = false;
            customerAccountsDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            if (AccountNumber.isValidFormat(result)) {
                for (int i = 0; i < accountList.size(); i++) {
                    if (accountList.get(i).getAccountNumber().equals(result)) {
                        validAccount = true;
                    }
                }
            }
            if (validAccount)
                return result;
        }
    }

    private void customerAccountsDisplay() {
        List<Account> accountList = bank.getCustomer(customerNumber).accountList;
        for(int i=0;i<accountList.size();i++){
            System.out.println(accountList.get(i).getAccountNumber()+" "+accountList.get(i).getBalance()/100.0);
        }
        System.out.println("BMQ");
    }

    private String employeeFlow() {
        String result;
        while (true) {
            result = employeeGUI();
            if (isBMQ(result)) return result;

            switch (result) {
                case "1":
                    result = employeeNewCustomerFlow();
                    break;
                case "2":
                    result = employeeSearchFlow();
                    break;
            }
            if (isMQ(result)) return result;
        }
    }

    private String employeeGUI() {
        String result;

        while (true) {
            employeeDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            switch (result) {
                case "1":
                case "2":
                    return result;
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

    private String employeeSearchFlow() {
        String result;

        while (true) {
            result = employeeSearchGUI();
            switch (result) {
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
            if (isMQ(result)) return result;
        }
    }

    private String employeeSearchGUI() {
        String result;

        while (true) {
            employeeSearchDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            switch (result) {
                case "1":
                case "2":
                case "3":
                    return result;
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

    private String employeeNewCustomerFlow() {
        String result;
        result = employeeNewCustomerGUI();
        // TODO
        return "";
    }

    private String employeeNewCustomerGUI() {
        //TODO
        employeeNewCustomerDisplay();
        return "";
    }

    private void employeeNewCustomerDisplay() {
        //TODO
    }

    private String employeeSearchNameFlow() {
        String result;
        String customerName;
        List<Customer> customerList;

        while (true) {
            customerList = new ArrayList<>();
            result = employeeSearchNameGUI();

            for (int i = 0; i < bank.getCustomerList().size(); i++) {
                customerName = bank.getCustomerList().get(i).firstName + " " + bank.getCustomerList().get(i).lastName;
                if (customerName.equals(result)) { // TODO
                    customerList.add(bank.getCustomerList().get(i));
                }
            }

            if (customerList.size() == 0) {
                result = employeeSearchNoMatchFlow();
            } else if (customerList.size() == 1) {
                result = employeeCustomerFlow();
            } else { // >1
                result = employeeSearchMatchesFlow();
            }
            if (isMQ(result)) return result;
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

    private String employeeSearchNoMatchFlow() {
        String result;
        result = employeeSearchNoMatchGUI();
        return result;
        //TODO
    }

    private String employeeSearchNoMatchGUI() {
        String result;
        employeeSearchNoMatchDisplay();
        return "";
        //TODO
    }

    private void employeeSearchNoMatchDisplay() {
        //TODO
    }

    private String employeeSearchMatchesFlow() {
        String result;
        result = employeeSearchMatchesGUI();
        return result;
        //TODO
    }

    private String employeeSearchMatchesGUI() {
        String result;
        employeeSearchMatchesDisplay();
        return "";
        //TODO
    }

    private void employeeSearchMatchesDisplay() {
        //TODO
    }

    private String employeeSearchCustomerNumberFlow() {
        String result;

        while (true) {
            result = employeeSearchCustomerNumberGUI();
            if (isBMQ(result)) return result;
            result = employeeCustomerFlow();
            if (isMQ(result)) return result;
        }
    }

    private String employeeSearchCustomerNumberGUI() {
        String result;
        int customerNumber;

        while (true) {
            employeeSearchCustomerNumberDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;

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
            if (isBMQ(result)) return result;
            result = employeeAccountFlow();
            if (isMQ(result)) return result;
        }
    }

    private String employeeCustomerGUI() {
        String result;
        while (true) {
            employeeCustomerDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            switch (result) {
                case "N":
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
            if (isBMQ(result)) return result;
            switch (result) {
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
            if (isMQ(result)) return result;

        }
    }

    private String employeeAccountGUI() {
        String result;
        while (true) {
            employeeAccountDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            switch (result) {
                case "1":
                case "2":
                case "3":
                case "4":
                    return result;
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
        if (isBMQ(result)) return result;
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
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            try {
                Double.parseDouble(result);
                return result;
            } catch (NumberFormatException ignore) {
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
        if (isBMQ(result)) return result;
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
            result = cleanBMQ(scanner.next());
            scanner.nextLine();

            if (isBMQ(result)) return result;
            try {
                doubleResult = Double.parseDouble(result) * 100.0;
                return "" + ((long) doubleResult);
            } catch (NumberFormatException ignore) {
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

        while (true) {
            error = 0;
            result = employeeWithdrawGUI();
            if (isBMQ(result)) return result;
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
            if (isMQ(result)) return result;
        }
    }

    private String employeeWithdrawGUI() {
        String result;
        double doubleResult;

        while (true) {
            employeeWithdrawDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
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

    private String employeeNoOverdraftAllowedFlow() {
        String result;
        result = employeeNoOverdraftAllowedGUI();
        return result;
        //TODO
    }

    private String employeeNoOverdraftAllowedGUI() {
        String result;
        employeeNoOverdraftAllowedDisplay();
        return "";
        //TODO
    }

    private void employeeNoOverdraftAllowedDisplay() {
        //TODO
    }

    private String employeeNotEnoughCashFlow() {
        String result;
        result = employeeNotEnoughCashGUI();
        return result;
    }

    private String employeeNotEnoughCashGUI() {
        String result;
        employeeNotEnoughCashDisplay();
        return "";
        //TODO
    }

    private void employeeNotEnoughCashDisplay() {
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

    private String adminFlow() {
        String result;
        //TODO
        result = adminGUI();
        return "";
    }

    private String adminGUI() {
        //TODO
        adminDisplay();
        return "";
    }

    private void adminDisplay() {
        //TODO
    }
}
