package com.davidnardya.upractice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Plan;
import com.davidnardya.upractice.viewmodel.PlanListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<Plan> planList;

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_plan_cell, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        final Plan plan = planList.get(position);
        holder.planName.setText(plan.getPlanName());
        //TODO write logic of progress bar and change parameter according to logic
        holder.planProgress.setProgress(50);
    }

    @Override
    public int getItemCount() {
        if(planList == null){
            return 0;
        } else {
            return planList.size();
        }

    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView planName;
        private ProgressBar planProgress;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            planName = itemView.findViewById(R.id.single_cell_plan_name_text_view);
            planProgress = itemView.findViewById(R.id.plan_progress_bar);
        }


    }
}
