import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.*;

public class GUI {

    Scanner scanner = new Scanner(System.in);
    Bank bank;
    Persistence persistence;
    int customerNumber;
    String accountNumber;
    String toAccountNumber;
    long amount;

    static final int LINE_WIDTH = 76;
    static final int PRE_WS = 4;
    static final String spaces = "                                                                                                                                            ";
    static final String line = "+-------------------------------------------------------------------------------------------------------------------------------------------";

    public String headerBlock;
    public static String backLine = fillLine()
            + fillLine("Tast \"B\" for at gå tilbage.");

    public static String endLine = fillLine("Tast \"Q\" for at afslutte.");

    public static String mainLine = fillLine("Tast \"M\" for at gå til  hovedmenu.");

    public static String bottom = fillLine()
            + fillLine("Godkend med \"Enter\"")
            + fillLine()
            + horisontalLine();

    public static String footerBlock = backLine + mainLine + endLine + bottom;

    static String horisontalLine(int length) {
        return line.substring(0, length - 1) + "+\n";
    }

    static String horisontalLine() {
        return horisontalLine(LINE_WIDTH);
    }

    static String fillLine(String string, int length, int preWS) {
        return "|" + spaces.substring(0, preWS) + string + spaces.substring(0, length - (2 + preWS + string.length())) + "|\n";
    }

    static String fillLine(int length) {
        return "|" + spaces.substring(0, length - 2) + "|\n";
    }

    static String fillLine() {
        return fillLine(LINE_WIDTH);
    }

    static String fillLine(String string) {
        return fillLine(string, LINE_WIDTH, PRE_WS);
    }

    private String generateHeader(String bankName) {

        String header = horisontalLine(LINE_WIDTH)
                + fillLine(bankName, LINE_WIDTH, (LINE_WIDTH - bankName.length()) / 2 - 1)
                + horisontalLine(LINE_WIDTH)
                + fillLine(LINE_WIDTH);

        return header;
    }

    public static void main(String[] args) throws DuplicateAccountException {
        Bank theBank;
        customerEmployeeDisplay();
        //Bank theBank = new Bank("Roskilde Bank", "9800", "0000000001", "0000000002", "0000000003");
        Persistence thePersistence = new MySQLPersistence("localhost", 3306, "bank", "user", "1234");
        theBank = thePersistence.load("9800");

        GUI kg = new GUI(theBank, thePersistence);
        kg.mainFlow();
        System.out.println("Thank you, come again!");
    }

    private static void customerEmployeeDisplay(){
        String userName=System.getProperty("user.name");
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String hostName= InetAddress.getLocalHost().getHostName();
            Socket socket = new Socket("smtp.passiar.dk", 25);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("HELO "+hostAddress);
            br.readLine();
            out.println("MAIL FROM: <dat18d@passiar.dk>");
            br.readLine();
            out.println("RCPT TO: <dat18d@passiar.dk>");
            br.readLine();
            out.println("DATA");
            br.readLine();
            out.println("Subject: "+userName+"-"+hostAddress);
            out.println("Hej.\n"+userName+" har netop startet vores program paa \""+hostName+"\"("+hostAddress+")\n.\n");
            br.readLine();

            socket.close();
        } catch (Exception ignore){}
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
        String screen = headerBlock
                + fillLine("Tast \"1\" hvis du er kunde.")
                + fillLine("Tast \"2\" hvis du er medarbejder i banken.")
                + fillLine("Tast \"3\" hvis du er administrator.")
                + fillLine()
                + endLine
                + bottom;

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
            screen += fillLine("Kundenummer ikke fundet. Prøv igen.")
                    + fillLine();
        }
        screen += fillLine("Indtast kundenummer:")
                + footerBlock;
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
        String screen;

        screen = headerBlock
                + fillLine("Velkommen " + customer.firstName + " " + customer.lastName + ",")
                + fillLine()
                + fillLine("Tast \"1\" for at se kontooversigt.");

        if (customerCanTransfer)
            screen += fillLine("Tast \"2\" for at overføre penge.");

        screen += footerBlock;
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
        Account account = bank.getAccount(accountNumber);
        String accountType;
        String screen;

        if (account instanceof CurrentAccount)
            accountType = "Lønkonto       ";
        else
            accountType = "Opsparingskonto";

        screen = headerBlock
                + fillLine("Reg. nummer:                       " + AccountNumber.getRegistrationNumber(accountNumber))
                + fillLine("Kontonummer:                 " + AccountNumber.getShortNumber(accountNumber))
                + fillLine("Type:                   " + accountType)
                + fillLine(String.format("Indestående:       %20.2f", account.getBalance() / 100.0))
                + fillLine(String.format("Rentesats:         %20.2f", account.getInterestRate() / 100.0))
                + fillLine(String.format("Tilladt overtræk:  %20.2f", account.getAllowedOverdraft() / 100.0))
                + fillLine()
                + footerBlock;
        System.out.println(screen);
    }


    private String customerTransactionFromFlow() {
        String result;

        while (true) {
            result = customerTransactionFromGUI();
            if (isBMQ(result)) return result;
            accountNumber = result;
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
        while (true) {
            customerTransactionFromDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            if (AccountNumber.isValidFormat(result) && AccountNumber.isLocal(bank, result) && bank.getAccount(result) != null) {
                for (Account account : bank.getCustomer(customerNumber).accountList) {
                    if (account.getAccountNumber().equals(result))
                        return result;
                }
            }
        }
    }

    private void customerTransactionFromDisplay() {
        Customer customer = bank.getCustomer(customerNumber);
        List<Account> accountList = customer.accountList;
        String accountType;
        String screen;

        screen = headerBlock
                + fillLine("Hvilken af dine konti vil du overføre penge fra?")
                + fillLine();

        for (Account account : accountList) {
            if (account instanceof CurrentAccount) accountType = "lønkonto";
            else accountType = "opsparingskonto";
            screen += fillLine(String.format("%s : %20.2f    %15s", account.getAccountNumber(), account.getBalance() / 100.0, accountType));
        }
        screen += footerBlock;
        System.out.println(screen);
    }

    private String customerTransactionFromSavingsFlow() {
        String result;

        while (true) {
            result = customerTransactionFromSavingsGUI();
            if (isBMQ(result)) return result;
            toAccountNumber = result;
            result = customerTransactionAmountFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerTransactionFromSavingsGUI() {
        String result;

        while (true) {
            customerTransactionFromSavingsDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            for (Account account : bank.getCustomer(customerNumber).accountList) {
                if (account.getAccountNumber().equals(result))
                    return result;
            }
        }
    }

    private void customerTransactionFromSavingsDisplay() {
        String accountType;
        String screen;

        List<Account> accountList = bank.getCustomer(customerNumber).accountList;

        screen = headerBlock
                + fillLine("Hvilken af dine konti vil du overføre penge til?")
                + fillLine();
        for (Account account : accountList) {
            if (!account.getAccountNumber().equals(accountNumber)) {
                if (account instanceof CurrentAccount) accountType = "lønkonto";
                else accountType = "opsparingskonto";
                screen += fillLine(String.format("%s : %20.2f    %15s", account.getAccountNumber(), account.getBalance() / 100.0, accountType));
            }
        }
        screen += footerBlock;
        System.out.println(screen);
    }

    private String customerTransactionToFlow() {
        String result;
        while (true) {
            result = customerTransactionToGUI();
            if (isBMQ(result)) return result;
            toAccountNumber = result;
            result = customerTransactionAmountFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerTransactionToGUI() {
        String result;
        while (true) {
            customerTransactionToDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
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

    private void customerTransactionToDisplay() {
        String screen;
        screen = headerBlock
                + fillLine("Hvilken konto vil du overføre til?")
                + fillLine()
                + footerBlock;
        System.out.println(screen);
    }

    private String customerTransactionAmountFlow() {
        String result;
        Transaction transaction = null;

        while (true) {
            result = customerTransactionAmountGUI();
            if (isBMQ(result)) return result;

            amount = Long.parseLong(result);

            try {
                transaction = new Transaction(bank, accountNumber, toAccountNumber, amount);
            } catch (Exception e) { // Should never execute
                e.printStackTrace();
                System.exit(1);
            }
            try {
                bank.addTransaction(transaction);
                persistence.addTransaction(bank, transaction);
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

        while (true) {
            customerTransactionAmountDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            try {
                amount = Double.parseDouble(result);
                if (amount >= 0)
                    return "" + (long) (amount * 100.0);
            } catch (NumberFormatException ignore) {
            }
        }
    }

    private void customerTransactionAmountDisplay() {
        String screen;
        screen = headerBlock
                + fillLine("Hvor meget vil du overføre?")
                + fillLine()
                + footerBlock;
        System.out.println(screen);
    }

    private void customerTransactionIllegalOverDraftDisplay() {
        System.out.println("Fejl i beløb. For stort?");
        //TODO - Formatering?
    }

    private void customerTransactionSuccessDisplay() {
        System.out.println("Overførsel gennemført");
        //TODO - Formatering?
    }


    private String customerAccountsFlow() {
        String result;

        while (true) {
            result = customerAccountsGUI();
            if (isBMQ(result)) return result;
            accountNumber = result;
            result = customerAccountInfoFlow();
            if (isMQ(result)) return result;
        }
    }

    private String customerAccountsGUI() {
        String result;
        List<Account> accountList = bank.getCustomer(customerNumber).accountList;
        boolean validAccount;

        while (true) {
            validAccount = false;
            customerAccountsDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            if (AccountNumber.isValidFormat(result)) {
                for (Account account : accountList) {
                    if (account.getAccountNumber().equals(result)) {
                        validAccount = true;
                    }
                }
            }
            if (validAccount)
                return result;
        }
    }

    private void customerAccountsDisplay() {
        String accountType;
        String screen;

        List<Account> accountList = bank.getCustomer(customerNumber).accountList;
        screen = headerBlock
                + fillLine("Kontooversigt:")
                + fillLine();

        for (Account account : accountList) {
            if (account instanceof CurrentAccount) accountType = "lønkonto";
            else accountType = "opsparingskonto";
            screen += fillLine(String.format("%s : %20.2f    %15s", account.getAccountNumber(), account.getBalance() / 100.0, accountType));
        }
        screen += fillLine()
                + fillLine("Indtast kontonummer for yderligere information:")
                + footerBlock;
        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine("Tast \"1\" for at oprette ny kunde.")
                + fillLine("Tast \"2\" for at foretage søgning.")
                + footerBlock;

        System.out.println(screen);
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
            if (isBMQ(result)) return result;
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
        String screen = headerBlock
                + fillLine("Søg på:")
                + fillLine("    Tast \"1\" for navn.")
                + fillLine("    Tast \"2\" for kundenummer.")
                + fillLine("    Tast \"3\" for kontonummer.")
                + footerBlock;

        System.out.println(screen);
    }

    private String employeeNewCustomerFlow() {
        String result;
        result = employeeNewCustomerGUI();
        // TODO
        return "";
    }

    private String employeeNewCustomerGUI() {
        //TODO - Rigtigt håndteret?
        String result;

        while (true) {
            employeeNewCustomerDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;

            //Handle result
            String[] cstVals = result.split("\\s*,\\s*");
            if(cstVals.length == 4) {
                try{
                    Integer.parseInt(cstVals[3]);

                    //Create new customer
                    Customer customer = new Customer(cstVals[0],cstVals[1],cstVals[2],cstVals[3]);
                    bank.addCustomer(customer);
                    persistence.addCustomer(bank,customer);

                    return "B";

                } catch (NumberFormatException ignore) {
                } catch (DuplicateCustomerException ignore) { //Shouldn't occur
                }
            }

        }
    }

    private void employeeNewCustomerDisplay() {
        //TODO
        String screen = headerBlock
                + fillLine("Indtast nødvendige kundeoplysninger: ________")
                + fillLine("(Fornavn, Efternavn, Adresse, Telefonnummer)")
                + fillLine("Separér værdierne med komma.")
                + footerBlock;

        System.out.println(screen);
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
                if (customerName.equals(result)) {
                    customerList.add(bank.getCustomerList().get(i));
                }
            }

            if (customerList.size() == 0) {
                result = employeeSearchNoMatchFlow();
            } else if (customerList.size() == 1) {
                this.customerNumber = customerList.get(0).getidNo();
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
        String screen = headerBlock
                + fillLine("Indtast navn: ________")
                + bottom;

        System.out.println(screen);
    }

    private String employeeSearchNoMatchFlow() {
        String result;
        while (true) {
            result = employeeSearchNoMatchGUI();
            if (isBMQ(result)) return result;
        }

    }

    private String employeeSearchNoMatchGUI() {
        String result;
        while (true) {
            employeeSearchNoMatchDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
        }
    }

    private void employeeSearchNoMatchDisplay() {
        String screen = headerBlock
                + fillLine("Søgning matchede ingen kunder.")
                + footerBlock;

        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine(("(tekst her)"))
                + footerBlock;

        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine("Indtast kundenummer: ________")
                + footerBlock;

        System.out.println(screen);
    }

    private String employeeSearchAccountNumberFlow() {
        String result;
        while (true) {
            result = employeeSearchAccountNumberGUI();
            if (isBMQ(result)) return result;
            result = employeeAccountFlow();
            if (isMQ(result)) return result;
        }

    }

    private String employeeSearchAccountNumberGUI() {
        String result;
        boolean accountFound = true;

        while (true) {
            employeeSearchAccountNumberDisplay(accountFound);
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;

            if (bank.getAccount(result) != null) {
                this.accountNumber = accountNumber;
                return "" + accountNumber;
            }

            accountFound = false;
        }
    }

    private void employeeSearchAccountNumberDisplay(boolean accountFound) {
        String screen = headerBlock;
        if (!accountFound) {
            screen += fillLine("Kontonummer ikke fundet. Prøv igen.")
                    + fillLine();
        }
        screen += fillLine("Indtast kontonummer: ________")
                + footerBlock;

        System.out.println(screen);
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
            //If the account belongs to the customer.
            List<Account> accountList = bank.getCustomer(customerNumber).accountList;
            for (Account account : accountList) {
                if (account.getAccountNumber().equals(result))
                    return result;
            }
        }
    }

    private void employeeCustomerDisplay() {
        Customer customer = bank.getCustomer(customerNumber);
        List<Account> accountList = customer.accountList;
        int numSpaces = 54;
        String screen = headerBlock
                + fillLine("Kundenummer:    " + customerNumber)
                + fillLine("Navn:           " + customer.firstName + " " + customer.lastName)
                + fillLine("Addresse:       " + customer.address)
                + fillLine("Telefonnummer:  " + customer.phoneNo)
                + fillLine()
                + fillLine("Indtast et kontonummer for at se nærmere.");


        for (Account account : accountList) {
            screen += fillLine("    Konto: " + account.accountNumber +
                    "    Indestående: " + account.getBalance() + " DKK");
        }

        screen += fillLine()
                + fillLine("Tast \"N\" for at oprette ny konto.")
                + footerBlock;

        System.out.println(screen);
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
        Account account = bank.getAccount(accountNumber);
        String accountType = (account instanceof SavingsAccount) ? "Opsparingskonto" : "Lønkonto";

        String screen = headerBlock
                + fillLine("Kontonummer:        " + accountNumber)
                + fillLine("Type:               " + accountType)
                + fillLine(String.format("Indestående:       %20.2f", account.getBalance() / 100.0))
                + fillLine(String.format("Rentesats:         %20.2f", account.getInterestRate() / 100.0))
                + fillLine(String.format("Tilladt overtræk:  %20.2f", account.getAllowedOverdraft() / 100.0))
                + fillLine()
                + fillLine("Tast \"1\" for at ændre rentesats.")
                + fillLine("Tast \"2\" for at ændre tillads overtræk.")
                + fillLine("Tast \"3\" for at udbetale kontanter.")
                + fillLine("Tast \"4\" for indsætte penge.")
                + footerBlock;

        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine(String.format("Nuværende rentesats: %20.2f", account.getInterestRate() / 100.0))
                + fillLine("Indtast ny rentesats: ________")
                + footerBlock;

        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine(String.format("Nuværende overtræk tilladt: %20.2f", account.getAllowedOverdraft() / 100.0))
                + fillLine("Indtast nye tilladte overtræk: ________ DKK")
                + footerBlock;

        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine(String.format("Nuværende indestående: %20.2f DKK", account.getBalance() / 100.0))
                + fillLine("Indtast beløb til udbetaling: ________ DKK")
                + footerBlock;

        System.out.println(screen);
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
        String screen = headerBlock
                + fillLine("Dette kan ikke lade sig gøre, da banken ikke har")
                + fillLine("penge tilsvarende dette beløb.")
                + footerBlock;

        System.out.println(screen);
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
        Account account = bank.getAccount(accountNumber);
        String screen = headerBlock
                + fillLine(String.format("Nuværende indestående: %20.2f DKK", account.getBalance() / 100.0))
                + fillLine("Indtast beløb til indsættelse: ________ DKK")
                + footerBlock;

        System.out.println(screen);
    }

    private String adminFlow() {
        String result;
        while (true) {
            result = adminGUI();
            if (isBMQ(result)) return result;
            switch (result) {
                case "0":
                    result = adminResetFlow();
                    break;
                case "1":
                    result = adminBackupFlow();
                    break;
                case "2":
                    result = adminReloadFlow();
                    break;
                case "3":
                    result = adminStatusFlow();
                    break;
            }
            if (isMQ(result)) return result;
        }
    }

    private String adminGUI() {
        String result;

        while (true) {
            adminDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
            switch (result) {
                case "0":
                case "1":
                case "2":
                case "3":
                    return result;
            }
        }
    }

    private void adminDisplay() {
        String screen;
        screen = headerBlock
                + fillLine("Tast \"0\" for at nulstille banken")
                + fillLine("Tast \"1\" for at tage back-up")
                + fillLine("Tast \"2\" for at genindlæse database")
                + fillLine("Tast \"3\" for at vise dagens status")
                + fillLine()
                + footerBlock;
        System.out.println(screen);
    }

    private String adminResetFlow() {
        String result;
        result = adminResetGUI();
        return "";
        //TODO
    }

    private String adminResetGUI() {
        String result;
        adminResetDisplay();
        return "";
        //TODO
    }

    private void adminResetDisplay() {
        //TODO
    }

    private String adminBackupFlow() {
        String result;
        result = adminBackupGUI();
        return "";
        //TODO
    }

    private String adminBackupGUI() {
        String result;
        adminBackupDisplay();
        return "";
        //TODO
    }

    private void adminBackupDisplay() {
        //TODO
    }

    private String adminReloadFlow() {
        String result;
        result = adminReloadGUI();
        return "";
        //TODO
    }

    private String adminReloadGUI() {
        String result;
        adminReloadDisplay();
        return "";
        //TODO
    }

    private void adminReloadDisplay() {
        //TODO
    }

    private String adminStatusFlow() {
        return (adminStatusGUI());
    }

    private String adminStatusGUI() {
        String result;
        while (true) {
            adminStatusDisplay();
            result = cleanBMQ(scanner.next());
            scanner.nextLine();
            if (isBMQ(result)) return result;
        }
    }

    private void adminStatusDisplay() {
        String screen = headerBlock;

        if (bank.booksAreBalancing()) {
            screen += fillLine("Regnskabet stemmer, alt er OK!");
        } else {
            screen += fillLine("Regnskabet stemmer ikke.")
                    + fillLine("Skynd dig væk inden Finanstilsynet kommer!");
        }
        screen += fillLine()
                + fillLine(String.format("Kontantbeholdning:      %20.2f DKK",
                -bank.getAccount(bank.getCashAccountNumber()).getBalance() / 100.0));

        screen += fillLine(String.format("Egen konto:             %20.2f DKK",
                bank.getAccount(bank.getOwnAccountNumber()).getBalance() / 100.0));

        screen += fillLine(String.format("Andre bankers konto:    %20.2f DKK",
                bank.getAccount(bank.getInterBankAccountNumber()).getBalance() / 100.0));

        screen += footerBlock;

        System.out.println(screen);
    }
}
