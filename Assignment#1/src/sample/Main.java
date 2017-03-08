package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.image.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main extends Application {
    private Stage window;
    private BorderPane layout;
    private TableView<TestFile> table;
    private TextField accuracy, precision;
    private Vector<String> fileNames = new Vector<String>();

    public Vector<String> checkIsFolder(File folder){



        for(File foundFile :  folder.listFiles()){
            if(foundFile.isDirectory()){
                checkIsFolder(foundFile);
            }else{
                fileNames.addElement(folder + "/" + foundFile.getName());
            }

        }

        return fileNames;
    }



    public double calculateSpam(double spam){

        double newSpam;
        newSpam = ((Math.log(1 - spam)) - (Math.log(spam)));
        return newSpam;
    }

    public double finalSpamCalculate(double spam){

        double finalProbability = (1 / (1 + Math.pow(Math.E,spam)));
        return finalProbability;

    }


    @Override
    public void start(Stage primaryStage) throws Exception {




        /*Get the Directory in which the user has placed their data file*/

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("/home"));
        File mainDirectory = directoryChooser.showDialog(primaryStage);


        //HashMaps
        HashMap<String, Integer> starting = new HashMap<>();
        HashMap<String, Integer> trainSpamMap = new HashMap<>();
        HashMap<String, Integer> trainHamMap = new HashMap<>();
        HashMap<String, Integer> trainHam2Map = new HashMap<>();
        HashMap<String, Double> spamProbMap = new HashMap<>();
        HashMap<String, Double> hamProbMap = new HashMap<>();
        HashMap<String, Double> probFileIsSpam = new HashMap<>();

        //Test Variables
        double totalspam;
        double finalFileSpam;
        String fileNameFinal;


        //File names
        String spamTrainFolder = mainDirectory + "/train/spam/";
        String hamTrainFolder = mainDirectory + "/train/ham/";
        String ham2TrainFolder = mainDirectory + "/train/ham2/";
        String TestFolder = mainDirectory + "/test/";
        String hamTestFolder = mainDirectory + "/test/ham/";
        String spamTestFolder = mainDirectory + "/test/spam/";


        /*Training*/
        //Training spam
        DataSource spamTrain = new DataSource(spamTrainFolder, starting);
        trainSpamMap = spamTrain.mainProcess();
        int numOfSpamFiles = spamTrain.getFilesAmount();

        //Training ham
        DataSource hamTrain = new DataSource(hamTrainFolder, starting);
        trainHamMap = hamTrain.mainProcess();
        int numOfHamFiles = hamTrain.getFilesAmount();


        //Training ham2 - gets total ham map
        DataSource ham2Train = new DataSource(ham2TrainFolder, trainHamMap);
        trainHam2Map = ham2Train.mainProcess();
        int numOfHam2Files =  ham2Train.getFilesAmount();

        int totalHamFiles = numOfHamFiles + numOfHam2Files;

        System.out.println("Calculating probabilities");
        /*Get Probabilities*/
        //spam probability
        spamProbMap = spamTrain.calculateProbability(trainSpamMap, numOfSpamFiles);
        hamProbMap = ham2Train.calculateProbability(trainHam2Map, totalHamFiles);





        Set<String> spamProbKeys = spamProbMap.keySet();
        Iterator<String> spamIterator = spamProbKeys.iterator();



        while(spamIterator.hasNext()){

            String keyWord = spamIterator.next();
            double spamProb = spamProbMap.get(keyWord);


            if(hamProbMap.containsKey(keyWord)) {

                double hamProb = hamProbMap.get(keyWord);
                //System.out.println("spam:" + spamProb);
                //System.out.println("ham:" + hamProb);
                double finalProbability = (spamProb / (hamProb + spamProb));
                probFileIsSpam.put(keyWord, finalProbability);


            }else if(!hamProbMap.containsKey(keyWord)){
                System.out.println("ELSE");
                probFileIsSpam.put(keyWord, 1.00);

            }
        }

        //hamTrain.printHashMapDouble(spamProbMap);

        ObservableList<TestFile> fileRecord = FXCollections.observableArrayList();
        String Name ="";
        File getFile;
        File file = new File(TestFolder);
        String OFFICIALNAME;
        fileNames = checkIsFolder(file);                                                                                //get names of all files and returns each name to "fileNames" vector
        for(int i = 0; i < fileNames.size(); i++){
            try{                                                                                                        //try-catch statement for file IO errors

                getFile = new File(fileNames.get(i));
                OFFICIALNAME = getFile.getParent();
                if(getFile.getPath().contains("spam")){
                    Name = "spam";
                }else{
                    Name = "ham";
                }
                FileReader fReader = new FileReader(fileNames.get(i));                      //creates a new filereader that will read through each file in the folder
                //FileReader fReader = new FileReader(OFFICIALNAME);
                fileNameFinal = fileNames.get(i);
                BufferedReader bfReader = new BufferedReader(fReader);                                                  //buffered reader to read character input-stream from filereader
                String line = null;                                                                                     //line of each file
                String space = " ";                                                                                     //represents space in each file
                String[] holder;
                while((line = bfReader.readLine()) != null){                                                            // while buffered reader is not null and contains information to read
                    holder = line.split(space);                                                                         //place all words seperated by a space read from each line in the array
                    totalspam = 0;
                    for (String word : holder){                                                                         //for each word in the array



                        Double wordCount = probFileIsSpam.get(word);                                                    //hold the spam probability for each unique word
                        //System.out.println(calculateSpam(wordCount));
                        if(wordCount == null){
                            wordCount = 1.00;
                        }
                        totalspam = 1.0; //calculateSpam(wordCount);
                        finalFileSpam = finalSpamCalculate(totalspam);
                        System.out.println(wordCount);
                        fileRecord.add(new TestFile(fileNameFinal,finalFileSpam,Name));


                    }
                    //close my file reader stream

                }
                bfReader.close();                                                                                       //close my buffered Reader
                fReader.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }

        //Some Stage setup
        primaryStage.setTitle("Spam Master 3000");
        table.setItems(fileRecord);

        TableColumn filenameCOl = new TableColumn("filename");
        TableColumn actualCLassCOl = new TableColumn("Actual Class");
        TableColumn spamProbabilityCOL = new TableColumn("Spam Probability");

        filenameCOl.setMinWidth(400);
        actualCLassCOl.setMinWidth(150);
        spamProbabilityCOL.setMinWidth(200);

        table.getColumns().addAll(filenameCOl,actualCLassCOl,spamProbabilityCOL);

        filenameCOl.setCellValueFactory(new PropertyValueFactory<TestFile, String>("filename"));
        actualCLassCOl.setCellValueFactory(new PropertyValueFactory<TestFile, String>("ActualClass"));
        spamProbabilityCOL.setCellValueFactory(new PropertyValueFactory<TestFile, Double>("SpamProbRounded"));


        //spamTrain.printHashMapDouble(probFileIsSpam);

        //System.out.println(numOfSpamFiles);
        //System.out.println(totalHamFiles);




        layout.setCenter(table);


        Scene scene = new Scene(layout, 800, 700);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView imageFile(String filename) {
        return new ImageView(new Image("file:"+filename));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
