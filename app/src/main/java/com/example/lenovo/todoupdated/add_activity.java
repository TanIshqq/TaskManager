package com.example.lenovo.todoupdated;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.lenovo.todoupdated.Constants.key_task;

public class add_activity extends AppCompatActivity {
    public static final int ADD_SUCCESS = 1;
    final static int RQS_1 = 1;
    DatePicker pickerDate;
    TimePicker pickerTime;
    Tasks task;
    EditText add_event;
    EditText add_venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        Intent i = getIntent();
        task =(Tasks) i.getSerializableExtra(key_task);
        pickerDate = (DatePicker)findViewById(R.id.date_picker);
        pickerTime = (TimePicker)findViewById(R.id.time_picker);
        add_event = (EditText)findViewById(R.id.add_event);
        add_venue = (EditText) findViewById(R.id.add_venue);

        Calendar now = Calendar.getInstance();
        pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));

    }



    public void add(View view){
        String eventText = add_event.getText().toString();
        String venueText = add_venue.getText().toString();
        TaskOpenHelper openHelper = TaskOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.task_event,eventText);
        contentValues.put(Contract.task_venue,venueText);
        long id = db.insert(Contract.task_tablename,null,contentValues);

        Calendar current = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(pickerDate.getYear(),
                pickerDate.getMonth(),
                pickerDate.getDayOfMonth(),
                pickerTime.getCurrentHour(),
                pickerTime.getCurrentMinute(),
                00);

        if(cal.compareTo(current) <= 0){
            //The set Date/Time already passed
            Toast.makeText(getApplicationContext(),
                    "Invalid Date/Time",
                    Toast.LENGTH_LONG).show();
        }else{
            setAlarm(cal);
        }


        Intent result = new Intent();
        result.putExtra(Constants.key_taskid,id);
        setResult(ADD_SUCCESS, result);
        db.close();
        finish();
    }

    private void setAlarm(Calendar targetCal){

//        info.setText("\n\n***\n"
//                + "Alarm is set@ " + targetCal.getTime() + "\n"
//                + "***\n");

        Intent intent = new Intent(getBaseContext(), AlarmReciever.class);
        intent.putExtra(Constants.key_task,task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

}
