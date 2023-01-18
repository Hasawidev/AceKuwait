package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.FilterResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;


public class FilterViewModel extends ViewModel {
    private ProductRepository productRepository;

    public FilterViewModel() {
        productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<FilterResponse>> getFilter(String jsonParams, String language) {
        return productRepository.getFilters(jsonParams, language);
    }
}
