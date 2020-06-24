package com.davidnardya.upractice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.ExerciseRecyclerAdapter;

public class ViewPlanActivity extends AppCompatActivity {

    private RecyclerView exerciseList;
    ExerciseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);

        exerciseList = findViewById(R.id.exercises_recycler_view);

        exerciseList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ExerciseRecyclerAdapter();

        exerciseList.setAdapter(adapter);

        //On click in the recycler view you get to the next activity
        adapter.setOnExerciseClickListener(new ExerciseRecyclerAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseCLick(int position) {
                Intent intent = new Intent(ViewPlanActivity.this, ViewExerciseActivity.class);
                intent.putExtra("exercisePosition", position);
                startActivity(intent);
            }
        });
    }
}
