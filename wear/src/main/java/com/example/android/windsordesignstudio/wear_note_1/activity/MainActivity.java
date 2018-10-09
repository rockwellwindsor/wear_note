package com.example.android.windsordesignstudio.wear_note_1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableRecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.windsordesignstudio.wear_note_1.R;
import com.example.android.windsordesignstudio.wear_note_1.adapter.RecyclerViewAdapter;
import com.example.android.windsordesignstudio.wear_note_1.model.Note;
import com.example.android.windsordesignstudio.wear_note_1.utils.ConfirmationUtils;
import com.example.android.windsordesignstudio.wear_note_1.utils.Constants;
import com.example.android.windsordesignstudio.wear_note_1.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends WearableActivity implements RecyclerViewAdapter.ItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1001;
    private RecyclerViewAdapter mAdapter;
    private List<Note> myDataSet = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureUI();

        // Realm init
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    private void configureUI() {
        WearableRecyclerView recyclerView = (WearableRecyclerView) findViewById(R.id.wearable_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter();
        mAdapter.setListNote(myDataSet);
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);

        EditText editText = (EditText) findViewById(R.id.edit_text);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_SEND) {
                    String text = textView.getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        Note note = createNote(null, text);
                        SharedPreferencesUtils.saveNote(note, textView.getContext());
                        updateData(note, Constants.ACTION_ADD);
                        textView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void updateAdapter() {
        // Realm Read Query
        RealmResults<Note> results = realm.where(Note.class).findAll();
        myDataSet.clear();
        myDataSet.addAll(results);
        mAdapter.setListNote(myDataSet);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(int position) {
        Intent intent = new Intent(getApplicationContext(), DeleteActivity.class);
        intent.putExtra(Constants.ITEM_POSITION, position);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void updateData(Note note, int action) {
        if (action == Constants.ACTION_ADD) {
            ConfirmationUtils.showMessage(getString(R.string.note_saved), this);
        } else if (action == Constants.ACTION_DELETE) {
            deleteRecord(note.getId());
            ConfirmationUtils.showMessage(getString(R.string.note_removed), this);
        }
        updateAdapter();
    }

    private void prepareUpdate(String id, String title, int action) {
        if (!(TextUtils.isEmpty(id) && TextUtils.isEmpty(title))) {
            Note note = createNote(id, title);
            updateData(note, action);
        }
    }

    private Note createNote(String id, String note) {
        if (id == null) {
            id = String.valueOf(System.currentTimeMillis());
            realm.beginTransaction();
            Note notes = realm.createObject(Note.class);
            notes.setId(id);
            notes.setNotes(note);
            realm.commitTransaction();
        }
        return new Note(id, note);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra(Constants.ITEM_POSITION)) {
                int position = data.getIntExtra(Constants.ITEM_POSITION, -1);
                if (position > -1) {
                    Note note = myDataSet.get(position);
                    updateData(note, Constants.ACTION_DELETE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapter();
    }

    public void deleteRecord(String id) {
       RealmResults<Note> results = realm.where(Note.class).equalTo("id", id).findAll();
       realm.beginTransaction();
       results.deleteAllFromRealm();
       realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
