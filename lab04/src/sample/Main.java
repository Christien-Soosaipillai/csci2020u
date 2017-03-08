package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //BorderPane
        BorderPane Layout = new BorderPane();


        //Grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label Username = new Label("Username");
        grid.add(Username, 0, 1);
        TextField tUsername = new TextField();
        grid.add(tUsername,1,1);

        Label Password = new Label("Password");
        grid.add(Password, 0, 2);
        TextField tPassword = new TextField();
        grid.add(tPassword,1,2);

        Label FullName = new Label("Full Name");
        grid.add(FullName, 0, 3);
        TextField tFullName = new TextField();
        grid.add(tFullName,1,3);

        Label Email = new Label("E-mail");
        grid.add(Email, 0, 4);
        TextField tEmail = new TextField();
        grid.add(tEmail,1,4);

        Label Phone = new Label("Phone #");
        grid.add(Phone, 0, 5);
        TextField tPhone = new TextField();
        grid.add(tPhone,1,5);

        Label DateofBirth = new Label("Date of Birth");
        grid.add(DateofBirth,0,6);
        DatePicker date =  new DatePicker();
        grid.add(date, 1,6);

        Button Register = new Button("Register");
        Register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String full = tFullName.getText();
                String user = tUsername.getText();
                String mail = tEmail.getText();
                String pNumber =  tPhone.getText();

                System.out.println("Full name is: " + full);
                System.out.println("Username is: " + user);
                System.out.println("Email address is: "+ mail);
                System.out.println("Phone NUmber is: "+ pNumber);
            }
        });


        grid.add(Register,1,7);

        Layout.setTop(grid);
        primaryStage.setTitle("Hello");
        primaryStage.setScene(new Scene(Layout,680,480));
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}