package com.example.lenovo.todoupdated;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Lenovo on 09-10-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.TaskViewHolder>{

    ArrayList<Tasks> mTasks;
    Context mContext;
    TaskClickListener mListener;


    public CustomAdapter(Context context,ArrayList<Tasks> tasks,TaskClickListener listener){
        mTasks = tasks;
        mContext = context;
        mListener = listener;

    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(mContext).inflate(R.layout.row_task,parent,false);
        return new TaskViewHolder(rowView,mListener);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        Tasks task = mTasks.get(position);
        holder.event.setText(task.getEvent());
        holder.venue.setText(task.getVenue());


    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView event;
        TextView venue;
        View itemView;
        TaskClickListener clickListener;

        public TaskViewHolder(View itemView, TaskClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            clickListener = listener;
            event = (TextView) itemView.findViewById(R.id.event);
            venue = (TextView) itemView.findViewById(R.id.venue);
            itemView.setOnClickListener(this);

        }



        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            if(view == itemView) {
                clickListener.onItemClick(position);
            }


        }
    }

    public interface TaskClickListener{

        void onItemClick(int position);

    }

}
