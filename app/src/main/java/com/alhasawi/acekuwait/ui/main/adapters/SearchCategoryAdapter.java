package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductCategory;
import com.alhasawi.acekuwait.databinding.LayoutSearchCategoryAdapterItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> {
    List<SearchProductCategory> searchProductCategoryList;
    Context context;

    public SearchCategoryAdapter(Context context) {
        this.context = context;
        this.searchProductCategoryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSearchCategoryAdapterItemBinding searchCategoryAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_search_category_adapter_item, parent, false);
        return new ViewHolder(searchCategoryAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.searchCategoryAdapterItemBinding.tvName.setText(searchProductCategoryList.get(position).getCategoryName());
//        holder.searchProductItemBinding.tvProductName.setText(productSearchList.get(position).getNameEn()+"\n");
////        holder.searchProductItemBinding.tvProductDescrption.setText(productSearchList.get(position).getBrand());
//        Glide.with(context)
//                .load(productSearchList.get(position).getImageUrl())
//                .into(holder.searchProductItemBinding.imageViewProduct);
//        String strDouble = String.format("%.3f", productSearchList.get(position).getPrice());
//        holder.searchProductItemBinding.tvPrice.setText("KWD " + strDouble);
    }

    @Override
    public int getItemCount() {
        return searchProductCategoryList.size();
    }


    public void addAll(List<SearchProductCategory> itemList) {
        searchProductCategoryList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.searchProductCategoryList = itemList;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutSearchCategoryAdapterItemBinding searchCategoryAdapterItemBinding;

        public ViewHolder(@NonNull LayoutSearchCategoryAdapterItemBinding searchCategoryAdapterItemBinding) {
            super(searchCategoryAdapterItemBinding.getRoot());
            this.searchCategoryAdapterItemBinding = searchCategoryAdapterItemBinding;
        }
    }
}
