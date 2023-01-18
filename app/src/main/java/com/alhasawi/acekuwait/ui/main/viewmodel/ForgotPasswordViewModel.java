package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.ForgotPasswordResponse;
import com.alhasawi.acekuwait.data.repository.UserAuthenticationRepository;

public class ForgotPasswordViewModel extends ViewModel {
    UserAuthenticationRepository userAuthenticationRepository;

    public ForgotPasswordViewModel() {
        this.userAuthenticationRepository = new UserAuthenticationRepository();
    }

    public MutableLiveData<Resource<ForgotPasswordResponse>> forgotPassword(String emailId) {
        return userAuthenticationRepository.forgotPassword(emailId);
    }

    public MutableLiveData<Resource<ForgotPasswordResponse>> verifyEmail(String emailId) {
        return userAuthenticationRepository.verifyEmail(emailId);
    }

    public MutableLiveData<Resource<ForgotPasswordResponse>> resetPassword(String emailId, String newPassword, String confirmPassword) {
        return userAuthenticationRepository.resetPassword(emailId, newPassword, confirmPassword);
    }
}
