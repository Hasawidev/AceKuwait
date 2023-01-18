package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.databinding.FragmentNotifyMeBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.RelatedProductsRecyclerviewAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.NotifyViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.LoginSignupDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NotifyMeFragment extends BaseFragment {
    FragmentNotifyMeBinding fragmentNotifyMeBinding;
    NotifyViewModel notifyViewModel;
    DashboardActivity dashboardActivity;
    String userId = "", sessionToken = "";
    RelatedProductsRecyclerviewAdapter productsRecyclerviewAdapter;
    List<Product> productList = new ArrayList<>();
    private String languageId = "";
    private boolean isUserLoggedIn = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_notify_me;
    }

    @Override
    protected void setup() {
        fragmentNotifyMeBinding = (FragmentNotifyMeBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        notifyViewModel = new ViewModelProvider(this).get(NotifyViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentNotifyMeBinding.recyclerviewNotifylist.setLayoutManager(gridLayoutManager);

        if (isUserLoggedIn)
            callGetNotifcationListApi();
        else {
            LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
            loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
        }

        fragmentNotifyMeBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    private void callGetNotifcationListApi() {
        fragmentNotifyMeBinding.progressBar.setVisibility(View.VISIBLE);
        notifyViewModel.getNotifyList(userId, "0", "30", sessionToken, languageId).observe(getActivity(), notifyResponseResource -> {
            fragmentNotifyMeBinding.progressBar.setVisibility(View.GONE);
            switch (notifyResponseResource.status) {
                case SUCCESS:
                    if (notifyResponseResource.data.getStatusCode() == 200) {
                        try {
                            if (notifyResponseResource.data != null) {
                                if (notifyResponseResource.data.getData().getProductList() != null) {
                                    productList = notifyResponseResource.data.getData().getProductList().getProducts();
                                    if (productList.size() == 0)
                                        fragmentNotifyMeBinding.cvEmptyNotify.setVisibility(View.VISIBLE);
                                    else {
                                        fragmentNotifyMeBinding.cvEmptyNotify.setVisibility(View.GONE);
                                        productsRecyclerviewAdapter = new RelatedProductsRecyclerviewAdapter(getContext(), (ArrayList<Product>) productList) {
                                            @Override
                                            public void onLikeClicked(Product product, boolean isWishlisted) {
                                                callWishlistApi(product, isWishlisted);
                                            }

                                            @Override
                                            public void onCartClciked(Product product) {

                                            }

                                            @Override
                                            public void onItemClicked(Product productContent) {
                                                Product selectedProduct = productContent;
                                                Gson gson = new Gson();
                                                String objectString = gson.toJson(selectedProduct);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("selected_product_object", objectString);
                                                dashboardActivity.handleActionMenuBar(false, false);

                                                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);

                                            }
                                        };
                                        fragmentNotifyMeBinding.recyclerviewNotifylist.setAdapter(productsRecyclerviewAdapter);

                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", notifyResponseResource.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", notifyResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
    }


    private void callWishlistApi(Product product, boolean isWishlisted) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        notifyViewModel.addToWishlist(product.getProductId(), userID, sessionToken, languageId).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (wishlistResponse.data.getStatusCode() == 200) {
                        dashboardActivity.showCustomWislistToast(isWishlisted);
                        if (isWishlisted) {
                            try {
                                InAppEvents.logAddToWishlistEvent(product.getDescriptions().get(0).getProductName(),
                                        product.getProductId(), product.getCategories().get(0).getCategoryId(),
                                        product.getOriginalPrice(), product.getSku());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });
    }
}
