package com.davidnardya.upractice.datamanager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataManager {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();


//    dataBase.collection("Users").document(userID).collection("Plans").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (DocumentSnapshot snapshot : queryDocumentSnapshots){
//                    Plan plan = snapshot.toObject(Plan.class);
//                    Toast.makeText(AddNewPlanActivity.this, plan.getPlanName(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


}
