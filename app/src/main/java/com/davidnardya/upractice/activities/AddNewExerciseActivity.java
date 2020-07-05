package com.davidnardya.upractice.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Exercise;
import com.davidnardya.upractice.pojo.ExerciseStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class AddNewExerciseActivity extends AppCompatActivity {

    EditText newExerciseName, newExerciseDescription;
    FloatingActionButton finishExerciseFAB;
    String ExerciseName, ExerciseDescription;
    ExerciseStatus exerciseStatus = ExerciseStatus.NOT_STARTED;
    Button pickTimeButton, pickDateButton;

    Calendar timePicked = Calendar.getInstance();

    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();


    String exerciseID;

    String planName, planDescription, planID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);

        Intent intent = getIntent();
        planName = intent.getStringExtra(AddNewPlanActivity.EXTRA_PLAN_NAME);
        planDescription = intent.getStringExtra(AddNewPlanActivity.EXTRA_PLAN_DESCRIPTION);
        planID = intent.getStringExtra(AddNewPlanActivity.EXTRA_PLAN_ID);

        newExerciseName = findViewById(R.id.new_exercise_name_edit_text);
        newExerciseDescription = findViewById(R.id.new_exercise_description_edit_text);
        finishExerciseFAB = findViewById(R.id.finish_exercise_fab);
        pickTimeButton = findViewById(R.id.exercise_set_time_button);
        pickDateButton = findViewById(R.id.exercise_set_date_button);

        finishExerciseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewExercise();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentYear = calendar.get(Calendar.YEAR);
        pickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewExerciseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timePicked = Calendar.getInstance();
                        timePicked.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        timePicked.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        String time = simpleDateFormat.format(timePicked.getTime());
                        pickTimeButton.setText(time);
                    }
                },currentHour,currentMinute,android.text.format.DateFormat.is24HourFormat(AddNewExerciseActivity.this));
                timePickerDialog.show();
            }
        });

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewExerciseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        timePicked.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        timePicked.set(Calendar.MONTH, month);
                        timePicked.set(Calendar.YEAR, year);
                        String date = DateFormat.getDateInstance(DateFormat.FULL).format(timePicked.getTime());
                        pickDateButton.setText(date);
                    }
                },currentYear,currentMonth,currentDay);
                datePickerDialog.show();
            }
        });


    }

    public void createNewExercise(){

        ExerciseName = newExerciseName.getText().toString();
        ExerciseDescription = newExerciseDescription.getText().toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date exerciseDate = timePicked.getTime();

//        SimpleDateFormat finalDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        String exerciseTime = finalDate.format(exerciseDate.getTime());
//
//        Toast.makeText(this, exerciseTime, Toast.LENGTH_SHORT).show();

        Exercise exercise = new Exercise(ExerciseName, ExerciseDescription, exerciseStatus, exerciseDate);
        exerciseID = dataBase.collection("Users").document(userID).collection("Plans").document(planID).collection("Exercises").document().getId();
        exercise.setExerciseID(exerciseID);
        dataBase.collection("Users").document(userID).collection("Plans").document(planID).collection("Exercises").document(exerciseID).set(exercise);

        Intent intent = new Intent(AddNewExerciseActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
