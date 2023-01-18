package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductSearch;
import com.alhasawi.acekuwait.databinding.LayoutSearchCardItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {
    List<ProductSearch> productSearchList;
    Context context;

    public SearchProductAdapter(Context context) {
        this.context = context;
        this.productSearchList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSearchCardItemBinding searchProductItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_search_card_item, parent, false);
        return new ViewHolder(searchProductItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.searchProductItemBinding.tvProductName.setText(productSearchList.get(position).getNameEn() + "\n");
//        holder.searchProductItemBinding.tvProductDescrption.setText(productSearchList.get(position).getBrand());
        Glide.with(context)
                .load(productSearchList.get(position).getImageUrl())
                .into(holder.searchProductItemBinding.imageViewProduct);
        String strDouble = String.format("%.3f", productSearchList.get(position).getPrice());
        holder.searchProductItemBinding.tvPrice.setText("KWD " + strDouble);
    }

    @Override
    public int getItemCount() {
        return productSearchList.size();
    }

    public void addAll(List<ProductSearch> itemList) {
        productSearchList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.productSearchList = itemList;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSearchCardItemBinding searchProductItemBinding;

        public ViewHolder(@NonNull LayoutSearchCardItemBinding searchProductItemBinding) {
            super(searchProductItemBinding.getRoot());
            this.searchProductItemBinding = searchProductItemBinding;
        }
    }
}
