import jdk.internal.util.xml.impl.ReaderUTF8;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /**
     * nieaktualne zostawione do testow
     */
    static int port = 4694;
    static int threadNumber = 1;
    static ServerSocket serverSocket = null;
    //static Socket clientSocket = null;
    static ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
    //do wczytania bajtu w metodzie menu, tablica na jeden znak, warto zmienic pozniej
    static char menuChar;
    static boolean stopped = false;
    //do zmiany testowo
    static ServerThread serverThread;
    static Thread clientThread;
    static Scanner scanner = new Scanner(System.in);
    static boolean SocketsConnected = false;
    /* nieaktualne-do testow
    static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));*/


    public static void main(String[] args) {
//        serverStart();
//        menu();
//        stopServerThread();
//        closeStreams();



//----------------------------------------------------------------------------------------------------------------------
        //nieakutalne
        /*try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Błąd we/wy przy tworzeniu serwera (serverSocket)" + e);
            return;
        }

        System.out.println("Serwer uruchomiony pomyslnie");
        System.out.println("q-aby zakonczyc dzialanie \nw-aby wyslac wiadomosc");
        while (true) {
            try {
                //blokuje caly program watek??
                clientSocket = serverSocket.accept();

            } catch (IOException e) {
                System.out.println("Błąd we/wy przy akceptacji połączenia (clientSocket)" + e);
            }
            //nowy watek dla klienta
            clientList.add(new ClientThread(clientSocket, (String) "Wątek nr: " + threadNumber));
            threadNumber++;
        }*/
//----------------------------------------------------------------------------------------------------------------------
    }
//----------------------------------------------------------------------------------------------------------------------
/**
*  INNA WERSJA METOD/FUNKCJI/KLAS POPRZEDNIE ZOSTAWIONE DO TESTOW
**/

//public class server {
//    int port = 4694;
//    int threadNumber = 1;
//    ServerSocket serverSocket = null;
//    public ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
//    //do wczytania bajtu w metodzie menu, tablica na jeden znak, warto zmienic pozniej
//    char menuChar;
//    boolean stopped = false;
//    //do zmiany testowo
//    ServerThread serverThread;
//    Thread clientThread;
//    Scanner scanner = new Scanner(System.in);
//    boolean SocketsConnected = false;
//
//
//    public void start() {
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//            new serverThread();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
//
//public class serverThread implements Runnable {
//    private ServerSocket serverSocket = null;
//
//    serverThread(ServerSocket serverSocket) {
//        this.serverSocket = serverSocket;
//    }
//
//    @Override
//    public void run() {
//
//
//    }
//}



//----------------------------------------------------------------------------------------------------------------------
    public static void serverStart(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Błąd we/wy przy tworzeniu serwera (serverSocket)" + e);
            return;
        }
        //serverThread = new ServerThread(serverSocket); jakis blad, nieaktualne instrukcje narazie zakomentowane
        System.out.println("Serwer uruchomiony pomyslnie");
    }

    public static void menu() {
        //System.out.println("Menu:\nw-wiadomosc\nq-wyjscie\nm-wyswietla ponownie menu");
        ShowMenu();
        while (!stopped){
            try {
                menuChar = (char) System.in.read();
            } catch (IOException none) {
                //e.printStackTrace();
            }

            switch (menuChar) {
                case 'w':
                    sendMessage();
                    break;
                case 'q':
                    //wysjscie z programu
                    stopped = true;
                    break;
                case 'm':
                    ShowMenu();
                    break;
//                default:
//                    //System.out.println(menuChar);
//                   ShowMenu();
            }
        }
    }

    private static void ShowMenu() {
        System.out.println("Menu:\nw-wiadomosc\nq-wyjscie\nm-wyswietla ponownie menu");
    }
    //nieaktualne
//----------------------------------------------------------------------------------------------------------------------
    /*private static void stopServerThread(){
        serverThread.stopped = true;
        try {
            serverThread.thrd.join(); //czeka na zakonczenie watku serwera
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
//----------------------------------------------------------------------------------------------------------------------
    private static void sendMessage() {
        String input;
        System.out.println("Wiadomosc: ");
        serverThread.sendToClients(getInput());
    }
    private static String getInput() {
        scanner.nextLine(); //czysci bufor z smieci
        return scanner.nextLine(); //ta dopiero blokuje

    }
    private static void closeStreams() {
        try {
            if(!serverSocket.isClosed()) {
                serverSocket.close();
                //clientSocket.close();
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//----------------------------------------------------------------------------------------------------------------------
// nieaktualne-do testow
//    public void write(Object obj) {
//        try{
//
//        }
//    }

//    public void sendToAll() {
//        for(ClientThread thread : clientList) {
//
//
//        }
//
//    }
//----------------------------------------------------------------------------------------------------------------------
}
