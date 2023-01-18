package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.User;
import com.alhasawi.acekuwait.data.api.response.ChangePasswordResponse;
import com.alhasawi.acekuwait.data.api.response.UserProfileResponse;
import com.alhasawi.acekuwait.data.repository.UserAccountRepository;
import com.alhasawi.acekuwait.data.repository.UserAuthenticationRepository;

public class UserProfileViewModel extends ViewModel {

    SavedStateHandle savedStateHandle;
    String userID;
    MutableLiveData<User> user;
    UserAccountRepository userAccountRepository;
    UserAuthenticationRepository userAuthenticationRepository;

    public UserProfileViewModel() {
        userAccountRepository = new UserAccountRepository();
        userAuthenticationRepository = new UserAuthenticationRepository();
    }

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<User>();
        }
        return user;
    }

    public MutableLiveData<Resource<UserProfileResponse>> userProfile(String emailId, String sessionToken, String laguange) {
        return userAccountRepository.userProfile(emailId, sessionToken, laguange);
    }

    public MutableLiveData<Resource<ChangePasswordResponse>> changePassword(String customerId, String oldPassword, String newPassword, String sessiontoken, String laguange) {
        return userAuthenticationRepository.changePaaword(customerId, oldPassword, newPassword, sessiontoken);
    }


    public MutableLiveData<Resource<UserProfileResponse>> editUserProfile(String customerId, String sessiontoken, User editUser, String laguange) {
        return userAccountRepository.editUserProfile(customerId, sessiontoken, editUser, laguange);
    }
}
