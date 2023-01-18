package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.databinding.FragmentReturnReasonBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.AceReturnProductsAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ReturnViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SelectReturnReasonFragment extends BaseFragment {

    FragmentReturnReasonBinding fragmentReturnReasonBinding;
    DashboardActivity dashboardActivity;
    String objString = "";
    Bundle returnBundle;
    ReturnViewModel returnViewModel;
    AceReturnProductsAdapter aceReturnProductsAdapter;
    List<String> returnReasonList = new ArrayList<>();
    private Order selectedOrder;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_return_reason;
    }

    @Override
    protected void setup() {

        fragmentReturnReasonBinding = (FragmentReturnReasonBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        returnViewModel = new ViewModelProvider(this).get(ReturnViewModel.class);

        try {
            returnBundle = getArguments();
            if (returnBundle != null) {
                Gson gson = new Gson();
                objString = returnBundle.getString("selected_order");
                selectedOrder = gson.fromJson(objString, Order.class);
//                setOrderUIValues();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        getReturnReasons();

//        fragmentReturnReasonBinding.spinnerReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (selectedOrder != null) {
//                    selectedOrder.setReturnReason(returnReasonList.get(position));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        fragmentReturnReasonBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOrder.setOrderReturn(true);
//                selectedOrder.setOrderReturnType(OrderReturnType.FULL.name());
                Gson gson = new Gson();
                objString = gson.toJson(selectedOrder);
                returnBundle.putString("selected_order", objString);
//                dashboardActivity.replaceFragment(R.id.fragment_replacer, new com.hasawi.sears_outlet.ui.main.view.dashboard.user_settings.user_account.my_returns.SelectRefundModeFragment(), returnBundle, true, false);
            }
        });

    }

//    private void setOrderUIValues() {
//        fragmentReturnReasonBinding.tvOrderId.setText(selectedOrder.getOrderId());
//        fragmentReturnReasonBinding.tvOrderAmount.setText("KWD " + selectedOrder.getTotal());
//
//        try {
//            String dateOfPurchase = selectedOrder.getDateOfPurchase();
//            String formattedString = changeDateFormatFromAnother(dateOfPurchase);
//            fragmentReturnReasonBinding.tvOrderedDate.setText(formattedString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            fragmentReturnReasonBinding.tvOrderQuantity.setText(selectedOrder.getOrderProducts().size() + "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        fragmentReturnReasonBinding.tvOrderStatus.setText(selectedOrder.getOrderStatus());
//        switch (selectedOrder.getOrderStatus().toLowerCase()) {
//            case "delivered":
//                fragmentReturnReasonBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.delivered, 0, 0, 0);
//                break;
//            default:
//                break;
//        }
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentReturnReasonBinding.recyclerviewProductList.getContext(),
//                LinearLayoutManager.HORIZONTAL,
//                false);
//
//        // Since this is a nested layout, so
//        // to define how many child items
//        // should be prefetched when the
//        // child RecyclerView is nested
//        // inside the parent RecyclerView,
//        // we use the following method
//        layoutManager.setInitialPrefetchItemCount(selectedOrder.getOrderProducts().size());
//
//        // Create an instance of the child
//        // item view adapter and set its
//        // adapter, layout manager and RecyclerViewPool
//        OrderHistoryProductImagesAdapter productImagesAdapter = new OrderHistoryProductImagesAdapter((ArrayList<OrderProduct>) selectedOrder.getOrderProducts(), getContext());
//        fragmentReturnReasonBinding.recyclerviewProductList.setLayoutManager(layoutManager);
//        fragmentReturnReasonBinding.recyclerviewProductList.setAdapter(productImagesAdapter);
//        fragmentReturnReasonBinding.recyclerviewProductList.setRecycledViewPool(viewPool);
//    }
//
//
//    private void getReturnReasons() {
//        fragmentReturnReasonBinding.progressBar.setVisibility(View.VISIBLE);
//        returnViewModel.getReturnReasons().observe(this, returnReasonResponseResource -> {
//            switch (returnReasonResponseResource.status) {
//                case SUCCESS:
//                    returnReasonList = returnReasonResponseResource.data.getReturnReasons();
//                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, returnReasonList);
//                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    fragmentReturnReasonBinding.spinnerReasons.setAdapter(arrayAdapter);
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    GeneralDialog generalDialog = new GeneralDialog("Error", returnReasonResponseResource.message);
//                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
//                    break;
//            }
//            fragmentReturnReasonBinding.progressBar.setVisibility(View.GONE);
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.setTitle("My Returns");
    }
}
