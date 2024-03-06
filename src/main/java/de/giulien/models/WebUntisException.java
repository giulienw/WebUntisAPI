package de.giulien.models;

public class WebUntisException extends Exception {
    public WebUntisException() {}

    // Constructor that accepts a message
    public WebUntisException(String message)
    {
        super(message);
    }
}
