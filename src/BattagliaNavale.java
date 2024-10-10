import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class BattagliaNavale {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        final String TURN_LINE = "(¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´¯`·.¸¸.·´)\n";
        final String CENTER = "                                                               ";
        final String MID_CENTER = "           ";

        boolean gameOver = false;
        //to skip the intro
        boolean skip = false;

        System.out.println(TURN_LINE);
        System.out.println(CENTER + "Benvenuto in Battaglia Navale!");
        sep(2);
        // MAIN
        do{
            // INTRO MENU:
            if (!skip) {
                do {
                    int choice;
                    System.out.println(CENTER + "Premi [0] -> GIOCA! ");
                    System.out.println(CENTER + "Premi [1] -> REGOLAMENTO");
                    System.out.println(CENTER + "Premi [2] -> ESCI");
                    System.out.print(CENTER + "-> : ...");
                    choice = input.nextInt();
                    switch (choice) {
                        case 0:// PLAY
                            System.out.println(CENTER + "Bene!");
                            sep(2);
                            skip = true;
                            break;
                        case 1:// RULES
                            sep(2);
                            System.out.println(CENTER +"Scegli la dimensione della plancia digitando un numero (MIN:5)");
                            System.out.println(CENTER +"Scegli dove posizionare le tue navi digitando le coordinate [RIGA][COLONNA] ed il loro VERSO (es. verso destra");
                            System.out.println(CENTER +"Ti alternerai con il computer nello sparare colpi. Dovrai digitare le coordinate per scegliere dove vuoi sparare.");
                            System.out.println(CENTER +"'X' segnerà che il colpo è andato a segno, 'O' che lo hai mancato.");
                            System.out.println(CENTER +"Vince chi prima distrugge le navi avversarie \n");
                            break;
                        case 2:// EXIT
                            System.out.println(CENTER +"Uscita dal gioco...");
                            skip = true;
                            gameOver = true;
                            break;
                        default:// ERROR
                            System.out.println(CENTER +"Scelta non valida...");
                    }
                } while (!skip);
            }
            // GAME:
            if (!gameOver){
                // SETTINGS:
                int N;
                do {
                    System.out.print(CENTER +"Inserisci la grandezza della plancia (MIN: 5): ");
                    N = input.nextInt();
                }while (N<5);
                // Generate an empty table for each player,
                // to display and to track the player actions,
                String[][] playerTable = generateTable(N);
                String[][] botTable = generateTable(N);
                String[][] gameTable = generateTable(N);
                // and set the game variables
                int numShip = N/5;
                int dimShip = N/5+1;
                int hpPlayer = dimShip*numShip;
                int hpBot = dimShip*numShip;
                System.out.println();
                // Init the variables for the position of the ships
                boolean valid;
                int x;
                int y;
                int orientation;
                // BUILDING
                for (int i = 1; i < numShip+1; i++) {
                // Check if the ship CAN be created
                    valid=false;
                    do {
                        // Displays a table for a better UX
                        printTable(playerTable);
                        //
                        System.out.println(CENTER + "Hai a disposizione "+ numShip + " nave/i di dimensione " + dimShip);
                        System.out.println(CENTER + "Stai posizionando la "+ i + "ª nave");
                        sep(2);
                        System.out.println(CENTER + "Dove vuoi posizionare la tua nave?");
                        System.out.print(CENTER + "Inserisci n° RIGA: ->");
                        x = input.nextInt();
                        System.out.print(CENTER + "Inserisci n° COLONNA: ->");
                        y = input.nextInt();
                        System.out.println(CENTER + "In che verso la vuoi posizionare?");
                        System.out.println( CENTER + "[8] -> verso SU\n" +
                                            CENTER + "[6] -> verso DESTRA\n" +
                                            CENTER + "[2] -> verso GIU\n" +
                                            CENTER + "[4] -> verso SINISTRA\n");
                        System.out.print(CENTER + "Inserisci verso: ->");
                        orientation = input.nextInt();
                        // Control if the ship can be built in the table
                        if (validateInput(orientation)
                                && validateShipBuild(playerTable, x, y, orientation, dimShip)){
                            valid = true;
                        };
                    }while(!valid);
                // Since it passed all controls, edit the tables with the ships
                // V2.0: Draw n ships into the table
                    setShip(playerTable, x, y, orientation, dimShip);
                    System.out.println( CENTER + "[✓] Nave posizionata con successo! [✓]\n");
                    setBotShip(botTable, dimShip);
                }
                //TURNs:
                    System.out.println(TURN_LINE);
                do {
                    // Declaration of the coordinates for the shot
                    System.out.println(CENTER + MID_CENTER+ "<- YOUR TABLE     GAME TABLE ->");
//                    System.out.println(CENTER + "BOT TABLE");
//                    printTable(botTable);
                    showTurn(playerTable, gameTable);
                    System.out.println(CENTER + "Scegli le coordinate da colpire!");
                    System.out.print(CENTER + "Inserisci n° RIGA: ->");
                    x = input.nextInt();
                    System.out.print(CENTER + "Inserisci n° COLONNA: ->");
                    y = input.nextInt();
                    System.out.println();
                    // If the shot is valid, do it
                    if (validateShot(botTable, x, y, false)){
                        // HUMAN TURN
                        hpBot = shoot(botTable,gameTable, x, y, hpBot);
                        //Check if the game is ended
                        if (hpBot == 0) {
                            gameOver = true;
                            printTable(gameTable);
                            System.out.println(CENTER + "°º¤ø,¸¸,ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø");
                            System.out.println(CENTER + "Congratulazioni! HAI VINTO!");
                            System.out.println(CENTER + "°º¤ø,¸¸,ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø");
                        }
                        // BOT TURN
                        // If it's not ended, now it's the turn of the bot
                        if (!gameOver) {
                            hpPlayer = botTurn(playerTable, hpPlayer, true);
                        }
                        //Check if the game is ended
                        if (hpPlayer == 0){
                            gameOver=true;
                            printTable(botTable);
                            System.out.println(CENTER + "Peccato! Il BOT ha vinto.");
                            System.out.println(CENTER + "(ノಠ益ಠ)ノ彡┻━┻ (ノಠ益ಠ)ノ彡┻━┻\n");
                        }

                    }
                    System.out.println(TURN_LINE);
                }while (!gameOver);
            }
            // END
        }while (!gameOver);
        sep(2);
        System.out.println(CENTER + "Grazie per aver giocato!");
        System.out.println(CENTER + "°º¤ø,¸¸,ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø");
    }

//  PRINT and SETTINGS FUNCTIONs ////
    public static String[][] generateTable (int n){
        // Generate and return a matrix NxN
        // Each element is replaced with ~ for more "sea" context.
        String[][] table = new String[n][n];
        for (int x = 0; x < table.length; x++) {
            for (int y = 0; y < table[x].length; y++) {
                table[x][y] = "~";
            }
        }
        return table;
    }

    public static void printTable (String[][] table){
        // Function to print a matrix on more lines
        // Used to make more readable and understandable the table for the game.

        // Vertical indexes
        //+ "    "
        System.out.print("                                                                ");
        for (int i = 0; i < table.length; i++) {
            if (i<9) {
                System.out.print(""+(i)+"  ");
            }
            else {
                System.out.print(""+(i)+" ");
            }
        }
        // Horizontal indexes and rows
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            if (i<10) {
                System.out.println("                                                            "+(i)+"  "+Arrays.toString(table[i]));
            }else {
                System.out.println("                                                            "+(i)+" "+Arrays.toString(table[i]));
            }

        }
        System.out.println();
    }

    public static void showTurn (String[][] table, String[][] gameTable){
        // Function to print 2 matrix on more lines
        // Used to make more readable and understandable the turn for the game.
        final String CENTER="                                               ";
        final String DIV="         ";
        // Vertical indexes for both the tables
        //+ "    "
        System.out.print(CENTER + "    ");
        for (int n = 0; n < 2; n++) {
            for (int i = 0; i < table.length; i++) {
                if (i<9) {
                    System.out.print(""+(i)+"  ");
                }
                else {
                    System.out.print(""+(i)+" ");
                }
            }
            // Little control for layout purpose, basically adds a blank space when is
            // two-digit number.
            if (table.length > 9) {
                System.out.print(DIV + "    ");
            }
            else{
                System.out.print(DIV + "   ");
            }
        }
        // Horizontal indexes and rows for both the tables
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            if (i<10) {
                System.out.print(CENTER + (i) +"  "+Arrays.toString(table[i]));
                System.out.print(DIV);
                System.out.println((i)+"  "+Arrays.toString(gameTable[i]));
            }else {
                System.out.print(CENTER + (i)+" "+Arrays.toString(table[i]));
                System.out.print(DIV);
                System.out.println((i)+" "+Arrays.toString(gameTable[i]));
            }

        }
        System.out.println();
    }

    public static void sep(int n){
        // add N "." \n for layout purpose
        for (int i = 0; i < n; i++) {
            System.out.println(".");
        }
        System.out.println();
    }

    public static void setShip(String[][] table, int x, int y, int orientation, int shipLength){
        // Function to edit elements from the table with the ship's char "@" on its coordinates
        table[x][y] = "@";
        for (int i = 0; i < shipLength - 1; i++) {
            // Build the other pieces of the ship
            switch (orientation) {
                case 8: x -= 1; break; //up
                case 2: x += 1; break; //down
                case 4: y -= 1; break; //right
                case 6: y += 1; break; //left
            }
            table[x][y] = "@";
        }
    }

//  CHECKERs  /////////
    public static boolean validateShipBuild(String[][] table, int x, int y, int orientation, int shipLength){
        // Check if it's possible to create an n-dimensional ship, returns true if there is no overflow
        boolean overflow = false;
        if (validatePoint(table, x, y)){
            for (int i = 0; i < (shipLength - 1) && !overflow; i++) {
                // Check the other pieces of the ship
                switch (orientation) {
                    case 8: x -= 1; break; //up
                    case 2: x += 1; break; //down
                    case 4: y -= 1; break; //right
                    case 6: y += 1; break; //left
                }
                if (!(validatePoint(table, x, y))){
                    overflow = true;
                }
            }
        } else overflow = true;
        if (overflow){
            System.out.println("===== !ERROR! ===== !ERROR! ===== !ERROR! ========== !ERROR! ===== !ERROR! ===== !ERROR! =====");
            System.out.println("La nave non entra nella plancia oppure si scontra con un'altra nave, controlla le coordinate ed il verso");
            System.out.println();
        }
        // Its valid if there is NOT an overflow
        return !overflow;
    }

    public static boolean validateShot(String[][] table, int x, int y, boolean bot){
        // if the target is outofbounds or already targeted, returns false.
        boolean valid = true;
        if(checkOverflow(table.length, x, y)){
            // if already targeted
            if (Objects.equals(table[x][y], "O") || Objects.equals(table[x][y], "X")){
                valid=false;
            }
        }else valid = false;
        // If it's the bot turn, ignore the error message
        if (!valid && !bot){
            System.out.println("===== !ERROR! ===== !ERROR! ===== !ERROR! ========== !ERROR! ===== !ERROR! ===== !ERROR! =====");
            System.out.println("ERRORE: \n La coordinata non è valida oppure hai sparato una casella già colpita!");
            System.out.println();
        }
        return valid;
    }

    public static boolean validatePoint(String[][] table, int x, int y){
        // if is not in overflow                                            and not a Ship!, returns true
        return checkOverflow(table.length, x, y) && !(Objects.equals(table[x][y], "@")); //
    }

    public static boolean checkOverflow(int len, int x, int y){
        // Check if the array goes in overflow at that specific coordinate
        // If it goes not in overflow, returns true.
        return !((x >= len) || (y >= len) || (x < 0) || (y < 0));
    }

    public static boolean validateInput(int orientation){
        // Check if the input given is one of the hotkeys displayed, returns true if the input is valid
        switch (orientation){
            case 8: break;
            case 6: break;
            case 2: break;
            case 4: break;
            default:
                System.out.println("===== !ERROR! ===== !ERROR! ===== !ERROR! ========== !ERROR! ===== !ERROR! ===== !ERROR! =====");
                System.out.println("ERRORE: \n-> Digita uno tra 8-6-2-4 per definire il verso\n");
                return false;
        }
        return true;
    }

//  GAME FUNCTIONs ////
    public static int shoot(String[][] botTable, String[][] gameTable, int x, int y, int hp){
        // Edit the table with the effect on that coordinate and return the hp(that might be edited)
        // Do all the controls on the bot table and copying it on the game table, so the ships remains hidden
        String center = "                                                               ";
        // hit
        if (Objects.equals(botTable[x][y], "@")){
            botTable[x][y] = "X";
            gameTable[x][y] = "X";
            hp--;
            System.out.println(center + "        Colpito!\n"+center+" Ti mancano altre [" + hp + "] caselle!" );
            System.out.println(center + "╰(°▽°)╯ ╰(°▽°)╯ ╰(°▽°)╯\n");

        } else {
            //miss
            botTable[x][y] = "O";
            gameTable[x][y] = "O";
            System.out.println(center + "       Mancato!");
            System.out.println(center + "(╯°□°)╯︵ ┻━┻ (╯°□°)╯︵ ┻━┻\n");
        }
        return hp;
    }

    public static int botShoot(String[][] table, int x, int y, int hp){
        // Same as shoot with little changes
        String center = "                                                               ";
        if (Objects.equals(table[x][y], "@")){
            table[x][y] = "X";
            hp--;
            System.out.println(center+"Il BOT ti ha colpito!\n"+center+" Ti restano altre " + hp + " caselle!" );
            System.out.println(center + "(╯︵╰,) (╯︵╰,) (╯︵╰,) (╯︵╰,)\n");

        } else {
            table[x][y] = "O";
            System.out.println(center + "Il bot ti ha mancato!");
            System.out.println(center + "( ˘⌣˘) ( ˘⌣˘) ( ˘⌣˘)\n");
        }
        return hp;
    }

    public static void setBotShip(String[][] botTable, int shipLength){
        // Builds a ship in the table but all the params are randomly generated
        Random random = new Random();
        boolean valid = false;
        int x;
        int y;
        int orientation;
        do {
            x = random.nextInt(botTable.length); // Returns only 0<x>tab.length
            y = random.nextInt(botTable.length); // same
            orientation = random.nextInt(4) * 2 + 2; // Returns only 2 4 6 8
            if (validateInput(orientation)
                    && validateShipBuild(botTable, x, y, orientation, shipLength)){
                valid = true;
            }
        }while(!valid);
        setShip(botTable, x, y, orientation, shipLength);
    }

    public static int botTurn(String[][] playerTable, int hp, boolean bot){
        // Checks if the bot targets an already targeted unit and repeat while it can target a valid unit.
        // Edits the table and the hp with the effect of the shot .
        boolean valid = false;
        Random random = new Random();
        int x;
        int y;
        do {
            x = random.nextInt(playerTable.length); // 0<>tab.length
            y = random.nextInt(playerTable.length);
            if (validateShot(playerTable, x, y, bot)){
                valid = true;
            }
        } while(!valid);
        // It finds good coordinates for the shot.
        return botShoot(playerTable, x, y, hp);
    }
}
