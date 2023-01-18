package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.databinding.LayoutSubcategoryBinding;

import java.util.ArrayList;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    private ArrayList<Category> categoryArrayList;
    private Context context;


    public SubcategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categoryArrayList = categories;
        if (categoryArrayList == null)
            categoryArrayList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public SubcategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSubcategoryBinding subcategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_subcategory, parent, false);
        return new SubcategoryAdapter.ViewHolder(subcategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Category categoryItem = categoryArrayList.get(position);
            String categoryName = categoryItem.getDescriptions().get(0).getCategoryName();
            holder.subcategoryBinding.tvSubCategoryName.setText(categoryName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (categoryArrayList == null)
            return 0;
        else
            return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutSubcategoryBinding subcategoryBinding;

        public ViewHolder(@NonNull LayoutSubcategoryBinding subcategoryBinding) {
            super(subcategoryBinding.getRoot());
            this.subcategoryBinding = subcategoryBinding;
        }


    }
}
