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
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderTrack;
import com.alhasawi.acekuwait.databinding.LayoutOrderHistoryRecyclerItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public abstract class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    ArrayList<Order> orderList;
    Context context;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private LinearLayoutManager layoutManager;

    public OrderHistoryAdapter(Context context) {
        this.orderList = new ArrayList<>();
        this.context = context;
    }

    public abstract void onCancelOrderClicked(Order order);

    public abstract void onProductClicked(Order order, OrderProduct product);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderHistoryRecyclerItemBinding orderHistoryRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_history_recycler_item, parent, false);
        return new ViewHolder(orderHistoryRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order orderItem = orderList.get(position);
        holder.orderHistoryRecyclerItemBinding.tvOrderId.setText(orderItem.getOrderId());
        String strDouble = String.format("%.3f", orderItem.getTotal());
        holder.orderHistoryRecyclerItemBinding.tvOrderAmount.setText("KWD " + strDouble);

        try {
            String dateOfPurchase = orderItem.getDateOfPurchase();
            String formattedString = changeDateFormatFromAnother(dateOfPurchase);
            holder.orderHistoryRecyclerItemBinding.tvOrderedDate.setText(formattedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.orderHistoryRecyclerItemBinding.tvOrderQuantity.setText(orderItem.getOrderProducts().size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<OrderTrack> orderTrackList = orderItem.getOrderTrackList();
        String currentStatus = "";
        switch (orderTrackList.size()) {
            case 1:
                currentStatus = "Order Placed";
                break;
            case 2:
                currentStatus = "In Process";
                break;
            case 3:
                currentStatus = "In Transit";
                break;
            case 4:
                currentStatus = "Delivered";
                break;
            default:
                break;
        }
        holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setText(currentStatus);
        switch (currentStatus) {
            case "Order Placed":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.order_placed, 0, 0, 0);
                break;
            case "In Process":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ongoing, 0, 0, 0);
                break;
            case "In Transit":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.in_transit, 0, 0, 0);
                break;
            case "Delivered":
                holder.orderHistoryRecyclerItemBinding.tvOrderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.delivered, 0, 0, 0);
                break;
            default:
                break;
        }

        holder.orderHistoryRecyclerItemBinding.cvCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrderClicked(orderItem);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.getContext(),
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
        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.setLayoutManager(layoutManager);
        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.setAdapter(productImagesAdapter);
        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.setRecycledViewPool(viewPool);

        holder.orderHistoryRecyclerItemBinding.recyclerviewProductList.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onProductClicked(orderItem, orderItem.getOrderProducts().get(position));
            }
        }));

    }

    public void addAll(ArrayList<Order> orders) {
        if (orders != null && orders.size() > 0) {
            this.orderList = orders;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderHistoryRecyclerItemBinding orderHistoryRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutOrderHistoryRecyclerItemBinding orderHistoryRecyclerItemBinding) {
            super(orderHistoryRecyclerItemBinding.getRoot());
            this.orderHistoryRecyclerItemBinding = orderHistoryRecyclerItemBinding;
        }
    }

}