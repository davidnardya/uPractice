package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.MainFirestoreAdapter;
import com.davidnardya.upractice.pojo.Plan;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements MainFirestoreAdapter.OnPlanClick {

    private RecyclerView plansRecyclerView;
    private MainFirestoreAdapter adapter;
    private FloatingActionButton addNewActivityFab;
    public static final String EXTRA_PLAN_ID = "com.davidnardya.upractice.activities.EXTRA_PLAN_ID";
    public static final String EXTRA_SPECIFIC_PLAN_NAME = "com.davidnardya.upractice.activities.EXTRA_SPECIFIC_PLAN_NAME";


    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference plansCollection = dataBase.collection("Users").document(userID).collection("Plans");

    private Button logOutBtn;
    private TextView userNameDisplay;
    private GoogleSignInAccount signInAccount;
    private String welcomeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutBtn = findViewById(R.id.log_out_btn);
        userNameDisplay = findViewById(R.id.welcome_message_text_view);
        plansRecyclerView = findViewById(R.id.plans_recycler_view);
        addNewActivityFab = findViewById(R.id.add_new_plan_to_plans_page_fab);

        Query query = plansCollection;

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Plan> firestoreRecyclerOptions = new FirestorePagingOptions.Builder<Plan>()
                .setQuery(query, config, Plan.class).build();

        adapter = new MainFirestoreAdapter(firestoreRecyclerOptions, this);

        plansCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                plansRecyclerView.setHasFixedSize(true);
                plansRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                plansRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        //logout click listener
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //new plan click listener
        addNewActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewPlanActivity.class);
                startActivity(intent);
            }
        });

        //Used to retrieve username from Google account
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        welcomeText = "Welcome, " + signInAccount.getDisplayName();
        userNameDisplay.setText(welcomeText);

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
    public void onPlanClick(DocumentSnapshot snapshot, int position) {
        Intent intent = new Intent(MainActivity.this, ViewPlanActivity.class);
        String planID = snapshot.getId();
        intent.putExtra(EXTRA_PLAN_ID, planID);
        startActivity(intent);
    }
}
