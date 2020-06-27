package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.PlanFirestoreAdapter;
import com.davidnardya.upractice.pojo.Exercise;
import com.davidnardya.upractice.pojo.Plan;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewPlanActivity extends AppCompatActivity implements PlanFirestoreAdapter.OnExerciseClick {

    String planID;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference exercisesCollection;
    private RecyclerView exercisesRecyclerView;

    PlanFirestoreAdapter adapter;
    TextView planName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);

        Intent intent = getIntent();
        planID = intent.getStringExtra(MainActivity.EXTRA_PLAN_ID);
        exercisesCollection = dataBase.collection("Users").document(userID).collection("Plans").document(planID).collection("Exercises");


        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        planName = findViewById(R.id.plan_details_plan_name_text_view);
        planName.setText(planID);

        Query query = exercisesCollection;

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Exercise> firestoreRecyclerOptions = new FirestorePagingOptions.Builder<Exercise>()
                .setQuery(query, config, Exercise.class).build();

        adapter = new PlanFirestoreAdapter(firestoreRecyclerOptions, this);

        exercisesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                exercisesRecyclerView.setHasFixedSize(true);
                exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                exercisesRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewPlanActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onExerciseClick(DocumentSnapshot snapshot, int position) {
        Intent intent = new Intent(ViewPlanActivity.this, ViewExerciseActivity.class);
        startActivity(intent);

    }
}
