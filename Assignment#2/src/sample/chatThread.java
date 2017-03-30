package sample;

import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by christien on 18/03/17.
 */
public class chatThread implements Runnable {

    private Socket userSocket = null;
    private int numClients;




    public chatThread(Socket socket, int numClients){

        this.userSocket = socket;
        this.numClients = numClients;

    }


    public void run(){

        try {

            File serverDirectory = new File("/home/christien/Desktop/serverFolder/");

            System.out.println("Thread started");



            BufferedReader bReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
            String line;
            //line = bReader.readLine();


            System.out.println("entering loop");
            line = bReader.readLine();
            System.out.println("in loop");

            if(line.equals("UPLOAD")){
                handleUpload(bReader.readLine());



            }else if(line.equals("DOWNLOAD")){
                    handleDownload(bReader.readLine());


            }else if(line.equals("DIRECTORY")){
                //System.out.println("you made it here");
                displayDirectory(serverDirectory);
                System.out.println("finished sending files");

            }

            System.out.println("out of loop");
            bReader.close();
            System.out.println("Client " + numClients + " disconnected" );
            userSocket.close();





        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void handleUpload(String file){

        String line;

        try {

            System.out.println("getting file....");
            BufferedReader bReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
            while((line = bReader.readLine()) != null ){

                bWriter.write(line + "\n");


            }

            bReader.close();
            bWriter.close();



        }catch(IOException e){
            e.printStackTrace();

        }



    }

    public void handleDownload(String file){

        int count;

        try {
            File sendFile = new File(file);                                                                             //create a file instance of the existing file
            PrintWriter pWriter = new PrintWriter(userSocket.getOutputStream());
            String line;
            Scanner scanner = new Scanner(sendFile);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                pWriter.println(line);
            }
            pWriter.close();
            }   catch(IOException e){
            e.printStackTrace();
        }
    }



            //binStream = new BufferedInputStream(new FileInputStream(sendFile));                                       //allows the buffered input stream to handle the data retrieved from the File input stream











    public void displayDirectory(File folder){

        System.out.println("getting server files");

        try {
            PrintWriter pStream = new PrintWriter(userSocket.getOutputStream(), true);


            for (File foundFile : folder.listFiles()) {
                if (foundFile.isDirectory()) {
                    displayDirectory(foundFile);
                } else {
                    //System.out.println("sending folders");
                    pStream.println(foundFile.getName());
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }


}
