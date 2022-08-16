package com.bloomtech.codealong.MagicEightBall.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;

public class MagicEightBallResponse {

    private int    returnCode;
    private String processingMessage;
    private double timeToRespondSeconds;
    private String question;
    private String answer;

    public MagicEightBallResponse() {
        this.returnCode = 0;
        this.processingMessage = "Question successfully answered";
    };

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getProcessingMessage() {
        return processingMessage;
    }

    public void setProcessingMessage(String processingMessage) {
        this.processingMessage = processingMessage;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getTimeToRespondSeconds() {
        return timeToRespondSeconds;
    }

    public void setTimeToRespondSeconds(long timeToRespondMillis) {
        this.timeToRespondSeconds = timeToRespondMillis / 1000.0;
    }
}
