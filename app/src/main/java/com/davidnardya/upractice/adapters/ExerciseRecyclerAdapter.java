package com.davidnardya.upractice.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Exercise;

import java.util.ArrayList;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ExerciseViewHolder> {

    public static ArrayList<Exercise> exerciseArrayList;
    private OnExerciseClickListener mListener;

    //On click in the recycler view you get to the next activity
    public interface OnExerciseClickListener{
        void onExerciseCLick(int position);
    }

    public void setOnExerciseClickListener(OnExerciseClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_exercise_cell, parent, false);
        return new ExerciseViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        final Exercise exercise = exerciseArrayList.get(position);
        holder.exerciseName.setText(exercise.getExerciseName());
        switch (exercise.getExerciseStatus()){
            case IN_PROGRESS:
                holder.exerciseStatus.setBackgroundColor(Color.parseColor("@color/exerciseStatusNotStarted"));
                break;
            case COMPLETED:
                holder.exerciseStatus.setBackgroundColor(Color.parseColor("@color/exerciseStatusInProgress"));
                break;
            default:
                holder.exerciseStatus.setBackgroundColor(Color.parseColor("@color/exerciseStatusCompleted"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(exerciseArrayList == null){
            return 0;
        } else {
            return exerciseArrayList.size();
        }
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{

        TextView exerciseName;
        View exerciseStatus;

        public ExerciseViewHolder(@NonNull View itemView, final OnExerciseClickListener listener) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.single_cell_plan_name_text_view);
            exerciseStatus = itemView.findViewById(R.id.exercise_status_single_cell);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((listener != null)){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onExerciseCLick(position);
                        }
                    }
                }
            });
        }
    }
}
