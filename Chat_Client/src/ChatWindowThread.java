import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;


public class ChatWindowThread implements Runnable {

    private TextArea chatWindow ;
    private BufferedReader inputStream;
    Thread thrd;
    private boolean stopped = false; /*false*/

    ChatWindowThread(TextArea chatWindow, BufferedReader inputStream) {
        thrd = new Thread(this);
        this.chatWindow = chatWindow;
        this.inputStream = inputStream;
        thrd.start(); // uruchamia watek odrazu po utowrzeniu
    }

    @Override
    public void run() {
        //stopped false
        while (!stopped) {
            try {
                showMessage(inputStream.readLine());

            } catch (IOException none) {
                threadStop();
                //none.printStackTrace();
            }
        }

    }
    private void showMessage(String message) {
                chatWindow.appendText(message);
                chatWindow.appendText("\n");
    }
    synchronized void threadStop() {
        stopped = true;
        closeThreadStream();
    }
    private void closeThreadStream() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
