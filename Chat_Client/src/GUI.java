import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


public class GUI extends Application {
    //zmienne i obiekty
    private Button sendButton;

    private Button connectButton;

    private TextField msg;

    private TextField nickName;

    private TextArea chatWindow;

    private Label labNick;
    private String nick;

    private Label hostAddrLabel;
    private TextField hostAddrField;

    private Label portLabel;
    private TextField portField;
    //private TextFormatter <Integer> portField; //konwertuje textfiled by zwracal integer

    /******************************************
     *status jakies bledy zostawione do testow*
     * zmiana koncepcji obslugi bledow        *
     ******************************************/
    //private Label info;

    private PrintWriter outStream;
    private BufferedReader inputStream;

    private String host = "127.0.0.1";
    private int port = 4694;

    private Socket connection;

    private ChatWindowThread chatWindowThread;  //nieuzywane

    private boolean isConnected = false;




    @Override
    public void init() {
        //inicjalizacja zmiennych i obiektow
        System.out.println("init");

        sendButton = new Button("Wyślij");
        connectButton = new Button("Połącz");

        msg = new TextField();
        msg.setPromptText("Wpisz wiadomość...");

        nickName = new TextField();
        nickName.setPromptText("Nick");

        chatWindow = new TextArea();

        labNick = new Label();

        /******************************************
         *status jakies bledy zostawione do testow*
         * zmiana koncepcji obslugi bledow        *
         ******************************************/
        /*info = new Label();
        info.setText("Status: OK");*/

        hostAddrLabel = new Label("Adres hosta: ");
        hostAddrField = new TextField();
        hostAddrField.setText(null);
        hostAddrField.setPromptText(host);

        portLabel = new Label("Port: ");
        portField = new TextField();
        portField.setText(null);
        portField.setPromptText(String.valueOf(port));


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("start");

        primaryStage.setTitle("Multithreaded Chat Client 0.2");
        primaryStage.setResizable(false);

        FlowPane flowPane = new FlowPane(10, 10);
        Scene scene = new Scene(flowPane, 640,480);

        msg.setPrefWidth(570);

        nickName.setPrefWidth(130);
        //nickName.

        hostAddrField.setPrefWidth(80);


        portField.setPrefWidth(50);

        chatWindow.setPrefSize(640,400);
        chatWindow.setEditable(false);

        labNick.setText("Brak");


        primaryStage.setScene(scene);


        connectButton.setOnAction((ae) -> {
            connectToServer();
            //watek do obslugi okna i przychodzacych wiadomosci, oraz jego referencja
            if(isConnected)
                chatWindowThread = new ChatWindowThread(chatWindow,inputStream);
        });

        //pole tekstowe do wyslania wiadomosci jest puste
        msg.setOnAction((ae) -> {
            /************************************
             *instrukcja ktora wysyla wiadomosc *
             ************************************/
            sendMessage(msg.getText());
            /***********************************************
             *czysci pole testkowe po wcisnieciu przycisku *
             ***********************************************/
            msg.setText("");
        });

        //dzialanie po wcisnieciu przycisku wyslij
        sendButton.setOnAction((ae) -> {
            /************************************
             *instrukcja ktora wysyla wiadomosc *
             ************************************/
            sendMessage(msg.getText());


            /***********************************************
             *czysci pole testkowe po wcisnieciu przycisku *
             ***********************************************/
            msg.setText("");
        });


        nickName.setOnAction((ae) -> {
            nick = nickName.getText();
            labNick.setText("Nick: " + nick);
            });

        flowPane.getChildren().addAll(chatWindow,msg, sendButton, nickName, labNick, hostAddrLabel, hostAddrField,
                portLabel, portField, connectButton/*, info*/);

        primaryStage.show();

    }

    @Override
    public void stop() throws IOException {
        System.out.println("stop");

        /*do testów
        System.out.println(portField.getText());
        System.out.println(hostAddrField.getText());
        */
//        if(chatWindowThread.thrd.isAlive() != true) //powoduje blad gdy nie ma polaczenia
//            chatWindowThread.threadStop();

        if(isConnected)
            closeStreams();

    }

    public void connectToServer() /*throws IOException*/ {

        try {
            connection = new Socket();

            if((hostAddrField.getText() == null) && (portField.getText() == null)) {
                connection.connect(new InetSocketAddress("127.0.0.1", 4694), 500);
            }
            else if(hostAddrField.getText() == null) {
                connection.connect(new InetSocketAddress("127.0.0.1", Integer.parseInt(portField.getText())),
                        500);
            }
            else if(portField.getText() == null) {
                connection.connect(new InetSocketAddress(hostAddrField.getText(), 4694), 500);
            }
            else {
                connection.connect(new InetSocketAddress(hostAddrField.getText(), Integer.parseInt(portField.getText())),
                        500);
            }

            //ustawia streamy
            setStreams();
            //zmiana flagi polaczenia
            isConnected = true;

        }
        catch (UnknownHostException none){
            showError("***Problem z połączeniem z serwerem UnknownHostException***");
            isConnected = false;
            //info.setText("Problem z połączeniem z hostem");
        }
        catch (IOException none) {
            showError("***Problem z nawiązaniem połączenia z serwerem IOException***");
            isConnected = false;
            //info.setText("Błąd we/wy");
        }

    }

    private void sendMessage(String message) {
        outStream.println(nick + ": " + message);
        outStream.flush();
    }

    private void setStreams() throws IOException {
        outStream = new PrintWriter(connection.getOutputStream()); //true?
        inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void closeStreams() throws IOException {
        outStream.println("QFD%^&$");
        outStream.flush();
        outStream.close();
        inputStream.close();
    }

    void showError(String err) {
        chatWindow.appendText(err);
        chatWindow.appendText("\n");

    }
    //do testów
   /* private void showMessage(String message) {
        chatWindow.appendText(message);
    }*/
}