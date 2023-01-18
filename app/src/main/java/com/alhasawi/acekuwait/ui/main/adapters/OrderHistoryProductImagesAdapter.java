package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.LayoutOrderHistoryImageItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderHistoryProductImagesAdapter extends RecyclerView.Adapter<OrderHistoryProductImagesAdapter.ViewHolder> {
    ArrayList<OrderProduct> orderProductArrayList;
    Context context;

    public OrderHistoryProductImagesAdapter(ArrayList<OrderProduct> orderProducts, Context context) {
        this.orderProductArrayList = orderProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderHistoryImageItemBinding imageItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_order_history_image_item, parent, false);
        return new ViewHolder(imageItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            OrderProduct orderProduct = orderProductArrayList.get(position);
            holder.imageItemBinding.tvProductName.setText(orderProduct.getProductName());
            holder.imageItemBinding.tvProductPrice.setText("KWD " + orderProduct.getOneTimePrice());
            Glide.with(context)
                    .load(orderProductArrayList.get(position).getProductImage())
                    .into(holder.imageItemBinding.imageViewProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return orderProductArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutOrderHistoryImageItemBinding imageItemBinding;

        public ViewHolder(@NonNull LayoutOrderHistoryImageItemBinding imageItemBinding) {
            super(imageItemBinding.getRoot());
            this.imageItemBinding = imageItemBinding;
        }
    }
}