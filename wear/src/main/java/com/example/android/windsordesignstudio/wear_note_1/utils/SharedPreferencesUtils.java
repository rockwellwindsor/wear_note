package com.example.android.windsordesignstudio.wear_note_1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.windsordesignstudio.wear_note_1.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Rockwell Windsor Rice on 10/2/18.
 */
public class SharedPreferencesUtils {

    /**
     * Save the note
     * @param note
     * @param context
     */
    public static void saveNote(Note note, Context context) {
        if (note != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(note.getId(), note.getNotes());
            editor.apply();
        }
    }

    /**
     * Retrieve all notes
     * @param context
     * @return
     */
    public static List<Note> getAllNotes(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<Note> noteList = new ArrayList<>();
        Map<String, ?> key = sharedPreferences.getAll();

        for(Map.Entry<String, ?> entry : key.entrySet()) {
            String savedValue = (String) entry.getValue();

            if (savedValue != null) {
                Note note = new Note(entry.getKey(), savedValue);
                noteList.add(note);
            }
        }
        return noteList;
    }

    /**
     * Delete the note
     * @param id
     * @param context
     */
    public static void removeNote(String id, Context context) {
        if (id != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(id);
            editor.apply();
        }
    }
}
