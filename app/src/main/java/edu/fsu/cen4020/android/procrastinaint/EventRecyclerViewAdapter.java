package edu.fsu.cen4020.android.procrastinaint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// https://www.youtube.com/watch?v=Vyqz_-sJGFk&t=472s
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = EventRecyclerViewAdapter.class.getCanonicalName();
    private ArrayList<String[]> eventsArrayList = new ArrayList<String[]>();
    private Context mContext;

    public EventRecyclerViewAdapter(Context mContext, ArrayList<String[]> eventsArrayList ) {
        this.eventsArrayList = eventsArrayList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.eventTitleTextView.setText(eventsArrayList.get(position)[0]);
        holder.startDateTextView.setText(eventsArrayList.get(position)[1]);
        holder.endDateTextView.setText(eventsArrayList.get(position)[2]);
        holder.startTimeTextView.setText(eventsArrayList.get(position)[3]);
        holder.endTimeTextView.setText(eventsArrayList.get(position)[4]);




    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox saveEventCheckBox;
        TextView eventTitleTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        ConstraintLayout eventRecyclerViewConstraintLayout;

        public ViewHolder(View itemView){
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.eventTitleTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            eventRecyclerViewConstraintLayout = itemView.findViewById(R.id.eventRecyclerViewConstraintLayout);
        }
    }
}
