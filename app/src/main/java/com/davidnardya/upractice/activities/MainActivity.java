package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.adapters.MainFirestoreAdapter;
import com.davidnardya.upractice.fragments.PlanDescriptionFragment;
import com.davidnardya.upractice.fragments.SplashScreenFragment;
import com.davidnardya.upractice.pojo.LoadingDialog;
import com.davidnardya.upractice.pojo.Plan;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MainFirestoreAdapter.OnPlanClick, PopupMenu.OnMenuItemClickListener {

    //Strings
    private String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public static final String MainActivityEXTRA_PLAN_ID = "com.davidnardya.upractice.MainActivity.EXTRA_PLAN_ID";
    private String welcomeText;
    String planID;

    //TextViews
    private TextView userNameDisplay;
    TextView empty;

    //Other properties
    private ImageView logOutBtn;
    private RecyclerView plansRecyclerView;
    private MainFirestoreAdapter adapter;
    private FloatingActionButton addNewActivityFab;
    private GoogleSignInAccount signInAccount;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    SplashScreenFragment fragment;
    private CollectionReference plansCollection = dataBase.collection("Users").document(userID).collection("Plans");

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popup_menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.popup_menu_info:
                Toast.makeText(this, "Made by David Nardya", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutBtn = findViewById(R.id.log_out_btn);
        userNameDisplay = findViewById(R.id.welcome_message_text_view);
        plansRecyclerView = findViewById(R.id.plans_recycler_view);
        addNewActivityFab = findViewById(R.id.add_new_plan_to_plans_page_fab);

        configureActivity();

    }

    private void configureActivity() {
        openFragment();
        configureButtons();

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

        retrieveGoogleAccountUsername();
    }

    private void retrieveGoogleAccountUsername() {
        //Used to retrieve username from Google account
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        welcomeText = "Welcome, " + signInAccount.getDisplayName();
        userNameDisplay.setText(welcomeText);
    }


    private void configureButtons() {
        configureLogoutButton();
        configureFAB();
    }

    private void configureFAB() {
        //new plan click listener
        addNewActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewPlanActivity.class);
                startActivity(intent);
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
    public void onPlanClick(DocumentSnapshot snapshot, int position) {
        Intent intent = new Intent(MainActivity.this, ViewPlanActivity.class);
        planID = snapshot.getId();
        intent.putExtra(MainActivityEXTRA_PLAN_ID, planID);
        startActivity(intent);
    }

    public void openFragment() {
        fragment = SplashScreenFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.main_activity_fragment_container, fragment, "MAIN_ACTIVITY_FRAGMENT").commit();
        plansRecyclerView.setVisibility(View.INVISIBLE);
        addNewActivityFab.setVisibility(View.INVISIBLE);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(fragment.getActivity()).getSupportFragmentManager().popBackStack();
                plansRecyclerView.setVisibility(View.VISIBLE);
                addNewActivityFab.setVisibility(View.VISIBLE);
                hideRecyclerViewIfEmpty();

            }
        }, 3000);

    }

    public void hideRecyclerViewIfEmpty() {
        plansCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (Objects.requireNonNull(task.getResult()).size() > 0) {
                    empty = findViewById(R.id.empty);
                    empty.setVisibility(View.GONE);
                    plansRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    empty = findViewById(R.id.empty);
                    String emptyString = "Looks like your list is empty,\nadd new a plan!";
                    empty.setText(emptyString);
                    empty.setVisibility(View.VISIBLE);
                    plansRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void configureLogoutButton() {
        //logout click listener
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.main_activity_popup_menu);
                popupMenu.show();
            }
        });
    }

}
