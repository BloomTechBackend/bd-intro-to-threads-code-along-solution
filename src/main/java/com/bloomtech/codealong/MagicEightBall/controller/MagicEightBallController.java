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

        List<MagicEightBallResponse> answers = new ArrayList<>(); // Data store for responses

        int questionNumber = 0;  // Used to enumerate questions from the input array as they are processed

        // Loop through the array of questions received with the request
        for (String aQuestion : theRequest.getQuestions()) {
            // Instantiate an object to interact with the Magic8Ball
            AskThe8Ball theMagic8Ball = new AskThe8Ball(++questionNumber, aQuestion);

            // ask the question and store answer in response
            answers.add(theMagic8Ball.shakeAndTurnOver());
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
     */
    class AskThe8Ball {
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
}  // End of MagicEightBallController Class

