package com.example.myhome.home.exception;

public class EmptyObjectException extends RuntimeException {
    public EmptyObjectException() {super("Empty object");}
    public EmptyObjectException(String msg) {super(msg);}
}
