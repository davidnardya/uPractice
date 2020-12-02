package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.fragments.PlanDescriptionFragment;
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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ViewPlanActivity extends AppCompatActivity implements PlanFirestoreAdapter.OnExerciseClick,
        PlanDeletionDialog.PlanDeletionListener {

    public static final String ViewPlanActivityEXTRA_PLAN_ID = "com.davidnardya.upractice.ViewPlanActivity.EXTRA_PLAN_ID";
    public static final String ViewPlanActivityEXTRA_EXERCISE_ID = "com.davidnardya.upractice.ViewPlanActivity.EXTRA_EXERCISE_ID";

    String planID;
    String exerciseID;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference exercisesCollection;
    private RecyclerView exercisesRecyclerView;

    PlanFirestoreAdapter adapter;
    TextView planName;
    FloatingActionButton addNewExerciseFab;
    CardView viewPlansPageCardView;

    boolean deletePlan;
    boolean fragmentIsActive = false;

    private FrameLayout planDescriptionFragmentContainer;
    PlanDescriptionFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plan);

        Intent intent = getIntent();
        if (intent.getStringExtra(MainActivity.MainActivityEXTRA_PLAN_ID) != null) {
            planID = intent.getStringExtra(MainActivity.MainActivityEXTRA_PLAN_ID);
        } else if (intent.getStringExtra(ViewExerciseActivity.ViewExerciseActivityEXTRA_PLAN_ID) != null){
            planID = intent.getStringExtra(ViewExerciseActivity.ViewExerciseActivityEXTRA_PLAN_ID);
        } else if (intent.getStringExtra(ViewPlanActivity.ViewPlanActivityEXTRA_PLAN_ID) != null){
            planID = intent.getStringExtra(ViewPlanActivity.ViewPlanActivityEXTRA_PLAN_ID);
        } else {
            Toast.makeText(this, "Plan ID is Null", Toast.LENGTH_SHORT).show();
        }

        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        planName = findViewById(R.id.plan_details_plan_name_text_view);
        addNewExerciseFab = findViewById(R.id.add_new_exercise_to_plan_page_fab);
        planDescriptionFragmentContainer = findViewById(R.id.plan_description_fragment_container);
        viewPlansPageCardView = findViewById(R.id.view_plans_page_card_view);

        exercisesCollection = dataBase.collection("Users").document(userID)
                .collection("Plans").document(planID).collection("Exercises");

        //To get the parent's plan name
        DocumentReference nameRef = dataBase.collection("Users")
                .document(userID).collection("Plans").document(planID);
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
                Toast.makeText(ViewPlanActivity.this, "Failed!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        addNewExerciseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDataToNewExerciseActivity();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
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
                            Exercise exerciseNameToDelete = queryDocumentSnapshots.getDocuments()
                                    .get(viewHolder.getAdapterPosition()).toObject(Exercise.class);
                            exercisesCollection.document(id).delete();
                            AppDB.getInstance(getApplicationContext()).entitiesDao()
                                    .deleteExercise(exerciseNameToDelete.getExerciseID());
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(ViewPlanActivity.this, ViewPlanActivity.class);
                            intent.putExtra(ViewPlanActivity.ViewPlanActivityEXTRA_PLAN_ID, planID);
                            startActivity(intent);
                            finish();
                        } else if (collectionCount == 1){
                            openDeletePlanDialog(planID, exerciseID);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).attachToRecyclerView(exercisesRecyclerView);

        planName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference nameRef = dataBase.collection("Users")
                        .document(userID).collection("Plans").document(planID);
                nameRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null){
                            String planDescription = document.getString("planDescription");
                            openFragment(planDescription);
                        } else {
                            Toast.makeText(ViewPlanActivity.this, "Name is null!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



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
        intent.putExtra(ViewPlanActivityEXTRA_PLAN_ID, planID);
        exerciseID = snapshot.getId();
        intent.putExtra(ViewPlanActivityEXTRA_EXERCISE_ID, exerciseID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(!fragmentIsActive) {
            Intent intent = new Intent(ViewPlanActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (fragmentIsActive) {
            Objects.requireNonNull(fragment.getActivity()).getSupportFragmentManager().popBackStack();
            fragmentIsActive = false;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPlansPageCardView.setVisibility(View.VISIBLE);
                    addNewExerciseFab.setVisibility(View.VISIBLE);
                }
            }, 150);
        }
    }

    public void passDataToNewExerciseActivity(){

        Intent intent = new Intent(ViewPlanActivity.this, AddNewExerciseActivity.class);
        intent.putExtra(ViewPlanActivityEXTRA_PLAN_ID, planID);
        startActivity(intent);
        finish();
    }

    public void openDeletePlanDialog(String planIDToDelete, String exerciseIDToDelete){
        PlanDeletionDialog planDeletionDialog = new PlanDeletionDialog(planIDToDelete, exerciseIDToDelete);
        planDeletionDialog.show(getSupportFragmentManager(), "Deletion Dialog");
    }


    @Override
    public void deletePlanInterfaceMethod(boolean ifTrueDeletePlan) {
        deletePlan = ifTrueDeletePlan;
    }

    public void openFragment(String planDescription){
        fragment = PlanDescriptionFragment.newInstance(planDescription);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.plan_description_fragment_container, fragment, "PLAN_DESCRIPTION_FRAGMENT").commit();
        fragmentIsActive = true;
        addNewExerciseFab.setVisibility(View.INVISIBLE);
        viewPlansPageCardView.setVisibility(View.INVISIBLE);

    }
}
