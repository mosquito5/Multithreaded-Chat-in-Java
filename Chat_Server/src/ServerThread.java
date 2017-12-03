import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerThread extends Server implements Runnable {
    /**************************
     * odwoalnie do tego watku*
     **************************/
    private Thread thrd;
    /*********************
     * socket dla klienta*
     *********************/
    private Socket clientSocket;
    /*****************
     * socket serwera*
     *****************/
    private ServerSocket serverSocket;

    private int i;

    public ClientThread clientThread;

    /*****************
     * licznik watkow*
     *****************/
    public static int threadNumber;

    public static final int MAX_CLIENTS = 10;
    public static ClientThread[] THREAD = new ClientThread[MAX_CLIENTS];

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public boolean stopped = false;

     ServerThread(ServerSocket serverSocket) {
        thrd = new Thread(this);

        this.serverSocket = serverSocket;
        thrd.start();
    }



    @Override
    public void run() {
        while (!stopped){
            try {
                clientSocket = serverSocket.accept();
                /***************************************
                 * zwraca adres ip podlaczonego klienta*
                 ***************************************/
                System.out.println("nowe polaczenie adres ip: " + clientSocket.getInetAddress().getHostAddress());
                for(threadNumber = 0; threadNumber < MAX_CLIENTS; threadNumber++){
                    if(THREAD[threadNumber] == null){
                        THREAD[threadNumber] = new ClientThread(clientSocket, "Klient nr: ", threadNumber,
                                clientSocket.getInetAddress().getHostAddress() );
                        break;
                    }
                }
                if(threadNumber == MAX_CLIENTS){
                    clientThread = new ClientThread(clientSocket, (String) "odrzucony" );
                    clientThread.sendMessage("Wszystkie miejsca zajete");
                    clientThread.stopped = true;
                }
            }
            catch (IOException e) {
                //e.printStackTrace();
                /***********************************
                 *jesli glowny watek konczy zadanie*
                 * to tutaj jest lapany wyjatek    *
                 * ktory powoduje wyjscie z petli  *
                 * i konczy dzialanie watku        *
                 ***********************************/
                stopped = true;
            }
        }
        /**************************************
         *zatrzymuje wszystkie watki klientow *
         * i czeka na ich zakonczenie         *
         **************************************/
        stopClientThreads();
    }
    private void stopClientThreads()  {
        i = 0;
        while (!(THREAD[i] == null)){
            THREAD[i].threadStop();
            i++;
        }
    }

    public void sendToClients(String message) {
         i = 0;
        do{
            THREAD[i].sendMessage(message);
            i++;
        } while (!(THREAD[i] == null));
    }
    synchronized void showClients(){
         i = 0;
         while (!(THREAD[i] == null)) {
             System.out.println(THREAD[i].thrd.getName());
             System.out.println("Adres ip: " + THREAD[i].ipAdr + "\n");
             i++;
         }
    }
}


