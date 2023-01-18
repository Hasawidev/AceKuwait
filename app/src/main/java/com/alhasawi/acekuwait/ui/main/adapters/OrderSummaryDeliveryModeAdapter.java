package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.databinding.LayoutOrderSummaryDeliverymodeAdapterItemBinding;

import java.util.ArrayList;

public class OrderSummaryDeliveryModeAdapter extends RecyclerView.Adapter<OrderSummaryDeliveryModeAdapter.ViewHolder> {
    ArrayList<ShippingMode> shippingModeArrayList;
    Context context;

    public OrderSummaryDeliveryModeAdapter(Context context, ArrayList<ShippingMode> shippingModes) {
        this.context = context;
        this.shippingModeArrayList = shippingModes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderSummaryDeliverymodeAdapterItemBinding orderSummaryDeliverymodeAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_summary_deliverymode_adapter_item, parent, false);
        return new ViewHolder(orderSummaryDeliverymodeAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryDeliveryModeAdapter.ViewHolder holder, int position) {
        holder.orderSummaryDeliverymodeAdapterItemBinding.tvDelivermode.setText(shippingModeArrayList.get(position).getName());
        String strDouble = String.format("%.3f", shippingModeArrayList.get(position).getCharge());
        holder.orderSummaryDeliverymodeAdapterItemBinding.tvDeliveryRate.setText("KWD " + strDouble);
    }

    @Override
    public int getItemCount() {
        return shippingModeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderSummaryDeliverymodeAdapterItemBinding orderSummaryDeliverymodeAdapterItemBinding;

        public ViewHolder(@NonNull LayoutOrderSummaryDeliverymodeAdapterItemBinding orderSummaryDeliverymodeAdapterItemBinding) {
            super(orderSummaryDeliverymodeAdapterItemBinding.getRoot());
            this.orderSummaryDeliverymodeAdapterItemBinding = orderSummaryDeliverymodeAdapterItemBinding;
        }

    }
}
