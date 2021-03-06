package sample;

import java.io.*;
import java.net.*;
import java.util.Vector;

/**
 * Created by christien on 16/03/17.
 */
public class sSocket {
    protected Socket clientConnection       = null;
    protected ServerSocket serverSocket     = null;
    //protected serverThreads[] threads = null
    protected int numClients                = 1;
    protected Vector messages               = new Vector();

    public static int SERVER_PORT = 8080;


    public void startServer() {
        try {


            serverSocket = new ServerSocket(SERVER_PORT);                                                               //Create a new Server Socket
            System.out.println("Waiting for clients to connect....");

            while(true) {

                clientConnection = serverSocket.accept();                                                               //newSocket to accept connection request
                System.out.println("Connection Established! Welcome User " + numClients + "\n" );
                chatThread cThread = new chatThread(clientConnection,numClients);
                Thread userThread = new Thread(cThread);
                userThread.start();
                numClients++;

            }


        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args){

        sSocket server = new sSocket();
        System.out.println("STARTING SERVER");
        server.startServer();


    }


}