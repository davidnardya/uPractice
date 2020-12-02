package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.notifications.NotificationService;
import com.davidnardya.upractice.pojo.Exercise;
import com.davidnardya.upractice.pojo.ExerciseStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewExerciseActivity extends AppCompatActivity {

    //Strings
    public static final String ViewExerciseActivityEXTRA_PLAN_NAME = "com.davidnardya.upractice.ViewExerciseActivity.EXTRA_PLAN_NAME";
    public static final String ViewExerciseActivityEXTRA_PLAN_ID = "com.davidnardya.upractice.ViewExerciseActivity.EXTRA_PLAN_ID";
    public static final String ViewExerciseActivityEXTRA_PLAN_DESCRIPTION = "com.davidnardya.ViewExerciseActivity.activities.EXTRA_PLAN_DESCRIPTION";
    public static final String ViewExerciseActivityEXTRA_EXERCISE_ID = "com.davidnardya.upractice.ViewExerciseActivity.EXTRA_EXERCISE_ID";
    private static final String TAG = "ViewExerciseActivity";
    String planID, exerciseID, planName, planDescription;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


    //Radio group
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton rbNotStarted;
    RadioButton rbInProgress;
    RadioButton rbCompleted;

    //Other properties
    TextView exerciseName, exerciseDescription, exerceiseDateTextView;
    Button editExerciseButton;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    DocumentReference exerciseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        exerciseName = findViewById(R.id.exercise_details_exercise_name_text_view);
        exerciseDescription = findViewById(R.id.exercise_details_exercise_description_text_view);
        exerceiseDateTextView = findViewById(R.id.view_exercise_page_exercise_alert_date_text_view);
        editExerciseButton = findViewById(R.id.edit_exercise_button);
        radioGroup = findViewById(R.id.radio_group);
        rbNotStarted = findViewById(R.id.rb_not_started);
        rbInProgress = findViewById(R.id.rb_in_progress);
        rbCompleted = findViewById(R.id.rb_completed);

        configureActivity();
    }

    private void configureActivity() {
        configureGetIntent();
        configureExercise();
        configureEditExerciseButton();
    }

    private void configureEditExerciseButton() {
        editExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToNewExerciseActivity();
            }
        });
    }

    private void configureExercise() {
        //To get the exercise's details
        exerciseRef = dataBase.collection("Users").document(userID)
                .collection("Plans").document(planID)
                .collection("Exercises").document(exerciseID);
        exerciseRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    exerciseName.setText(document.getString("exerciseName"));
                    exerciseDescription.setText(document.getString("exerciseDescription"));

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
                    Timestamp timestamp = (Timestamp) document.get("exerciseAlertDate");
                    if (timestamp != null) {
                        Date date = timestamp.toDate();
                        String time = "Alert set to:\n" + simpleDateFormat.format(date);
                        exerceiseDateTextView.setText(time);
                    } else {
                        Log.d(TAG, "onComplete: Exercise is null, cannot get exercise date");
                    }

                    Exercise exercise = document.toObject(Exercise.class);
                    if (exercise != null) {
                        switch (exercise.getExerciseStatus()) {
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
                        Log.d(TAG, "onComplete: Exercise is null, cannot get exercise status");
                    }
                } else {
                    Log.d(TAG, "onComplete: Exercise is null, cannot get exercise");
                }
            }
        });
    }

    private void configureGetIntent() {
        Intent intent = getIntent();
        if (intent.getStringExtra(ViewPlanActivity.ViewPlanActivityEXTRA_PLAN_ID) != null) {
            planID = intent.getStringExtra(ViewPlanActivity.ViewPlanActivityEXTRA_PLAN_ID);
            exerciseID = intent.getStringExtra(ViewPlanActivity.ViewPlanActivityEXTRA_EXERCISE_ID);
        } else if (intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_PLAN_ID) != null) {
            planID = intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_PLAN_ID);
            exerciseID = intent.getStringExtra(AddNewExerciseActivity.AddNewExerciseActivityEXTRA_EXERCISE_ID);
        } else if (intent.getStringExtra(NotificationService.NotificationServiceEXTRA_PLAN_ID) != null) {
            planID = intent.getStringExtra(NotificationService.NotificationServiceEXTRA_PLAN_ID);
            exerciseID = intent.getStringExtra(NotificationService.NotificationServiceEXTRA_EXERCISE_ID);
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        String id = radioButton.getText().toString();
        ExerciseStatus exerciseStatusUpdate;

        switch (id) {
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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewExerciseActivity.this, ViewPlanActivity.class);
        intent.putExtra(ViewExerciseActivityEXTRA_PLAN_ID, planID);
        startActivity(intent);
        finish();
    }

    public void passDataToNewExerciseActivity() {
        Intent intent = new Intent(ViewExerciseActivity.this, AddNewExerciseActivity.class);
        intent.putExtra(ViewExerciseActivityEXTRA_PLAN_ID, planID);
        intent.putExtra(ViewExerciseActivityEXTRA_EXERCISE_ID, exerciseID);
        startActivity(intent);
    }
}
