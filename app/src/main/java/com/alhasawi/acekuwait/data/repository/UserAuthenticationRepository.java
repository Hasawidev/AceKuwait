package com.alhasawi.acekuwait.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.ChangePasswordResponse;
import com.alhasawi.acekuwait.data.api.response.ForgotPasswordResponse;
import com.alhasawi.acekuwait.data.api.response.LoginResponse;
import com.alhasawi.acekuwait.data.api.response.SignupResponse;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAuthenticationRepository {

    public MutableLiveData<Resource<SignupResponse>> userSignup(Map<String, Object> inputParams) {
        MutableLiveData<Resource<SignupResponse>> signupResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(inputParams)).toString());
        Call<SignupResponse> signupResponseCall = RetrofitApiClient.getInstance().getApiInterface().signup(requestBody);
        signupResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatusCode() == 400)
                        signupResponseMutableLiveData.setValue(Resource.error("Email ID Already Exists", null));
                    else if (response.body().getStatusCode() != 200) {
                        signupResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                    } else if (response.body().getData() != null)
                        signupResponseMutableLiveData.setValue(Resource.success(response.body()));
                } else {
                    signupResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                }


            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                signupResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return signupResponseMutableLiveData;
    }

    public MutableLiveData<Resource<LoginResponse>> userLogin(Map<String, Object> inputParams) {
        MutableLiveData<Resource<LoginResponse>> loginResponseMutableLiveData = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(inputParams)).toString());
        Call<LoginResponse> loginResponseCall = RetrofitApiClient.getInstance().getApiInterface().login(requestBody);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() != 200) {
                    loginResponseMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null)
                    loginResponseMutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResponseMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return loginResponseMutableLiveData;
    }

    public MutableLiveData<Resource<ChangePasswordResponse>> changePaaword(String customerId, String oldPassword, String newPassword, String sessiontoken) {
        MutableLiveData<Resource<ChangePasswordResponse>> changePswdMutableLiveData = new MutableLiveData<>();
        Call<ChangePasswordResponse> changePasswordResponseCall = RetrofitApiClient.getInstance().getApiInterface().changePassword(customerId, oldPassword, newPassword, "Bearer " + sessiontoken);
        changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.code() != 200) {
                    changePswdMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null)
                    changePswdMutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                changePswdMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return changePswdMutableLiveData;
    }

    public MutableLiveData<Resource<ForgotPasswordResponse>> forgotPassword(String emailId) {
        MutableLiveData<Resource<ForgotPasswordResponse>> forgotPswdMutableLiveData = new MutableLiveData<>();
        Call<ForgotPasswordResponse> forgotPasswordResponseCall = RetrofitApiClient.getInstance().getApiInterface().forgotPassword(emailId);
        forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.code() != 200) {
                    forgotPswdMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null)
                    forgotPswdMutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                forgotPswdMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return forgotPswdMutableLiveData;
    }


    public MutableLiveData<Resource<ForgotPasswordResponse>> verifyEmail(String emailId) {
        MutableLiveData<Resource<ForgotPasswordResponse>> forgotPswdMutableLiveData = new MutableLiveData<>();
        Call<ForgotPasswordResponse> forgotPasswordResponseCall = RetrofitApiClient.getInstance().getApiInterface().verifyEmail(emailId);
        forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.code() != 200) {
                    forgotPswdMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null)
                    forgotPswdMutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                forgotPswdMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return forgotPswdMutableLiveData;
    }

    public MutableLiveData<Resource<ForgotPasswordResponse>> resetPassword(String emailId, String newPassword, String confirmPassword) {
        MutableLiveData<Resource<ForgotPasswordResponse>> forgotPswdMutableLiveData = new MutableLiveData<>();
        Call<ForgotPasswordResponse> forgotPasswordResponseCall = RetrofitApiClient.getInstance().getApiInterface().resetPassowrd(emailId, newPassword, confirmPassword);
        forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                if (response.code() != 200) {
                    forgotPswdMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null)
                    forgotPswdMutableLiveData.setValue(Resource.success(response.body()));
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                forgotPswdMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return forgotPswdMutableLiveData;
    }
}
