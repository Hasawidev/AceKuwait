package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history;

import static com.alhasawi.acekuwait.utils.DateTimeUtils.changeDateFormatFromAnother;

import android.os.Bundle;
import android.view.View;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderTrack;
import com.alhasawi.acekuwait.databinding.FragmentTrackOrderBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.TrackOrderAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TrackOrderFragment extends BaseFragment {
    FragmentTrackOrderBinding fragmentTrackOrderBinding;
    TrackOrderAdapter trackOrderAdapter;
    Order selectedOrder;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_track_order;
    }

    @Override
    protected void setup() {
        fragmentTrackOrderBinding = (FragmentTrackOrderBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            String orderObject = bundle.getString("order_object");
            Gson gson = new Gson();
            selectedOrder = gson.fromJson(orderObject, Order.class);
            fragmentTrackOrderBinding.tvOrderId.setText(selectedOrder.getOrderId());
            try {
                String dateOfPurchase = selectedOrder.getDateOfPurchase();
//                String estimatedDelivery=selectedOrder.
                String formattedString = changeDateFormatFromAnother(dateOfPurchase);
                fragmentTrackOrderBinding.tvOrderDate.setText(formattedString);
                fragmentTrackOrderBinding.tvEstimatedDelivery.setText(formattedString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<OrderTrack> orderTrackList = selectedOrder.getOrderTrackList();
//            fragmentTrackOrderBinding.recyclerviewOrderStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
            trackOrderAdapter = new TrackOrderAdapter((ArrayList<OrderTrack>) orderTrackList, getActivity());
//            fragmentTrackOrderBinding.recyclerviewOrderStatus.setAdapter(trackOrderAdapter);
//            fragmentTrackOrderBinding.btnProductDelivered.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getParentFragmentManager().popBackStackImmediate();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentTrackOrderBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
        fragmentTrackOrderBinding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.clearTopFragments();
            }
        });
    }
}
