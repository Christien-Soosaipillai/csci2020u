package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class clientGUI extends Application {

    private Socket cSocket = null;
    private Group group= new Group();
    private Scene scene = new Scene(group, 800,600);
    private BorderPane layout = new BorderPane();


    @Override
    public void start(Stage primaryStage) throws Exception{

        Group group= new Group();
        Scene scene = new Scene(group, 800,600);
        BorderPane layout = new BorderPane();

        SplitPane clientFolder = new SplitPane();
        SplitPane serverFolder = new SplitPane();


        layout.setLeft(clientFolder);
        layout.setRight(serverFolder);
        primaryStage.setTitle("File Sharer v1.0");
        primaryStage.setScene(new Scene(layout, 800,800));
        primaryStage.show();
    }

    public void startClient(){

        try{

            cSocket = new Socket("localhost", 8080);


        }catch (IOException e){
            e.printStackTrace();
        }


    }



    public static void main(String[] args) {
        ClientSocket cSocket = new ClientSocket();
        cSocket.startClient();
        launch(args);
    }
}
