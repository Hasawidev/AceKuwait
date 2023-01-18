package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.StoreResponse;
import com.alhasawi.acekuwait.data.repository.DynamicDataRepository;

public class StoreViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public StoreViewModel() {
        this.dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<StoreResponse>> getStoreList(String language, String token) {
        return dynamicDataRepository.getStoreList(language, token);
    }
}