package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javafx.collections.*;
import javafx.stage.FileChooser;
import java.io.File;


import java.util.HashMap;

public class DataSource {

    private HashMap<String, Integer> map = new HashMap<String, Integer>();
    public File file;
    private Vector<String> fileNames = new Vector<String>();
    private int numSpamFiles = 0;


    //Constructor
    public DataSource(String folderName, HashMap<String, Integer> map){
        this.file = new File(folderName);
        this.map = map;
    }



    public int getFilesAmount(){

        return numSpamFiles;

    }

    private boolean isWord(String token) {
        String pattern = "^[a-zA-Z]*$";
        if (token.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    //Gets Hashmap for each folder
    public HashMap<String, Integer> mainProcess(){


        fileNames = checkIsFolder(file);                                                                                //get names of all files and returns each name to "fileNames" vector
        for(int i = 0; i < fileNames.size(); i++){
            numSpamFiles += 1;                                                                                          //keep track off the number of files
            try{                                                                                                        //try-catch statement for file IO errors
                //System.out.println(file + "/" + fileNames.get(i));
                FileReader fReader = new FileReader(file + "/" + fileNames.get(i));                            //creates a new filereader that will read through each file in the folder
                BufferedReader bfReader = new BufferedReader(fReader);                                                  //buffered reader to read character input-stream from filereader
                String line = null;                                                                                     //line of each file
                String space = " ";                                                                                     //represents space in each file
                String[] holder;
                while((line = bfReader.readLine()) != null){                                                            // while buffered reader is not null and contains information to read
                    holder = line.split(space);                                                                         //place all words seperated by a space read from each line in the array

                    for (String word : holder){                                                                         //for each word in the array

                        Integer wordCount = map.get(word);                                                              //hold an integer counter for each unique word
                        if(isWord(word)) {
                            if (wordCount == null) {                                                                          //if their is no counter for thhat word; means its a new word
                                map.put(word, 1);                                                                           //therefore add a new counter for the new wword and place it in the hashmap
                            } else {                                                                                          //else
                                map.put(word, wordCount + 1);                                                                 //just increment the counter by 1 for each unique word that is found again
                            }
                        }


                    }
                                                                                                                        //close my file reader stream

                }
                bfReader.close();                                                                                       //close my buffered Reader
                fReader.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
        return map;
    }


    /*
    * Searches through the given folder
    * gets each filename
    * returns a vector of strings which contain the name of each file
    * */
    public Vector<String> checkIsFolder(File folder){



        for(File foundFile :  folder.listFiles()){
            if(foundFile.isDirectory()){
                checkIsFolder(foundFile);
            }else{
                fileNames.addElement(foundFile.getName());
            }

        }

        return fileNames;
    }



    /*
    * Calculates that the probability of a file
    *
    *
    *
    * */
    public HashMap<String, Double> calculateProbability(HashMap<String, Integer> map, int numOfFiles){

        double probability;
        HashMap<String, Double> probabilityMap = new HashMap<>();
        Set<String> keys = map.keySet();
        Iterator<String> kIterator = keys.iterator();
        while(kIterator.hasNext()){

            String keyWord = kIterator.next();
            int wordCount = map.get(keyWord);
            probabilityMap.put(keyWord,(double)wordCount/numOfFiles);

        }

        return probabilityMap;
    }


    public void printHashMapDouble(HashMap<String, Double> hashMap){
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

        }



    }

    public void printHashMapInt(HashMap<String, Integer> hashMap){
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

        }



    }

}