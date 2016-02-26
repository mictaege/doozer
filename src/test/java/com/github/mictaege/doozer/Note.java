package com.github.mictaege.doozer;

import java.util.Date;

/** */
public class Note {

    public enum Fields implements DeclaredField<Note> {
        creationDate, message
	}

    private final Date creationDate;
    private String message;

    public Note() {
        super();
        creationDate = new Date();
        message = "";
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
