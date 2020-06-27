package com.davidnardya.upractice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Plan;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainFirestoreAdapter extends FirestorePagingAdapter<Plan, MainFirestoreAdapter.PlanViewHolder> {

    private OnPlanClick onPlanClick;

    public MainFirestoreAdapter(@NonNull FirestorePagingOptions<Plan> options, OnPlanClick onPlanClick) {
        super(options);
        this.onPlanClick = onPlanClick;
    }

    @NonNull
    public MainFirestoreAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_plan_cell, parent, false);
        return new MainFirestoreAdapter.PlanViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainFirestoreAdapter.PlanViewHolder holder, int position, @NonNull Plan model) {
        holder.planName.setText(model.getPlanName());
        holder.progressBar.setProgress(50);
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
