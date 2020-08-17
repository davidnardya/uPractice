package com.davidnardya.upractice.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.davidnardya.upractice.activities.ViewPlanActivity;

public class PlanDeletionDialog extends AppCompatDialogFragment {

    private boolean deletePlan = false;
    private PlanDeletionListener planDeletionListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Caution! Delete Plan")
                .setMessage("If you will delete the last exercise for this plan, the plan will be deleted as well.")
                .setPositiveButton("OK, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePlan = true;
                        planDeletionListener.deletePlanInterfaceMethod(deletePlan);
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
