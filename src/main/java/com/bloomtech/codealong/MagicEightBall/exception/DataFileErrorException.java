package com.bloomtech.codealong.MagicEightBall.exception;

public class DataFileErrorException extends RuntimeException {
        public DataFileErrorException() { super(); }
        public DataFileErrorException(String errorMessage) {
                super(errorMessage);
               }
}
