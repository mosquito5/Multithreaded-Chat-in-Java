import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientThread implements Runnable {
    Thread thrd;
    private Socket clientSocket = null;
    private PrintWriter outStream = null;
    private BufferedReader inputStream = null;
    public boolean stopped = false;
    private String message = null;
    private Server server = new Server();
    private int threadNumber;
    private ClientThread[] TEMPORARY_THREAD = new ClientThread[ServerThread.MAX_CLIENTS];
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private int i;
    public String ipAdr;



    ClientThread(Socket clientSocket, String name, int threadNumber, String ipAdr) {
        this.threadNumber = threadNumber;
        this.ipAdr = ipAdr;
        thrd = new Thread(this, (String) name + threadNumber); //wątek otrzymuje nazwę w momencie utworzenia
        this.clientSocket = clientSocket;
        thrd.start(); //startuje watek odrazu po utworzeniu
    }

    ClientThread(Socket clientSocket, String name) {
        thrd = new Thread(this, name); //wątek otrzymuje nazwę w momencie utworzenia
        this.clientSocket = clientSocket;
        thrd.start();
    }


    @Override
    public void run() {
        try {
            setStreams();
            sendMessage("SERWER: Witaj");
            whileChatting();
            closeStreams();
        } catch (IOException e) {
            System.out.println("Błąd we/wy SetStreams na: " + thrd.getName());
            System.out.println("Kod błędu" + e);
        }
    }


    private void setStreams() throws IOException {
        outStream = new PrintWriter(clientSocket.getOutputStream(), true); //autoFlush czysci automatycznie bufor
        inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private void closeStreams() throws IOException {
            outStream.close();
            inputStream.close();
            updateTHREADarray();
    }

    /*******************************************
     * sciaga wiadomosc z serwera, i wysyla ja *
     *******************************************/
    private void whileChatting() throws IOException {
        while (!stopped)
            receiveMessage();
    }

    /***********************************
     * do obslugi wiadomosci wysylanych*
     * przez serwer                    *
     ***********************************/
    public synchronized void sendMessage(String message) {
        outStream.println(time() + " " + message);
        outStream.flush();
    }

    /***********************************
     * do obslugi wiadomosci           *
     * przychodzacych glwony watek??   *
     ***********************************/
    private void  receiveMessage() throws IOException {
        String msg = inputStream.readLine();
        System.out.println(msg);

        if(msg.equals("QFD%^&$")){
            stopped = true;
            return;
        }

        if(!isNickSet(msg) && !msg.equals("QFD%^&$"))
            sendMessage("SERWER: Aby móc pisać ustaw nick i zatwierdź klawiszem enter");
        else {
            i = 0;
            do {
                ServerThread.THREAD[i].sendMessage(msg);
                i++;
            } while (!(ServerThread.THREAD[i] == null));
        }
    }

    /**********************************
     *sprawdza czy klient ustawil nick*
     **********************************/
    private boolean isNickSet(String msg) {
        if(msg.substring(0,4).equals("null"))
            return false;
        else
            return true;
    }

    /*****************************
     * zatrzymuje wszystkie watki*
     *****************************/
    synchronized void threadStop() {
        stopped = true;
        try {
            closeStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String time() {
        return simpleDateFormat.format(new Date());
    }

    /**************************************************
     * instrukcja na poczatku czysci wpis o kliencie  *
     * z ktorego zostala wywolana, cofa licznik       *
     * threadNumber o jeden, a nastepnie uklada       *
     * wpisy jeden po drugim aby nie bylo miedzy nimi *
     * przerwy. Jest wywolywana tylko gdy watek do    *
     * obslugi klienta konczy swoje dzialanie         *
     *************************************************/

    synchronized void updateTHREADarray() {
        ServerThread.THREAD[threadNumber] = null;
        ServerThread.threadNumber--;

        for (i = 0; i < ServerThread.MAX_CLIENTS; i++) {
            if(!(ServerThread.THREAD[i] == null))
                TEMPORARY_THREAD[i] = ServerThread.THREAD[i];
                ServerThread.THREAD[i] = TEMPORARY_THREAD[i];
        }
    }

}
