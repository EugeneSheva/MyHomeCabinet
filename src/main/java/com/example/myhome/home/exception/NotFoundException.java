package com.example.myhome.home.exception;

public class NotFoundException extends RuntimeException{
        public NotFoundException() {
            super("Object not found");
        }

}
