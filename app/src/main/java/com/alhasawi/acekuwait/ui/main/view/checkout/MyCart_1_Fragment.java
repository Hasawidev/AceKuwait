package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.databinding.FragmentCartBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.CartAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.CartViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.DefaultDeliveryModeDialog;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.RemoveItemFromCartDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCart_1_Fragment extends BaseFragment implements View.OnClickListener {
    FragmentCartBinding fragmentCartBinding;
    CartAdapter cartAdapter;
    CartViewModel cartViewModel;
    List<ShoppingCartItem> cartItemsList = new ArrayList<>();
    int cartCount = 0;
    String totalPrice = "", userID = "", username = "";
    String sessionToken;
    String cartId;
    boolean isAlertShowedOnce = false;
    boolean isUserLoggedIn = false;
    DashboardActivity dashboardActivity;
    List<ShoppingCartItem> availableItemList = new ArrayList<>();
    List<ShoppingCartItem> outOfStockItemList = new ArrayList<>();
    boolean isQuantityIncreased;
    int selectedItemAdapterPosition = -1;
    boolean enableCheckout = false;
    List<ShippingMode> shippingModeList;
    ShoppingCartItem currentSelectedProduct;
    String languageId = "";
    private String cartResponseString, selectedPickupDate = "";
    private boolean alertMessageClicked = false;
    private boolean isFromShippingMode = false;
    private DefaultDeliveryModeDialog defaultDeliveryModeDialog;
    private RemoveItemFromCartDialog removeItemFromCartDialog;
//    private ShippingMode defaultShippingModeSelected;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void setup() {
        fragmentCartBinding = (FragmentCartBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            isUserLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
            languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isUserLoggedIn)
            dashboardActivity.handleActionMenuBar(false, false);
        else {
            dashboardActivity.handleActionMenuBar(true, true);
            fragmentCartBinding.cvGuestUserEmptyCart.setVisibility(View.VISIBLE);
        }

        fragmentCartBinding.cvProceedToCheckout.setOnClickListener(this);
        fragmentCartBinding.lvBack.setOnClickListener(this);
        fragmentCartBinding.layoutNoItemsCart.btnShopNow.setOnClickListener(this);
        fragmentCartBinding.layoutGuestNoItemsCart.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
            }
        });
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartAdapter = new CartAdapter(dashboardActivity) {
            @Override
            public void onItemDeleteClicked(ShoppingCartItem cartItem) {
                showRemoveItemAlert(cartItem);
            }

            @Override
            public void onDeliveryOptionsClicked(ShoppingCartItem cartItem, int adapterPosition) {
                dashboardActivity.showToolBar();
                dashboardActivity.handleActionMenuBar(false, false);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String cartItemString = gson.toJson(cartItem);
                bundle.putString("selected_cart_item", cartItemString);
                selectedItemAdapterPosition = adapterPosition;
//                lanuchDeliverModeFragment(bundle);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectDeliveryModeFragment(), bundle, true, false);
            }

            @Override
            public void onApplianceProtectionClicked(ShoppingCartItem cartItem, int adapterPosition) {
                selectedItemAdapterPosition = adapterPosition;
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ApplianceProtectionFragment(), null, true, false);
            }

            @Override
            public void cartItemsUpdated(ShoppingCartItem cartItem, int quantity, boolean isIncreased) {
                String addressId = "";
                if (cartItem.getAddress() != null)
                    addressId = cartItem.getAddress().getAddressId();
                try {
                    updateCartItemsApi(cartItem.getProductId(), cartItem.getProductConfigurable().getRefSku(), quantity + "", cartItem.getShippingMode(), cartItem.getPickupDate(), cartItem.getPickupTime(), cartItem.getStoreId(), addressId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                isQuantityIncreased = isIncreased;
            }

            @Override
            public void onCartItemClicked(ShoppingCartItem cartItem) {
                try {
                    Product product = cartItem.getProduct();
                    Gson gson = new Gson();
                    String objectString = gson.toJson(product);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_product_object", objectString);
                    dashboardActivity.showToolBar();
                    dashboardActivity.handleActionMenuBar(false, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentCartBinding.recyclerviewCart.setLayoutManager(mLayoutManager);
        fragmentCartBinding.recyclerviewCart.setItemAnimator(new DefaultItemAnimator());
        fragmentCartBinding.recyclerviewCart.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        fragmentCartBinding.recyclerviewCart.setAdapter(cartAdapter);

        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                if (bundle.containsKey("selected_cart_item")) {
                    Gson gson = new Gson();
                    String selectedItemString = bundle.getString("selected_cart_item");
                    currentSelectedProduct = gson.fromJson(selectedItemString, ShoppingCartItem.class);
                    if (bundle.containsKey("pickup_date")) {
                        selectedPickupDate = bundle.getString("pickup_date");
                    }

                    if (currentSelectedProduct != null) {
                        if (currentSelectedProduct.getShippingMode().isPickup())
                            updateCartItemsApi(currentSelectedProduct.getProductId(), currentSelectedProduct.getRefSku(), currentSelectedProduct.getQuantity() + "", currentSelectedProduct.getShippingMode(), selectedPickupDate, currentSelectedProduct.getPickupTime(), currentSelectedProduct.getStore().getStoreId(), "");
                        else {
                            updateCartItemsApi(currentSelectedProduct.getProductId(), currentSelectedProduct.getRefSku(), currentSelectedProduct.getQuantity() + "", currentSelectedProduct.getShippingMode(), currentSelectedProduct.getPickupDate(), currentSelectedProduct.getPickupTime(), "", currentSelectedProduct.getAddress().getAddressId());
                            isFromShippingMode = true;
                        }

                    }

//                    PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
//                    String defaultShippingModeString = preferenceHandler.getData(PreferenceHandler.DEFAULT_SHIPPING_MODE, "");
//                    if (!defaultShippingModeString.equals("")) {
//                        defaultShippingModeSelected = gson.fromJson(defaultShippingModeString, ShippingMode.class);
//                    }
                } else {
                    if (isUserLoggedIn)
                        callMyCartApi();
                    else {
                        dashboardActivity.handleActionMenuBar(true, true);
                        fragmentCartBinding.cvGuestUserEmptyCart.setVisibility(View.VISIBLE);
                    }

                }
            } else {
                if (isUserLoggedIn)
                    callMyCartApi();
                else {
                    dashboardActivity.handleActionMenuBar(true, true);
                    fragmentCartBinding.cvGuestUserEmptyCart.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUiValues() {
        fragmentCartBinding.tvBagCount.setText(cartCount + "");
        String strDouble = String.format("%.3f", totalPrice);
        fragmentCartBinding.tvTotalAmount.setText("KWD " + strDouble);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvProceedToCheckout:
                if (outOfStockItemList.size() > 0) {
                    GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), getContext().getResources().getString(R.string.remove_out_of_stock));
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                } else {
                    enableCheckout = checkDeliveryOptionsAddedForAllProducts();
                    if (enableCheckout) {
                        dashboardActivity.handleActionMenuBar(false, false);
                        Bundle bundle = new Bundle();
                        bundle.putString("cart_response", cartResponseString);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderSummaryFragment(), bundle, true, false);

                    } else {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), getContext().getResources().getString(R.string.select_all_options));
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }
                }
                break;
            case R.id.btnShopNow:
                dashboardActivity.showToolBar();
                int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < fragmentCount; i++) {
                    getParentFragmentManager().popBackStackImmediate();
                }
                dashboardActivity.handleActionMenuBar(true, true);
                dashboardActivity.handleActionBarIcons(true);

                break;
            case R.id.lv_back:
                dashboardActivity.onBackPressed();
                break;
            default:
                break;
        }
    }

    private boolean checkDeliveryOptionsAddedForAllProducts() {
        boolean enable = false;
        int count = 0;
        for (int i = 0; i < cartItemsList.size(); i++) {
            if (cartItemsList.get(i).getShippingMode() != null && cartItemsList.get(i).getAddress() != null && cartItemsList.get(i).getAddressId() != null
                    && !cartItemsList.get(i).getAddressId().equalsIgnoreCase(""))
                count++;
        }
        if (count == cartItemsList.size())
            enable = true;
        return enable;
    }

    public void callMyCartApi() {
        fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.getCartItems(userID, sessionToken, languageId, alertMessageClicked).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    if (cartResponse.data.getStatusCode() == 200) {
                        if (cartResponse.data.getCartData() != null) {
                            dashboardActivity.handleActionMenuBar(false, false);
                            updateCartValues(cartResponse);
                            try {
                                InAppEvents.logViewCartEvent((ArrayList<ShoppingCartItem>) cartResponse.data.getCartData().getShoppingCartItems(), cartResponse.data.getCartData().getTotal());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (cartResponse.data.getCartData().isShowAlert() && !isAlertShowedOnce)
                                showDefaultDeliveryModeDialog();
                            cartCount = cartResponse.data.getCartData().getItemCount();
                            dashboardActivity.setCartBadgeNumber(cartCount);
                        } else {
                            dashboardActivity.handleActionMenuBar(true, true);
                            fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
                        }
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), cartResponse.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), cartResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
        });
    }

    public void showDefaultDeliveryModeDialog() {
        isAlertShowedOnce = true;
        defaultDeliveryModeDialog = new DefaultDeliveryModeDialog();
        defaultDeliveryModeDialog.setClickListener(new DefaultDeliveryModeDialog.DefaultDeliveryModeInterface() {
            @Override
            public void onNo() {

            }

            @Override
            public void onYes() {
                alertMessageClicked = true;
                callMyCartApi();
            }
        });
        defaultDeliveryModeDialog.showDialog(dashboardActivity);
    }

    public void showRemoveItemAlert(ShoppingCartItem cartItem) {
        removeItemFromCartDialog = new RemoveItemFromCartDialog();
        removeItemFromCartDialog.setClickListener(new RemoveItemFromCartDialog.RemoveItemDialogInterface() {

            @Override
            public void onYes() {
                fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
                removeItemFromCartApi(cartItem);
            }

            @Override
            public void onNo() {
                removeItemFromCartDialog.dismiss();
            }
        });
        removeItemFromCartDialog.showDialog(dashboardActivity);
    }

    private void updateCartValues(Resource<CartResponse> cartResponse) {
        try {
            cartItemsList = new ArrayList<>();
            availableItemList = cartResponse.data.getCartData().getAvailable();
            for (int i = 0; i < availableItemList.size(); i++) {
                availableItemList.get(i).setOutOfStock(false);
                cartItemsList.add(availableItemList.get(i));

                try {
                    InAppEvents.logShippingInfoEvent(cartItemsList.get(i).getShippingMode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            outOfStockItemList = cartResponse.data.getCartData().getOutOfStock();
            for (int i = 0; i < outOfStockItemList.size(); i++) {
                outOfStockItemList.get(i).setOutOfStock(true);
                cartItemsList.add(outOfStockItemList.get(i));
            }

//            for (int i = 0; i < cartItemsList.size(); i++) {
//                if (defaultShippingModeSelected != null) {
//                    cartItemsList.get(i).setShippingMode(defaultShippingModeSelected);
//                }
//                if (cartItemsList.get(i).getProductId().equals(currentSelectedProduct.getProductId())) {
//                    cartItemsList.set(i, currentSelectedProduct);
//                }


//            }
            String strDouble = String.format("%.3f", cartResponse.data.getCartData().getTotal());
            fragmentCartBinding.tvAmountTotal.setText("KWD " + strDouble);
            fragmentCartBinding.tvTotalAmount.setText("KWD " + strDouble);
            try {
                InAppEvents.logItemsInCartEvent(cartItemsList, cartResponse.data.getCartData().getTotal());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        cartResponse.data.getCartData().setShoppingCartItems(cartItemsList);
        cartResponseString = gson.toJson(cartResponse.data);
        if (cartItemsList.size() == 0) {
            fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
        } else {
            try {
                cartId = cartResponse.data.getCartData().getShoppingCartId();
                PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
                preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_CART_ID, cartId);

                totalPrice = cartResponse.data.getCartData().getTotal() + "";

                cartCount = cartResponse.data.getCartData().getItemCount();
                cartAdapter.addAll(cartItemsList);
                setUiValues();
                dashboardActivity.setCartBadgeNumber(cartCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeItemFromCartApi(ShoppingCartItem shoppingCartItem) {

        cartViewModel.removeItemFromCart(userID, shoppingCartItem.getShoppingCartItemId(), sessionToken, languageId).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    if (cartResponse.data.getStatusCode() == 200) {
//                        if (cartResponse.data.getCartData() != null) {
//                            dashboardActivity.handleActionMenuBar(false, false);
//                            updateCartValues(cartResponse);
//
//                        } else {
//                            dashboardActivity.handleActionMenuBar(true, true);
//                            fragmentCartBinding.cvLoggedUserEmptyCart.setVisibility(View.VISIBLE);
//                        }
//                        Toast.makeText(dashboardActivity, "Removed item from cart successfully", Toast.LENGTH_SHORT).show();
                        cartCount = cartResponse.data.getCartData().getItemCount();
                        dashboardActivity.setCartBadgeNumber(cartCount);
                        callMyCartApi();
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), cartResponse.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), cartResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
        });
        try {
            InAppEvents.logRemoveCartEvent(
                    shoppingCartItem.getProductId(),
                    shoppingCartItem.getProduct().getDescriptions().get(0).getProductName(),
                    shoppingCartItem.getProduct().getOriginalPrice()
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void updateCartItemsApi(String productId, String refSku, String quantity, ShippingMode shippingMode, String pickupDate, String timeslot, String storeId, String addressId) {
        fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        String shippingId = "";
        if (shippingMode != null)
            shippingId = shippingMode.getShippingId();

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("productId", productId);
        jsonParams.put("refSku", refSku);
        jsonParams.put("quantity", quantity);
        jsonParams.put("shippingId", shippingId);
        jsonParams.put("pickupDate", pickupDate);
        jsonParams.put("time", timeslot);
        jsonParams.put("storeId", storeId);
        jsonParams.put("addressId", addressId);

        String jsonParamString = (new JSONObject(jsonParams)).toString();
        cartViewModel.updateCartItems(userID, jsonParamString, sessionToken, languageId).observe(getActivity(), cartResponseResource -> {
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
            switch (cartResponseResource.status) {
                case SUCCESS:
                    if (cartResponseResource.data.getStatusCode() == 200) {
                        updateCartValues(cartResponseResource);
                        try {
                            InAppEvents.logItemsInCartEvent((ArrayList<ShoppingCartItem>) cartResponseResource.data.getCartData().getShoppingCartItems(), cartResponseResource.data.getCartData().getTotal());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(dashboardActivity, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", cartResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }
                    if (isFromShippingMode)
                        callMyCartApi();

                    break;
                case LOADING:
                    break;
                case ERROR:
                    cartAdapter.addAll(cartItemsList);
                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), cartResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.hideToolBar();
    }

    public void showProgressbar(boolean shouldShowProgressbar) {
        if (shouldShowProgressbar)
            fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        else
            fragmentCartBinding.progressBar.setVisibility(View.GONE);
    }

    private void deleteItem(ShoppingCartItem cartItem, int adapterPosition) {
        String name = cartItem.getProduct().getDescriptions().get(0).getProductName();
        // backup of removed item for undo purpose
        ShoppingCartItem deletedItem = cartItem;
        final int deletedIndex = adapterPosition;
        fragmentCartBinding.progressBar.setVisibility(View.VISIBLE);
        removeItemFromCartApi(deletedItem);
        // remove the item from recycler view
        cartAdapter.removeItem(adapterPosition);

//         showing snack bar with Undo option
        Snackbar snackbar = Snackbar
                .make(fragmentCartBinding.constraintLayout16, name + " Removed from cart!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // undo is selected, restore the deleted item
                cartAdapter.restoreItem(deletedItem, deletedIndex);
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.light_blue));
        snackbar.show();
    }

//    private void lanuchDeliverModeFragment(Bundle bundle) {
//        SelectDeliveryModeFragment selectDeliveryModeFragment = new SelectDeliveryModeFragment();
//        String TAG = "SELECT_DELIVERY_MODE";
//        selectDeliveryModeFragment.setTargetFragment(this, 202);
//        selectDeliveryModeFragment.setArguments(bundle);
//        dashboardActivity.replaceFragment(R.id.fragment_replacer, selectDeliveryModeFragment, bundle, true, false);
//    }
//
//    public void setSelectedDeliveryMode(ShoppingCartItem cartItem) {
//        cartAdapter.notifyItemChanged(selectedItemAdapterPosition, cartItem);
//    }
}
