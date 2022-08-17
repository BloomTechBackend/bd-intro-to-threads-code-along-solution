package com.bloomtech.codealong.MagicEightBall.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;

public class MagicEightBallResponse implements Comparable, Cloneable {

    private int    questionNumber = 0;
    private String question;
    private String answer;
    private double timeToRespondSeconds;
    private int    returnCode;
    private String processingMessage;

    public MagicEightBallResponse() {
        this.returnCode = 0;
        this.processingMessage = "Question successfully answered";
        this.questionNumber = 1;
    };

    public MagicEightBallResponse(int questionNumber) {
        this.returnCode = 0;
        this.processingMessage = "Question successfully answered";
        this.questionNumber = questionNumber;
    };

    public int  getQuestionNumber() {
        return questionNumber;
    }
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }
    public void   setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
    public void   setAnswer(String answer) {
        this.answer = answer;
    }

    public double getTimeToRespondSeconds() {
        return timeToRespondSeconds;
    }
    public void   setTimeToRespondSeconds(double timeToRespondMillis) {this.timeToRespondSeconds = timeToRespondMillis;}
    public void   setTimeToRespondSeconds(long   timeToRespondMillis) {this.timeToRespondSeconds = timeToRespondMillis / 1000.0;}

    public int  getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getProcessingMessage() {
        return processingMessage;
    }
    public void   setProcessingMessage(String processingMessage) {
        this.processingMessage = processingMessage;
    }


    /**
     * clone() override to provide a deep copy for objects of this class
     * a deep copy will copy the data stored with a reference rather than just the reference itself
     * the Object class clone() method does a shallow copy (copies references not data for the reference)
     *
     * @throws CloneNotSupportedException
     */
    @Override
    public MagicEightBallResponse clone() throws CloneNotSupportedException {
        // instantiate a shallow copy of the object using the object
        MagicEightBallResponse copyOfMagicEightBallResponse = new MagicEightBallResponse();

        // copy the data from the source object to the new copy
        copyOfMagicEightBallResponse.setQuestionNumber(this.getQuestionNumber());
        copyOfMagicEightBallResponse.setQuestion(this.getQuestion());
        copyOfMagicEightBallResponse.setAnswer(this.getAnswer());
        copyOfMagicEightBallResponse.setTimeToRespondSeconds(this.getTimeToRespondSeconds());
        copyOfMagicEightBallResponse.setReturnCode(this.getReturnCode());
        copyOfMagicEightBallResponse.setProcessingMessage(this.getProcessingMessage());

        // return the deep copy of the object
        return copyOfMagicEightBallResponse;
    }

    /**
     * compareTo() override so we can use the Collections.sort() method on our data store
     *
     * compareTo() will return an int indicating how data on one object of the class
     *             relates to data in another object of the class as follows:
     *
     *                    0       - they are equal
     *             negative-value - the first is less than the second
     *             positive-value - the first is greater than the second
     *
     * the first object is referenced using this.
     * the second object is referenced as a parameter to the method
     *
     * This will sort by ascending timeToRespondInSeconds
     *
     * @param otherResponseObject
     */
    @Override
    public int compareTo(Object otherResponseObject) {
        // Sort based on ascending respone time in seconds
        // values multiplied by 1000 and cast to int for more accurate comparison
        //   return      the first-object-value          minus     the-second-object-value
        return (int) (this.getTimeToRespondSeconds()*1000) - (int) (((MagicEightBallResponse) otherResponseObject).getTimeToRespondSeconds() * 1000);
    }
}
