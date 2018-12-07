public class GUIConstructor {

    //CUSTOMER_ACCESS --> customerAccessScreen(false);
    //CUSTOMER_INVALID --> customerAccessScreen(true);


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


    public String mainScreen() {
        String screen = headerBlock;
        screen +=
                "|    Tast \"1\" hvis du er kunde.                                            |\n" +
                "|    Tast \"2\" hvis du er medarbejder i banken.                             |\n" +
                "|    Tast \"3\" hvis du er administrator.                                    |\n" +
                "|                                                                          |\n";
        screen += end;
        screen += bottom;
        return screen;
    }

    public String customerAccessScreen(boolean invalid) {
        String screen = headerBlock;
        if(invalid){
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
