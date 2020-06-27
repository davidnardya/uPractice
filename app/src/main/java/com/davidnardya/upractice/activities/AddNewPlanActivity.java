package com.davidnardya.upractice.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Plan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;


public class AddNewPlanActivity extends AppCompatActivity {

    EditText newPlanName, newPlanDescription;
    FloatingActionButton addExerciseFAB;
    public static final String EXTRA_PLAN_NAME = "com.davidnardya.upractice.activities.EXTRA_PLAN_NAME";
    public static final String EXTRA_PLAN_DESCRIPTION = "com.davidnardya.upractice.activities.EXTRA_PLAN_DESCRIPTION";
    public static final String EXTRA_PLAN_ID = "com.davidnardya.upractice.activities.EXTRA_PLAN_ID";

    String planName, planDescription, planID;

    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    DocumentReference planRef = dataBase.collection("Users").document(userID).collection("Plans").document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_plan);

        newPlanName = findViewById(R.id.new_plan_name_edit_text);
        newPlanDescription = findViewById(R.id.new_plan_description_edit_text);
        addExerciseFAB = findViewById(R.id.add_new_exercise_fab);

        addExerciseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlanInDataBase();
            }
        });

    }

    public void createPlanInDataBase(){
            planName = newPlanName.getText().toString();
            planDescription = newPlanDescription.getText().toString();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Plan plan = new Plan(planName, planDescription);
            plan.setPlanID(planRef.getId());
            planID = plan.getPlanID();
            planRef.set(plan);
            passDataToNewExerciseActivity();
    }

    public void passDataToNewExerciseActivity(){

        Intent intent = new Intent(AddNewPlanActivity.this, AddNewExerciseActivity.class);
        intent.putExtra(EXTRA_PLAN_NAME, planName);
        intent.putExtra(EXTRA_PLAN_DESCRIPTION, planDescription);
        intent.putExtra(EXTRA_PLAN_ID, planID);
        startActivity(intent);
    }

}
