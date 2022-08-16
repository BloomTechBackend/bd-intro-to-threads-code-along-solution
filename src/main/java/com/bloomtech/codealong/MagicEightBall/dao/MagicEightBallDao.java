package com.bloomtech.codealong.MagicEightBall.dao;

import com.bloomtech.codealong.MagicEightBall.exception.DataFileErrorException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*******************************************************************************************
 * This class will handle all MagicEightBall question/response processing
 *******************************************************************************************/

public class MagicEightBallDao {

    private static List<String> responses; // response data store

    // static initializer used to initialize static data references
    //        will be run before the first time an object of this class is instantiated or referenced
    // static initializers are a safer way to initialize static data members than constructors
    //        becouse static data may be referenced before any object of the class is instantiated
    //        thereby leaving the static data unitialized when refererence.

    static {
        responses = new ArrayList<>(20);   // Instantiate an ArrayList of initial size of 20 (expected # of responses)
        loadAppData();                     // Call helper method to load response data store
    }

    /********************************************************************************************
     * Return a random response
     ********************************************************************************************/
     public String getResponse() throws InterruptedException {
         // Instantiate a Random object for use in generating a ramdom response index
         Random randomNumberGenerator = new Random();

         // This pause is purposely included to increase response time
         // pause anywhere from 1/2 second to 1.5 seconds
         Thread.sleep(new Random().nextInt(1500 - 500) + 500);

         // Return a response using a random index between 0 and the last element index
         return responses.get(randomNumberGenerator.nextInt(responses.size()));
     }

    /********************************************************************************************
     * Load application data structure
     ********************************************************************************************/
    private static void loadAppData() { //throws FileNotFoundException  {

        String  aLine          = null;                            // Hold a line from the file
        // location and name of file holding reponses data to be used by teh Magic 8 Ball
        String  DATA_FILE      = "./src/main/java/com/bloomtech/codealong/MagicEightBall/dao/resources/Magic8BallResponses.txt";       // Name of file holding data to be loaded
        File    theDataFile    = null;                            // File object to represent file to be loaded
        Scanner dataFileReader = null;                            // Scanner object to read the file

        try {
            theDataFile = new File(DATA_FILE);             // Instantiate File object resprenenting data
            dataFileReader  = new Scanner(theDataFile);    // Instantiate Scanner to read file

            while (dataFileReader.hasNextLine()) {         // Loop as long as there is data in the file
                aLine = dataFileReader.nextLine().strip(); //      Get a line from the file and store it
                responses.add(aLine);                      //      Add line to response data store
            }
        }
        catch(FileNotFoundException exceptionObj) {
            System.err.println(exceptionObj.getMessage());
            exceptionObj.printStackTrace();
            throw new DataFileErrorException(DATA_FILE + " not found - see error log for details");
        }
        catch (IllegalStateException exceptionObject) {
            System.err.println("Error processing response data file: " + DATA_FILE);
            System.err.println("Call stack:");
            exceptionObject.printStackTrace();

            System.out.println("Error processing response file: " + DATA_FILE);
            System.out.println("Please see error log file for details");
            System.err.println("System message: " + exceptionObject.getMessage() );
        }
        finally {   // Whether there is an exception or not
            dataFileReader.close();
        }
    }  // End of loadAppData()

}
