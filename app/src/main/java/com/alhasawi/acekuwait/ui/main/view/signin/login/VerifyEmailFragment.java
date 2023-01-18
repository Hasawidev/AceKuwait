package com.alhasawi.acekuwait.ui.main.view.signin.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentVerifyEmailBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ForgotPasswordViewModel;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;


public class VerifyEmailFragment extends BaseFragment {
    FragmentVerifyEmailBinding fragmentVerifyEmailBinding;
    ForgotPasswordViewModel forgotPasswordViewModel;
    SigninActivity signinActivity;
    CountDownTimer cTimer = null;
    String email = "";
    boolean isVerifyButtonClicked = false;
    private Gson gson = new Gson();
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 5000;
    private GeneralDialog generalDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_verify_email;
    }

    @Override
    protected void setup() {

        fragmentVerifyEmailBinding = (FragmentVerifyEmailBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        forgotPasswordViewModel = new ViewModelProvider(getActivity()).get(ForgotPasswordViewModel.class);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("email"))
                email = bundle.getString("email");
        }

        String maskedEmail = email.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*");
        fragmentVerifyEmailBinding.edtEmail.setText(maskedEmail);

        fragmentVerifyEmailBinding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callResendMailApi(email);
                isVerifyButtonClicked = false;
            }
        });

        fragmentVerifyEmailBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVerifyButtonClicked = true;
                callVerifyEmailApi(email);
                //goToReset();
            }
        });

        startTimer();

        /*new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                callVerifyEmailApi(email);
            }
        }, 5000);*/
    }

    public void startTimer() {
        fragmentVerifyEmailBinding.progressBar.setVisibility(View.VISIBLE);
        cTimer = new CountDownTimer(3 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                fragmentVerifyEmailBinding.tvtimer.setText("(0:" + millisUntilFinished / 1000 + ")");
                //callVerifyEmailApi(email);
            }

            public void onFinish() {
                fragmentVerifyEmailBinding.progressBar.setVisibility(View.GONE);
                fragmentVerifyEmailBinding.tvtimer.setText("0:00");
                fragmentVerifyEmailBinding.tvtimer.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();
    }

    public void cancelTimer() {
        fragmentVerifyEmailBinding.progressBar.setVisibility(View.GONE);
        if (cTimer != null)
            cTimer.cancel();

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                callVerifyEmailApi(email);
            }
        }, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelTimer();
    }

    private void callVerifyEmailApi(String email) {
        if (isVerifyButtonClicked)
            fragmentVerifyEmailBinding.progressBar.setVisibility(View.VISIBLE);
        forgotPasswordViewModel.verifyEmail(email).observe(getActivity(), forgotPasswordResponseResource -> {
            //Log.e("callVerifyEmail", gson.toJson(forgotPasswordResponseResource));
            if (isVerifyButtonClicked)
                fragmentVerifyEmailBinding.progressBar.setVisibility(View.GONE);

            switch (forgotPasswordResponseResource.status) {
                case SUCCESS:
                    if (forgotPasswordResponseResource.data.getStatusCode() == 200) {
                        if (forgotPasswordResponseResource.data.getData().getCustomer() != null) {
                            if (forgotPasswordResponseResource.data.getData().getCustomer().getFpconfirm()) {
                                signinActivity.replaceFragment(new ResetPasswordFragment(), getArguments(), false, false);
                            }
                        }
                    } else {
                        dismissDialog();
                        //generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.data.getMessage());
                        //generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        dismissDialog();
                        generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.message);
                        generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        });

    }

    private void callResendMailApi(String email) {
        fragmentVerifyEmailBinding.progressBar.setVisibility(View.VISIBLE);
        forgotPasswordViewModel.forgotPassword(email).observe(getActivity(), forgotPasswordResponseResource -> {
            switch (forgotPasswordResponseResource.status) {
                case SUCCESS:
                    if (forgotPasswordResponseResource.data.getStatusCode() == 200) {
                        if (forgotPasswordResponseResource.data.getData() != null) {
                            startTimer();
                        }
                    } else {
                        dismissDialog();
                        generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    fragmentVerifyEmailBinding.progressBar.setVisibility(View.GONE);
                    dismissDialog();
                    generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.message);
                    generalDialog.show(getChildFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });

    }

    private void dismissDialog() {
        if (generalDialog != null) {
            generalDialog.dismiss();
        }
    }

    private void goToReset() {
        signinActivity.replaceFragment(new ResetPasswordFragment(), getArguments(), false, false);
    }

}
