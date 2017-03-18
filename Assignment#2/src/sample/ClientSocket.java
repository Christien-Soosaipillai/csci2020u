package sample;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by christien on 16/03/17.
 */
public class ClientSocket {

    private Socket cSocket = null;

    public void startClient(){

        try{
            Scanner scanner = new Scanner(System.in);
            cSocket = new Socket("localhost", 8080);
            //scanner

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args){
        ClientSocket cSocket = new ClientSocket();
        cSocket.startClient();
        clientGUI ClientInterface = new clientGUI();
        Stage stage = new Stage();
        try {
            ClientInterface.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
