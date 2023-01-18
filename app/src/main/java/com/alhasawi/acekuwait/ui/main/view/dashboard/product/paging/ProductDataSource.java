package com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.data.api.model.pojo.FilterAttributeValues;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.response.ProductResponse;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;
import com.alhasawi.acekuwait.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataSource extends PageKeyedDataSource<Long, Product> {

    private static final String TAG = ProductDataSource.class.getSimpleName();

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private MutableLiveData<Integer> totalProductsFound;
    private MutableLiveData<Map<String, List<FilterAttributeValues>>> filterAttributeMap;
    private MutableLiveData<List<String>> sortStrings;
    private MutableLiveData<List<Category>> categoryList;
    private MutableLiveData<String> isPreferenceLiveData;
    private String page_no = "0", inputPayload = "";
    private boolean loadedInitial = false;
    private String languageId = "en";

    public ProductDataSource(String inputPayload) {
        this.inputPayload = inputPayload;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        filterAttributeMap = new MutableLiveData<>();
        sortStrings = new MutableLiveData<>();
        categoryList = new MutableLiveData<>();
        isPreferenceLiveData = new MutableLiveData<>();
        totalProductsFound = new MutableLiveData<>();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    public MutableLiveData<String> getIsPreferenceLiveData() {
        return isPreferenceLiveData;
    }

    public MutableLiveData<Integer> getTotalProductsFound() {
        return totalProductsFound;
    }

    public MutableLiveData<Map<String, List<FilterAttributeValues>>> getFilterAttributeMap() {
        return filterAttributeMap;
    }

    public MutableLiveData<List<String>> getSortStrings() {
        return sortStrings;
    }

    public MutableLiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, Product> callback) {

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), inputPayload);

        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, page_no, languageId);
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    loadedInitial = true;
                    if (response.isSuccessful()) {
                        if (response.body().getData().getFilterAttributes() != null) {
                            filterAttributeMap.postValue(response.body().getData().getFilterAttributes());
                        } else {
                            filterAttributeMap.postValue(null);
                        }
                        if (response.body().getData().getSortStrings() != null) {
                            sortStrings.postValue(response.body().getData().getSortStrings());
                        } else {
                            sortStrings.postValue(null);
                        }
                        if (response.body().getData().getCategories() != null) {
                            categoryList.postValue(response.body().getData().getCategories());
                        } else {
                            categoryList.postValue(null);
                        }
                        if (response.body().getData().getProductList() == null) {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        } else {
                            List<Product> productList = new ArrayList<>();
                            if (response.body().getData().getPreferenceProductList().size() != 0) {
                                productList = response.body().getData().getPreferenceProductList();
                                productList.addAll(response.body().getData().getProductList().getProducts());
                            } else {
                                productList = response.body().getData().getProductList().getProducts();
                            }
                            totalProductsFound.postValue(response.body().getData().getProductList().getTotalElements());

                            if (productList.size() > 0) {
                                callback.onResult(productList, null, 1l);
                                initialLoading.postValue(NetworkState.LOADED);
                                networkState.postValue(NetworkState.LOADED);
                            } else
                                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));


                        }
                        if (response.body().getData().getProductList().getPageable().getPageNumber() == response.body().getData().getProductList().getPageable().getPageSize()) {
                            isPreferenceLiveData.postValue("no");
                        } else {
                            isPreferenceLiveData.postValue("yes");
                        }
                    } else {
                        initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });

    }


    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, Product> callback) {

    }


    /*
     * Step 3: This method it is responsible for the subsequent call to load the data page wise.
     * This method is executed in the background thread
     * We are fetching the next page data from the api
     * and passing it via the callback method to the UI.
     * The "params.key" variable will have the updated value.
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, Product> callback) {

        Log.i(TAG, "Loading Rang " + params.key + " Count " + params.requestedLoadSize);
        if (loadedInitial)
            networkState.postValue(NetworkState.LOADING);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), inputPayload);
        Call<ProductResponse> productsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductsList(body, params.key + "", languageId);
        productsResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                /*
                 * If the request is successful, then we will update the callback
                 * with the latest feed items and
                 * "params.key+1" -> set the next key for the next iteration.
                 */
                try {
                    if (response != null)
                        if (response.isSuccessful()) {
                            long nextKey = 0;
                            if (response.body() != null && response.body().getData() != null && response.body().getData().getProductList() != null)
                                nextKey = response.body().getData().getProductList().getPageable().getPageNumber() + 1;
//                    long nextKey = (params.key == response.body().getData().getProducts().getPageable().getPageNumber()) ? -1 : params.key + 1;
                            List<Product> productList = new ArrayList<>();
                            if (response.body().getData().getPreferenceProductList().size() != 0) {
                                productList = response.body().getData().getPreferenceProductList();
                                productList.addAll(response.body().getData().getProductList().getProducts());
                            } else {
                                productList = response.body().getData().getProductList().getProducts();
                            }

                            callback.onResult(productList, nextKey);
                            networkState.postValue(NetworkState.LOADED);
                            if (response.body().getData().getFilterAttributes() != null) {
                                filterAttributeMap.postValue(response.body().getData().getFilterAttributes());
                            } else {
                                filterAttributeMap.postValue(null);
                            }
                            if (response.body().getData().getSortStrings() != null) {
                                sortStrings.postValue(response.body().getData().getSortStrings());
                            } else {
                                sortStrings.postValue(null);
                            }
                            if (response.body().getData().getProductList().getPageable().getPageNumber() == response.body().getData().getProductList().getPageable().getPageSize()) {
                                isPreferenceLiveData.postValue("no");
                            } else {
                                isPreferenceLiveData.postValue("yes");
                            }


                        } else
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                String errorMessage = t == null ? "unknown error" : t.getMessage();
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
            }
        });

    }


}
