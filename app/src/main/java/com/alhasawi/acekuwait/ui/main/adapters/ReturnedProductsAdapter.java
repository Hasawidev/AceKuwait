package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.LayoutReturnProductBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ReturnedProductsAdapter extends RecyclerView.Adapter<ReturnedProductsAdapter.ViewHolder> {

    List<OrderProduct> productList = new ArrayList<>();
    List<String> returnReasons = new ArrayList<>();
    List<OrderProduct> selectedProductsForReturn = new ArrayList<>();
    Context context;

    public ReturnedProductsAdapter(Context context, List<OrderProduct> productList) {
        this.context = context;
        this.productList = productList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutReturnProductBinding layoutReturnProductBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_return_product, parent, false);
        return new ViewHolder(layoutReturnProductBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            OrderProduct product = productList.get(position);
            holder.layoutReturnProductBinding.tvProductName.setText(product.getProductName());
            String strDouble = String.format("%.3f", product.getAmount());
            holder.layoutReturnProductBinding.tvAmount.setText("KWD " + strDouble);
            Glide.with(context)
                    .load(product.getProductImage())
                    .into(holder.layoutReturnProductBinding.imageViewProduct);
            holder.layoutReturnProductBinding.tVProductColor.setText(product.getProductColorCode());
//            holder.layoutReturnProductBinding.tvProductSize.setText(product.getProductSize());
//            holder.layoutReturnProductBinding.tvItemQuantity.setText(product.getReturnQty());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutReturnProductBinding layoutReturnProductBinding;

        public ViewHolder(@NonNull LayoutReturnProductBinding layoutReturnProductBinding) {
            super(layoutReturnProductBinding.getRoot());
            this.layoutReturnProductBinding = layoutReturnProductBinding;
        }
    }
}
