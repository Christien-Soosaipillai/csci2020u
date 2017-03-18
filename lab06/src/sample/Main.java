package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    @FXML
    private Canvas canvas;

    static double[] avgHousingPricesByYear = {
            247381.0,264171.4,287715.3,294736.1,
            308431.4,322635.9,340253.0,363153.7
    };
    static double[] avgCommercialPricesByYear = {
            1121585.3,1219479.5,1246354.2,1295364.8,
            1335932.6,1472362.0,1583521.9,1613246.3
    };

    private static String[] ageGroups = {
            "18-25", "26-35", "36-45", "46-55", "56-65", "65+"
    };
    private static int[] purchasesByAgeGroup = {
            648, 1021, 2453, 3173, 1868, 2247
    };
    private static Color[] pieColours = {
            Color.AQUA, Color.GOLD, Color.DARKORANGE,
            Color.DARKSALMON, Color.LAWNGREEN, Color.PLUM
    };

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);

        canvas = new Canvas();

        // broken?
        //canvas.widthProperty().bind(primaryStage.widthProperty());
        //canvas.heightProperty().bind(primaryStage.heightProperty());
        canvas.setHeight(800);
        canvas.setWidth(1000);

        root.getChildren().add(canvas);
        primaryStage.setTitle("UI Demo - 2D Graphics");
        primaryStage.setScene(scene);
        primaryStage.show();


        drawGraphs(root);
        //draw(root);


    }

    //Cant have the same GraphicsContext  per function
    private void drawGraphs(Group group) {

        GraphicsContext rc = canvas.getGraphicsContext2D();
        System.out.println("width: " + canvas.getWidth());
        System.out.println("height: " + canvas.getHeight());
        rc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Draw Axis
        rc.setStroke(Color.BLACK);                                                                                      //(x-start,y-start,x-end,y-end)
        rc.strokeLine(40, 580, 360, 580);                                                              //X-AXIS
        rc.strokeLine(40, 580, 40, 80);                                                                //Y-AXIS



        Font font = new Font("Arial", 11);
        rc.setFont(font);

        for(int a = 0; a < 8; a++){

            rc.strokeLine(40 +(40*a),570, 40 +(a*40), 590);                                             //X-AXIS TICS
            rc.strokeLine(30,580-(71.5*a), 50, 580-(71.5*a));                                           //Y-AXIS TICS
            //rc.fillText((Double.toString(40.0*a)), 40 +(40*a), 590);                                          //Numerical X-axis plot
            //rc.fillText((Double.toString(a*60)), 0, 580-(70*a));                                              //Numerical Y-axis plot


        }


        //Drawing out the Bar graph
        double spaceing = 100.0;

        for(int i = 0; i < avgHousingPricesByYear.length; i++){

            //Set fill colour
            rc.setFill(Color.RED);
            //Set stroke colour
            rc.setStroke(Color.RED);
            //set actual rectangle loccation and size(x-coordinate, y-coordinate, width, height)
            rc.fillRect(40+(40*i), 580-(avgHousingPricesByYear[i]/3000.0),15,avgHousingPricesByYear[i]/3000.0 );
            //rc.strokeRect(250, 175, 25, 75);


            rc.setFill(Color.BLUE);
            rc.setStroke(Color.BLUE);
            rc.fillRect(55+(40*i), 580-avgCommercialPricesByYear[i]/3500.0, 15,avgCommercialPricesByYear[i]/3500.0);    //(x-topleft,y-topleft, width, height)

        }

        //Drawing out the pie chart

        double currentAngle = 0.0;
        double totalpurchases = 11410;

        for(int i =0; i < 6; i++){

            double arcExtentPercent = purchasesByAgeGroup[i]/totalpurchases;
            double arcExtentAngle = arcExtentPercent*360;


            rc.setFill(pieColours[i]);
            rc.fillArc(420,200, 300,300,currentAngle, arcExtentAngle, ArcType.ROUND );
            currentAngle += arcExtentAngle;

        }

        //Setting up the legend for the bar graph
        rc.setFill(Color.BEIGE);
        rc.fillRect(160,30,220,80);
        rc.setFill(Color.RED);
        rc.fillRect(170,50,20,20);
        rc.setFill(Color.BLUE);
        rc.fillRect(170,80,20,20);
        rc.setFill(Color.BLACK);
        rc.fillText("= avgHousingPricesByYear",200,60);
        rc.fillText("= avgCommercialPricesByYear",200,90);

        //Setting up the legend for the pie chart
        rc.setFill(Color.BEIGE);
        rc.fillRect(500,30,250,150);
        String pieSectionName;

        for(int i  = 0; i < 6; i++){
            rc.setFill(pieColours[i]);
            rc.fillRect(510,40+(i*23),15,15);
            pieSectionName = "=" + ageGroups[i];
            rc.setFill(Color.BLACK);
            rc.fillText(pieSectionName,530,50+(i*23));



        }







    }


    private void draw(Group group) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        System.out.println("width: " + canvas.getWidth());
        System.out.println("height: " + canvas.getHeight());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // line
        gc.setStroke(Color.RED);
        gc.strokeLine(50, 50, 150, 250);

        // rectangles
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.fillRect(250, 50, 100, 75);
        gc.strokeRect(250, 175, 100, 75);

        // rounded rectangles
        gc.setFill(Color.BEIGE);
        gc.setStroke(Color.BEIGE);
        gc.fillRoundRect(450, 50, 100, 75, 10, 10);
        gc.strokeRoundRect(450, 175, 100, 75, 20, 20);

        // ovals (ellipses)
        gc.setFill(Color.CORAL);
        gc.setStroke(Color.CORAL);
        gc.strokeOval(650, 50, 100, 75);
        gc.fillOval(650, 175, 100, 75);

        // arcs
        gc.setFill(Color.DARKCYAN);
        gc.setStroke(Color.DARKCYAN);
        /*
        ArcType.ROUND - pie shaped
        ArcType.CHORD - endpoint connected to startpoint with line
         */
        gc.strokeArc(50, 350, 100, 75, 115.0, 45.0, ArcType.ROUND);
        gc.fillArc(50, 500, 100, 75, 45.0, 115.0, ArcType.ROUND);

        // polygons (one filled semi-transparent)
        gc.setFill(Color.color(0.8, 0.0, 0.3, 0.5));
        gc.setStroke(Color.HOTPINK);
        gc.strokePolygon(new double[] {250, 310, 300, 250}, new double[] {350, 360, 380, 400}, 4);
        gc.fillPolygon(new double[] {250, 310, 300, 250}, new double[] {500, 510, 530, 550}, 4);

        // text (with adjusted font)
        Font font = new Font("Arial", 24);
        gc.setFont(font);
        gc.setFill(Color.OLIVE);
        gc.setStroke(Color.OLIVE);
        gc.strokeText("CSCI2020u", 450, 400);
        gc.fillText("CSCI2020u", 450, 550);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
