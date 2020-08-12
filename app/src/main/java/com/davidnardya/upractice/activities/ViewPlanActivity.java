package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.PlanDeletionDialog;
import com.davidnardya.upractice.adapters.PlanFirestoreAdapter;
import com.davidnardya.upractice.db.AppDB;
import com.davidnardya.upractice.pojo.Exercise;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewPlanActivity extends AppCompatActivity implements PlanFirestoreAdapter.OnExerciseClick {

    public static final String EXTRA_PLAN_ID = "com.davidnardya.upractice.activities.EXTRA_PLAN_ID";
    public static final String EXTRA_EXERCISE_ID = "com.davidnardya.upractice.activities.EXTRA_EXERCISE_ID";

    String planID;
    String exerciseID;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference exercisesCollection;
    private RecyclerView exercisesRecyclerView;

    PlanFirestoreAdapter adapter;
    TextView planName;
    FloatingActionButton addNewExerciseFab;

    boolean planIsForDeletion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);

        Intent intent = getIntent();
        planID = intent.getStringExtra(MainActivity.EXTRA_PLAN_ID);

        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        planName = findViewById(R.id.plan_details_plan_name_text_view);
        addNewExerciseFab = findViewById(R.id.add_new_exercise_to_plan_page_fab);

        exercisesCollection = dataBase.collection("Users").document(userID).collection("Plans").document(planID).collection("Exercises");

        //exerciseID = exercisesCollection.document().getId();

        //To get the parent's plan name
        DocumentReference nameRef = dataBase.collection("Users").document(userID).collection("Plans").document(planID);
        nameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document != null){
                    planName.setText(document.getString("planName"));
                } else {
                    Toast.makeText(ViewPlanActivity.this, "Name is null!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Query query = exercisesCollection;

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
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

        addNewExerciseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToNewExerciseActivity();
            }
        });

        //Delete exercise
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                exercisesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String id = queryDocumentSnapshots.getDocuments().get(viewHolder.getAdapterPosition()).getId();
                        int collectionCount = queryDocumentSnapshots.getDocuments().size();

                        if(collectionCount > 1){
                            Exercise exerciseNameToDelete = queryDocumentSnapshots.getDocuments().get(viewHolder.getAdapterPosition()).toObject(Exercise.class);
                            exercisesCollection.document(id).delete();
                            AppDB.getInstance(getApplicationContext()).entitiesDao().deleteExercise(exerciseNameToDelete.getExerciseID());
                            adapter.notifyDataSetChanged();
                        } else {
                            openDeletePlanDialog();

                            boolean delete = planIsForDeletion;
                            if(delete){
                                Exercise exerciseNameToDelete = queryDocumentSnapshots.getDocuments().get(viewHolder.getAdapterPosition()).toObject(Exercise.class);
                                //exercisesCollection.document(id).delete();
                                dataBase.collection("Users").document(userID).collection("Plans").document(planID).delete();
                                openDeletePlanDialog();
                                AppDB.getInstance(getApplicationContext()).entitiesDao().deleteExercise(exerciseNameToDelete.getExerciseID());
                                adapter.notifyDataSetChanged();
                                Intent intent = new Intent(ViewPlanActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }).attachToRecyclerView(exercisesRecyclerView);

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
        intent.putExtra(EXTRA_PLAN_ID, planID);
        exerciseID = snapshot.getId();
        intent.putExtra(EXTRA_EXERCISE_ID, exerciseID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewPlanActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void passDataToNewExerciseActivity(){

        Intent intent = new Intent(ViewPlanActivity.this, AddNewExerciseActivity.class);
        intent.putExtra(EXTRA_PLAN_ID, planID);
        startActivity(intent);
    }

    public void openDeletePlanDialog(){
        PlanDeletionDialog planDeletionDialog = new PlanDeletionDialog();
        planDeletionDialog.show(getSupportFragmentManager(), "Deletion Dialog");
    }

    public static boolean deletePlan(boolean yesOrNo){
        return yesOrNo;
    }
}
