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
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class PlanFirestoreAdapter extends FirestorePagingAdapter<Exercise, PlanFirestoreAdapter.ExerciseViewHolder> {

    private PlanFirestoreAdapter.OnExerciseClick onExerciseClick;

    public PlanFirestoreAdapter(@NonNull FirestorePagingOptions<Exercise> options, PlanFirestoreAdapter.OnExerciseClick onExerciseClick) {
        super(options);
        this.onExerciseClick = onExerciseClick;
    }

    @NonNull
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_exercise_cell, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, @NonNull Exercise model) {
        holder.exerciseName.setText(model.getExerciseName());

        switch (model.getExerciseStatus()) {
            case NOT_STARTED:
                holder.exerciseStatus.setBackgroundResource(R.color.exerciseStatusNotStarted);
                break;
            case IN_PROGRESS:
                holder.exerciseStatus.setBackgroundResource(R.color.exerciseStatusInProgress);
                break;
            case COMPLETED:
                holder.exerciseStatus.setBackgroundResource(R.color.exerciseStatusCompleted);
                break;
            default:
                break;
        }
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView exerciseName;
        private View exerciseStatus;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseName = itemView.findViewById(R.id.single_cell_plan_name_text_view);
            exerciseStatus = itemView.findViewById(R.id.exercise_status_single_cell);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onExerciseClick.onExerciseClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnExerciseClick {
        void onExerciseClick(DocumentSnapshot snapshot, int position);
    }

}