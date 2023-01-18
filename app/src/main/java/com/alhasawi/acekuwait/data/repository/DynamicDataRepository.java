package com.alhasawi.acekuwait.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.DynamicContentResponse;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.data.api.response.LanguageResponse;
import com.alhasawi.acekuwait.data.api.response.MainCategoryResponse;
import com.alhasawi.acekuwait.data.api.response.OriginCategoryResponse;
import com.alhasawi.acekuwait.data.api.response.StoreResponse;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DynamicDataRepository {

    public MutableLiveData<Resource<LanguageResponse>> getLanguagesLiveData() {
        MutableLiveData<Resource<LanguageResponse>> mutableLiveDataLanguageResponse = new MutableLiveData<>();
        Call<LanguageResponse> languageResponseCall = RetrofitApiClient.getInstance().getApiInterface().getLanguages();
        languageResponseCall.enqueue(new Callback<LanguageResponse>() {
            @Override
            public void onResponse(Call<LanguageResponse> call, Response<LanguageResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataLanguageResponse.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    LanguageResponse languageResponse = response.body();
                    if (languageResponse != null && languageResponse.getData() != null) {
                        mutableLiveDataLanguageResponse.postValue(Resource.success(languageResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<LanguageResponse> call, Throwable t) {
                mutableLiveDataLanguageResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataLanguageResponse;
    }


    public MutableLiveData<Resource<MainCategoryResponse>> getMainCategories(String language) {
        MutableLiveData<Resource<MainCategoryResponse>> mutableLiveDataMainCategoryResponse = new MutableLiveData<>();
        Call<MainCategoryResponse> mainCategoryResponseCall = RetrofitApiClient.getInstance().getApiInterface().getMainCategories(language);
        mainCategoryResponseCall.enqueue(new Callback<MainCategoryResponse>() {
            @Override
            public void onResponse(Call<MainCategoryResponse> call, Response<MainCategoryResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataMainCategoryResponse.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    MainCategoryResponse mainCategoryResponse = response.body();
                    if (mainCategoryResponse != null) {
                        mutableLiveDataMainCategoryResponse.postValue(Resource.success(mainCategoryResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<MainCategoryResponse> call, Throwable t) {
                mutableLiveDataMainCategoryResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataMainCategoryResponse;
    }

    public MutableLiveData<Resource<OriginCategoryResponse>> getOriginCategories(String categoryId, String languageId) {
        MutableLiveData<Resource<OriginCategoryResponse>> mutableLiveDataOriginCategroyResponse = new MutableLiveData<>();
        Call<OriginCategoryResponse> originCategoryResponseCall = RetrofitApiClient.getInstance().getApiInterface().getCategoriesByOrigin(categoryId, languageId);
        originCategoryResponseCall.enqueue(new Callback<OriginCategoryResponse>() {
            @Override
            public void onResponse(Call<OriginCategoryResponse> call, Response<OriginCategoryResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataOriginCategroyResponse.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    OriginCategoryResponse originCategoryResponse = response.body();
                    if (originCategoryResponse != null) {
                        mutableLiveDataOriginCategroyResponse.postValue(Resource.success(originCategoryResponse));
                    }
                }
            }

            @Override
            public void onFailure(Call<OriginCategoryResponse> call, Throwable t) {
                mutableLiveDataOriginCategroyResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataOriginCategroyResponse;
    }

    public MutableLiveData<Resource<HomeResponse>> getHomeData(String languageId) {
        MutableLiveData<Resource<HomeResponse>> homeLiveData = new MutableLiveData<>();
        Call<HomeResponse> homeResponseCall = RetrofitApiClient.getInstance().getApiInterface().getHomeUiData(languageId);
        homeResponseCall.enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                if (response.code() != 200) {
                    homeLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    homeLiveData.postValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                homeLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return homeLiveData;
    }

    public MutableLiveData<Resource<DynamicContentResponse>> getDynamicWebviewContent(String name, String language) {
        MutableLiveData<Resource<DynamicContentResponse>> webviewMutableLiveData = new MutableLiveData<>();
        Call<DynamicContentResponse> aboutUsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getDynamicWebviewContent(name, language);
        aboutUsResponseCall.enqueue(new Callback<DynamicContentResponse>() {
            @Override
            public void onResponse(Call<DynamicContentResponse> call, Response<DynamicContentResponse> response) {
                if (response.code() != 200) {
                    webviewMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    webviewMutableLiveData.postValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<DynamicContentResponse> call, Throwable t) {
                webviewMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return webviewMutableLiveData;
    }

    public MutableLiveData<Resource<StoreResponse>> getStoreList(String language, String token) {
        MutableLiveData<Resource<StoreResponse>> storeMutableLiveData = new MutableLiveData<>();
        Call<StoreResponse> storeResponseCall = RetrofitApiClient.getInstance().getApiInterface().getStoresList(language, "Bearer " + token);
        storeResponseCall.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                if (response.code() != 200) {
                    storeMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    storeMutableLiveData.postValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                storeMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return storeMutableLiveData;
    }


}
