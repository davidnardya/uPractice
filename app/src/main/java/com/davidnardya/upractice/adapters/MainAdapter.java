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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    FirebaseDatabase db;
    DocumentReference plans;
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentSnapshot documentSnapshot;

    private OnPlanClickListener mListener;

    //On click in the recycler view you get to the next activity
    public interface OnPlanClickListener{
        void onPlanClick(int position);
    }

    public void setOnPlanClickListener(OnPlanClickListener listener){
        mListener = listener;
    }

//    public void setPlanList(List<Plan> planList) {
//        this.planList = planList;
//    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_plan_cell, parent, false);
        return new MainViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
//        final Plan plan = planList.get(position);
//        holder.planName.setText(plan.getPlanName());
//        //TODO write logic of progress bar and change parameter according to logic
//        holder.planProgress.setProgress(50);

    }

    @Override
    public int getItemCount() {
//        if(planList == null){
//            return 0;
//        } else {
//            return planList.size();
//        }
        return 0;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        private TextView planName;
        private ProgressBar planProgress;

        public MainViewHolder(@NonNull View itemView, final OnPlanClickListener listener) {
            super(itemView);
            planName = itemView.findViewById(R.id.single_cell_plan_name_text_view);
            planProgress = itemView.findViewById(R.id.plan_progress_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onPlanClick(position);
                        }
                    }
                }
            });
        }


    }
}
