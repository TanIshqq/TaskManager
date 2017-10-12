package com.example.lenovo.todoupdated;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import static com.example.lenovo.todoupdated.Constants.key_position;
import static com.example.lenovo.todoupdated.Constants.key_task;

public class edit_activity extends AppCompatActivity {
    CustomAdapter adapter;
    Tasks task;
    EditText edit_event;
    EditText edit_venue;
    int position;
    public static final int RESULT_SAVE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        Intent i = getIntent();
        task =(Tasks) i.getSerializableExtra(key_task);
        position = i.getIntExtra(key_position,0);
        edit_event = (EditText)findViewById(R.id.edit_event);
        edit_venue = (EditText)findViewById(R.id.edit_venue);
        edit_event.setText(task.getEvent());
        edit_venue.setText(task.getVenue());


    }

    public void Edit(View view){

        String updatedEvent = edit_event.getEditableText().toString();
        String updatedVenue = edit_venue.getEditableText().toString();
        TaskOpenHelper openHelper = TaskOpenHelper.getInstance(getApplicationContext());
        int id = task.getId();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.task_event,updatedEvent);
        contentValues.put(Contract.task_venue,updatedVenue);
        db.update(Contract.task_tablename, contentValues, "_id=" + id, null);
        adapter.notifyDataSetChanged();
//        long id = db.insert(Contract.task_tablename,null,contentValues);
        Intent result = new Intent();
        result.putExtra(Constants.key_taskid,id);
        setResult(RESULT_SAVE, result);
        db.close();
        finish();

    }

}
