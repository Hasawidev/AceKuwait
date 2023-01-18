package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general;

import static com.alhasawi.acekuwait.utils.AppConstants.ACE_PHONE;
import static com.alhasawi.acekuwait.utils.AppConstants.CALL_PHONE_REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentContactUsBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ContactUsFragment extends BaseFragment {

    FragmentContactUsBinding fragmentContactUsBinding;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected void setup() {
        fragmentContactUsBinding = (FragmentContactUsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentContactUsBinding.tvAceMail.setText(AppConstants.ACE_EMAIL);
        fragmentContactUsBinding.tvAceCall.setText(getResources().getString(R.string.call) + " " + ACE_PHONE);
        fragmentContactUsBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
                dashboardActivity.handleActionMenuBar(true, false);
            }
        });
        fragmentContactUsBinding.tvAceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkPermission(Manifest.permission.CALL_PHONE, CALL_PHONE_REQUEST_CODE);
                runtimePermission(ACE_PHONE);
                try {
                    InAppEvents.logContactUsEvent(
                            "call"
                    );
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        fragmentContactUsBinding.tvAceMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.sendEmailToCustomerCare();
                try {
                    InAppEvents.logContactUsEvent(
                            "email"
                    );
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });


    }

    private void runtimePermission(String phoneNumber) {
        Dexter.withContext(requireContext())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startCall(phoneNumber);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(requireActivity(), "Call Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void startCall(String phoneNumber) {
        String phone = "tel:" + phoneNumber;
        Uri uri = Uri.parse(phone);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        startActivity(intent);
    }


    // Function to check and request permission
    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                dashboardActivity,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_REQUEST_CODE);
        } else {
            dashboardActivity.callAceCustomerCare();
        }
    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dashboardActivity.callAceCustomerCare();
            } else {
                Toast.makeText(dashboardActivity, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
