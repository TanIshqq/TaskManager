package com.example.lenovo.todoupdated;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.lenovo.todoupdated.Constants.key_event;
import static com.example.lenovo.todoupdated.Constants.key_position;
import static com.example.lenovo.todoupdated.Constants.key_task;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Tasks> mTasks;
    CustomAdapter adapter;
    public final static int REQUEST_ADD = 1;
    public final static int REQUEST_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTasks = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        //Log.d("check", "onCreate: ");
        TaskOpenHelper openHelper = TaskOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.task_tablename, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(Contract.task_event));
            String venue = cursor.getString(cursor.getColumnIndex(Contract.task_venue));
            int id = cursor.getInt(cursor.getColumnIndex(Contract.task_id));
            Tasks task = new Tasks(event, venue, id);
            mTasks.add(task);
        }
        cursor.close();
        //Log.d("check", "onCreatedb: ");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,add_activity.class);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });


        adapter = new CustomAdapter(this, mTasks, new CustomAdapter.TaskClickListener() {
            @Override
            public void onItemClick(int position) {
                Tasks task=mTasks.get(position);
                Snackbar.make(recyclerView,task.getEvent(), BaseTransientBottomBar.LENGTH_SHORT).show();
                Intent edit = new Intent(MainActivity.this,edit_activity.class);
                edit.putExtra(key_task, (Serializable) task);
                edit.putExtra(key_position, position);
                startActivityForResult(edit, REQUEST_EDIT);

            }

        });

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        Log.d("check", "onCreaterv: ");
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.UP,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Tasks fromTask = mTasks.get(from);
                Tasks toTask = mTasks.get(to);
                mTasks.set(from,toTask);
                mTasks.set(to,fromTask);
                adapter.notifyItemMoved(from,to);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskOpenHelper openHelper = TaskOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase db = openHelper.getWritableDatabase();
                int id = mTasks.get(viewHolder.getAdapterPosition()).getId();
                db.delete(Contract.task_tablename, Contract.task_id + " = " + id, null);
                db.close();
                mTasks.remove(position);
                adapter.notifyItemRemoved(position);

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.call) {
            int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
            if (permission == PERMISSION_GRANTED) {
                Intent call = new Intent();
                call.setAction(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:9310180802"));
                if (call.resolveActivity(getPackageManager()) != null) {
                    startActivity(call);
                }

            }
        }
        else if (id == R.id.aboutus) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String url = "http://google.com";
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        else if (id == R.id.feedback) {
            Intent feedback = new Intent();
            feedback.setAction(Intent.ACTION_SENDTO);
            feedback.setData(Uri.parse("mailto:tanishqgoel@gmail.com"));
            feedback.putExtra(Intent.EXTRA_TEXT, "FEEDBACK");
            if (feedback.resolveActivity(getPackageManager()) != null) {
                startActivity(feedback);
            }
        }


            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_EDIT){

            if(resultCode == edit_activity.RESULT_SAVE){
                adapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == REQUEST_ADD){
            if(resultCode == add_activity.ADD_SUCCESS){
                long id   =  data.getLongExtra(Constants.key_taskid,-1L);
                if(id > -1){
                    TaskOpenHelper openHelper = TaskOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase db = openHelper.getWritableDatabase();

                    Cursor cursor = db.query(Contract.task_tablename,null,
                            Contract.task_id + " = ?",new String[]{id + ""}
                            ,null,null,null);

                    if(cursor.moveToFirst()){
                        String add_event = cursor.getString(cursor.getColumnIndex(Contract.task_event));
                        String add_venue = cursor.getString(cursor.getColumnIndex(Contract.task_venue));
                        Tasks tasks = new Tasks(add_event,add_venue,(int)id);
                        mTasks.add(tasks);
                        adapter.notifyItemInserted(mTasks.size() - 1);
                        recyclerView.smoothScrollToPosition(mTasks.size() - 1);
                    }

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}