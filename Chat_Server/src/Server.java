import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;



public class Server {
    private int port;
    private int threadNumber;
    private ServerSocket serverSocket;
    //static Socket clientSocket = null;
    public ArrayList<ClientThread> clientList;
    //do wczytania bajtu w metodzie menu
    private char menuChar;
    private boolean stopped;
    private Scanner scanner;
    boolean SocketsConnected;
    public ServerThread serverThread;
    public SimpleDateFormat simpleDateFormat;
    public String msg = null;


    //default settings
    public Server() {
        port = 4694;
        //simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        threadNumber = 1;
        serverSocket = null;
        //do testow
        //clientList = new ArrayList<ClientThread>();
        stopped = false;
        SocketsConnected = false;
        scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            serverThread = new ServerThread(serverSocket/*, THREADS, MAX_CLIENTS*/);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu() {
            while (!stopped){
            ShowMenu();
                try {
                    menuChar = (char) System.in.read();
                    /***************
                     * czysci bufor*
                     ***************/
                    System.in.read();
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
                    case 'l':
                        serverThread.showClients();
                        break;
                }
            }
        }
    private void ShowMenu() {

        System.out.println("Menu:\nw-wiadomosc\nq-wyjscie\nl-lista podlaczonych klientow\nm-wyswietla ponownie menu");
    }
    private void sendMessage() {
        String input;
        System.out.print("Wiadomość: ");
        serverThread.sendToClients("SERWER: " + getInput());
    }
    private String getInput() {
        /*************************
         * czysci bufor ze smieci*
         *************************/
        scanner.nextLine();
        /*****************
         * wczytuje znak*
         ****************/
        return scanner.nextLine();
    }

    /********************
     * zamyka strumienie*
     ********************/
    private void close() {
            try {
                if(!serverSocket.isClosed()) {
                    serverSocket.close();
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public static void main(String args[]) {
        Server server = new Server();
        server.start();
        server.menu();
        server.close();
    }
}

