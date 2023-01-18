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
import com.alhasawi.acekuwait.databinding.LayoutProductItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class RelatedProductsRecyclerviewAdapter extends RecyclerView.Adapter<RelatedProductsRecyclerviewAdapter.ViewHolder> {
    ArrayList<Product> productArrayList;
    Context context;

    public RelatedProductsRecyclerviewAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.productArrayList = products;
    }

    public abstract void onLikeClicked(Product product, boolean isWishlisted);

    public abstract void onCartClciked(Product product);

    public abstract void onItemClicked(Product productContent);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProductItemBinding productItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_product_item, parent, false);
        return new ViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Product productContent = productArrayList.get(position);
            if (productContent.getWishlist())
                holder.productItemBinding.radioButtonWishlist.setChecked(true);
            else
                holder.productItemBinding.radioButtonWishlist.setChecked(false);

            try {
                if (productContent.getDescriptions().size() > 0) {
                    holder.productItemBinding.tvProductName.setText(productContent.getDescriptions().get(0).getProductName() + "\n\n");
                }

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


//                if (productContent.getDiscountPercentage() != null && productContent.getDiscountPercentage() != 0) {
//
//                    productItemBinding.tvOfferPercent.setText(productContent.getDiscountPercentage() + "%");
//                    productItemBinding.tvOfferPercent.setVisibility(View.VISIBLE);
//                    if (productContent.getDiscountPrice() > 0) {
//                        productItemBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
//                        String strDiscounted = String.format("%.3f", productContent.getDiscountPrice());
//                        productItemBinding.tvOurPrice.setText("KWD " + productContent.getDiscountPrice());
//                        productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getOriginalPrice());
//                        productItemBinding.tvOriginalPrice.setPaintFlags(productItemBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    }
//                } else {
//                    productItemBinding.tvOfferPercent.setVisibility(View.GONE);
//                    productItemBinding.tvOriginalPrice.setText("KWD " + productContent.getOriginalPrice());
//                }
            } catch (Exception e) {
                e.printStackTrace();
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
                    onLikeClicked(productContent, holder.productItemBinding.radioButtonWishlist.isChecked());
                }
            });

            holder.productItemBinding.imageViewAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartClciked(productContent);
                }
            });

            holder.productItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(productContent);
                }
            });
//            if (productContent.getManufature() != null)
//                productItemBinding.tvBrandName.setText(productContent.getManufature().getManufactureDescriptions().get(0).getName());
            if (!productContent.isAvailable()) {
                holder.productItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
                holder.productItemBinding.tvOfferPercent.setVisibility(View.GONE);
            }

            if (!productContent.isAvailable()) {
                if (productContent.getEta() != null && !productContent.getEta().equals("")) {
                    holder.productItemBinding.tvOutOfStock.setText(productContent.getEta());
                    holder.productItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
                    holder.productItemBinding.tvRibbon.setVisibility(View.GONE);
                } else {
                    holder.productItemBinding.tvOutOfStock.setVisibility(View.GONE);

                }
            } else {
                holder.productItemBinding.tvOutOfStock.setVisibility(View.GONE);

            }

            if (productContent.isAvailable() && productContent.getDiscountPercentage() > 0) {
                holder.productItemBinding.tvRibbon.setText("");
                String text = holder.productItemBinding.tvRibbon.getText().toString();
                String newText = "";
                newText = new StringBuilder(text).append("S").toString();
                newText = new StringBuilder(newText).append("\n").toString();
                newText = new StringBuilder(newText).append("A").toString();
                newText = new StringBuilder(newText).append("\n").toString();
                newText = new StringBuilder(newText).append("L").toString();
                newText = new StringBuilder(newText).append("\n").toString();
                newText = new StringBuilder(newText).append("E").toString();

                holder.productItemBinding.tvRibbon.setText(newText);
                holder.productItemBinding.tvRibbon.setVisibility(View.VISIBLE);
            } else {
                holder.productItemBinding.tvRibbon.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public void addAll(List<Product> list) {
        productArrayList.addAll(list);
    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public void clear() {
        productArrayList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutProductItemBinding productItemBinding;

        public ViewHolder(@NonNull LayoutProductItemBinding layoutProductItemBinding) {
            super(layoutProductItemBinding.getRoot());
            this.productItemBinding = layoutProductItemBinding;
        }

    }

}
