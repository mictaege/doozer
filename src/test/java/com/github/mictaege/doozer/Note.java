package com.github.mictaege.doozer;

import java.time.LocalDate;

/** */
public class Note {

    public enum Fields {
        creationDate, message
    }

    private final LocalDate creationDate;
    private String message;

    public Note() {
        super();
        creationDate = LocalDate.now();
        message = "";
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
