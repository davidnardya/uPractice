package com.davidnardya.upractice.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.davidnardya.upractice.activities.MainActivity;
import com.davidnardya.upractice.activities.ViewPlanActivity;
import com.davidnardya.upractice.db.AppDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlanDeletionDialog extends AppCompatDialogFragment {

    private boolean deletePlan = false;
    private PlanDeletionListener planDeletionListener;
    public static final String EXTRA_PLAN_ID = "com.davidnardya.upractice.activities.EXTRA_PLAN_ID";

    String planID;
    String exerciseID;
    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public PlanDeletionDialog(String planID, String exerciseID){
        this.planID = planID;
        this.exerciseID = exerciseID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Caution! Delete Plan")
                .setMessage("If you will delete the last exercise for this plan, the plan will be deleted as well." + exerciseID)
                .setPositiveButton("OK, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AppDB.getInstance(getActivity()).entitiesDao().deleteExercise(exerciseID);

                        DocumentReference planToDelete = dataBase.collection("Users")
                                .document(userID).collection("Plans").document(planID);
                        DocumentReference exerciseToDelete = planToDelete
                                .collection("Exercises").document(exerciseID);
                        exerciseToDelete.delete();
                        planToDelete.delete();


//                        deletePlan = true;
//                        planDeletionListener.deletePlanInterfaceMethod(deletePlan);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        try {
                            getActivity().finish();
                            Toast.makeText(getActivity(), "Plan deleted successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("Do not delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            planDeletionListener = (PlanDeletionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement PlanDeletionListener");
        }
    }

    public interface PlanDeletionListener{
        void deletePlanInterfaceMethod(boolean ifTrueDeletePlan);
    }
}
