package com.alhasawi.acekuwait.data.repository;

import android.util.ArrayMap;

import androidx.lifecycle.MutableLiveData;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductListResponse;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.FilterResponse;
import com.alhasawi.acekuwait.data.api.response.GetNotifyListResponse;
import com.alhasawi.acekuwait.data.api.response.NotifyResponse;
import com.alhasawi.acekuwait.data.api.response.ProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.SearchResponse;
import com.alhasawi.acekuwait.data.api.response.SearchedProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    public static RequestBody addInputParams(String categoryId, JSONArray filterArray) {
        JSONArray categoryIds = new JSONArray();
        categoryIds.put(categoryId);
        if (filterArray == null)
            filterArray = new JSONArray();
        JSONArray brandIds = new JSONArray();
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("categoryIds", categoryIds);
        jsonParams.put("attributeIds", filterArray);
        jsonParams.put("brandIds", brandIds);


        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
    }


    public MutableLiveData<Resource<ProductDetailsResponse>> getProductDetails(String productName, String language) {
        MutableLiveData<Resource<ProductDetailsResponse>> mutableLiveDataProductDetailsResponse = new MutableLiveData<>();
        Call<ProductDetailsResponse> productDetailsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getProductDetails(productName);
        productDetailsResponseCall.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {
                if (response.code() != 200) {
                    mutableLiveDataProductDetailsResponse.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    mutableLiveDataProductDetailsResponse.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                mutableLiveDataProductDetailsResponse.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return mutableLiveDataProductDetailsResponse;
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String query, String language) {
        MutableLiveData<Resource<SearchProductListResponse>> searchProductListResponseMutableLiveData = new MutableLiveData<>();
        Call<SearchProductListResponse> searchProductListResponseCall = RetrofitApiClient.getInstance().getApiInterface().searchProducts(query, language);
        searchProductListResponseCall.enqueue(new Callback<SearchProductListResponse>() {
            @Override
            public void onResponse(Call<SearchProductListResponse> call, Response<SearchProductListResponse> response) {
                if (response.code() != 200) {
                    searchProductListResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    searchProductListResponseMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SearchProductListResponse> call, Throwable t) {
                searchProductListResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return searchProductListResponseMutableLiveData;
    }

    public MutableLiveData<Resource<SearchResponse>> getSearchResults(String query, String category, String language) {
        MutableLiveData<Resource<SearchResponse>> searchResultsMutableLiveData = new MutableLiveData<>();
        Call<SearchResponse> searchResponseCall = RetrofitApiClient.getInstance().getApiInterface().getSearchResults(query, category, language);
        searchResponseCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.code() != 200) {
                    searchResultsMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    searchResultsMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                searchResultsMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return searchResultsMutableLiveData;
    }

    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String sku, String language, String customerId) {
        MutableLiveData<Resource<SearchedProductDetailsResponse>> searchedProductDetailsResponseMutableLiveData = new MutableLiveData<>();
        Call<SearchedProductDetailsResponse> searchedProductDetailsResponseCall = RetrofitApiClient.getInstance().getApiInterface().getSearchedProductDetails(sku, language, customerId);
        searchedProductDetailsResponseCall.enqueue(new Callback<SearchedProductDetailsResponse>() {
            @Override
            public void onResponse(Call<SearchedProductDetailsResponse> call, Response<SearchedProductDetailsResponse> response) {
                if (response.code() != 200) {
                    searchedProductDetailsResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    searchedProductDetailsResponseMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<SearchedProductDetailsResponse> call, Throwable t) {
                searchedProductDetailsResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return searchedProductDetailsResponseMutableLiveData;
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productId, String userID, String sessionToken, String language) {

        MutableLiveData<Resource<WishlistResponse>> wishlistResponseMutableLiveData = new MutableLiveData<>();
        Call<WishlistResponse> wishlistResponseCall = RetrofitApiClient.getInstance().getApiInterface().addToWishlist(userID, productId, "Bearer " + sessionToken);
        wishlistResponseCall.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                if (response.code() != 200) {
                    wishlistResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    wishlistResponseMutableLiveData.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {

                wishlistResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return wishlistResponseMutableLiveData;
    }

    public MutableLiveData<Resource<WishlistResponse>> getWishListedProducts(String userID, String sessionToken, String language) {

        MutableLiveData<Resource<WishlistResponse>> wishListItemsLiveData = new MutableLiveData<>();
        Call<WishlistResponse> wishlistResponseCall = RetrofitApiClient.getInstance().getApiInterface().getWishlistedProducts(userID, "Bearer " + sessionToken, language);
        wishlistResponseCall.enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(Call<WishlistResponse> call, Response<WishlistResponse> response) {
                if (response.code() != 200) {
                    wishListItemsLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    wishListItemsLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<WishlistResponse> call, Throwable t) {
                wishListItemsLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return wishListItemsLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        MutableLiveData<Resource<CartResponse>> addToCartResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);

        Call<CartResponse> addToCartResponseCall = RetrofitApiClient.getInstance().getApiInterface().addToCart(userID, requestBody, "Bearer " + sessionToken, language);
        addToCartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    addToCartResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    addToCartResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                addToCartResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return addToCartResponseMutableLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> updateCartItems(String userID, String jsonParams, String sessionToken, String language) {
        MutableLiveData<Resource<CartResponse>> updateCartResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);

        Call<CartResponse> updateCartResponseCall = RetrofitApiClient.getInstance().getApiInterface().updateCartItems(userID, requestBody, "Bearer " + sessionToken, language);
        updateCartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    updateCartResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    updateCartResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                updateCartResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return updateCartResponseMutableLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> removeFromCart(String userID, String shoppingCartItemId, String sessionToken, String language) {
        MutableLiveData<Resource<CartResponse>> addToCartResponseMutableLiveData = new MutableLiveData<>();
        Call<CartResponse> addToCartResponseCall = RetrofitApiClient.getInstance().getApiInterface().removeFromCart(userID, shoppingCartItemId, "Bearer " + sessionToken, language);
        addToCartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    addToCartResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    addToCartResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                addToCartResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return addToCartResponseMutableLiveData;
    }

    public MutableLiveData<Resource<CartResponse>> getCartItems(String userID, String sessionToken, String language, boolean isAlertClicked) {
        MutableLiveData<Resource<CartResponse>> cartItemsMutableLiveData = new MutableLiveData<>();
        Call<CartResponse> cartResponseCall = RetrofitApiClient.getInstance().getApiInterface().getCartItems(userID, "Bearer " + sessionToken, language, isAlertClicked);
        cartResponseCall.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.code() != 200) {
                    cartItemsMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    cartItemsMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                cartItemsMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return cartItemsMutableLiveData;
    }

    public MutableLiveData<Resource<FilterResponse>> getFilters(String inputParams, String language) {
        MutableLiveData<Resource<FilterResponse>> filterMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), inputParams);
        Call<FilterResponse> filterResponseCall = RetrofitApiClient.getInstance().getApiInterface().getFilters(requestBody, language);
        filterResponseCall.enqueue(new Callback<FilterResponse>() {
            @Override
            public void onResponse(Call<FilterResponse> call, Response<FilterResponse> response) {
                if (response.code() != 200) {
                    filterMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    filterMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<FilterResponse> call, Throwable t) {
                filterMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return filterMutableLiveData;
    }

    public MutableLiveData<Resource<NotifyResponse>> addToNotifyList(String jsonParams, String sessionToken, String language) {
        MutableLiveData<Resource<NotifyResponse>> addToNotifyResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);

        Call<NotifyResponse> addToNotifyResponseCall = RetrofitApiClient.getInstance().getApiInterface().addToNotifyList(requestBody, "Bearer " + sessionToken);
        addToNotifyResponseCall.enqueue(new Callback<NotifyResponse>() {
            @Override
            public void onResponse(Call<NotifyResponse> call, Response<NotifyResponse> response) {
                if (response.code() != 200) {
                    addToNotifyResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    addToNotifyResponseMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<NotifyResponse> call, Throwable t) {
                addToNotifyResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return addToNotifyResponseMutableLiveData;
    }

    public MutableLiveData<Resource<GetNotifyListResponse>> getNotifyList(String customerId, String page, String size, String sessionToken, String language) {
        MutableLiveData<Resource<GetNotifyListResponse>> notifyListMutableLiveData = new MutableLiveData<>();

        Call<GetNotifyListResponse> getNotifyResponseCall = RetrofitApiClient.getInstance().getApiInterface().getNotifyList(customerId, page, size, "Bearer " + sessionToken, language);
        getNotifyResponseCall.enqueue(new Callback<GetNotifyListResponse>() {
            @Override
            public void onResponse(Call<GetNotifyListResponse> call, Response<GetNotifyListResponse> response) {
                if (response.code() != 200) {
                    notifyListMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    notifyListMutableLiveData.setValue(Resource.success(response.body()));
                }

            }

            @Override
            public void onFailure(Call<GetNotifyListResponse> call, Throwable t) {
                notifyListMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return notifyListMutableLiveData;
    }
}
