import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;



public class Server {
    private int port;
    //private int threadNumber;
    private ServerSocket serverSocket;
    //do wczytania bajtu w metodzie menu
    private char menuChar;
    private boolean stopped;
    private Scanner scanner;
    private String input;
    boolean SocketsConnected;
    boolean isValid;
    public ServerThread serverThread;
    //private SimpleDateFormat simpleDateFormat;
    public String msg = null;


    public Server() {
        port = 4694;
        isValid = true;
        //simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        //threadNumber = 1;
        serverSocket = null;

        stopped = false;
        SocketsConnected = false;
        scanner = new Scanner(System.in);
    }

    private void setUp() {
        //System.out.println(simpleDateFormat.format(new Date()));
        System.out.println("Chcesz skonfigurowac serwer? [t/n/a-atomatycznie]");

        input = scanner.nextLine();

        if(input.equals("n")){
                System.out.println("Serwer zostanie uruchoamiony z domyślnymi wartosciami \nport: " + port);
        }
        else if (input.equals("t")) {
            do {
                System.out.print("Podaj port: ");
                port = getPort();
            }while (!isValid);

            //System.out.println("Serwer zostanie uruchoamiony z wartosciami \nport: " + port);
        }
        else if(input.equals("a"))
        /************************************************
         * podczas tworzenia scoketu serwera,           *
         * zostanie port zostanie alokowany atumatycznie*
         ************************************************/
            port = 0;
        else {
            System.out.println("[t/n/a-atomatycznie]");
        }
        System.out.println();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            serverThread = new ServerThread(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu() {
            while (!stopped){
            ShowMenu();
                try {
                    //do zmiany
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
        System.out.print("Wiadomość: ");
        serverThread.sendToClients("SERWER: " + getInput());
    }

    private String getInput() {
        /*************************
         * czysci bufor ze smieci*
         *************************/
        //scanner.nextLine();
        /*****************
         * wczytuje znak*
         ****************/
        return scanner.nextLine();
    }

    private int getPort() {
        int value = -1;
        try{
            value = scanner.nextInt();
            if(!isValid)
                isValid = true;
        } catch (InputMismatchException none) {
            System.out.println("Port musi byc liczba");
            isValid = false;
        }
        if(value < 1023 || value > 65535) {
            System.out.println("Zbyt wysoki/niski port");
            isValid = false;
        }
        return value;
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
        server.setUp();
        server.start();
        System.out.println("Serwer uruchomiony pomyslnie\nPort: " + server.serverSocket.getLocalPort()
                + "\nAdres: " + server.serverSocket.getInetAddress() + "\n");
        server.menu();
        server.close();
    }
}

