package com.example.lenovo.todoupdated;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lenovo on 10-10-2017.
 */
public class TaskOpenHelper extends SQLiteOpenHelper {


    private static TaskOpenHelper instance;


    public static TaskOpenHelper getInstance(Context context) {
        if(instance == null){
            instance = new TaskOpenHelper(context);
        }
        return instance;
    }

    private TaskOpenHelper(Context context) {
        super(context, "task_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + Contract.task_tablename + " ( " +
                Contract.task_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.task_event + " TEXT, " +
                Contract.task_venue + " TEXT)";


        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

