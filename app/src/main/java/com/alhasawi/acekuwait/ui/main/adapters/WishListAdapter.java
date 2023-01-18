package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.model.pojo.Wishlist;
import com.alhasawi.acekuwait.databinding.LayoutProductItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    List<Wishlist> wishlists;
    Context context;

    public WishListAdapter(Context context) {
        this.context = context;
        this.wishlists = new ArrayList<>();
    }

    public abstract void onWishListClicked(Product selectedProduct, boolean isChecked, int position);

    public abstract void onItemClicked(Product selectedProduct);

    public abstract void onAddToCartClicked(Product productContent);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProductItemBinding productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_product_item, parent, false);
        return new ViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wishlist wishlistItem = wishlists.get(position);
        Product productContent = wishlistItem.getProduct();
        if (productContent.getWishlist())
            holder.productItemBinding.radioButtonWishlist.setChecked(true);
        else
            holder.productItemBinding.radioButtonWishlist.setChecked(false);
        if (productContent.getDescriptions().size() > 0) {
            holder.productItemBinding.tvProductName.setText(productContent.getDescriptions().get(0).getProductName());
        }

//        try {
//            String strDouble = String.format("%.3f", productContent.getOriginalPrice());
//            holder.productItemBinding.tvOurPrice.setText("KWD " + strDouble);
//            if (productContent.getDiscountPercentage() != null && productContent.getDiscountPercentage() != 0) {
//                holder.productItemBinding.tvOfferPercent.setText(productContent.getDiscountPercentage() + "%");
//                holder.productItemBinding.tvOfferPercent.setVisibility(View.VISIBLE);
//                if (productContent.getDiscountPrice() > 0) {
//                    holder.productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getDiscountPrice());
//                    holder.productItemBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
//                    holder.productItemBinding.tvOriginalPrice.setPaintFlags(holder.productItemBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                }
//            } else {
//                holder.productItemBinding.tvOfferPercent.setVisibility(View.GONE);
//                holder.productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getOriginalPrice());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String strOriginalPrice = String.format("%.3f", productContent.getOriginalPrice());
        if (productContent.getDiscountPrice() != null && productContent.getDiscountPrice() > 0) {
            holder.productItemBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
            String strDiscounted = String.format("%.3f", productContent.getDiscountPrice());
            holder.productItemBinding.tvOurPrice.setText("KWD " + strOriginalPrice);
            holder.productItemBinding.tvOriginalPrice.setText("KWD " + strDiscounted);
            holder.productItemBinding.tvOriginalPrice.setPaintFlags(holder.productItemBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.productItemBinding.tvOurPrice.setText("KWD " + strOriginalPrice);
        }

        try {
            Glide.with(context)
                    .load(productContent.getProductConfigurables().get(0).getProductImages().get(0).getImageUrl())
                    .into(holder.productItemBinding.imageViewProductImage);
            Glide.with(context)
                    .load(productContent.getBrandLogoUrl())
                    .into(holder.productItemBinding.imageViewBrangLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.productItemBinding.radioButtonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWishListClicked(productContent, holder.productItemBinding.radioButtonWishlist.isChecked(), position);
            }
        });

        holder.productItemBinding.imageViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCartClicked(productContent);
            }
        });

        holder.productItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(productContent);
            }
        });
//        if (productContent.getManufature() != null)
//            holder.productItemBinding.tvBrandName.setText(productContent.getManufature().getManufactureDescriptions().get(0).getName());
        if (!productContent.isAvailable()) {
            if (productContent.getEta() != null && !productContent.getEta().equals("")) {
                holder.productItemBinding.tvOutOfStock.setText(productContent.getEta());
                holder.productItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
            } else {
                holder.productItemBinding.tvOutOfStock.setVisibility(View.GONE);
            }
        } else {
            holder.productItemBinding.tvOutOfStock.setVisibility(View.GONE);
        }
    }

    public void addAll(List<Wishlist> wishlistList) {
        if (wishlistList != null && wishlistList.size() > 0) {
            wishlists = wishlistList;
        }
        notifyDataSetChanged();
    }

    public void removeItemFromWishList(Product product, int position) {
        if (wishlists.contains(product))
            wishlists.remove(product);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutProductItemBinding productItemBinding;

        public ViewHolder(@NonNull LayoutProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());
            this.productItemBinding = productItemBinding;
        }
    }
}
