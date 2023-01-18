package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.DynamicContentResponse;
import com.alhasawi.acekuwait.data.repository.DynamicDataRepository;

public class DynamicContentViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public DynamicContentViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<DynamicContentResponse>> getDynamicWebviewContent(String name, String language) {
        return dynamicDataRepository.getDynamicWebviewContent(name, language);
    }
}
