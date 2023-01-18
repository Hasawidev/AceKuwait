package com.alhasawi.acekuwait.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.OrderHistoryResponse;
import com.alhasawi.acekuwait.data.api.response.OrderResponse;
import com.alhasawi.acekuwait.data.api.response.OrderReturnResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnPolicyResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnReasonResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnResponse;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    public MutableLiveData<Resource<OrderResponse>> orderConfirmation(String userID, String cartId, String sessionToken, Map<String, Object> jsonParams, String language) {
        String jsonParamString = new JSONObject(jsonParams).toString();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParamString);
        MutableLiveData<Resource<OrderResponse>> orderMutableLiveData = new MutableLiveData<>();
        Call<OrderResponse> orderResponseCall = RetrofitApiClient.getInstance().getApiInterface().orderConfirmation(userID, cartId, "Bearer " + sessionToken, requestBody, language);
        orderResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.code() != 200) {
                    orderMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    orderMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                orderMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return orderMutableLiveData;
    }

    public MutableLiveData<Resource<OrderHistoryResponse>> orderHistory(String customerId, String sessionToken, String language) {
        MutableLiveData<Resource<OrderHistoryResponse>> orderHistoryMutableLiveData = new MutableLiveData<>();
        Call<OrderHistoryResponse> orderHistoryResponseCall = RetrofitApiClient.getInstance().getApiInterface().orderHistory(customerId, "Bearer " + sessionToken, language);
        orderHistoryResponseCall.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.code() != 200) {
                    orderHistoryMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    orderHistoryMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                orderHistoryMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return orderHistoryMutableLiveData;
    }

    public MutableLiveData<Resource<ReturnResponse>> getEligibleReturnOrders(String customerId, String sessionToken, String language) {
        MutableLiveData<Resource<ReturnResponse>> returnMutableLiveData = new MutableLiveData<>();
        Call<ReturnResponse> returnResponseCall = RetrofitApiClient.getInstance().getApiInterface().getElibileReturnOrders(customerId, "Bearer " + sessionToken, language);
        returnResponseCall.enqueue(new Callback<ReturnResponse>() {
            @Override
            public void onResponse(Call<ReturnResponse> call, Response<ReturnResponse> response) {
                if (response.code() != 200) {
                    returnMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    returnMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<ReturnResponse> call, Throwable t) {
                returnMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return returnMutableLiveData;
    }

    public MutableLiveData<Resource<OrderReturnResponse>> orderReturn(String order, String sessionToken, String language) {
        MutableLiveData<Resource<OrderReturnResponse>> fullOrderReturnMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), order);
        Call<OrderReturnResponse> returnResponseCall = RetrofitApiClient.getInstance().getApiInterface().orderReturn(requestBody, "Bearer " + sessionToken, language);
        returnResponseCall.enqueue(new Callback<OrderReturnResponse>() {
            @Override
            public void onResponse(Call<OrderReturnResponse> call, Response<OrderReturnResponse> response) {
                if (response.code() != 200) {
                    fullOrderReturnMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    fullOrderReturnMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<OrderReturnResponse> call, Throwable t) {
                fullOrderReturnMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return fullOrderReturnMutableLiveData;
    }

    public MutableLiveData<Resource<ReturnPolicyResponse>> getReturnPolicy(String language) {
        MutableLiveData<Resource<ReturnPolicyResponse>> returnPolicyMutableLiveData = new MutableLiveData<>();
        Call<ReturnPolicyResponse> returnPolicyResponseCall = RetrofitApiClient.getInstance().getApiInterface().getReturnPolicy(language);
        returnPolicyResponseCall.enqueue(new Callback<ReturnPolicyResponse>() {
            @Override
            public void onResponse(Call<ReturnPolicyResponse> call, Response<ReturnPolicyResponse> response) {
                if (response.code() != 200) {
                    returnPolicyMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    returnPolicyMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<ReturnPolicyResponse> call, Throwable t) {
                returnPolicyMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return returnPolicyMutableLiveData;
    }

    public MutableLiveData<Resource<ReturnReasonResponse>> getReturnReasons(String language) {
        MutableLiveData<Resource<ReturnReasonResponse>> returnReasonMutableLiveData = new MutableLiveData<>();
        Call<ReturnReasonResponse> returnReasonResponseCall = RetrofitApiClient.getInstance().getApiInterface().getReturnReasons(language);
        returnReasonResponseCall.enqueue(new Callback<ReturnReasonResponse>() {
            @Override
            public void onResponse(Call<ReturnReasonResponse> call, Response<ReturnReasonResponse> response) {
                if (response.code() != 200) {
                    returnReasonMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    returnReasonMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<ReturnReasonResponse> call, Throwable t) {
                returnReasonMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return returnReasonMutableLiveData;
    }

}
