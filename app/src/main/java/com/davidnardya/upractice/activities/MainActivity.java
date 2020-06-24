package com.davidnardya.upractice.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.MainAdapter;
import com.davidnardya.upractice.pojo.Plan;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView planView;
    private MainAdapter adapter;
    private FloatingActionButton addNewActivityFab;




    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DocumentSnapshot documentSnapshot;

    Plan p;

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
        planView = findViewById(R.id.plans_recycler_view);
        addNewActivityFab = findViewById(R.id.add_new_plan_to_plans_page_fab);

        planView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new MainAdapter();

        planView.setAdapter(adapter);

        //On click in the recycler view you get to the next activity
        adapter.setOnPlanClickListener(new MainAdapter.OnPlanClickListener() {
            @Override
            public void onPlanClick(int position) {
                Intent intent = new Intent(MainActivity.this, ViewPlanActivity.class);
                intent.putExtra("planPosition", position);
                startActivity(intent);
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

//        planListViewModel = new ViewModelProvider(this, ViewModelProvider
//                .AndroidViewModelFactory
//                .getInstance(this.getApplication()))
//                .get(PlanListViewModel.class);
//
//        planListViewModel.getPlanListModelData().observe(this, new Observer<List<Plan>>() {
//            @Override
//            public void onChanged(List<Plan> planList) {
//                adapter.setPlanList(planList);
//                adapter.notifyDataSetChanged();
//            }
//        });




    }

}
