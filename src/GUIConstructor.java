public class GUIConstructor {

    //CUSTOMER_ACCESS --> customerAccessScreen(true);
    //CUSTOMER_INVALID --> customerAccessScreen(false);


    public static String headerBlock;
    public static String back =
            "|                                                                          |\n"+
            "|    Tast \"B\" for at gå tilbage.                                           |\n";

    public static String end =
            "|    Tast \"Q\" for at afslutte.                                             |\n";

    public static String bottom =
            "|                                                                          |\n" +
            "|    Godkend med \"Enter\".                                                  |\n" +
            "|                                                                          |\n" +
            "+--------------------------------------------------------------------------+\n\n";


    public GUIConstructor(String bankName) {
        this.headerBlock = generateHeader(bankName);
    }


    private String generateHeader(String bankName) {

        String header = "+--------------------------------------------------------------------------+\n";
        header += "|";

        int numSpaces = (74 - bankName.length()) / 2;

        for(int i = 0; i < numSpaces; i++) {
            header += " ";
        }

        header += bankName;

        for(int i = 0; i < numSpaces; i++) {
            header += " ";
        }

        if(!(bankName.length() % 2 == 0)) {
            header += " ";
        }

        header +=
                "|\n" +
                "+--------------------------------------------------------------------------+\n" +
                "|                                                                          |\n";

        return header;
    }


    public String mainScreen(boolean valid) {
        String screen = headerBlock;
        if(!valid){
            screen += "|    Udefineret input. Prøv igen.                                          |\n" +
                      "|                                                                          |\n";
        }
        screen +=
                "|    Tast \"1\" hvis du er kunde.                                            |\n" +
                "|    Tast \"2\" hvis du er medarbejder i banken.                             |\n" +
                "|    Tast \"3\" hvis du er administrator.                                    |\n" +
                "|                                                                          |\n";
        screen += end;
        screen += bottom;
        return screen;
    }

    public String customerAccessScreen(boolean valid) {
        String screen = headerBlock;
        if(!valid){
            screen += "|    Kundenummer ikke fundet. Prøv igen.                                   |\n" +
                      "|                                                                          |\n";
        }
        screen += "|    Indtast kundenummer: ________                                         |";
        screen += back;
        screen += end;
        screen += bottom;
        return screen;
    }

    public String customerScreen(String firstName, String lastName) {
        String screen = headerBlock;
        //        "|                                                                          |"

        screen += "|    Velkommen " + firstName + " " + lastName + ",";

        int nameLen = firstName.length() + lastName.length();
        for(int i = 0; i < 50-nameLen; ) {
            screen += " ";
        }
        screen += "|\n";
        screen += "|                                                                          |\n" +
                  "|    Tast \"1\" for at se kontooversigt.                                     |\n" +
                  "|    Tast \"2\" for at overføre penge.                                       |";
        screen += bottom;
        return screen;
    }



    /*
    public String xxxScreen() {
        String screen = headerBlock;
        //        "|                                                                          |"
        screen += ""; //Stuff
        screen += bottom;
        return screen;
    }
    */



}
