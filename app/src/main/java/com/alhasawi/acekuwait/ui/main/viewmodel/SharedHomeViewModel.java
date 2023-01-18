package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.FilterAttributeValues;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.ProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.ProductResponse;
import com.alhasawi.acekuwait.data.api.response.SearchedProductDetailsResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;
import com.alhasawi.acekuwait.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class SharedHomeViewModel extends ViewModel {

    private final MutableLiveData<Product> selectedProduct = new MutableLiveData<Product>();
    private final MutableLiveData<Map<String, List<FilterAttributeValues>>> filterAttributesLiveData = new MutableLiveData<>();
    MutableLiveData<Resource<ProductResponse>> generalMutsbleLiveData;
    MutableLiveData<List<Product>> wishListedProducts = new MutableLiveData<>();
    MutableLiveData<List<Product>> cartedProducts = new MutableLiveData<>();
    ArrayList<Product> wishlistItems = new ArrayList<>();
    ArrayList<Product> cartedItems = new ArrayList<>();
    private ProductRepository productRepository;
    //Pagination
    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Product>> productPaginationLiveData;


    public SharedHomeViewModel() {
        productRepository = new ProductRepository();
        generalMutsbleLiveData = new MutableLiveData<>();
    }

    public void select(Product product) {
        selectedProduct.setValue(product);
    }

    public LiveData<Product> getSelected() {
        return selectedProduct;
    }


    public MutableLiveData<Resource<ProductDetailsResponse>> getProductDetails(String productName, String language) {
        return productRepository.getProductDetails(productName, language);
    }


    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }


    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String sku, String language, String customerId) {
        return productRepository.getSearchedProductDetails(sku, language, customerId);
    }


    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.addToCart(userID, jsonParams, sessionToken, language);
    }
}
