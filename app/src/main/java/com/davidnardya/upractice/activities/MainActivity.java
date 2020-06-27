package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.pojo.Plan;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView plansRecyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FloatingActionButton addNewActivityFab;


    //On click in the recycler view you get to the next activity


    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static String planID;
    private CollectionReference plansCollection = dataBase.collection("Users").document(userID).collection("Plans");
//    private DocumentReference specificPlanDocument = dataBase.collection("Users").document(userID).collection("Plans").document(planID);

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

        FirestoreRecyclerOptions<Plan> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Plan>()
                .setQuery(query, Plan.class).build();
        adapter = new FirestoreRecyclerAdapter<Plan, PlanViewHolder>(firestoreRecyclerOptions) {
            @NonNull
            @Override
            public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_plan_cell, parent, false);
                return new PlanViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PlanViewHolder holder, int position, @NonNull Plan model) {
                holder.planName.setText(model.getPlanName());
                holder.progressBar.setProgress(50);
                //planID = model.getPlanID();
            }
        };

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

        //On click in the recycler view you get to the next activity


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

    private class PlanViewHolder extends RecyclerView.ViewHolder {

        private TextView planName;
        private ProgressBar progressBar;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            planName = itemView.findViewById(R.id.single_cell_plan_name_text_view);
            progressBar = itemView.findViewById(R.id.plan_progress_bar);

        }
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

}
