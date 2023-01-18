package com.alhasawi.acekuwait.ui.main.adapters;

import static com.alhasawi.acekuwait.utils.DateTimeUtils.changeDateFormatFromAnother;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.OrderReturnType;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.LayoutOrderReturnBinding;

import java.util.ArrayList;
import java.util.List;


public abstract class ReturnOrdersAdapter extends RecyclerView.Adapter<ReturnOrdersAdapter.ViewHolder> {

    List<Order> returnOrderList = new ArrayList<>();
    Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public ReturnOrdersAdapter(Context context) {
        this.context = context;
    }

    public abstract void onProductReturn(Order selectedOrder);

    public abstract void onFullOrderReturn(Order selectedOrder);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderReturnBinding orderReturnBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_return, parent, false);
        return new ViewHolder(orderReturnBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order orderItem = returnOrderList.get(position);
        holder.orderReturnBinding.tvOrderId.setText(orderItem.getOrderId());
        String strTotal = String.format("%.3f", orderItem.getTotal());
        holder.orderReturnBinding.tvOrderAmount.setText("KWD " + strTotal);

        try {
            String dateOfPurchase = orderItem.getDateOfPurchase();
            String formattedString = changeDateFormatFromAnother(dateOfPurchase);
            holder.orderReturnBinding.tvOrderedDate.setText(formattedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.orderReturnBinding.tvOrderQuantity.setText(orderItem.getOrderProducts().size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String currentStatus = orderItem.getOrderStatus();
        holder.orderReturnBinding.tvOrderStatus.setText(currentStatus);
        switch (currentStatus.toLowerCase()) {
            case "delivered":
                holder.orderReturnBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.delivered, 0, 0, 0);
                break;
            default:
                break;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.orderReturnBinding.recyclerviewProductList.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method
        layoutManager.setInitialPrefetchItemCount(orderItem.getOrderProducts().size());

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        OrderHistoryProductImagesAdapter productImagesAdapter = new OrderHistoryProductImagesAdapter((ArrayList<OrderProduct>) orderItem.getOrderProducts(), context);
        holder.orderReturnBinding.recyclerviewProductList.setLayoutManager(layoutManager);
        holder.orderReturnBinding.recyclerviewProductList.setAdapter(productImagesAdapter);
        holder.orderReturnBinding.recyclerviewProductList.setRecycledViewPool(viewPool);

        holder.orderReturnBinding.tvOrderReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFullOrderReturn(orderItem);
            }
        });
//        holder.orderReturnBinding.tvProductReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onProductReturn(orderItem);
//            }
//        });

        if (orderItem.getOrderReturnType() != null) {
            if (orderItem.getOrderReturnType().equals(OrderReturnType.FULL.name())) {
                holder.orderReturnBinding.tvOrderReturn.setVisibility(View.GONE);
//                holder.orderReturnBinding.tvProductReturn.setVisibility(View.GONE);
            } else {
                holder.orderReturnBinding.tvOrderReturn.setVisibility(View.VISIBLE);
//                holder.orderReturnBinding.tvProductReturn.setVisibility(View.VISIBLE);
            }
        } else {
            holder.orderReturnBinding.tvOrderReturn.setVisibility(View.VISIBLE);
//            holder.orderReturnBinding.tvProductReturn.setVisibility(View.VISIBLE);
        }


    }

    public void addAll(List<Order> orderList) {
        returnOrderList.clear();
        if (orderList.size() > 0 && orderList != null) {
            returnOrderList.addAll(orderList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return returnOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderReturnBinding orderReturnBinding;

        public ViewHolder(@NonNull LayoutOrderReturnBinding orderReturnBinding) {
            super(orderReturnBinding.getRoot());
            this.orderReturnBinding = orderReturnBinding;
        }
    }
}

