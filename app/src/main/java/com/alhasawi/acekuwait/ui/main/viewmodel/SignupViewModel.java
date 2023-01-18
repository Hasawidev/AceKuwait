package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.SignupResponse;
import com.alhasawi.acekuwait.data.repository.UserAuthenticationRepository;

import java.util.Map;

public class SignupViewModel extends ViewModel {
    UserAuthenticationRepository userAuthenticationRepository;

    public SignupViewModel() {
        this.userAuthenticationRepository = new UserAuthenticationRepository();
    }

    public MutableLiveData<Resource<SignupResponse>> userRegistration(Map<String, Object> inputParamsMap) {
        return userAuthenticationRepository.userSignup(inputParamsMap);
    }
}
