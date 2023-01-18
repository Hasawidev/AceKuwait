package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentDynamicContentBinding;
import com.alhasawi.acekuwait.ui.base.AceHardware;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.DynamicContentViewModel;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.bumptech.glide.Glide;

public class PrivatePolicyFragment extends BaseFragment {
    FragmentDynamicContentBinding fragmentDynamicContentBinding;
    DynamicContentViewModel dynamicContentViewModel;
    DashboardActivity dashboardActivity;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dynamic_content;
    }

    @Override
    protected void setup() {
        fragmentDynamicContentBinding = (FragmentDynamicContentBinding) viewDataBinding;
        dynamicContentViewModel = new ViewModelProvider(this).get(DynamicContentViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        fragmentDynamicContentBinding.tvTitle.setText(AceHardware.appContext.getString(R.string.privacy_policy));
        dashboardActivity.handleActionMenuBar(false, false);
        fragmentDynamicContentBinding.imageViewcontent.setImageDrawable(getResources().getDrawable(R.drawable.privacy_policy_big));
        getContent();
        fragmentDynamicContentBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    private void getContent() {
        fragmentDynamicContentBinding.progressBar.setVisibility(View.VISIBLE);
        dynamicContentViewModel.getDynamicWebviewContent(AppConstants.PRIVATE_POLICY, languageId).observe(this, dynamicContentResponseResource -> {
            switch (dynamicContentResponseResource.status) {
                case SUCCESS:
                    if (dynamicContentResponseResource.data.getStatusCode() == 200) {
                        try {
                            fragmentDynamicContentBinding.tvTitle.setText(dynamicContentResponseResource.data.getData().getName());
                            try {
                                Glide.with(getContext())
                                        .load(dynamicContentResponseResource.data.getData().getIcon())
                                        .into(fragmentDynamicContentBinding.imageViewcontent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String htmlContent = dynamicContentResponseResource.data.getData().getFulfilment();
                            final String mimeType = "text/html";
                            final String encoding = "UTF-8";
                            fragmentDynamicContentBinding.webviewDynamicContent.loadData(htmlContent, mimeType, encoding);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", dynamicContentResponseResource.data.getMessage());
                            generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", dynamicContentResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentDynamicContentBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        dashboardActivity.handleActionMenuBar(true, false);
    }
}