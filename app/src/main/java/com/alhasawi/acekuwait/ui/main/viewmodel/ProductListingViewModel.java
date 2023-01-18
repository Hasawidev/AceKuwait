package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.FilterAttributeValues;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductDataFactory;
import com.alhasawi.acekuwait.utils.NetworkState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductListingViewModel extends ViewModel {

    private ProductRepository productRepository;
    //Pagination
    private Executor executor;
    private LiveData<NetworkState> networkState;
    private LiveData<PagedList<Product>> productPaginationLiveData;
    private LiveData<Map<String, List<FilterAttributeValues>>> filterAttributeMap;
    private LiveData<List<String>> sortStrings;
    private LiveData<String> isPreferenceLiveData;
    private LiveData<Integer> totalProductsFound;


    public ProductListingViewModel() {
        productRepository = new ProductRepository();
    }

    public void fetchProductData(String inputPayload) {
        if (executor == null)
            executor = Executors.newFixedThreadPool(5);

        ProductDataFactory productDataFactory = new ProductDataFactory(inputPayload);
        networkState = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getNetworkState());
        Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getFilterAttributeMap());
        filterAttributeMap = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getFilterAttributeMap());
        sortStrings = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getSortStrings());
        isPreferenceLiveData = Transformations.switchMap(productDataFactory.getMutableLiveData(),
                dataSource -> dataSource.getIsPreferenceLiveData());
        totalProductsFound = Transformations.switchMap(productDataFactory.getMutableLiveData(), dataSource -> dataSource.getTotalProductsFound());

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(30)
                        .setPageSize(30).build();

        productPaginationLiveData = (new LivePagedListBuilder(productDataFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();

    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<Product>> getProductPaginationLiveData() {
        return productPaginationLiveData;
    }

    public void invalidateProductLiveData() {
        productPaginationLiveData = null;
    }

    public LiveData<Map<String, List<FilterAttributeValues>>> getFilterAttributeMap() {
        return filterAttributeMap;
    }

    public LiveData<List<String>> getSortStrings() {
        return sortStrings;
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken, String language) {
        return productRepository.addToWishlist(productID, userID, sessionToken, language);
    }

    public LiveData<String> getIsPreferenceLiveData() {
        return isPreferenceLiveData;
    }

    public LiveData<Integer> getTotalProductsFound() {
        return totalProductsFound;
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.addToCart(userID, jsonParams, sessionToken, language);
    }
}