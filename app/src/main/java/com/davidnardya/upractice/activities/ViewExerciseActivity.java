package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.PlanFirestoreAdapter;
import com.davidnardya.upractice.notifications.AlarmReceiver;
import com.davidnardya.upractice.notifications.NotificationService;
import com.davidnardya.upractice.pojo.Exercise;
import com.davidnardya.upractice.pojo.ExerciseStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.davidnardya.upractice.notifications.NotificationService.EXTRA_EXERCISE_ID;
import static com.davidnardya.upractice.notifications.NotificationService.EXTRA_PLAN_ID;

public class ViewExerciseActivity extends AppCompatActivity {

    String planID, exerciseID;
    TextView exerciseName, exerciseDescription;
    RadioGroup radioGroup;
    RadioButton radioButton;

    RadioButton rbNotStarted;
    RadioButton rbInProgress;
    RadioButton rbCompleted;


    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    DocumentReference exerciseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        exerciseName = findViewById(R.id.exercise_details_exercise_name_text_view);
        exerciseDescription = findViewById(R.id.exercise_details_exercise_description_text_view);
        radioGroup = findViewById(R.id.radio_group);

        rbNotStarted = findViewById(R.id.rb_not_started);
        rbInProgress = findViewById(R.id.rb_in_progress);
        rbCompleted = findViewById(R.id.rb_completed);

        Intent intent = getIntent();
        if(intent.getStringExtra(ViewPlanActivity.EXTRA_PLAN_ID) != null){
            planID = intent.getStringExtra(ViewPlanActivity.EXTRA_PLAN_ID);
            exerciseID = intent.getStringExtra(ViewPlanActivity.EXTRA_EXERCISE_ID);
        } else if (intent.getStringExtra(AddNewExerciseActivity.EXTRA_PLAN_ID) != null){
            planID = intent.getStringExtra(AddNewExerciseActivity.EXTRA_PLAN_ID);
            exerciseID = intent.getStringExtra(AddNewExerciseActivity.EXTRA_EXERCISE_ID);
        } else {
            planID = intent.getStringExtra(EXTRA_PLAN_ID);
            exerciseID = intent.getStringExtra(EXTRA_EXERCISE_ID);
        }


        //To get the exercise's details
        exerciseRef = dataBase.collection("Users").document(userID)
                .collection("Plans").document(planID)
                .collection("Exercises").document(exerciseID);
        exerciseRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document != null){
                    exerciseName.setText(document.getString("exerciseName"));
                    exerciseDescription.setText(document.getString("exerciseDescription"));
                    Exercise exercise = document.toObject(Exercise.class);
                    switch (exercise.getExerciseStatus()){
                        case UNKNOWN:
                            rbNotStarted.setChecked(true);
                            break;
                        case IN_PROGRESS:
                            rbInProgress.setChecked(true);
                            break;
                        case COMPLETED:
                            rbCompleted.setChecked(true);
                            break;
                        default:
                            rbNotStarted.setChecked(true);
                            break;
                    }
                } else {
                    Toast.makeText(ViewExerciseActivity.this, "Name is null!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        String id = radioButton.getText().toString();
        ExerciseStatus exerciseStatusUpdate;

        switch (id){
            case "Not Started":
                exerciseStatusUpdate = ExerciseStatus.NOT_STARTED;
                exerciseRef.update("exerciseStatus", exerciseStatusUpdate);
                Toast.makeText(this, "Exercise status updated to not started", Toast.LENGTH_SHORT).show();

                break;
            case "In Progress":
                exerciseStatusUpdate = ExerciseStatus.IN_PROGRESS;
                exerciseRef.update("exerciseStatus", exerciseStatusUpdate);
                Toast.makeText(this, "Exercise status updated to in progress", Toast.LENGTH_SHORT).show();
                break;
            case "Completed":
                exerciseStatusUpdate = ExerciseStatus.COMPLETED;
                exerciseRef.update("exerciseStatus", exerciseStatusUpdate);
                Toast.makeText(this, "Exercise status updated to completed", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("izik", "onNewIntent: ");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewExerciseActivity.this, ViewPlanActivity.class);
        intent.putExtra(ViewPlanActivity.EXTRA_PLAN_ID,planID);
        startActivity(intent);
        finish();
    }
}
