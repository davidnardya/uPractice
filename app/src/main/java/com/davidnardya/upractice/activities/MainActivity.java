package com.davidnardya.upractice.activities;

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

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.MainAdapter;
import com.davidnardya.upractice.pojo.Plan;
import com.davidnardya.upractice.viewmodel.PlanListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView planView;
    private PlanListViewModel planListViewModel;
    private MainAdapter adapter;


    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    private CollectionReference plansRef = dataBase.collection("plans");
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

        planView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new MainAdapter();

        planView.setAdapter(adapter);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Used to retrieve username from Google account
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        welcomeText = "Welcome, " + signInAccount.getDisplayName();
        userNameDisplay.setText(welcomeText);

        planListViewModel = new ViewModelProvider(this).get(PlanListViewModel.class);
        planListViewModel.getPlanListModelData().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> planList) {
                adapter.setPlanList(planList);
                adapter.notifyDataSetChanged();
            }
        });

    }

}
