package com.davidnardya.upractice.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davidnardya.upractice.FirebaseRepository;
import com.davidnardya.upractice.pojo.Plan;

import java.util.List;

public class PlanListViewModel extends ViewModel implements FirebaseRepository.OnFirestoreTaskComplete {

    private MutableLiveData<List<Plan>> planListModelData = new MutableLiveData<>();

    public LiveData<List<Plan>> getPlanListModelData() {
        return planListModelData;
    }

    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);

//    Constructor
    public PlanListViewModel(){
        firebaseRepository.getPlansData();
    }

    @Override
    public void planListDataAdded(List<Plan> planList) {
        planListModelData.setValue(planList);
    }

    @Override
    public void onError(Exception e) {

    }
}
