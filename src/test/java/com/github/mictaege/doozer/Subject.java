package com.github.mictaege.doozer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.unmodifiableList;

/** */
public abstract class Subject {

    public enum Fields implements DeclaredField<Subject> {
        id, notes
    }

    private final String id;
    private final List<Note> notes;


    protected Subject() {
        super();
        this.id = UUID.randomUUID().toString();
        this.notes = new ArrayList<>();
    }

    public final String getId() {
        return id;
    }

    public final List<Note> getNotes() {
        return unmodifiableList(notes);
    }

    public final boolean addNote(final Note note) {
        return notes.add(note);
    }

}
