package com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.databinding.FragmentProductListingBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.FilterFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.SortFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.WishListFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.ProductListingViewModel;
import com.alhasawi.acekuwait.utils.Connectivity;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.CartDialog;
import com.alhasawi.acekuwait.utils.dialogs.DisabledCartDialog;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.NoInternetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductListingFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int SET_SORT_REQUEST_CODE = 200;
    private static final int SET_FILTER_REQUEST_CODE = 201;
    DashboardActivity dashboardActivity;
    String currentSelectedCategory = "";
    JSONArray selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData, selectedProductIdsData = null;
    String selectedSortString = "";
    JSONArray filterArray;
    String attributeIds = null, productIds = null;
    List<String> sortStrings = new ArrayList<>();
    NoInternetDialog noInternetDialog;
    String sessionToken;
    boolean shouldRefreshTheList = false;
    boolean isUserLoggedIn = false;
    String userId = "", isPreference = "yes";
    JSONObject homeProductPayload = null;
    String currentInputProductPayload = "";
    PreferenceHandler preferenceHandler;
    private ProductPagedListAdapter productPagedListAdapter;
    private ProductListingViewModel productListingViewModel;
    private FragmentProductListingBinding fragmentProductListingBinding;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_listing;
    }

    @Override
    protected void setup() {
        fragmentProductListingBinding = (FragmentProductListingBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getBaseActivity();
        dashboardActivity.handleActionBarIcons(true);
        dashboardActivity.handleActionMenuBar(true, true);
        productListingViewModel = new ViewModelProvider(getBaseActivity()).get(ProductListingViewModel.class);
        fetchDataFromSharedPref();
        preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
//        fragmentProductListingBinding.swipeRefreshProducts.setOnRefreshListener(this);
        fragmentProductListingBinding.imageButtonSort.setOnClickListener(this);
        fragmentProductListingBinding.imageButtonFilter.setOnClickListener(this);
        fragmentProductListingBinding.layoutNoProducts.tvContinueShopping.setOnClickListener(this);
        fragmentProductListingBinding.imageButtonBack.setOnClickListener(this);
        // Redirecting from home sub category
        try {
            Bundle bundle = getArguments();
            if (bundle.containsKey("SELECTED_BRAND")) {
                selectedBrandData = new JSONArray();
                selectedBrandData.put(bundle.getString("SELECTED_BRAND"));
                currentSelectedCategory = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
            } else {
                currentSelectedCategory = bundle.getString("category_id");
                String currentCategoryName = bundle.getString("category_name");
                if (bundle.containsKey("attribute_ids"))
                    attributeIds = bundle.getString("attribute_ids");
                if (bundle.containsKey("product_ids"))
                    productIds = bundle.getString("product_ids");
//                fragmentProductListingBinding.tvTopDealsHeading.setText(currentCategoryName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!Connectivity.isConnected(dashboardActivity)) {
            showNoInternetDialog();
        } else {
            if (attributeIds == null || attributeIds.equalsIgnoreCase("")) {
                homeProductPayload = null;
            } else {
                try {
                    homeProductPayload = new JSONObject(attributeIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (productIds != null && !productIds.equals("")) {
//                fragmentProductListingBinding.cvTopBar.setVisibility(View.GONE);
                selectedProductIdsData = new Gson().fromJson(productIds, JSONArray.class);
            }
            setupProductRecyclerview();
            productListingViewModel.getSortStrings().observe(this, strings -> {
                sortStrings = strings;
            });
            productListingViewModel.getIsPreferenceLiveData().observe(this, preference -> {
                isPreference = preference;
                if (preference.equals("no")) {
//                    refreshProductList(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData, selectedSortString, isPreference);
                }
            });
        }
        new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN).saveData(PreferenceHandler.LOGIN_BRANCH_CATEGORY_ID, "");
        handleWishlistCartApiBeforeLoggedIn();
    }

    private void handleWishlistCartApiBeforeLoggedIn() {
        if (!preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "").equalsIgnoreCase("") && isUserLoggedIn) {
            dashboardActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    shouldRefreshTheList = true;
                    String productId = preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
                    callWishlistApi(productId, null, true);
                }
            });

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        dashboardActivity.handleActionMenuBar(true, true);
    }

    private void fetchDataFromSharedPref() {
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            currentSelectedCategory = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
            String currentCategoryName = preferenceHandler.getData(PreferenceHandler.LOGIN_CATEGORY_NAME, "");
//            fragmentProductListingBinding.tvTopDealsHeading.setText(currentCategoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupProductRecyclerview() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        fragmentProductListingBinding.recyclerviewProducts.setLayoutManager(gridLayoutManager);
        currentInputProductPayload = addInputParams(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData);
        loadProductsFromApi(currentInputProductPayload);
    }

    private void loadProductsFromApi(String inputPayload) {
        showProgressBarDialog(true);
        initializeProductAdapter();
        productListingViewModel.fetchProductData(inputPayload);
        productListingViewModel.getProductPaginationLiveData().observe(this, pagedList -> {
            fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.GONE);
            productPagedListAdapter.submitList(pagedList);
            shouldRefreshTheList = false;

        });

        /*
         * Step 5: When a new page is available, we call submitList() method
         * of the PagedListAdapter class
         *
         * */
        productListingViewModel.getNetworkState().observe(this, networkState -> {
            productPagedListAdapter.setNetworkState(networkState);
        });


    }

    private void initializeProductAdapter() {
        productPagedListAdapter = new ProductPagedListAdapter(getContext()) {
            @Override
            public void onLikeClicked(Product product, boolean checked) {
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
                if (isAlreadyLoggedIn) {
                    callWishlistApi(product.getProductId(), product, checked);
                } else {
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, product.getProductId());
                    dashboardActivity.handleActionMenuBar(true, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
                }
            }

            @Override
            public void onItemClicked(Product productContent) {
                try {

                    Product selectedProduct = productContent;
                    Gson gson = new Gson();
                    String objectString = gson.toJson(selectedProduct);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);
                    bundle.putBoolean("from_product_list", true);

                    dashboardActivity.handleActionMenuBar(false, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer_product, new ProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAddToCartClicked(Product product) {
                if (product != null && product.isAvailable())
                    addingProductToCart(product);
                else {
                    DisabledCartDialog disabledCartDialog = new DisabledCartDialog(dashboardActivity);
                    disabledCartDialog.show(getParentFragmentManager(), "DISABLED_CART_DIALOG");
                }
            }

            @Override
            public void onLoadedProducts() {
                showProgressBarDialog(false);
            }

            @Override
            public void onItemCountChanged(int itemCount) {
                if (itemCount == 0) {
                    fragmentProductListingBinding.progressBar.setVisibility(View.GONE);
                    fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.VISIBLE);
                } else {
                    fragmentProductListingBinding.lvNoProductsFound.setVisibility(View.GONE);
                }
            }

        };

        fragmentProductListingBinding.recyclerviewProducts.setAdapter(productPagedListAdapter);
    }

    private void callWishlistApi(String productId, Product product, boolean isWishlisted) {

        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        productListingViewModel.addToWishlist(productId, userID, sessionToken, languageId).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    try {
                        if (wishlistResponse.data.getStatusCode() == 200) {
                            if (product != null)
                                if (isWishlisted)
                                    product.setWishlist(true);
                                else
                                    product.setWishlist(false);
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
                            if (shouldRefreshTheList)
                                refreshProductList(currentInputProductPayload);
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
    }

    private void launchSortFragment() {
        SortFragment sortFragment = new SortFragment();
        sortFragment.setTargetFragment(this, SET_SORT_REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("sort_string", (ArrayList<String>) sortStrings);
        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, sortFragment, bundle, true, false);

    }

    private void launchFilterFragment() {
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setTargetFragment(this, SET_FILTER_REQUEST_CODE);
        Bundle bundle = new Bundle();
        bundle.putString("category_id", currentSelectedCategory);
        bundle.putString("input_payload", currentInputProductPayload.toString());
        dashboardActivity.handleActionMenuBar(true, true);
        dashboardActivity.handleActionBarIcons(false);
        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, filterFragment, bundle, true, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSort:
                launchSortFragment();
                break;
            case R.id.imageButtonFilter:
                launchFilterFragment();
                break;
            case R.id.tvContinueShopping:
                dashboardActivity.onBackPressed();
                break;
            case R.id.imageButtonBack:
                dashboardActivity.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleActionMenuBar(true, true);
        dashboardActivity.setTitle("");
    }

    @Override
    public void onRefresh() {
//        itemCount = 0;
//        currentPage = PAGE_START;
//        isLastPage = false;
//        productRecyclerviewAdapter.clear();
//        currentProductList.clear();
//        Toast.makeText(dashboardActivity, "Call Api on Refresh", Toast.LENGTH_SHORT).show();
//        loadFromApi(currentPage, currentSelectedCategory);
    }


    @Override
    public void onItemClickListener(int position, View view) {
        refreshProductList(currentInputProductPayload);
    }

    private void refreshProductList(String inputPayload) {
        productListingViewModel.invalidateProductLiveData();
        loadProductsFromApi(inputPayload);
    }

    public void setFilterData(JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray, String isPreference) {
        this.selectedFilterData = filterArray;
        this.selectedBrandData = brandArray;
        this.selectedColorData = colorArray;
        this.selectedSizeData = sizeArray;
        this.isPreference = isPreference;
        currentInputProductPayload = addInputParams(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData);
        refreshProductList(currentInputProductPayload);
    }

    public void setSortData(String sort, String isPreference) {
        this.selectedSortString = sort;
        this.isPreference = isPreference;
        currentInputProductPayload = addInputParams(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData);
        refreshProductList(currentInputProductPayload);
    }

    public void showNoInternetDialog() {
        noInternetDialog = new NoInternetDialog();
        noInternetDialog.setClickListener(new NoInternetDialog.NoInternetDialogInterface() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onRetry() {
                currentInputProductPayload = addInputParams(currentSelectedCategory, selectedFilterData, selectedBrandData, selectedColorData, selectedSizeData);
                loadProductsFromApi(currentInputProductPayload);
            }

        });
        noInternetDialog.showDialog(dashboardActivity);
    }

    private void showProgressBarDialog(boolean shouldShow) {
        if (shouldShow)
            fragmentProductListingBinding.progressBar.setVisibility(View.VISIBLE);
        else
            fragmentProductListingBinding.progressBar.setVisibility(View.GONE);

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
        if (isAlreadyLoggedIn) {
            callAddToCartApi(jsonParamString);
            try {
                InAppEvents.logAddCartEvent(product.getProductId(), product.getDescriptions().get(0).getProductName(), product.getOriginalPrice(), product.getSku());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, product.getSku());
            dashboardActivity.handleActionMenuBar(false, false);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
        }
//        } else {
//            showDisabledCartDialog();
//        }

    }

    public void callAddToCartApi(String jsonParamString) {

        fragmentProductListingBinding.progressBar.setVisibility(View.VISIBLE);
        productListingViewModel.addToCart(userId, jsonParamString, sessionToken, languageId).observe(getActivity(), addToCartResponse -> {
            fragmentProductListingBinding.progressBar.setVisibility(View.GONE);
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
                        try {
                            InAppEvents.logItemsInCartEvent(addToCartResponse.data.getCartData().getShoppingCartItems(), addToCartResponse.data.getCartData().getTotal());
                        } catch (Exception e2) {
                            e2.printStackTrace();
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

    private String addInputParams(String categoryId, JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray) {
        String sortApplied = "";
        JSONArray categoryIds = new JSONArray();
        if (categoryId == null) {
            categoryIds = new JSONArray();
        } else
            categoryIds.put(categoryId);

        JSONArray attributeIds, productIds;
        if (selectedProductIdsData == null)
            productIds = new JSONArray();
        else
            productIds = selectedProductIdsData;
        if (filterArray == null)
            attributeIds = new JSONArray();
        else
            attributeIds = filterArray;
        JSONArray brandIds, colorIds, sizeIds;
        if (brandArray == null)
            brandIds = new JSONArray();
        else
            brandIds = brandArray;

        if (colorArray == null)
            colorIds = new JSONArray();
        else
            colorIds = colorArray;

        if (sizeArray == null)
            sizeIds = new JSONArray();
        else
            sizeIds = sizeArray;
        if (homeProductPayload == null) {
            if (selectedSortString.equals(""))
                sortApplied = "";
            else
                sortApplied = selectedSortString;
        }

        Map<String, Object> jsonParams = new ArrayMap<>();
        try {
            if (homeProductPayload != null) {
                if (!homeProductPayload.getString("sorting").equals("") && selectedSortString.equals(""))
                    sortApplied = homeProductPayload.getString("sorting");
                else
                    sortApplied = selectedSortString;

                JSONArray homeCategoryIds = homeProductPayload.getJSONArray("categoryIds");
                if (homeCategoryIds.length() > 0)
                    for (int i = 0; i < homeCategoryIds.length(); i++) {
                        categoryIds.put(homeCategoryIds.get(i));
                    }
                JSONArray homeAttributeIds = homeProductPayload.getJSONArray("attributeIds");
                if (homeAttributeIds.length() > 0)
                    for (int i = 0; i < homeAttributeIds.length(); i++) {
                        attributeIds.put(homeAttributeIds.get(i));
                    }
                JSONArray homeBrandIds = homeProductPayload.getJSONArray("brandIds");
                if (homeBrandIds.length() > 0)
                    for (int i = 0; i < homeBrandIds.length(); i++) {
                        brandIds.put(homeBrandIds.get(i));
                    }
                JSONArray homeProductIds = homeProductPayload.getJSONArray("productIds");
                if (homeProductIds.length() > 0)
                    for (int i = 0; i < homeProductIds.length(); i++) {
                        //homeProductIds.put(homeProductIds.get(i));
                        productIds.put(homeProductIds.get(i));
                    }
                JSONArray homeSizes = homeProductPayload.getJSONArray("sizes");
                if (homeSizes.length() > 0)
                    for (int i = 0; i < homeSizes.length(); i++) {
                        sizeIds.put(homeSizes.get(i));
                    }
                JSONArray homeColors = homeProductPayload.getJSONArray("colors");
                if (homeColors.length() > 0)
                    for (int i = 0; i < homeColors.length(); i++) {
                        colorIds.put(homeColors.get(i));
                    }

            }

            jsonParams.put("categoryIds", categoryIds);
            jsonParams.put("attributeIds", attributeIds);
            jsonParams.put("brandIds", brandIds);
            jsonParams.put("productIds", productIds);
            jsonParams.put("sorting", sortApplied);
            jsonParams.put("customerId", userId);
            jsonParams.put("sizes", sizeIds);
            jsonParams.put("colors", colorIds);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String input = (new JSONObject(jsonParams)).toString();
        return input;
    }
}



