package com.alhasawi.acekuwait.data.repository;import androidx.lifecycle.MutableLiveData;import com.alhasawi.acekuwait.data.api.model.Resource;import com.alhasawi.acekuwait.data.api.model.User;import com.alhasawi.acekuwait.data.api.response.AddressResponse;import com.alhasawi.acekuwait.data.api.response.DeleteAddressResponse;import com.alhasawi.acekuwait.data.api.response.GetAllAddressResponse;import com.alhasawi.acekuwait.data.api.response.UserProfileResponse;import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;import com.google.gson.Gson;import org.json.JSONObject;import java.util.Locale;import java.util.Map;import okhttp3.RequestBody;import retrofit2.Call;import retrofit2.Callback;import retrofit2.Response;public class UserAccountRepository {    String language = Locale.getDefault().getLanguage();    public MutableLiveData<Resource<AddressResponse>> addNewAddress(String userID, Map<String, Object> jsonParams, String sessionToken, String language) {        MutableLiveData<Resource<AddressResponse>> addResourceMutableLiveData = new MutableLiveData<>();        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());        Call<AddressResponse> addressResponseCall = RetrofitApiClient.getInstance().getApiInterface().addNewAddress(userID, requestBody, "Bearer " + sessionToken, language);        addressResponseCall.enqueue(new Callback<AddressResponse>() {            @Override            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {                if (response.code() != 200) {                    addResourceMutableLiveData.setValue(Resource.error(response.message(), null));                } else if (response.body() != null) {                    addResourceMutableLiveData.setValue(Resource.success(response.body()));                }            }            @Override            public void onFailure(Call<AddressResponse> call, Throwable t) {                addResourceMutableLiveData.setValue(Resource.error(t.getMessage(), null));            }        });        return addResourceMutableLiveData;    }    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userID, String sessionToken, String language) {        MutableLiveData<Resource<GetAllAddressResponse>> getAllAddressMutableLiveData = new MutableLiveData<>();        Call<GetAllAddressResponse> getAllAddressResponseCall = RetrofitApiClient.getInstance().getApiInterface().getAddresses(userID, "Bearer " + sessionToken, language);        getAllAddressResponseCall.enqueue(new Callback<GetAllAddressResponse>() {            @Override            public void onResponse(Call<GetAllAddressResponse> call, Response<GetAllAddressResponse> response) {                if (response.code() != 200) {                    getAllAddressMutableLiveData.setValue(Resource.error(response.message(), null));                } else if (response.body() != null) {                    getAllAddressMutableLiveData.setValue(Resource.success(response.body()));                }            }            @Override            public void onFailure(Call<GetAllAddressResponse> call, Throwable t) {                getAllAddressMutableLiveData.setValue(Resource.error(t.getMessage(), null));            }        });        return getAllAddressMutableLiveData;    }    public MutableLiveData<Resource<AddressResponse>> editAddress(String userID, String addressId, String sessionToken, Map<String, Object> jsonParams, String language) {        MutableLiveData<Resource<AddressResponse>> editAddressMutableLiveData = new MutableLiveData<>();        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());        Call<AddressResponse> editAddressResponseCall = RetrofitApiClient.getInstance().getApiInterface().editAddress(userID, addressId, "Bearer " + sessionToken, requestBody);        editAddressResponseCall.enqueue(new Callback<AddressResponse>() {            @Override            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {                if (response.code() != 200) {                    editAddressMutableLiveData.setValue(Resource.error(response.message(), null));                } else if (response.body() != null) {                    editAddressMutableLiveData.setValue(Resource.success(response.body()));                }            }            @Override            public void onFailure(Call<AddressResponse> call, Throwable t) {                editAddressMutableLiveData.setValue(Resource.error(t.getMessage(), null));            }        });        return editAddressMutableLiveData;    }    public MutableLiveData<Resource<DeleteAddressResponse>> deleteAddress(String addressId, String sessionToken, String language) {        MutableLiveData<Resource<DeleteAddressResponse>> editAddressMutableLiveData = new MutableLiveData<>();        Call<DeleteAddressResponse> deleteAddressResponseCall = RetrofitApiClient.getInstance().getApiInterface().deleteAddress(addressId, "Bearer " + sessionToken);        deleteAddressResponseCall.enqueue(new Callback<DeleteAddressResponse>() {            @Override            public void onResponse(Call<DeleteAddressResponse> call, Response<DeleteAddressResponse> response) {                if (response.code() != 200) {                    editAddressMutableLiveData.setValue(Resource.error(response.message(), null));                } else if (response.code() == 200) {                    editAddressMutableLiveData.setValue(Resource.success(response.body()));                }            }            @Override            public void onFailure(Call<DeleteAddressResponse> call, Throwable t) {                editAddressMutableLiveData.setValue(Resource.error(t.getMessage(), null));            }        });        return editAddressMutableLiveData;    }    public MutableLiveData<Resource<UserProfileResponse>> userProfile(String emailId, String sessionToken, String language) {        MutableLiveData<Resource<UserProfileResponse>> userProfileMutableLiveData = new MutableLiveData<>();        Call<UserProfileResponse> userProfileResponseCall = RetrofitApiClient.getInstance().getApiInterface().userProfile(emailId, "Bearer " + sessionToken, language);        userProfileResponseCall.enqueue(new Callback<UserProfileResponse>() {            @Override            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {                if (response.code() != 200) {                    userProfileMutableLiveData.setValue(Resource.error(response.message(), null));                } else if (response.body() != null) {                    userProfileMutableLiveData.setValue(Resource.success(response.body()));                }            }            @Override            public void onFailure(Call<UserProfileResponse> call, Throwable t) {                userProfileMutableLiveData.setValue(Resource.error(t.getMessage(), null));            }        });        return userProfileMutableLiveData;    }    public MutableLiveData<Resource<UserProfileResponse>> editUserProfile(String customerId, String sessionToken, User editUser, String language) {        MutableLiveData<Resource<UserProfileResponse>> userProfileMutableLiveData = new MutableLiveData<>();        String jsonparams = new Gson().toJson(editUser);        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonparams);        Call<UserProfileResponse> userProfileResponseCall = RetrofitApiClient.getInstance().getApiInterface().editUserProfile(customerId, "Bearer " + sessionToken, requestBody);        userProfileResponseCall.enqueue(new Callback<UserProfileResponse>() {            @Override            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {                if (response.code() != 200) {                    userProfileMutableLiveData.setValue(Resource.error(response.message(), null));                } else if (response.body() != null) {                    if (response.body().getStatusCode() == 200)                        userProfileMutableLiveData.setValue(Resource.success(response.body()));                    else                        userProfileMutableLiveData.setValue(Resource.error(response.message(), null));                }            }            @Override            public void onFailure(Call<UserProfileResponse> call, Throwable t) {                userProfileMutableLiveData.setValue(Resource.error(t.getMessage(), null));            }        });        return userProfileMutableLiveData;    }}