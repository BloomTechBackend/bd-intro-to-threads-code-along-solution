package com.bloomtech.codealong.MagicEightBall.controller;

import com.bloomtech.codealong.MagicEightBall.dao.MagicEightBallDao;
import com.bloomtech.codealong.MagicEightBall.model.MagicEightBallRequest;
import com.bloomtech.codealong.MagicEightBall.model.MagicEightBallResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController  // This class contains RESTful controllers
public class MagicEightBallController {

    // Instantiate the Dao to be used to acquire data
    private MagicEightBallDao theEightBall = new MagicEightBallDao();

    /**
     * Handle HTTP POST requests for responses to questions
     *        with the URL: /magic8Ball/ask
     *
     * HTTP POST requests provide data for the request as JSON in the body of the request
     * The @RequestBody annotation tells Spring Boot to convert the JSON in the body to
     *     an object of the class specified
     *
     * @param theRequest - Will contain a JSON array of strings passed through the request body
     *                     each element in the array is a question to ask the Magic 8 Ball
     * @return           - A List of Magic8BallResponse objects, sorted by ascending response time,
     *                     each one containing a question asked, the answer to the question, response time,
     *                     return code and processing message
     */
    @PostMapping(value="/magic8ball/ask")
    public List<MagicEightBallResponse> getResponse(@RequestBody MagicEightBallRequest theRequest) throws InterruptedException, CloneNotSupportedException {
        // Log the request to the server log
        System.out.println("-".repeat(100));
        logRequest("Request received via HTTP POST URL: /magic8ball/ask with " + theRequest.getQuestions().size() + " questions");

        long requestStartTime = System.currentTimeMillis();       // Record start time of process

        // Data store for responses
        List<MagicEightBallResponse> answers = new ArrayList<>();

        // Hold Threads in order to wait for all of them to complete
        List<Thread> ask8BallThreads = new ArrayList<>();

        // Hold the objects interacting with the Magic8Ball
        //      so we can get the responses from them when thier thread completes
        List<AskThe8Ball> questionsFor8Ball = new ArrayList<>();

        // Used to enumerate questions from the input array as they are processed
        int questionNumber = 0;

        // Loop through the questions received
        for (String aQuestion : theRequest.getQuestions()) {
            // Instantiate an object to interact with the Magic 8 Ball with a question
            AskThe8Ball askThe8Ball = new AskThe8Ball(++questionNumber, aQuestion);
            // Store the object so we can extract the resonse when its Thread completes
            questionsFor8Ball.add(askThe8Ball);

            // Instantiate and start a Thread on which to run the Magic 8 Ball interaction object
            Thread aThread = new Thread(askThe8Ball);  // Instantiate Thread and assign it the askThe8Ball object
            ask8BallThreads.add(aThread);              // Remember the Thread so we can wait for it to complete
            aThread.start();                           // Start the Thread
        }

        // Now that all the Threads have been started, we need to wait for them to complete
        // before we can copy the response to the list of responses we are returning
        waitForThreadsToComplete(ask8BallThreads);

        // Now that all Threads have completed, we can copy the responses of their askthe8Ball processes
        //     to the list of responses we are returning
        for(AskThe8Ball an8BallInteration : questionsFor8Ball) {
            answers.add(an8BallInteration.getTheResponse());
        }

        // Sort the responses in ascending responses times
        Collections.sort(answers);   // Note: This will use the compareTo() method in the class of the object

        // Log the end of request processing to the server log
        logRequest("Request completed in " + (System.currentTimeMillis() - requestStartTime) + " milliseconds");

        // Return a List of response objects with the question asked, answer and processing time from the Magic Eight Ball
        return answers;
        } // End of processing for HTTP POST for /magic8Ball/ask

    /**
     * Handle HTTP Get requests for responses to questions
     *        with the URL: /magic8Ball/ask?question="value"
     *
     * HTTP GET requests provide data for the request as a value in in the URL
     * The @RequestParam annotation tells Spring Boot to convert the request parameter name in the URL to
     *     an object of the class specified
     *
     * @param question - Will contain the question to be asked as a query parameter
     * @return         - A Magic8BallResponse object
     */
    @GetMapping (value="/magic8ball/ask")
    public MagicEightBallResponse getResponse(@RequestParam String question) throws InterruptedException, CloneNotSupportedException {

        // Log the request to the server log
        logRequest("Request received via HTTP GET URL: /magic8ball/ask and question: " + question);

        // Instantiate an object to interact with the Magic8Ball
        AskThe8Ball theMagic8Ball = new AskThe8Ball(question);

        // Get an repsonse from the Magic8Ball and return it
        return theMagic8Ball.shakeAndTurnOver();
     }  // End of processing for HTTP GET for /magic8Ball/ask

    /*****************************************************************************************
     * Helper method to log a message provided via parameter with timestamp
     *
     * @param message
     */
    private void logRequest(String message) {
        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        System.out.println(new Timestamp(datetime) + "\t--> " + message);
    } // End of logRequest() method

    /**
     * Nested class for obtaining a response from the magnificent Magic 8 Ball
     *
     * Make the class able to run on a Thread
     */
    class AskThe8Ball implements Runnable {
        //    Instantiate an object to hold question response
        private MagicEightBallResponse theResponse = new MagicEightBallResponse();

        public AskThe8Ball(String question) {
            this.theResponse.setQuestionNumber(1);   // Set the question number to 1
            this.theResponse.setQuestion(question);  // Copy the question to the response object
        }

        public AskThe8Ball(int questionNumber, String question) {
            this.theResponse.setQuestionNumber(questionNumber); //    Copy the question number to the response object
            this.theResponse.setQuestion(question);             //    Copy the question to the response object
        }

        public MagicEightBallResponse getTheResponse() {
            return theResponse;
        }

        public void run() {
            try {
                this.shakeAndTurnOver();
            } catch (InterruptedException | CloneNotSupportedException e) {
                theResponse.setReturnCode(500);
                theResponse.setProcessingMessage("Error interacting with Magic 8 Ball");
                e.printStackTrace();
            }
        }

        // Obtain a response from the Magic8Ball
        public MagicEightBallResponse shakeAndTurnOver() throws InterruptedException, CloneNotSupportedException {
            // Remember when processing started
            long startMilliseconds = System.currentTimeMillis();

            //  get an answer and store in the response
            theResponse.setAnswer(theEightBall.getResponse());                //    ask the question and store answer in response

            // Calculate time to process response and store it in response object
            theResponse.setTimeToRespondSeconds(System.currentTimeMillis()-startMilliseconds);

            // defensive return the response from the Magic 8 Ball
            return theResponse.clone();
        }  // End of shakeAndTurnOver() method
    }  // End of AskThe8Ball class

    /**
     * Helper method to wait for all Threads to complete before resuming
     *
     * Makes the thread calling this method wait until all passed in threads are done executing before proceeding.
     *
     * @param threads to wait on
     * @throws InterruptedException
     *
     * the .join() method will wait until a Thread is complete before resuming execution
     *
     */
    private void waitForThreadsToComplete(List<Thread> threads) throws InterruptedException {
        // Go through the List of threads we started and wait for them all to complete
        for (Thread thread : threads) {
            thread.join(); // wait for the current Thread to complete before resuming processing
        }
    }  // End of waitForThreadsToComplete() method
}  // End of MagicEightBallController Class

