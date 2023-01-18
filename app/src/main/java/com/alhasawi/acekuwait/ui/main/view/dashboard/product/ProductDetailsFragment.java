package com.alhasawi.acekuwait.ui.main.view.dashboard.product;

import static com.alhasawi.acekuwait.utils.AppConstants.CALL_PHONE_REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductAttribute;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductConfigurable;
import com.alhasawi.acekuwait.data.api.model.pojo.StoreInventory;
import com.alhasawi.acekuwait.databinding.ActivityImageShowingBinding;
import com.alhasawi.acekuwait.databinding.FragmentProductDetailsBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.ProductAttributesAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.ProductColorAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.ProdutImageViewPagerAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.RelatedProductsRecyclerviewAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.StoreListAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.WishListFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.ProductDetailViewModel;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.CartDialog;
import com.alhasawi.acekuwait.utils.dialogs.DisabledCartDialog;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDetailsFragment extends BaseFragment implements View.OnClickListener {

    FragmentProductDetailsBinding fragmentProductDetailsBinding;
    DashboardActivity dashboardActivity;
    ProductDetailViewModel productDetailViewModel;
    Product currentSelectedProduct;
    ProductConfigurable selectedProductConfigurable;
    String selectedSize = "", selectedColor = "";
    ProductColorAdapter productColorAdapter;
    StoreListAdapter storeListAdapter;
    int dotscount = 0;
    boolean isLoggedIn = false;
    PreferenceHandler preferenceHandler;
    private ArrayList<ProductAttribute> productAttributeArrayList = new ArrayList<>();
    private ProductAttributesAdapter productAttributesAdapter;
    private ArrayList<String> offersList = new ArrayList<>();
    //    private ArrayList<String> services = new ArrayList<>();
    private ArrayList<Product> recommendedProductList = new ArrayList<>();
    private boolean isSearch = false;
    private String selectedSKU = "";
    private String sessionToken, userID;
    private List<ProductConfigurable> productConfigurableList = new ArrayList<>();
    private ArrayList<String> currentColorsList = new ArrayList<>();
    private ArrayList<String> currentSizeList = new ArrayList<>();
    private boolean isbuyNow = false;
    private boolean disableAddToCart = false;
    private boolean isRedirectedFromProductListing = false;
    private String languageId = "";
    private boolean hasAlreadyNotified = false;
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_details;
    }

    @Override
    protected void setup() {

        fragmentProductDetailsBinding = (FragmentProductDetailsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        productDetailViewModel = new ViewModelProvider(getActivity()).get(ProductDetailViewModel.class);
        preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        fragmentProductDetailsBinding.recyclerviewProductDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bundle = getArguments();
        try {
            selectedSKU = bundle.getString("product_object_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String selectedProductObj = bundle.getString("selected_product_object");
            isRedirectedFromProductListing = bundle.getBoolean("from_product_list");
            Gson gson = new Gson();
            currentSelectedProduct = gson.fromJson(selectedProductObj, Product.class);
            selectedSKU = currentSelectedProduct.getSku();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentProductDetailsBinding.cvProductDetails.setOnClickListener(this);
        fragmentProductDetailsBinding.checkboxWishlist.setOnClickListener(this);
        fragmentProductDetailsBinding.lvAddToCart.setOnClickListener(this);
        fragmentProductDetailsBinding.lvBuynow.setOnClickListener(this);
        fragmentProductDetailsBinding.checkboxSpecification.setOnClickListener(this);
        fragmentProductDetailsBinding.checkboxProductDescription.setOnClickListener(this);
        fragmentProductDetailsBinding.lvBack.setOnClickListener(this);
        fragmentProductDetailsBinding.cvNotifyMe.setOnClickListener(this);
        fragmentProductDetailsBinding.lvCartBadge.setOnClickListener(this);
        fragmentProductDetailsBinding.layoutShare.imageViewCall.setOnClickListener(this);
        fragmentProductDetailsBinding.layoutShare.imageViewChat.setOnClickListener(this);
        fragmentProductDetailsBinding.layoutShare.imageViewMail.setOnClickListener(this);
        fragmentProductDetailsBinding.imageViewShare.setOnClickListener(this);
        callProductDetailApi();
        preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_PRODUCT_ID, "");
        try {
            fragmentProductDetailsBinding.cartBadge.cartBadge.setText(dashboardActivity.getCartCount() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_product_details:
                if (fragmentProductDetailsBinding.recyclerviewProductDetails.getVisibility() != View.VISIBLE)
                    fragmentProductDetailsBinding.recyclerviewProductDetails.setVisibility(View.VISIBLE);
                break;
            case R.id.imageViewSelected:
                Intent i = new Intent(dashboardActivity, ProductImageActivity.class);
                i.putExtra("image_url", selectedProductConfigurable.getProductImages().get(0).getImageUrl());
                startActivity(i);
                break;
            case R.id.checkboxWishlist:
                if (isLoggedIn)
                    callWishlistApi(currentSelectedProduct, fragmentProductDetailsBinding.checkboxWishlist.isChecked());
                else {
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, currentSelectedProduct.getProductId());
                    dashboardActivity.setTitle("Wishlist");
                    dashboardActivity.handleActionMenuBar(true, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);
                }
                break;
            case R.id.lv_add_to_cart:
                if (disableAddToCart) {
                    showDisabledCartDialog();
                } else {
                    addingProductToCart();
                    isbuyNow = false;
                }
                break;
            case R.id.lv_buynow:
                if (disableAddToCart) {
                    showDisabledCartDialog();
                } else {
                    addingProductToCart();
                    isbuyNow = true;
                }
                break;
            case R.id.lv_cart_badge:
                dashboardActivity.handleActionMenuBar(true, true);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
                break;
            case R.id.cv_notify_me:
                //fragmentProductDetailsBinding.lvAddToCart.setClickable(false);
                //fragmentProductDetailsBinding.lvBuynow.setClickable(false);
                if (isLoggedIn) {
                    if (!hasAlreadyNotified)
                        callAddToNotify();
                } else {
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, currentSelectedProduct.getSku());
                    dashboardActivity.handleActionMenuBar(true, true);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
                }
                break;
            case R.id.lv_back:
                dashboardActivity.onBackPressed();
                break;
            case R.id.checkbox_product_description:
                if (fragmentProductDetailsBinding.checkboxProductDescription.isChecked()) {
                    fragmentProductDetailsBinding.tvProductDescrption.setVisibility(View.VISIBLE);
                } else {
                    fragmentProductDetailsBinding.tvProductDescrption.setVisibility(View.GONE);
                }
                break;
            case R.id.checkbox_specification:
                if (fragmentProductDetailsBinding.checkboxSpecification.isChecked()) {
                    fragmentProductDetailsBinding.recyclerviewProductDetails.setVisibility(View.VISIBLE);
                } else {
                    fragmentProductDetailsBinding.recyclerviewProductDetails.setVisibility(View.GONE);
                }
                break;
            case R.id.imageViewCall:
                runtimePermission(AppConstants.ACE_PHONE);
                break;
            case R.id.imageViewChat:
//                startFreshChat();
                break;
            case R.id.imageViewMail:
                dashboardActivity.sendEmailToCustomerCare();
                break;
            case R.id.imageView_share:
                if (currentSelectedProduct != null) {
                    dashboardActivity.shareProductInfo(currentSelectedProduct);
                }
                break;
            default:
                break;
        }
    }

    private void startFreshChat() {
//        Freshchat.showConversations(requireView().getContext());
        try {
            InAppEvents.logContactUsEvent(
                    "chat"
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void callProductDetailApi() {
        fragmentProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        productDetailViewModel.getSearchedProductDetails(selectedSKU, languageId, userID).observe(getActivity(), searchedProductDetailsResponse -> {
            switch (searchedProductDetailsResponse.status) {
                case SUCCESS:
                    try {
                        if (searchedProductDetailsResponse.data.getStatusCode() == 200) {
                            currentSelectedProduct = searchedProductDetailsResponse.data.getData().getProduct();
                            dashboardActivity.setCurrentlyShowingProductId(currentSelectedProduct.getSku());
                            dashboardActivity.setCurrentlyShowingProductName(currentSelectedProduct.getDescriptions().get(0).getProductName() + "\n" + currentSelectedProduct.getDescriptions().get(0).getProductDescription());
                            if (currentSelectedProduct.getDescriptions() != null)
                                dashboardActivity.handleActionMenuBar(false, false);
                            recommendedProductList = (ArrayList<Product>) searchedProductDetailsResponse.data.getData().getRecommendedProductList();
                            setUIValues(currentSelectedProduct);
                            setRelatedProducts();
                            try {
                                productConfigurableList = currentSelectedProduct.getProductConfigurables();
                                try {
                                    loadImages(productConfigurableList.get(0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (productConfigurableList != null) {
                                    if (productConfigurableList.size() > 0) {
                                        if (productConfigurableList.get(0).getStoreInventoryList().size() == 0)
                                            fragmentProductDetailsBinding.cvStores.setVisibility(View.GONE);
                                        else
                                            setStoreList((ArrayList<StoreInventory>) productConfigurableList.get(0).getStoreInventoryList());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                        logProductViewEvent(currentSelectedProduct);
//                        logViewContentEvent(currentSelectedProduct);
                        } else {
                            try {
                                GeneralDialog generalDialog = new GeneralDialog("Error", searchedProductDetailsResponse.data.getMessage());
                                generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", searchedProductDetailsResponse.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

            fragmentProductDetailsBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void showDisabledCartDialog() {
        DisabledCartDialog disabledCartDialog = new DisabledCartDialog(dashboardActivity);
        disabledCartDialog.show(getParentFragmentManager(), "DISABLED_CART_DIALOG");
    }

    private void setUIValues(Product currentSelectedProduct) {
        try {
            try {
                fragmentProductDetailsBinding.tvLoyaltyPoints.setText(currentSelectedProduct.getLoyaltyPoint() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentSelectedProduct.getDescriptions().size() > 0) {
                fragmentProductDetailsBinding.tvProductName.setText(currentSelectedProduct.getDescriptions().get(0).getProductName());
                String description = currentSelectedProduct.getDescriptions().get(0).getProductDescription();
                String htmlContent = "";
                if (currentSelectedProduct.getDescriptions() != null && currentSelectedProduct.getDescriptions().size() > 0)
                    htmlContent = currentSelectedProduct.getDescriptions().get(0).getProductDescription();
                fragmentProductDetailsBinding.tvProductDescrption.setText(HtmlCompat.fromHtml(htmlContent, 0));
            }
            if (currentSelectedProduct.getWishlist())
                fragmentProductDetailsBinding.checkboxWishlist.setChecked(true);
            else
                fragmentProductDetailsBinding.checkboxWishlist.setChecked(false);

            try {
                String strOriginalPrice = String.format("%.3f", currentSelectedProduct.getOriginalPrice());
                if (currentSelectedProduct.getDiscountPrice() != null && currentSelectedProduct.getDiscountPrice() > 0) {
                    fragmentProductDetailsBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
                    String strDiscounted = String.format("%.3f", currentSelectedProduct.getDiscountPrice());
                    fragmentProductDetailsBinding.tvProductPrice.setText("KWD " + strOriginalPrice);
                    fragmentProductDetailsBinding.tvOriginalPrice.setText("KWD " + strDiscounted);
                    fragmentProductDetailsBinding.tvOriginalPrice.setPaintFlags(fragmentProductDetailsBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    fragmentProductDetailsBinding.tvProductPrice.setText("KWD " + strOriginalPrice);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

//            String strOriginalPrice = String.format("%.3f", currentSelectedProduct.getOriginalPrice());
//            if (currentSelectedProduct.isDiscount()) {
//                fragmentProductDetailsBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
//                String strDiscountPrice = String.format("%.3f", currentSelectedProduct.getDiscountPrice());
//                fragmentProductDetailsBinding.tvOriginalPrice.setPaintFlags(fragmentProductDetailsBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                fragmentProductDetailsBinding.tvProductPrice.setText("KWD " + strDiscountPrice);
//                fragmentProductDetailsBinding.tvOriginalPrice.setText("KWD " + strOriginalPrice);
//            } else {
//                fragmentProductDetailsBinding.tvProductPrice.setText("KWD " + strOriginalPrice);
//                fragmentProductDetailsBinding.tvOriginalPrice.setVisibility(View.GONE);
//            }

            if (currentSelectedProduct.getSku() != null)
                fragmentProductDetailsBinding.tvSku.setText(currentSelectedProduct.getSku());
            if (!currentSelectedProduct.isAvailable()) {
                if (currentSelectedProduct.getEta() != null && !currentSelectedProduct.getEta().equals("")) {
                    fragmentProductDetailsBinding.tvEta.setVisibility(View.VISIBLE);
                    fragmentProductDetailsBinding.tvEtaCard.setVisibility(View.VISIBLE);
                    fragmentProductDetailsBinding.txtEta.setVisibility(View.VISIBLE);
                    fragmentProductDetailsBinding.tvEta.setText(currentSelectedProduct.getEta());
                    fragmentProductDetailsBinding.tvEtaCard.setText(currentSelectedProduct.getEta());

                } else {
                    fragmentProductDetailsBinding.tvEtaCard.setVisibility(View.GONE);
                    fragmentProductDetailsBinding.tvEta.setVisibility(View.GONE);
                    fragmentProductDetailsBinding.txtEta.setVisibility(View.GONE);
                }
                fragmentProductDetailsBinding.cvNotifyMe.setVisibility(View.VISIBLE);
            } else {
                if (currentSelectedProduct.getDiscountPercentage() == null || currentSelectedProduct.getDiscountPercentage() == 0)
                    fragmentProductDetailsBinding.tvOfferPercent.setVisibility(View.GONE);
                else {
                    fragmentProductDetailsBinding.tvOfferPercent.setText(currentSelectedProduct.getDiscountPercentage() + "% OFF");
                    fragmentProductDetailsBinding.tvOfferPercent.setVisibility(View.VISIBLE);
                }
                fragmentProductDetailsBinding.tvEta.setVisibility(View.GONE);
                fragmentProductDetailsBinding.txtEta.setVisibility(View.GONE);
                fragmentProductDetailsBinding.tvEtaCard.setVisibility(View.GONE);
            }


            fragmentProductDetailsBinding.tvSku.setText(currentSelectedProduct.getSku());
            if (currentSelectedProduct.getManufature() != null)
                fragmentProductDetailsBinding.tvBrand.setText(currentSelectedProduct.getManufature().getManufactureDescriptions().get(0).getName());
//            try {
//                Glide.with(getContext())
//                        .load(currentSelectedProduct.getBrandLogoUrl())
//                        .centerCrop()
//                        .into(fragmentSelectedProductDetailsBinding.imageViewBrandLogo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//


            productAttributeArrayList = (ArrayList<ProductAttribute>) currentSelectedProduct.getProductAttributes();
            productAttributesAdapter = new ProductAttributesAdapter(getActivity(), productAttributeArrayList);
            fragmentProductDetailsBinding.recyclerviewProductDetails.setAdapter(productAttributesAdapter);
            if (!currentSelectedProduct.isAvailable()) {
//                fragmentProductDetailsBinding.recyclerviewProductDetails.tvStockStatus.setText(getResources().getString(R.string.out_of_stock));
                disableAddToCart = true;
            }
            InAppEvents.logProductViewEvent(
                    currentSelectedProduct.getProductId(),
                    currentSelectedProduct.getDescriptions().get(0).getProductName(),
                    currentSelectedProduct
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setStoreList(ArrayList<StoreInventory> storeInventories) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        fragmentProductDetailsBinding.recyclerviewStores.setLayoutManager(layoutManager);
        storeListAdapter = new StoreListAdapter(getActivity(), storeInventories);
        fragmentProductDetailsBinding.recyclerviewStores.setAdapter(storeListAdapter);


    }

    public void loadImages(ProductConfigurable productConfigurable) {
        fragmentProductDetailsBinding.SliderDots.removeAllViews();
        ProdutImageViewPagerAdapter viewPagerAdapter = new ProdutImageViewPagerAdapter(dashboardActivity, productConfigurable.getProductImages());
        fragmentProductDetailsBinding.viewPagerProductImages.setAdapter(viewPagerAdapter);
        dotscount = productConfigurable.getProductImages().size();
        if (dotscount == 1)
            dotscount = 0;
        ImageView[] dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.grey_outlined_rounded_rectangle));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(25, 25);

            params.setMargins(8, 0, 8, 0);

            fragmentProductDetailsBinding.SliderDots.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ace_theme_color_rounded_rectangle_8dp));

        fragmentProductDetailsBinding.viewPagerProductImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                try {
                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.grey_outlined_rounded_rectangle));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ace_theme_color_rounded_rectangle_8dp));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    private void setRelatedProducts() {
        if (recommendedProductList != null && recommendedProductList.size() > 0) {
//            fragmentProductDetailsBinding.tvRecommendedProducts.setVisibility(View.VISIBLE);
            fragmentProductDetailsBinding.recyclerviewRecommendedProducts.setVisibility(View.VISIBLE);
            RelatedProductsRecyclerviewAdapter relatedProductsRecyclerviewAdapter = new RelatedProductsRecyclerviewAdapter(getContext(), (ArrayList<Product>) recommendedProductList) {
                @Override
                public void onLikeClicked(Product product, boolean isWishlisted) {
                    callWishlistApi(product, isWishlisted);
                }

                @Override
                public void onCartClciked(Product product) {
                    try {
                        PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                        boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
                        Map<String, Object> jsonParams = new ArrayMap<>();
                        jsonParams.put("productId", product.getProductId());
                        jsonParams.put("refSku", product.getProductConfigurables().get(0).getRefSku());
                        jsonParams.put("quantity", 1 + "");
                        String jsonParamString = (new JSONObject(jsonParams)).toString();
                        if (isAlreadyLoggedIn) {
                            if (product.isAvailable())
                                callAddToCartApi(jsonParamString);
                            else {
                                DisabledCartDialog disabledCartDialog = new DisabledCartDialog(dashboardActivity);
                                disabledCartDialog.show(getParentFragmentManager(), "DISABLED_CART_DIALOG");
                            }

                        } else {
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, product.getSku());
                            dashboardActivity.handleActionMenuBar(true, true);
                            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
                        }
                        try {
                            InAppEvents.logAddCartEvent(
                                    product.getProductId(),
                                    product.getDescriptions().get(0).getProductName(),
                                    product.getOriginalPrice(), product.getSku());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onItemClicked(Product productContent) {

                    try {
                        InAppEvents.logLikeProductEvent(productContent);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                    Product selectedProduct = productContent;
                    Gson gson = new Gson();
                    String objectString = gson.toJson(selectedProduct);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);
                    dashboardActivity.handleActionMenuBar(false, false);

                    /*if (isRedirectedFromProductListing)
                        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, new ProductDetailsFragment(), bundle, true, false);
                    else
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);*/

                    if (isRedirectedFromProductListing)
                        dashboardActivity.replaceFragment(R.id.fragment_replacer_product, new ProductDetailsFragment(), bundle, true);
                    else
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true);


                }
            };

            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            fragmentProductDetailsBinding.recyclerviewRecommendedProducts.setLayoutManager(horizontalLayoutManager);
            fragmentProductDetailsBinding.recyclerviewRecommendedProducts.setAdapter(relatedProductsRecyclerviewAdapter);
        } else {
//            fragmentSelectedProductDetailsBinding.tvRecommendedProducts.setVisibility(View.GONE);
            fragmentProductDetailsBinding.recyclerviewRecommendedProducts.setVisibility(View.GONE);

        }

    }

    private void callWishlistApi(Product product, boolean isWishlisted) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        productDetailViewModel.addToWishlist(product.getProductId(), userID, sessionToken, languageId).observe(getActivity(), wishlistResponse -> {
            switch (wishlistResponse.status) {
                case SUCCESS:
                    if (wishlistResponse.data.getStatusCode() == 200) {
                        if (isWishlisted)
                            fragmentProductDetailsBinding.checkboxWishlist.setChecked(true);
                        else
                            fragmentProductDetailsBinding.checkboxWishlist.setChecked(false);
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
                    GeneralDialog generalDialog = new GeneralDialog("Error", wishlistResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });
        try {
            if (isWishlisted) {
                InAppEvents.logAddToWishlistEvent(
                        currentSelectedProduct.getDescriptions().get(0).getProductName(),
                        currentSelectedProduct.getProductId(),
                        currentSelectedProduct.getDescriptions().get(0).getProductDescriptionId(),
                        currentSelectedProduct.getOriginalPrice(), product.getSku());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void addingProductToCart() {
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
            boolean isAlreadyLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("productId", currentSelectedProduct.getProductId());
            if (currentSelectedProduct.getProductConfigurables() != null && currentSelectedProduct.getProductConfigurables().size() > 0)
                jsonParams.put("refSku", currentSelectedProduct.getProductConfigurables().get(0).getRefSku());
            else
                jsonParams.put("refSku", "");
            jsonParams.put("quantity", 1 + "");
            String jsonParamString = (new JSONObject(jsonParams)).toString();
            if (isAlreadyLoggedIn)
                callAddToCartApi(jsonParamString);
            else {
                preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, currentSelectedProduct.getSku());
                dashboardActivity.handleActionMenuBar(true, true);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
            }
            try {
                InAppEvents.logAddCartEvent(
                        currentSelectedProduct.getProductId(),
                        currentSelectedProduct.getDescriptions().get(0).getProductName(),
                        currentSelectedProduct.getOriginalPrice(), currentSelectedProduct.getSku());
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callAddToCartApi(String jsonParamString) {

        fragmentProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        productDetailViewModel.addToCart(userID, jsonParamString, sessionToken, languageId).observe(getActivity(), addToCartResponse -> {
            fragmentProductDetailsBinding.progressBar.setVisibility(View.GONE);
            switch (addToCartResponse.status) {
                case SUCCESS:
                    if (addToCartResponse.data.getStatusCode() == 200) {
                        if (isbuyNow) {
                            dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
                            dashboardActivity.handleActionMenuBar(true, true);
                        } else {
                            try {
                                CartDialog cartDialog = new CartDialog(dashboardActivity);
                                cartDialog.show(dashboardActivity.getSupportFragmentManager(), "CART_DIALOG");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            int cartCount = addToCartResponse.data.getCartData().getAvailable().size();
                            fragmentProductDetailsBinding.cartBadge.cartBadge.setText(cartCount + "");
                            dashboardActivity.setCartBadgeNumber(cartCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            InAppEvents.logItemsInCartEvent(addToCartResponse.data.getCartData().getShoppingCartItems(), addToCartResponse.data.getCartData().getTotal());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

//                    logAddToCartEvent(addToCartResponse);
//                    logAddToCartFacebookEvent(addToCartResponse);
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

    public void callAddToNotify() {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", currentSelectedProduct.getProductId());
        jsonParams.put("customerId", userID);
        String jsonParamString = (new JSONObject(jsonParams)).toString();

        fragmentProductDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        productDetailViewModel.addToNotify(jsonParamString, sessionToken, languageId).observe(getActivity(), notifyResponseResource -> {
            fragmentProductDetailsBinding.progressBar.setVisibility(View.GONE);
            switch (notifyResponseResource.status) {
                case SUCCESS:
                    if (notifyResponseResource.data.getStatusCode() == 200) {
                        hasAlreadyNotified = true;
                        fragmentProductDetailsBinding.cvNotifyMe.setBackgroundColor(getResources().getColor(R.color.notify_me_grey));
                        //fragmentProductDetailsBinding.cvNotifyMe.setClickable(false);
                    } else if (notifyResponseResource.data.getStatusCode() == 400) {
                        GeneralDialog generalDialog = new GeneralDialog("Error", notifyResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        fragmentProductDetailsBinding.cvNotifyMe.setBackgroundColor(getResources().getColor(R.color.notify_me_grey));
                        //fragmentProductDetailsBinding.cvNotifyMe.setClickable(false);
                        hasAlreadyNotified = true;
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", notifyResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", notifyResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });
        try {
            InAppEvents.logNotifyMeEvent(currentSelectedProduct);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
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
        try {
            InAppEvents.logContactUsEvent(
                    "call"
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static class ProductImageActivity extends BaseActivity {

        ActivityImageShowingBinding imageShowingBinding;

        @Override
        protected void setup() {
            setContentView(R.layout.activity_image_showing);
            imageShowingBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_showing);
            try {
                String imageUrl = getIntent().getStringExtra("image_url");
                Glide.with(this)
                        .load(imageUrl)
                        .into(imageShowingBinding.imageViewSelected);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageShowingBinding.imageViewSelected.setMaxZoom(6f);
            imageShowingBinding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            finish();
        }
    }
}
