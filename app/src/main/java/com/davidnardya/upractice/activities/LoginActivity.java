package com.davidnardya.upractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davidnardya.upractice.R;
import com.davidnardya.upractice.db.AppDB;
import com.davidnardya.upractice.fragments.SplashScreenFragment;
import com.davidnardya.upractice.pojo.Exercise;
import com.davidnardya.upractice.pojo.LoadingDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    //Authentication
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    //Final properties
    private static final String TAG = "GOOGLE_SIGN_IN";
    private static final int RC_SIGN_IN = 1231;

    //Buttons
    private Button revokeAccessBtn;
    SignInButton signInButton;

    //Other properties
    TextView textView;
    SplashScreenFragment fragment;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        signInButton = findViewById(R.id.sign_in_google_btn);
        textView = findViewById(R.id.signinwithgoogl_textview);
        revokeAccessBtn = findViewById(R.id.revoke_access_btn);

        configureActivity();
    }

    private void configureActivity() {
        signInRequest();
        configureSignInButton();
        configureRevokeButton();
        setText();
    }

    private void configureSignInButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void configureRevokeButton() {
        revokeAccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();

            }
        });
    }

    private void setText() {
        String signIn = "sign in with your Google account";
        textView.setText(signIn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Cannot authenticate", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        openFragment();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String userID = FirebaseAuth.getInstance().getUid();
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(userID).collection("Plans").get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        Log.d(TAG, "onSuccess: ");
                                        QuerySnapshot querySnapshot = queryDocumentSnapshots;
                                        AppDB.getInstance(getApplicationContext()).entitiesDao()
                                                .insertExercises(queryDocumentSnapshots.toObjects(Exercise.class));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e);
                            }
                        });
                    }
                });
    }

    public void signInRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Used in the log in button
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void revokeAccess() {
        mGoogleSignInClient.revokeAccess();
        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.startLoadingDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        }, 3000);
    }

    public void openFragment() {
        fragment = SplashScreenFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.login_activity_fragment_container, fragment, "LOGIN_ACTIVITY_FRAGMENT").commit();
        signInButton.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        revokeAccessBtn.setVisibility(View.INVISIBLE);
    }

}
