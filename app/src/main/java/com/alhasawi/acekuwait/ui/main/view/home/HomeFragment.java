package com.alhasawi.acekuwait.ui.main.view.home;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.databinding.FragmentAceHomeBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.WebviewFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.HomeViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.CartDialog;
import com.alhasawi.acekuwait.utils.dialogs.DisabledCartDialog;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.LoginSignupDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends BaseFragment implements HomeSectionElementsAdapter.HomeItemClickListener {
    private final HomeResponse.Data UiData;
    FragmentAceHomeBinding fragmentAceHomeBinding;
    DashboardActivity dashboardActivity;
    HomeUiAdapter homeUiAdapter;
    HomeViewModel homeViewModel;
    String userId = "", sessionToken = "", selectedCategory = "";
    boolean isUserLoggedIn = false;
    private String languageId = "";

    public HomeFragment(String selectedCategory, HomeResponse.Data uiData) {
        this.selectedCategory = selectedCategory;
        this.UiData = uiData;
    }

    public static Fragment newInstance(int position, String selectedCategory, HomeResponse.Data uiData) {
        HomeFragment homeFragment = new HomeFragment(selectedCategory, uiData);
        return homeFragment;
    }

//    private String getJsonFromFile() throws JSONException {
//        Context context = getContext();
//        InputStream inputStream = context.getResources().openRawResource(R.raw.home);
//        String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
//        jsonString=jsonString.replace("\n","");
//
//        return jsonString;
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_ace_home;
    }

    @Override
    protected void setup() {

        fragmentAceHomeBinding = (FragmentAceHomeBinding) viewDataBinding;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        fragmentAceHomeBinding.recyclerviewHome.setLayoutManager(new LinearLayoutManager(dashboardActivity, LinearLayoutManager.VERTICAL, false));
        try {
            if (UiData != null) {
                homeUiAdapter = new HomeUiAdapter(dashboardActivity, (ArrayList<HomeSection>) UiData.getHomeSectionList(), this);
                fragmentAceHomeBinding.recyclerviewHome.setAdapter(homeUiAdapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");


    }

    private void redirectOnElementClick(HomeSection homeSection, Element element, String headerText) {
        logEvents(homeSection, element, headerText);
        String type = element.getTitle().getType();
        String callback = "";
        if (element.getAction().getJson() != null)
            callback = element.getAction().getJson();
        if (type.equalsIgnoreCase("C")) {
            String categoryId = "";
            if (element.getTitle().getRefKey() != null)
                categoryId = element.getTitle().getRefKey();
            Bundle bundle = new Bundle();
            bundle.putString("category_id", categoryId);
            bundle.putString("category_name", "");
            bundle.putString("attribute_ids", callback);
            dashboardActivity.handleActionMenuBar(true, false);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);

        } else if (type.equalsIgnoreCase("P")) {
            String sku = "";
            if (element.getTitle().getRefObject() != null)
                sku = element.getTitle().getRefObject().getSku();
            Bundle productBundle = new Bundle();
            productBundle.putString("product_object_id", sku);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), productBundle, true, false);
            dashboardActivity.handleActionMenuBar(false, false);
        } else if (type.equalsIgnoreCase("E")) {
            String link = "";
            if (element.getAction() != null)
                link = element.getAction().getLink();
            Bundle webviewBundle = new Bundle();
            webviewBundle.putString("url", link);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new WebviewFragment(), webviewBundle, true, false);
            dashboardActivity.handleActionMenuBar(true, true);
        }
    }

    private void logEvents(HomeSection homeSection, Element element, String headerText) {
        String categoryId = "", categoryName = "";
        try {
            String type = element.getTitle().getType();
            if (type.equalsIgnoreCase("C")) {
                if (element.getTitle().getRefKey() != null)
                    categoryId = element.getTitle().getRefKey();
            } else if (type.equalsIgnoreCase("P")) {
                String sku = "";
                if (element.getTitle().getRefObject() != null)
                    sku = element.getTitle().getRefObject().getSku();
                categoryId = sku;
            } else if (type.equalsIgnoreCase("E")) {
                String link = "";
                if (element.getAction() != null)
                    link = element.getAction().getLink();
                categoryId = link;
            }
            try {
                if (element.getTitle().getRefObject() != null)
                    categoryName = element.getTitle().getRefObject().getCategories().get(0).getDescriptions().get(0).getCategoryName();

            } catch (Exception e4) {
                e4.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (homeSection.getView().getTag()) {
            case "wishlist_view":
                try {
                    if (headerText.equals("BEST SELLING PRODUCTS")) {
                        String productId = element.getTitle().getRefObject().getProductId();
                        InAppEvents.logBestSellingProductClickEvent(UiData.getCategoryId(), categoryId, productId);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "category_view":
                try {
                    InAppEvents.logCategoryViewEvent(UiData.getCategoryId(), categoryId, categoryName);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "banner_view":
                try {
                    InAppEvents.logBannerClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "wishlist_cart_view":
                try {
                    InAppEvents.logTopSellingProductClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "carousel_view":
                try {
                    InAppEvents.logTopBannerClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "timer_view":
                try {
                    InAppEvents.logTodayDealClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "bag_view":
                try {
                    InAppEvents.logTrendingProductClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "herobanner_view":
                try {
                    InAppEvents.logHeroBannerClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            case "upcoming_view":
                try {
                    InAppEvents.logUpComingProductClickEvent(UiData.getCategoryId(), categoryId);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClicked(HomeSection homeSection, Element element, String headerText) {
        redirectOnElementClick(homeSection, element, headerText);
    }

    @Override
    public void onWishlistClicked(RefObject refObject, boolean ischecked) {
        if (isUserLoggedIn)
            callWishlistApi(refObject, ischecked);
        else {
            LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
            loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
        }
        try {
            InAppEvents.logAddToWishlistEvent(
                    refObject.getDescriptions().get(0).getProductName(),
                    refObject.getProductId(),
                    UiData.getCategoryId(),
                    refObject.getOriginalPrice(), refObject.getSku());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void onCartClicked(RefObject refObject) {
        try {
            if (refObject.isAvailable())
                addingProductToCart(refObject.getProductId(), refObject.getProductConfigurables().get(0).getRefSku(), refObject.getSku());
            else {
                DisabledCartDialog disabledCartDialog = new DisabledCartDialog(dashboardActivity);
                disabledCartDialog.show(getParentFragmentManager(), "DISABLED_CART_DIALOG");
            }
            InAppEvents.logAddCartEvent(
                    refObject.getProductId(),
                    refObject.getDescriptions().get(0).getProductName(),
                    refObject.getOriginalPrice(), refObject.getSku());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void callWishlistApi(RefObject refObject, boolean isWishlisted) {
        try {
            homeViewModel.addToWishlist(refObject.getProductId(), userId, sessionToken, languageId).observe(getActivity(), wishlistResponse -> {
                switch (wishlistResponse.status) {
                    case SUCCESS:
                        try {
                            if (wishlistResponse.data.getStatusCode() == 200) {
                                if (isWishlisted)
                                    refObject.setWishlist(true);
                                else
                                    refObject.setWishlist(false);
                                dashboardActivity.showCustomWislistToast(isWishlisted);
                                if (isWishlisted) {
//                            dashboardActivity.logAddToWishlistFirebaseEvent(product);
//                            dashboardActivity.logAddToWishlistEvent(product);
                                }
                            } else {
                                GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.data.getMessage());
                                generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.message);
                        generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addingProductToCart(String productId, String refsku, String sku) {
//        if (selectedProductConfigurable.getQuantity() >= 1) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
        boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", productId);
        jsonParams.put("refSku", refsku);
        jsonParams.put("quantity", 1 + "");
        String jsonParamString = (new JSONObject(jsonParams)).toString();
        if (isAlreadyLoggedIn)
            callAddToCartApi(jsonParamString);
        else {
            preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, sku);
            dashboardActivity.handleActionMenuBar(true, true);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
        }
//        } else {
//            showDisabledCartDialog();
//        }

    }

    public void callAddToCartApi(String jsonParamString) {

        fragmentAceHomeBinding.progressBar.setVisibility(View.VISIBLE);
        homeViewModel.addToCart(userId, jsonParamString, sessionToken, languageId).observe(getActivity(), addToCartResponse -> {
            fragmentAceHomeBinding.progressBar.setVisibility(View.GONE);
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
