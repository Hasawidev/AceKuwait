package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.MainCategoryResponse;
import com.alhasawi.acekuwait.data.repository.DynamicDataRepository;

public class OnboardViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public OnboardViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getMainCateogries(String language) {
        return dynamicDataRepository.getMainCategories(language);
    }

}
