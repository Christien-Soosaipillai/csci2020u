package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;


import java.io.*;
import java.net.Socket;

import java.util.Vector;

public class clientGUI extends Application {


    //Socket
    private static Socket cSocket;

    //Gui components
    private Group group= new Group();
    private Scene scene = new Scene(group, 800,600);
    private BorderPane layout = new BorderPane();
    private GridPane grid;
    private ListView<String> tableC;
    private ListView<String> tableS;

    private String choosenFile;

    private static PrintStream Pstream;

    public File mainDirectory;


    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.println("Attempting to connect to server....");
        try{

            cSocket = new Socket("localhost", 8080);


        }catch (IOException e){
            e.printStackTrace();
        }



        Group group= new Group();
        Scene scene = new Scene(group, 800,600);

        //BorderPane
        BorderPane layout = new BorderPane();

        //Tables for both server and client side
        tableC = new ListView<>();
        tableS = new ListView<>();

        //Client Folder
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("/home"));
        mainDirectory = directoryChooser.showDialog(primaryStage);


        //Splitpane for client side
        SplitPane clientPane = new SplitPane();
        clientPane.setPrefWidth(400.0);
        clientPane.setPrefHeight(380.0);

        //Splitpane for server side
        SplitPane serverPane= new SplitPane();
        serverPane.setPrefWidth(400.0);
        serverPane.setPrefHeight(380.0);

        //add all file information to the table view for client side
        Vector<String> clientVec = getClientDirectory(mainDirectory);

        for(int i = 0; i < clientVec.size(); i++){
            //System.out.println(clientVec.get(i));
            tableC.getItems().add(clientVec.get(i));
        }

        clientPane.getItems().addAll(tableC);


        //add all file information to the table view for server side

        Vector<String> serverVec = getServerDirectory();

        for(int i = 0; i < serverVec.size(); i++){
            tableS.getItems().add(serverVec.get(i));
            //System.out.println("Made it past here");

        }


        serverPane.getItems().addAll(tableS);




        grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        //Buttons
        Button download = new Button("Download");
        Button upload = new Button("Upload");

        //On action for download
        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                PrintWriter prStream = null;
                try {
                    System.out.println("downloading....");
                    cSocket = new Socket("localhost", 8080);                                                 //re-open the socket connection since a new command is being entered
                    choosenFile = tableS.getSelectionModel().getSelectedItem();                                         //get the file to be downloaded by selecting it from the GUI
                    prStream = new PrintWriter(cSocket.getOutputStream(),true);                               //printwriter to server
                    prStream.println("DOWNLOAD");                                                                       //write out the download command to the server
                    prStream.println(choosenFile);                                                                      //get requested file
                    downloadFile(choosenFile);                                                                          //begin download process
                    System.out.println("finished");

                }catch (IOException e){
                    e.printStackTrace();
                }

                try{

                    primaryStage.close();
                    cSocket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }


            }
        });

        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                PrintWriter prStream = null;
                String fileName;
                try {

                    choosenFile = tableC.getSelectionModel().getSelectedItem();
                    fileName = mainDirectory + "/" + choosenFile;
                    System.out.println("Uploading...");
                    cSocket = new Socket("localhost", 8080);
                    prStream = new PrintWriter(cSocket.getOutputStream(),true);
                    prStream.println("UPLOAD");
                    uploadFile(fileName);

                }catch(IOException e){
                    e.printStackTrace();
                }

            }
        });




        grid.add(download,0,0);
        grid.add(upload,1,0);

        layout.setLeft(clientPane);
        layout.setRight(serverPane);
        layout.setTop(grid);
        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(layout, 800,800));
        primaryStage.show();
    }

    public void downloadFile(String fileName){

        try {



            BufferedReader bReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));                           //Create a buffered reader to get data being sent out foromt the server
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(mainDirectory + "/" + fileName,true));      //create a file writer to write to the new file all the data
            String line;

            while((line = bReader.readLine()) != null){

                bWriter.write(line + "\n");                                                                                     //write to file


            }
            System.out.println("write complete");                                                                                   //write complete once loop is finished
            bReader.close();
            bWriter.close();

        }catch(IOException e) {
            e.printStackTrace();
        }


    }

    public void uploadFile(String fileName){

        try{

            BufferedReader bReader = new BufferedReader(new FileReader(fileName));
            PrintWriter pWrtier = new PrintWriter(cSocket.getOutputStream(),true);
            String line;

            while( (line = bReader.readLine()) != null){

                pWrtier.println(line);

            }
            bReader.close();
            pWrtier.close();



        }catch(IOException e){
            e.printStackTrace();
        }


    }





    //gets all the file names from the server folder to be printed to the gui
    public Vector<String> getServerDirectory(){

        PrintWriter pStream = null;
        Vector<String> fileNames = new Vector<>();                                                                      //Vector to hold the names of all the files
        try{
            pStream = new PrintWriter(cSocket.getOutputStream(),true);                                        //set the printwriter to get the socket to write out
            pStream.println("DIRECTORY");                                                                               //write the Directory command out
        }catch(IOException e){
            System.out.println("Can't send message to server");
        }




        try {
            //System.out.println("on to buffer reader...");
            BufferedReader bReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));               //Buffered reader to read in the information sent from the server

            String line;

            while((line = bReader.readLine()) != null){                                                                 //while information is being recieved y the buffered reader
                //System.out.println(line);
                //System.out.println("Hello");
                fileNames.addElement(line);                                                                             //Add the names of each file to the vector
            }
            bReader.close();
            cSocket.close();


        }catch(IOException e){
            System.out.println("Can't get socket input stream");
        }


        return fileNames;                                                                                               //return the vector

    }



    //gets all the file names from the client folder
    public Vector<String> getClientDirectory(File folder) {

        Vector<String> fileNames = new Vector<>();

        for (File foundFile : folder.listFiles()) {
            if (foundFile.isDirectory()) {                                                                              //if the file found is directory recursively call the function again
                getClientDirectory(foundFile);
            } else {
                fileNames.addElement(foundFile.getName());                                                              //else store the filename
            }

        }
        return fileNames;                                                                                               //return the vector
    }






    public static void main(String[] args) {
        clientGUI client = new clientGUI();
        client.launch(args);
    }
}
