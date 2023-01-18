package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.databinding.LayoutSubcategoryRecyclerItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    private ArrayList<Category> categoryArrayList;
    private Context context;


    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categoryArrayList = categories;
        if (categoryArrayList == null)
            categoryArrayList = new ArrayList<>();
        this.context = context;
    }

    public static void setsSelected(int sSelected) {
        CategoryAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSubcategoryRecyclerItemBinding subcategoryRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_subcategory_recycler_item, parent, false);
        return new ViewHolder(subcategoryRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Category categoryItem = categoryArrayList.get(position);
            Glide.with(context)
                    .load(categoryItem.getDescriptions().get(0).getImageUrl())
                    .into(holder.subcategoryRecyclerItemBinding.imageViewCategory);
//            String categoryName = categoryItem.getDescriptions().get(0).getCategoryName();
//            holder.subcategoryRecyclerItemBinding.tvSubCategory.setText(categoryName);
//            if (sSelected == position) {
//                selectItem(holder, categoryItem);
//            } else {
//                unselectItem(holder, categoryItem);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void unselectItem(ViewHolder holder, Category adapterItem) {
//        holder.subcategoryRecyclerItemBinding.tvSubCategory.setBackgroundColor(context.getResources().getColor(R.color.white));
//        holder.subcategoryRecyclerItemBinding.tvSubCategory.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
    }

    public void selectItem(ViewHolder holder, Category adapterItem) {
//        holder.subcategoryRecyclerItemBinding.tvSubCategory.setBackgroundColor(context.getResources().getColor(R.color.txt_clr_blue));
//        holder.subcategoryRecyclerItemBinding.tvSubCategory.setTextColor(context.getResources().getColor(R.color.white));
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

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LayoutSubcategoryRecyclerItemBinding subcategoryRecyclerItemBinding;

        public ViewHolder(@NonNull LayoutSubcategoryRecyclerItemBinding subcategoryRecyclerItemBinding) {
            super(subcategoryRecyclerItemBinding.getRoot());
            this.subcategoryRecyclerItemBinding = subcategoryRecyclerItemBinding;
            subcategoryRecyclerItemBinding.imageViewCategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}