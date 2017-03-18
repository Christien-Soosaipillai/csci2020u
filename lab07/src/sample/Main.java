package sample;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;

public class Main extends Application {


    private Canvas canvas;
    private static Color[] pieColours = {
            Color.AQUA, Color.GOLD, Color.DARKORANGE,
            Color.DARKSALMON, Color.LAWNGREEN, Color.PLUM
    };

    @Override
    public void start(Stage primaryStage) throws Exception{


        //Variables
        int countFlashFlood = 0;
        int countSevereThunderstorm = 0;
        int countSpecialMarine = 0;
        int countTornado = 0;
        Group root = new Group();
        Scene scene = new Scene(root, 800,600);
        canvas = new Canvas();
        canvas.setHeight(600);
        canvas.setWidth(800);


        File inFile = new File("weatherwarnings-2015.csv");
        Scanner scanner = new Scanner(inFile);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String word[] = line.split(",");

            for(int i =0; i < word.length; i++){

                if(word[i].equalsIgnoreCase("FLASH FLOOD")){
                    countFlashFlood += 1;
                }else if(word[i].equalsIgnoreCase("SPECIAL MARINE")){
                    countSpecialMarine += 1;
                }else if(word[i].equalsIgnoreCase("SEVERE THUNDERSTORM")){
                    countSevereThunderstorm += 1;
                }else if(word[i].equalsIgnoreCase("TORNADO")){
                    countTornado += 1;
                }


            }




        }
        int totalCounts = countFlashFlood + countSevereThunderstorm + countSpecialMarine + countTornado;
        System.out.println(totalCounts);
        int counts[] = {countFlashFlood,countSpecialMarine, countSevereThunderstorm, countTornado};




        root.getChildren().add(canvas);
        primaryStage.setTitle("Lab07");
        primaryStage.setScene(scene);
        primaryStage.show();

        draw(counts,totalCounts);


    }

    public void draw(int array[], int total){

        GraphicsContext rc = canvas.getGraphicsContext2D();
        System.out.println("width: " + canvas.getWidth());
        System.out.println("height: " + canvas.getHeight());

        double angle = 0;
        String names[] = {"Flash Flood", "Special Marine", "Sever Thunderstorm" , "Tornado"};


        for(int i = 0; i < array.length; i++){
            System.out.println(array[i]);
            double arcExtentPercent = (double)array[i]/total
                    ;
            double arcExtentAngle = arcExtentPercent*360;
            System.out.println(arcExtentPercent);
            //System.out.println(arcExtentAngle);


            rc.setFill(Color.BLACK);
            rc.strokeArc(420,200, 300,300,angle, arcExtentAngle, ArcType.ROUND );
            rc.setFill(pieColours[i]);
            rc.fillArc(420,200, 300,300,angle, arcExtentAngle, ArcType.ROUND );
            angle += arcExtentAngle;



        }

        //Setting up the legend for the pie chart

        String pieSectionName;
        Font font = new Font("Arial", 24);
        rc.setFont(font);

        for(int i  = 0; i < 4; i++){
            rc.setFill(Color.BLACK);
            rc.strokeRect(80,200+(i*50),30,30);
            rc.setFill(pieColours[i]);
            rc.fillRect(80,200+(i*50),30,30);
            pieSectionName = "= " + names[i];
            rc.setFill(Color.BLACK);
            rc.fillText(pieSectionName,120,220+(i*50));



        }



    }


    public static void main(String[] args) {
        launch(args);
    }
}
