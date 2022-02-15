package javaclient;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

public class DicClient {
    public final static String SERVER = "127.0.0.1";
    public final static int PORT = 5000;
    public final static int TIMEOUT = 30000;

    public static void main(String[] args) {
        char[][] board = new char[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }

        String p1 = "J1";
        String p2 = "J2";

        boolean player1 = true;

        boolean gameEnded = false;
        Scanner in = new Scanner(System.in);
        while(!gameEnded) {
            drawBoard(board);

            if(player1) {
                System.out.println(p1 + "'s Turn (x):");
            } else {
                System.out.println(p2 + "'s Turn (o):");
            }

            char c = '-';
            if(player1) {
                c = 'x';
            } else {
                c = 'o';
            }

            int row = 0;
            int col = 0;

            while(true) {
                System.out.print("Entrer un numéro de ligne (0, 1, or 2): ");
                row = in.nextInt();
                System.out.print("Entrer un numéro de colonne (0, 1, or 2): ");
                col = in.nextInt();

                if(row < 0 || col < 0 || row > 2 || col > 2) {
                    System.out.println("La case choisie est hors limite.");
                } else if(board[row][col] != '-') {
                    System.out.println("Quelqu'un à déjà joué sur cette case.");
                } else {
                    break;
                }

            }

            board[row][col] = c;

            if(playerHasWon(board) == 'x') {
                System.out.println(p1 + " à gagné !");
                gameEnded = true;
            } else if(playerHasWon(board) == 'o') {
                System.out.println(p2 + " à gagné !");
                gameEnded = true;
            } else {
                if(boardIsFull(board)) {
                    System.out.println("C'est une égalité");
                    gameEnded = true;
                } else {
                    player1 = !player1;
                }

            }

        }
        drawBoard(board);

        /*String[] items = new String[2];
        items[0] =  player;
        items[1] =  content;
            if(items.length>1) {
                try {
                    request(addr, "POST", items[0], items[1]);
                    request(addr, "GET", "", "");
                }
                catch(IOException e) {
                    System.err.println(e.getMessage());
                }
            }*/
    }

    public static void drawBoard(char[][] board) {
        System.out.println("Board:");
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public static char playerHasWon(char[][] board) {

        for(int i = 0; i < 3; i++) {
            if(board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '-') {
                return board[i][0];
            }
        }

        for(int j = 0; j < 3; j++) {
            if(board[0][j] == board[1][j] && board[1][j] == board[2][j] && board[0][j] != '-') {
                return board[0][j];
            }
        }

        if(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-') {
            return board[0][0];
        }
        if(board[2][0] == board[1][1] && board[1][1] ==  board[0][2] && board[2][0] != '-') {
            return board[2][0];
        }

        return ' ';
    }

    //Make a function to check if all of the positions on the board have been filled
    public static boolean boardIsFull(char[][] board) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void request(SocketAddress addr, String verb, String url, String content) throws IOException
    {
        Socket socket = new Socket();

        socket.connect(addr, TIMEOUT);

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

        pw.printf("%s /%s HTTP/1.1\r\n", verb, url);
        pw.printf("Content-Type: text/plain\r\n");
        pw.printf("Content-Length: %d\r\n\r\n", content.length());
        pw.printf("%s\r\n", content);
        pw.flush();

        String tmp;

        while((tmp=br.readLine())!=null){
            System.out.println(tmp);
        }
        pw.close();
        br.close();
        socket.close();
    }
}
   /*

    public static void main(String[] args) throws UnknownHostException {
        SocketAddress addr = new InetSocketAddress(InetAddress.getByName(SERVER), PORT);
        Scanner sc =new Scanner(System.in);
        String player = "J1";
        String signe = "o";
        String caseJ1 = "";
        String caseJ2 = "";
        System.out.println("Le joueur 1 dispose des O et le joueur 2 dispose des X");
        String[] combinaisons = {"0","1","2","3","4","5","6","7","8"};

        String[][] win ={{"0","1","2"},{"3","4","5"},{"3","4","5"},{"6","7","8"},{"0","3","6"},{"1","4","7"},{"2","5","8"},{"0","4","8"},{"2","4","6"}};

        for (int i = 0; i <= 8; i++) {

            if(i > 0){
                player = (i % 2) == 0 ? "J1" : "J2";
                signe= (i % 2) == 0 ? "o" : "x";
            }

            System.out.println("Au joueur "+i+"-"+player+" de jouer, les combinaisons disponibles sont les suivantes :");

            for (int j = 0; j < combinaisons.length; j++) {
                if(j == 2 || j == 5 || j==8){
                    System.out.println(combinaisons[j]+"]");
                }else if(j == 0 || j == 3 || j==6){
                    System.out.print("["+combinaisons[j]+",");
                }else{
                    System.out.print(combinaisons[j]+",");
                }
            }

            System.out.println("Appuyer sur Q pour quitter");
            String content = sc.next();

            while(combinaisons[Integer.parseInt(content)].equals("o") || combinaisons[Integer.parseInt(content)].equals("x")){
                System.out.println("Choisir une case avec un chiffre");
                for (int j = 0; j < combinaisons.length; j++) {
                    if(j == 2 || j == 5 || j==8){
                        System.out.println(combinaisons[j]+"]");
                    }else if(j == 0 || j == 3 || j==6){
                        System.out.print("["+combinaisons[j]+",");
                    }else{
                        System.out.print(combinaisons[j]+",");
                    }
                }
                content = sc.next();
            }
            System.out.println("content="+content);

            combinaisons[Integer.parseInt(content)] = signe;

            if(content.equalsIgnoreCase("q")) {
                break;
            }

            String[] exploded2 = caseJ2.split(",");
            String[] exploded1 = caseJ1.split(",");

            if(player.equals("J1")){
                caseJ1 += content+",";
                content = caseJ1;
                exploded1 = caseJ1.split(",");
            }else{
                caseJ2 += content+",";
                content = caseJ2;
                exploded2 = caseJ2.split(",");
            }
            System.out.println(Arrays.toString(exploded1));
            System.out.println(Arrays.toString(exploded2));
            //if(i >= 5){

                for(int k = 0; k <= 7; k++){
                    System.out.println(Arrays.toString(win[k]));
                    if(player.equals("J2")){
                        System.out.println("on test si j2 à gg");
                        if(Arrays.equals(win[k], exploded1)){
                            System.out.println("T'as gagné champion "+player);
                        }else{
                            System.out.println("j2 à perdu");
                        }
                    }else{
                        System.out.println("on test si j1 à gg");
                        if(Arrays.equals(win[k], exploded2)){
                            System.out.println("T'as gagné champion "+player);
                        }else{
                            System.out.println("j1 à perdu");
                        }
                    }
                }
            //}




        }
        sc.close();
    }*/

    /**/
//}
