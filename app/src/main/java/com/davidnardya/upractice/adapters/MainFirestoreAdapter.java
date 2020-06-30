package com.davidnardya.upractice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Exercise;
import com.davidnardya.upractice.pojo.ExerciseStatus;
import com.davidnardya.upractice.pojo.Plan;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;

import java.util.ArrayList;
import java.util.List;

public class MainFirestoreAdapter extends FirestorePagingAdapter<Plan, MainFirestoreAdapter.PlanViewHolder> {

    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference plansRef = dataBase.collection("Users").document(userID).collection("Plans");
    List<Exercise> fullExercisesList = new ArrayList<>();
    List<Exercise> completedExercisesList = new ArrayList<>();

    int allExercises;
    int completedExercises;

    private OnPlanClick onPlanClick;

    public MainFirestoreAdapter(@NonNull FirestorePagingOptions<Plan> options, OnPlanClick onPlanClick) {
        super(options);
        this.onPlanClick = onPlanClick;
    }

    @NonNull
    public MainFirestoreAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_plan_cell, parent, false);
        return new MainFirestoreAdapter.PlanViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final MainFirestoreAdapter.PlanViewHolder holder, final int position, @NonNull final Plan model) {


        plansRef.document(model.getPlanID()).collection("Exercises").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    allExercises = 0;
                    completedExercises = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        Exercise exercise = document.toObject(Exercise.class);

                        if (exercise.getExerciseStatus() == ExerciseStatus.COMPLETED) {
                            completedExercises++;
                            allExercises++;
                        } else {
                            allExercises++;
                        }
                    }
                }

                int progress = completedExercises * 100 / allExercises;
                holder.planName.setText(model.getPlanName());
                holder.progressBar.setProgress(progress);
            }

        });


    }

    public class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView planName;
        private ProgressBar progressBar;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            planName = itemView.findViewById(R.id.single_cell_plan_name_text_view);
            progressBar = itemView.findViewById(R.id.plan_progress_bar);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPlanClick.onPlanClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnPlanClick {
        void onPlanClick(DocumentSnapshot snapshot, int position);
    }


}
