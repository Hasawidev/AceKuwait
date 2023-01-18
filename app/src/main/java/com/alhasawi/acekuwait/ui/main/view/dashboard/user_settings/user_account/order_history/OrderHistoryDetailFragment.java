package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history;

import static com.alhasawi.acekuwait.utils.DateTimeUtils.changeDateFormatFromAnother;

import android.os.Bundle;
import android.view.View;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.FragmentOrderDetailsBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.OrderedProductsAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

public class OrderHistoryDetailFragment extends BaseFragment {
    FragmentOrderDetailsBinding fragmentOrderDetailsBinding;
    Order selectedOrder;
    OrderProduct selectedProduct;
    OrderedProductsAdapter orderedProductsAdapter;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_details;
    }

    @Override
    protected void setup() {
        fragmentOrderDetailsBinding = (FragmentOrderDetailsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            String orderObject = bundle.getString("order_object");
            String orderProduct = bundle.getString("order_product");
            Gson gson = new Gson();
            selectedOrder = gson.fromJson(orderObject, Order.class);
            selectedProduct = gson.fromJson(orderProduct, OrderProduct.class);
            if (selectedOrder != null)
                setUiValues();
            fragmentOrderDetailsBinding.progressBar.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentOrderDetailsBinding.tvTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                Gson gson = new Gson();
                String orderString = gson.toJson(selectedOrder);
                bundle1.putString("order_object", orderString);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new TrackOrderFragment(), bundle1, true, false);
            }
        });
        fragmentOrderDetailsBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }

    private void setUiValues() {
        fragmentOrderDetailsBinding.tvProductOrderId.setText(selectedOrder.getOrderId());
        fragmentOrderDetailsBinding.tvOrderId.setText(selectedOrder.getOrderId());
        if (selectedOrder.getPaymentType() != null)
            fragmentOrderDetailsBinding.tvPaymentMode.setText(selectedOrder.getPaymentType());
        if (selectedProduct.getAddress() != null) {
            fragmentOrderDetailsBinding.tvName.setText(selectedProduct.getAddress().getFirstName() + " " + selectedProduct.getAddress().getLastName());
            String address = selectedProduct.getAddress().getStreet() + ", " +
                    selectedProduct.getAddress().getFlat() + " " + selectedProduct.getAddress().getBlock() + ", "
                    + selectedProduct.getAddress().getArea() + "\n" + selectedProduct.getAddress().getMobile();
            fragmentOrderDetailsBinding.tvAddress.setText(address);
        }
        if (selectedOrder.getTotal() != null) {
            String strTotal = String.format("%.3f", selectedOrder.getTotal());
            fragmentOrderDetailsBinding.tvOrderPrice.setText("KWD " + strTotal);
            fragmentOrderDetailsBinding.tvOrderAmount.setText("KWD " + strTotal);
        }

        try {
            Glide.with(getContext())
                    .load(selectedProduct.getProductImage()).into(fragmentOrderDetailsBinding.layoutProduct.imageViewProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (selectedOrder.getDiscounted() != null)
//            fragmentOrderDetailsBinding.tvSavedAmount.setText("KWD " + selectedOrder.getDiscounted());
        if (selectedProduct.getShippingMode() != null)
            fragmentOrderDetailsBinding.tvModeOfDelivery.setText(selectedProduct.getShippingMode().getName());
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            String email = preferenceHandler.getData(PreferenceHandler.LOGIN_EMAIL, "");
            String mobile = "+968 " + preferenceHandler.getData(PreferenceHandler.LOGIN_PHONENUMBER, "");
            fragmentOrderDetailsBinding.tvMobile.setText(mobile);
            fragmentOrderDetailsBinding.tvMailId.setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String dateOfPurchase = selectedOrder.getDateOfPurchase();
            String formattedString = changeDateFormatFromAnother(dateOfPurchase);
            fragmentOrderDetailsBinding.tvOrderDate.setText(formattedString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentOrderDetailsBinding.layoutProduct.tvProductName.setText(selectedProduct.getProductName());
        if (selectedOrder.getOrderTrackList() != null)
            fragmentOrderDetailsBinding.layoutProduct.tvOrderStatus.setText(selectedOrder.getOrderTrackList().get(0).getName());
        String strTotal = String.format("%.3f", selectedProduct.getAmount());
        fragmentOrderDetailsBinding.layoutProduct.tvAmount.setText("KWD " + strTotal);

    }
}
