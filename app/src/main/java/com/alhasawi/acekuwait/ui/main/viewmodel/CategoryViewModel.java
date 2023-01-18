package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.MainCategoryResponse;
import com.alhasawi.acekuwait.data.api.response.OriginCategoryResponse;
import com.alhasawi.acekuwait.data.repository.DynamicDataRepository;

public class CategoryViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;

    public CategoryViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getCategoryTree(String language) {
        return dynamicDataRepository.getMainCategories(language);
    }

    public MutableLiveData<Resource<OriginCategoryResponse>> getCategoriesByOrigin(String categoryId, String languageId) {
        return dynamicDataRepository.getOriginCategories(categoryId, languageId);
    }
}
