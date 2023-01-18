package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.FragmentConfirmReturnBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.ReturnedProductsAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.google.gson.Gson;

import java.util.List;

public class ConfirmReturnFragment extends BaseFragment {
    FragmentConfirmReturnBinding fragmentConfirmReturnBinding;
    DashboardActivity dashboardActivity;
    Order returnedOrder;
    ReturnedProductsAdapter returnedProductsAdapter;
    List<OrderProduct> returnedProducts;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_confirm_return;
    }

    @Override
    protected void setup() {

        fragmentConfirmReturnBinding = (FragmentConfirmReturnBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            Bundle bundle = getArguments();
            String orderObject = bundle.getString("returned_order");
            Gson gson = new Gson();
            returnedOrder = gson.fromJson(orderObject, Order.class);
            fragmentConfirmReturnBinding.tvTicketNumber.setText(returnedOrder.getAwb());
            String strTotal = String.format("%.3f", returnedOrder.getTotal());
            fragmentConfirmReturnBinding.tvOrderAmount.setText("KWD " + strTotal);
            fragmentConfirmReturnBinding.tvIban.setText(returnedOrder.getIban());
            setUiValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentConfirmReturnBinding.cvContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.clearTopFragments();
                dashboardActivity.handleActionBarIcons(true);
            }
        });
    }

    private void setOrderUIValues() {
//        fragmentConfirmReturnBinding.layoutOrder.tvOrderId.setText(returnedOrder.getOrderId());
//        fragmentConfirmReturnBinding.layoutOrder.tvOrderAmount.setText("KWD " + returnedOrder.getTotal());
//
//        try {
//            String dateOfPurchase = returnedOrder.getDateOfPurchase();
//            String formattedString = changeDateFormatFromAnother(dateOfPurchase);
//            fragmentConfirmReturnBinding.layoutOrder.tvOrderedDate.setText(formattedString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            fragmentConfirmReturnBinding.layoutOrder.tvOrderQuantity.setText(returnedOrder.getOrderProducts().size() + "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fragmentConfirmReturnBinding.layoutOrder.tvOrderStatus.setText(returnedOrder.getOrderStatus());
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentConfirmReturnBinding.layoutOrder.recyclerviewProductList.getContext(),
//                LinearLayoutManager.HORIZONTAL,
//                false);
//
//        // Since this is a nested layout, so
//        // to define how many child items
//        // should be prefetched when the
//        // child RecyclerView is nested
//        // inside the parent RecyclerView,
//        // we use the following method
//        layoutManager.setInitialPrefetchItemCount(returnedOrder.getOrderProducts().size());
//
//        // Create an instance of the child
//        // item view adapter and set its
//        // adapter, layout manager and RecyclerViewPool
//        OrderHistoryProductImagesAdapter productImagesAdapter = new OrderHistoryProductImagesAdapter((ArrayList<OrderProduct>) returnedOrder.getOrderProducts(), getContext());
//        fragmentConfirmReturnBinding.layoutOrder.recyclerviewProductList.setLayoutManager(layoutManager);
//        fragmentConfirmReturnBinding.layoutOrder.recyclerviewProductList.setAdapter(productImagesAdapter);
//        fragmentConfirmReturnBinding.layoutOrder.recyclerviewProductList.setRecycledViewPool(viewPool);
    }

    private void setUiValues() {
//        try {
//
//            if (returnedOrder.getOrderReturnType().equals(OrderReturnType.FULL.name())) {
//                fragmentConfirmReturnBinding.lvOrder.setVisibility(View.VISIBLE);
//                fragmentConfirmReturnBinding.recyclerviewProducts.setVisibility(View.GONE);
//                setOrderUIValues();
//            } else if (returnedOrder.getOrderReturnType().equals(OrderReturnType.PARTIAL.name())) {
//                for (int i = 0; i < returnedOrder.getOrderProducts().size(); i++) {
//                    if (returnedOrder.getOrderProducts().get(i).isProductReturn())
//                        returnedProducts.add(returnedOrder.getOrderProducts().get(i));
//                }
//                returnedProductsAdapter = new ReturnedProductsAdapter(getContext(), returnedProducts);
//                fragmentConfirmReturnBinding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
//                fragmentConfirmReturnBinding.recyclerviewProducts.setAdapter(returnedProductsAdapter);
//                fragmentConfirmReturnBinding.lvOrder.setVisibility(View.GONE);
//                fragmentConfirmReturnBinding.recyclerviewProducts.setVisibility(View.VISIBLE);
//
//            }
//
//            if (returnedOrder.getReturnPayment().equals(ReturnPaymentType.KNET.name())) {
//                fragmentConfirmReturnBinding.cvBankDetails.setVisibility(View.VISIBLE);
//                fragmentConfirmReturnBinding.tvAccountNumber.setText(returnedOrder.getBankAccount());
//                fragmentConfirmReturnBinding.tvIbanNumber.setText(returnedOrder.getIban());
//            } else {
//                fragmentConfirmReturnBinding.cvBankDetails.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.setTitle("Return Confirmation");
    }
}
