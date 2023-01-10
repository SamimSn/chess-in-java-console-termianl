import java.util.Scanner;

public class App {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean check = false;
        boolean checkMate = false;
        boolean staleMate = false;

        String[][] chessBoardTest = new String[19][19];

        System.out.println("\nthe (*) pieces are the black ones.");
        System.out.println("type the beginning and the final coordinates to play.");
        System.out.println("Form Example : e2");
        System.out.println("press (ENTER) to start.");
        sc.nextLine();

        String[] numbers = { " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
        String[] chars = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        String[][] whiteChessPieces = { { " R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R " }, { " P " } };
        String[][] blackChessPieces = { { "R* ", "N* ", "B* ", "Q* ", "K* ", "B* ", "N* ", "R* " }, { "P* " } };
        String[][] chessBoard = chessBoard(whiteChessPieces, blackChessPieces, numbers, chars);
        array2Dprinter(chessBoard);

        String beginning = "";
        String finale = "";
        int turn = 1;
        
        do{
            if(turn%2==1){ // white's turn
                System.out.println("\n|---White---|");
                if(check==true){
                    System.out.println("--> check!");
                } 
            }else{ // Black's turn
                System.out.println("\n|---Black---|");
                if(check==true){
                    System.out.println("--> check!");
                }
            }
            System.out.println("From?");
            beginning = sc.nextLine();
            System.out.println("To?");
            finale = sc.nextLine();
            if(validation(beginning, chars, numbers)==true && validation(finale, chars, numbers)==true){ // right coordinates
                if(rightPieace(beginning, chessBoard, turn)==true && rightPieace(finale, chessBoard, turn)==false){ // right piece
                    if(pieceFundamentalCheck(beginning, finale,chessBoard)==true){ // piece fundamental
                        for(int i=0; i < chessBoard.length; i++){
                            for(int j=0; j < chessBoard[i].length; j++){ 
                                chessBoardTest[i][j] = chessBoard[i][j]; // copying the original board into a test version
                            }
                        }
                        chessBoardTest = movingPieces(beginning, finale, chessBoardTest);
                        if(pinnedMove(chessBoardTest, turn)==false){ // pinned move
                            chessBoard = movingPieces(beginning, finale, chessBoard); // updating the  original board
                            if(pieaceFinder(finale, chessBoard).equals(whiteChessPieces[1][0]) || pieaceFinder(finale, chessBoard).equals(blackChessPieces[1][0])){ // pawn promotion
                                chessBoard = pawnPromotion(finale, chessBoard);
                            }
                            if(check(chessBoard, turn)==true){ // you're checked
                                if(isThereValidMove(chessBoard, turn)==true){ // continue
                                    check = true;
                                }else{
                                    checkMate = true; // checkmate
                                    array2Dprinter(chessBoard);
                                    break;
                                }
                            }
                            if(check(chessBoard, turn)==false && isThereValidMove(chessBoard, turn)==false){
                                staleMate = true;
                                array2Dprinter(chessBoard);
                                break;
                            }
                            array2Dprinter(chessBoard); // print 
                            turn++;
                        }else{
                            System.out.println("--> whether it's a (pin) move\nor\n--> You're (checked)\nokay? press (ENTER)");
                            sc.nextLine();
                            continue;
                        }
                    }else{ // error
                        System.out.println("Wrong move1!\nokay? press (ENTER)");
                        sc.nextLine();
                        continue;
                    }
                }else{ // error
                    System.out.println("Wrong move!\nokay? press (ENTER)");
                    sc.nextLine();
                    continue;
                }
            }else{ // error
                System.out.println("not in range\nrepeat your turn\nokay? press (ENTER)");
                sc.nextLine();
                continue;
            } 
        }while(true); // until not checkmated or stalemated

        if(checkMate==true){
            if(turn%2==1){
                System.out.println(" ---White--- is winner");
            }else{
                System.out.println(" ---Black--- is winner");
            }
        }
        if(staleMate==true){
            System.out.println("|-------DRAW-------|");
        }
            
    }
    // board
    public static String[][] chessBoard(String[][] white, String[][] black, String[] num, String[] row) {
        String[][] array2D = new String[19][19];
        int a = 1;
        int b = 3;
        int c = 3;
        int d = 3;
        for (int i = 0; i < array2D.length; i++) {
            for (int j = 0; j < array2D[i].length; j++) {
                if (i % 2 != 0 && j % 2 == 0 && j != 2) {
                    array2D[i][j] = "|";
                } else if (j == 2) {
                    array2D[i][j] = "||";
                } else if (i % 2 == 0 && j % 2 != 0 && i != 16) {
                    array2D[i][j] = "---";
                } else if (i == 16) {
                    array2D[i][j] = "==";
                } else if (i % 2 == 0 && j % 2 == 0) {
                    array2D[i][j] = "+";
                } else if (j == 1 && i % 2 != 0 && i != 17) {
                    array2D[i][j] = num[i - a];
                    a++;
                } else if (i == 17 && j % 2 != 0 && j != 1) {
                    array2D[i][j] = row[j - b];
                    b++;
                } else if (i == 1 && j != 1 && j % 2 != 0) {
                    array2D[i][j] = black[0][j - c];
                    c++;
                } else if (i == 3 && j != 1 && j % 2 != 0) {
                    array2D[i][j] = black[1][0];
                } else if (i == 15 && j != 1 && j % 2 != 0) {
                    array2D[i][j] = white[0][j - d];
                    d++;
                } else if (i == 13 && j != 1 && j % 2 != 0) {
                    array2D[i][j] = white[1][0];
                } else {
                    array2D[i][j] = "   ";
                }
            }
        }
        array2D[17][1] = "///";
        return array2D;
    }
    // print
    public static void array2Dprinter(String[][] array2D) {
        for (int i = 0; i < array2D.length; i++) {
            for (int j = 0; j < array2D[i].length; j++) {
                System.out.print(array2D[i][j]);
            }
            System.out.println();
        }
    }
    // validated coordinates
    public static boolean validation(String point, String[] row, String[] num) {
        int check = 0;
        for (int i = 0; i < row.length; i++) {
            if (Character.toString(point.charAt(0)).equalsIgnoreCase(row[i].trim())) { // A-H checker
                check++;
            }
        }
        for (int i = 0; i < num.length; i++) {
            if (Character.toString(point.charAt(1)).equalsIgnoreCase(num[i].trim())) { // 1-8 checker
                check++;
            }
        }
        if((point.trim()).length() != 2){ // form: e1 (length=2)
            check--;
        }
        if (check == 2) {
            return true;
        } else {
            return false;
        }
    }
    // piece finder
    public static String pieaceFinder(String point, String[][] board){ 
        int vertical = 0;
        int horizontal = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(Character.toString(point.charAt(0)).equalsIgnoreCase(board[i][j].trim())){ // finding A-H
                    vertical = j; 
                }
                if(Character.toString(point.charAt(1)).equalsIgnoreCase(board[i][j].trim())){ // finding 1-8
                    horizontal = i; 
                }
            }
        }
        return board[horizontal][vertical]; // what is on the coordinate? ((****(H,V)****))
    }
    // Horizontal
    public static int coordinaterH(String point, String[][] board){
        int horizontal = 0;
        for(int i = 1; i < board.length; i+=2){
            for(int j = 1; j < board[i].length; j+=2){
                if(Character.toString(point.charAt(1)).equalsIgnoreCase(board[i][j].trim())){ // finding 1-8
                    horizontal = i; 
                }
            }
        }
        return horizontal; // horizontal coordinate of the point on board
    }
    // Vertical
    public static int coordinaterV(String point, String[][] board){
        int vertical = 0;
        for(int i = 1; i < board.length; i+=2){
            for(int j = 1; j < board[i].length; j+=2){
                if(Character.toString(point.charAt(0)).equalsIgnoreCase(board[i][j].trim())){ // finding A-H
                    vertical = j; 
                }
            }
        }
        return vertical; // vertical coordinate of the point on board
    }
    // updating the board
    public static String[][] movingPieces(String beginning, String finale, String[][] board){
        
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        
        if(board[i2][j2].equalsIgnoreCase("   ")){ // moving
            String constant = board[i1][j1];
            board[i1][j1] = board[i2][j2];
            board[i2][j2] = constant;
        }else{ // taking 
            board[i2][j2] = board[i1][j1];
            board[i1][j1] = "   ";
        }
        return board;
    }
    //right piece
    public static boolean rightPieace(String point, String[][] board, int turn){
        int check = 0;
        if(turn%2==1){ // white's turn
            if(pieaceFinder(point, board).trim().length()==1){ // W form : P ==> length=1
                check++;
            }
        }else{ // black's turn
            if(pieaceFinder(point, board).trim().length()==2){ // B form: P* ==> length=2
                check++;
            }
        }
        if(check==1){
            return true;
        }else{
            return false;
        }      
    }
    // *******piece fundamentals*******
    // rook
    public static boolean rook(String beginning, String finale, String[][] board){  // up-down
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if(j1==j2 || i1==i2){
            return true;
        }else {
            return false;
        }
    }
    // bishop
    public static boolean bishop(String beginning, String finale, String[][] board){  // diognal
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if(Math.abs(j2-j1)==Math.abs(i2-i1)){
            return true;
        }else {
            return false;
        }
    }
    // queen
    public static boolean queen(String beginning, String finale, String[][] board){  // up-down or diognal
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if(Math.abs(j2-j1)==Math.abs(i2-i1) || (j1==j2 || i1==i2)){
            return true;
        }else {
            return false;
        }
    }
    // knight
    public static boolean knight(String beginning, String finale, String[][] board){  // fork(L)
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if((Math.abs(i2-i1)==2 && Math.abs(j2-j1)==4) || (Math.abs(i2-i1)==4 && Math.abs(j2-j1)==2)){  
            return true;
        }else {
            return false;
        }
    }
    // king
    public static boolean king(String beginning, String finale, String[][] board){  // small queen
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if((Math.abs(i2-i1)==2 && j1==j2) || (Math.abs(j2-j1)==2 && i1==i2) || (Math.abs(i2-i1)==2 && Math.abs(j2-j1)==2)){  
            return true;
        }else {
            return false;
        }
    }
    // white pawn
    public static boolean whitePawn(String beginning, String finale, String[][] board){  // up
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if(i1==13 && ((i2-i1==-2)||(i2-i1==-4)) && j1==j2 &&
        ((!board[i2][j2].equals("P* "))&&
        (!board[i2][j2].equals("R* "))&&
        (!board[i2][j2].equals("B* "))&&
        (!board[i2][j2].equals("N* "))&&
        (!board[i2][j2].equals("Q* "))&&
        (!board[i2][j2].equals("K* ")))
        ){  
            return true;
        }else if(i2-i1==-2 && j1==j2 &&
        ((!board[i2][j2].equals("P* "))&&
        (!board[i2][j2].equals("R* "))&&
        (!board[i2][j2].equals("B* "))&&
        (!board[i2][j2].equals("N* "))&&
        (!board[i2][j2].equals("Q* "))&&
        (!board[i2][j2].equals("K* ")))
        ) {
            return true;
        }else if(i2-i1==-2 && Math.abs(j2-j1)==2 && 
        ((board[i2][j2].equals("P* "))||
        (board[i2][j2].equals("R* "))||
        (board[i2][j2].equals("B* "))||
        (board[i2][j2].equals("N* "))||
        (board[i2][j2].equals("Q* ")))
            ){
            return true;
        }else{
            return false;
        }
    }
    // black pawn
    public static boolean blackPawn(String beginning, String finale, String[][] board){  // down
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if(i1==3 && ((i2-i1==2)||(i2-i1==4)) && j1==j2 &&
        ((!board[i2][j2].equals(" P "))&&
        (!board[i2][j2].equals(" R "))&&
        (!board[i2][j2].equals(" B "))&&
        (!board[i2][j2].equals(" N "))&&
        (!board[i2][j2].equals(" Q "))&&
        (!board[i2][j2].equals(" K ")))
        ){  
            return true;
        }else if(i2-i1==2 && j1==j2 &&
        ((!board[i2][j2].equals(" P "))&&
        (!board[i2][j2].equals(" R "))&&
        (!board[i2][j2].equals(" B "))&&
        (!board[i2][j2].equals(" N "))&&
        (!board[i2][j2].equals(" Q "))&&
        (!board[i2][j2].equals(" K ")))
        ) {
            return true;
        }else if(
            i2-i1==2 && Math.abs(j2-j1)==2 && 
            ((board[i2][j2].equals(" P "))||
            (board[i2][j2].equals(" R "))||
            (board[i2][j2].equals(" B "))||
            (board[i2][j2].equals(" N "))||
            (board[i2][j2].equals(" Q ")))
            ){
            return true;
        }
        else{
            return false;
        }
    }
    // betwen
    public static boolean between(String beginning, String finale, String[][] board){ // between the beginning and final checker
        
        int check = 0;
        
        int i1 = coordinaterH(beginning, board);
        int j1 = coordinaterV(beginning, board);
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        
        
        if(j1==j2){ // up-down
            if(i1>i2){
                i1 = coordinaterH(finale, board);
                i2 = coordinaterH(beginning, board);
            }
            for(int i=i1+2; i<=i2-2; i+=2){
                String square = board[i][j1];
                if(!square.equalsIgnoreCase("   ")){
                    check++;
                    break;
                }
            }
        }else if(i1==i2){ // left-right
            if(j1>j2){
                j1 = coordinaterV(finale, board);
                j2 = coordinaterV(beginning, board);
            }
            for(int j=j1+2; j<=j2-2; j+=2){
                String square = board[i1][j];
                if(!square.equalsIgnoreCase("   ")){
                    check++;
                    break;
                }
            }
        }else if(j2-j1==i1-i2){  // y=x
            if(j1>j2){
                i2 = coordinaterH(beginning, board);
                j2 = coordinaterV(beginning, board);
                i1 = coordinaterH(finale, board);
                j1 = coordinaterV(finale, board);
            }
            for(int i=i1-2, j=j1+2; i>=i2+2 && j<=j2-2; i-=2, j+=2){
                String square = board[i][j];
                if(!square.equalsIgnoreCase("   ")){
                    check++;
                    break;
                }
            }

        }else if(j2-j1==i2-i1){ // y=-x
            if(j1>j2){
                i2 = coordinaterH(beginning, board);
                j2 = coordinaterV(beginning, board);
                i1 = coordinaterH(finale, board);
                j1 = coordinaterV(finale, board);
            }
            for(int i=i1+2, j=j1+2; i<=i2-2 && j<=j2-2; i+=2, j+=2){
                String square = board[i][j];
                if(!square.equalsIgnoreCase("   ")){
                    check++;
                    break;
                }
            }
        }
        if(check==0){
            return true;
        }else{
            return false;
        }
    }
    // all piece fundamentals checker
    public static boolean pieceFundamentalCheck(String beginning, String finale, String[][] board){
        String Start = pieaceFinder(beginning,board);
        switch(Start){
            case " K ": // king
            case "K* ":
            if(king(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }

            case " Q ": // queen
            case "Q* ":
            if(queen(beginning, finale, board)==true && between(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }

            case " B ": // bishop
            case "B* ":
            if(bishop(beginning, finale, board)==true && between(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }

            case " N ": // knight
            case "N* ":
            if(knight(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }

            case " R ": // rook
            case "R* ":
            if(rook(beginning, finale, board)==true && between(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }

            case " P ": // white pawn
            if(whitePawn(beginning, finale, board)==true && between(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }

            case "P* ": // black pawn
            if(blackPawn(beginning, finale, board)==true && between(beginning, finale, board)==true){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    // pawn promotion
    public static String[][] pawnPromotion(String finale, String[][] board){ // only for pawn
        int i2 = coordinaterH(finale, board);
        int j2 = coordinaterV(finale, board);
        if(i2==1){ // white pawn promotion
            System.out.println("choose:\nQ\nR\nB\nN");
            String answer = sc.nextLine().toUpperCase().trim();
            switch(answer){
                case "Q":
                board[i2][j2] = " Q ";
                break;

                case "R":
                board[i2][j2] = " R ";
                break;

                case "B":
                board[i2][j2] = " B ";
                break;

                case "N":
                board[i2][j2] = " N ";
                break;
            }
        }
        if(i2==17){ // black pawn promotion
            System.out.println("choose:\nQ*\nR*\nB*\nN*");
            String answer = sc.nextLine().toUpperCase().trim();
            switch(answer){
                case "Q*":
                board[i2][j2] = "Q* ";
                break;

                case "R*":
                board[i2][j2] = "R* ";
                break;

                case "B*":
                board[i2][j2] = "B* ";
                break;

                case "N*":
                board[i2][j2] = "N* ";
                break;
            }
        }
        return board;
    }
    // king chess cooardinater
    public static String kingFinder(String[][] board, int turn){
        String num = "";
        String row = "";
        for(int i=0; i< board.length; i++){
            for(int j=0; j<board[i].length;j++){
                if(turn%2==1){
                    if(board[i][j].equals(" K ")){ // white king coordinates
                        row = (board[17][j]).trim();
                        num = (board[i][1]).trim();
                    }
                }else{
                    if(board[i][j].equals("K* ")){ // black king coordinates
                        row = (board[17][j]).trim();
                        num = (board[i][1]).trim();
                        
                    }
                }
            }
        }
        return row+num;
    }
    // pinnedMove
    public static boolean pinnedMove(String[][] board, int turn){
        String num = "";
        String row = "";
        String king = kingFinder(board, turn); // our king
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[i].length; j++){
                if(turn%2==1){ // white's turn
                    if(board[i][j].equals("R* ")||board[i][j].equals("N* ")||board[i][j].equals("B* ")||board[i][j].equals("Q* ")||board[i][j].equals("P* ")){
                        row = (board[17][j]).trim();
                        num = (board[i][1]).trim();
                        if(pieceFundamentalCheck((row+num), king, board)==true){
                            return true;
                        }
                    }
                }else{ // black's turn
                    if(board[i][j].equals(" R ")||board[i][j].equals(" N ")||board[i][j].equals(" B ")||board[i][j].equals(" Q ")||board[i][j].equals(" P ")){
                        row = (board[17][j]).trim();
                        num = (board[i][1]).trim();
                        if(pieceFundamentalCheck((row+num), king, board)==true){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    // check
    public static boolean check(String[][] board, int turn){ 
        String num = "";
        String row = "";
        String kingEnemy = kingFinder(board, (turn+1)); // enemy king
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[i].length; j++){
                if(turn%2==1){ // white's turn
                    if(board[i][j].equals(" R ")||board[i][j].equals(" N ")||board[i][j].equals(" B ")||board[i][j].equals(" Q ")||board[i][j].equals(" P ")){
                        row = (board[17][j]).trim();
                        num = (board[i][1]).trim();
                        if(pieceFundamentalCheck((row+num), kingEnemy, board)==true){
                            return true;
                        }
                    }
                }else{ // black's turn
                    if(board[i][j].equals("R* ")||board[i][j].equals("N* ")||board[i][j].equals("B* ")||board[i][j].equals("Q* ")||board[i][j].equals("P* ")){
                        row = (board[17][j]).trim();
                        num = (board[i][1]).trim();
                        if(pieceFundamentalCheck((row+num), kingEnemy, board)==true){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public static boolean isThereValidMove(String[][] board, int turn){
        int a = 0;
        String num1 = "";
        String row1 = "";
        String num2 = "";
        String row2 = "";
        for (int i = 1; i < board.length - 2; i+=2) {
            for (int j = 3; j < board[i].length; j+=2) {
                if(((turn+1))%2==1){ // white's turn
                    if( // is there any valid move for white?
                        board[i][j].equals(" R ")||
                        board[i][j].equals(" N ")||
                        board[i][j].equals(" B ")||
                        board[i][j].equals(" Q ")||
                        board[i][j].equals(" P ")||
                        board[i][j].equals(" K ")
                        ){
                        row1 = (board[17][j]).trim();
                        num1 = (board[i][1]).trim();
                        for (int z = 1; z < board.length - 2; z+=2) {
                            for (int k = 3; k < board[z].length; k+=2) {
                                row2 = (board[17][k]).trim();
                                num2 = (board[z][1]).trim();

                                if(rightPieace((row1+num1), board, ((turn+1)))==true && rightPieace((row2+num2), board, ((turn+1)))==false){ // right piece
                                    if(pieceFundamentalCheck((row1+num1), (row2+num2), board)==true){ // fundamentals
                                        String[][] chessBoardTest = new String[19][19];
                                        chessBoardTest = movingPieces((row1+num1), (row2+num2), copyBoard(board)); // copying to new test board
                                        if(pinnedMove(chessBoardTest, ((turn+1)))==false){ // pinned move
                                            a++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                }else{ // black's turn
                    if( // is there any valid move for black?
                        board[i][j].equals("R* ")||
                        board[i][j].equals("N* ")||
                        board[i][j].equals("B* ")||
                        board[i][j].equals("Q* ")||
                        board[i][j].equals("P* ")||
                        board[i][j].equals("K* ")
                        ){
                        row1 = (board[17][j]).trim();
                        num1 = (board[i][1]).trim();
                        for (int z = 1; z < board.length; z+=2) {
                            for (int k = 3; k < board[z].length; k+=2) {
                                row2 = (board[17][k]).trim();
                                num2 = (board[z][1]).trim();
                        
                                if(rightPieace((row1+num1), board, ((turn+1)))==true && rightPieace((row2+num2), board, ((turn+1)))==false){ // right piece
                                    if(pieceFundamentalCheck((row1+num1), (row2+num2), board)==true){ // fundamentals
                                        String[][] chessBoardTest = new String[19][19];
                                        chessBoardTest = movingPieces((row1+num1), (row2+num2), copyBoard(board)); // copying to new test board
                                        if(pinnedMove(chessBoardTest, ((turn+1)))==false){ // pinned move
                                            a++;
                                            break;
                                        }
                                    }
                                }
                                
                            }
                        }
                    }
                }
            }
        }

        if(a!=0){
            return true;
        }else{
            return false;
        }
    }
    public static String[][] copyBoard(String[][] board){
        String[][] chessBoardTest = new String[19][19];
        for(int i=0; i < board.length; i++){
            for(int j=0; j < board[i].length; j++){ 
                chessBoardTest[i][j] = board[i][j]; // copying the original board into a test version
            }
        }
        return chessBoardTest;
    }
}