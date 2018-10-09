package com.example.android.windsordesignstudio.wear_note_1.model;

import io.realm.RealmObject;

/**
 * Created by Rockwell Windsor Rice on 10/2/18.
 */
public class Note extends RealmObject {

    private String notes = "";
    private String id    = "";

    // Empty Constructor
    public Note() {

    }

    public Note(String id, String notes) {
        this.id    = id;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
