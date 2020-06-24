//package com.davidnardya.upractice;
//
//import androidx.annotation.NonNull;
//
//import com.davidnardya.upractice.pojo.Plan;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.List;
//
//public class FirebaseRepository {
//
//    private OnFirestoreTaskComplete onFirestoreTaskComplete;
//
//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    String UserId = firebaseAuth.getCurrentUser().getUid();
//
//    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//    private CollectionReference plansRef = firebaseFirestore.collection("Users").document(UserId).collection("PlansList");
//
//
//    public void addPlanData(Plan plan){
//        plansRef.add(plan);
//    }
//
//
//    public FirebaseRepository(OnFirestoreTaskComplete onFirestoreTaskComplete){
//        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
//    }
//
////    Method to get data of list of plans
//    public void getPlansData(){
//        plansRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    onFirestoreTaskComplete.planListDataAdded(task.getResult().toObjects(Plan.class));
//                } else {
//                    onFirestoreTaskComplete.onError(task.getException());
//                }
//            }
//        });
//    }
//
//    public interface OnFirestoreTaskComplete {
//        void planListDataAdded(List<Plan> planList);
//        void onError(Exception e);
//        void addPlan(Plan plan);
//    }
//
//
//}
