package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.model.pojo.Wishlist;
import com.alhasawi.acekuwait.databinding.FragmentWishlistBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.WishListAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.WishListViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.CartDialog;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WishListFragment extends BaseFragment {
    FragmentWishlistBinding fragmentWishlistBinding;
    WishListAdapter wishListAdapter;
    DashboardActivity dashboardActivity;
    WishListViewModel wishListViewModel;
    List<Wishlist> wishListItems = new ArrayList<>();
    String userID = "";
    String sessionToken;
    boolean isUserLoggedIn = false;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_wishlist;
    }

    @Override
    protected void setup() {
        fragmentWishlistBinding = (FragmentWishlistBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        wishListViewModel = new ViewModelProvider(dashboardActivity).get(WishListViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentWishlistBinding.recyclerviewWishlist.setLayoutManager(gridLayoutManager);
        wishListAdapter = new WishListAdapter(getContext()) {
            @Override
            public void onWishListClicked(Product selectedProduct, boolean isChecked, int position) {
                callAddRemoveWishlistApi(selectedProduct, isChecked, position);
            }

            @Override
            public void onItemClicked(Product product) {
                Product selectedProduct = product;
                Gson gson = new Gson();
                String objectString = gson.toJson(selectedProduct);
                Bundle bundle = new Bundle();
                bundle.putString("selected_product_object", objectString);

                dashboardActivity.handleActionMenuBar(false, false);
                dashboardActivity.setTitle(selectedProduct.getDescriptions().get(0).getProductName());
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);

            }

            @Override
            public void onAddToCartClicked(Product productContent) {
                addingProductToCart(productContent);
            }
        };
        fragmentWishlistBinding.recyclerviewWishlist.setAdapter(wishListAdapter);
        if (isUserLoggedIn)
            wishlistApi();
        else {
            fragmentWishlistBinding.cvGuestMissingWishlist.setVisibility(View.VISIBLE);
        }

        fragmentWishlistBinding.layoutMissingWishlist.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirectIntent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(redirectIntent);
            }
        });

        fragmentWishlistBinding.layoutLoggedUserNoWishlist.btnShopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.clearTopFragments();
            }
        });

        fragmentWishlistBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleActionMenuBar(true, true);
        if (isUserLoggedIn)
            wishlistApi();
    }

    public void wishlistApi() {
        fragmentWishlistBinding.progressBar.setVisibility(View.VISIBLE);
        wishListViewModel.getWishListedProducts(userID, sessionToken, languageId).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (wishlistResponse.data.getStatusCode() == 200) {
                        wishListItems = wishlistResponse.data.getData();
                        if (wishListItems.size() == 0) {
                            fragmentWishlistBinding.cvLoggedUserNoWishlist.setVisibility(View.VISIBLE);
                        } else {
                            wishListAdapter.addAll(wishListItems);
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentWishlistBinding.progressBar.setVisibility(View.GONE);

        });
    }

    public void callAddRemoveWishlistApi(Product product, boolean isWishlisted, int position) {
        wishListViewModel.addToWishlist(product.getProductId(), userID, sessionToken, languageId).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (wishlistResponse.data.getStatusCode() == 200) {
                        if (!isWishlisted) {

                            wishListAdapter.removeItemFromWishList(product, position);

//                        wishListAdapter.addAll(wishlistResponse.data.getData());
//                        Toast.makeText(dashboardActivity, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                            wishlistApi();
                        }
                        dashboardActivity.showCustomWislistToast(isWishlisted);
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

    private void addingProductToCart(Product product) {
//        if (selectedProductConfigurable.getQuantity() >= 1) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
        boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", product.getProductId());
        jsonParams.put("refSku", product.getProductConfigurables().get(0).getRefSku());
        jsonParams.put("quantity", 1 + "");
        String jsonParamString = (new JSONObject(jsonParams)).toString();
        if (isAlreadyLoggedIn)
            callAddToCartApi(jsonParamString);
        else {
            preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, product.getSku());
            dashboardActivity.handleActionMenuBar(false, false);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
        }
//        } else {
//            showDisabledCartDialog();
//        }

    }

    public void callAddToCartApi(String jsonParamString) {

        fragmentWishlistBinding.progressBar.setVisibility(View.VISIBLE);
        wishListViewModel.addToCart(userID, jsonParamString, sessionToken, languageId).observe(getActivity(), addToCartResponse -> {
            fragmentWishlistBinding.progressBar.setVisibility(View.GONE);
            switch (addToCartResponse.status) {
                case SUCCESS:
                    if (addToCartResponse.data.getStatusCode() == 200) {
                        CartDialog cartDialog = new CartDialog(dashboardActivity);
                        cartDialog.show(getParentFragmentManager(), "CART_DIALOG");
                        try {
                            int cartCount = addToCartResponse.data.getCartData().getAvailable().size();
                            dashboardActivity.setCartBadgeNumber(cartCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                    logAddToCartEvent(addToCartResponse);
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", addToCartResponse.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", addToCartResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });

    }


}
