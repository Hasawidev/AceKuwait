package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.LayoutCheckoutProductListItemBinding;

import java.util.ArrayList;

public class CheckoutProductListAdapter extends RecyclerView.Adapter<CheckoutProductListAdapter.ViewHolder> {
    ArrayList<OrderProduct> productArrayList;
    Context context;

    public CheckoutProductListAdapter(ArrayList<OrderProduct> products, Context context) {
        this.productArrayList = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutCheckoutProductListItemBinding checkoutProductListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.layout_checkout_product_list_item, viewGroup, false);
        return new ViewHolder(checkoutProductListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProduct product = productArrayList.get(position);
        holder.checkoutProductListItemBinding.tvProductName.setText(product.getProductName());
        String strDouble = String.format("%.3f", product.getAmount());
        holder.checkoutProductListItemBinding.tvProductPrice.setText("KWD " + strDouble);
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCheckoutProductListItemBinding checkoutProductListItemBinding;

        public ViewHolder(LayoutCheckoutProductListItemBinding checkoutProductListItemBinding) {
            super(checkoutProductListItemBinding.getRoot());
            this.checkoutProductListItemBinding = checkoutProductListItemBinding;
        }
    }
}
