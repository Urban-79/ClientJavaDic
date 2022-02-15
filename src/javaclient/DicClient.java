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
            String[] exploded2;
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
            for(int z=0; exploded1.length; z++){
                System.out.println(exploded1[z]);
            }
            if(i >= 5){
                System.out.println("on est dans le if");
                String winJ1 = "n";
                String winJ2 = "n";
                for(int k = 0; k <= 7; k++){
                    if(player.equals("J2")){
                        if(Arrays.equals(win[k], exploded1)){
                            System.out.println("T'as gagné champion "+player);
                            winJ1 = "o";
                        }
                    }else{
                        if(Arrays.equals(win[k], exploded2)){
                            System.out.println("T'as gagné champion "+player);
                            winJ2 = "o";
                        }
                    }
                }
            }



            String[] items = new String[2];
            items[0] =  player;
            items[1] =  content;
            /*if(items.length>1) {
                try {
                    request(addr, "POST", items[0], items[1]);
                    request(addr, "GET", "", "");
                }
                catch(IOException e) {
                    System.err.println(e.getMessage());
                }
            }*/
        }
        sc.close();
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
