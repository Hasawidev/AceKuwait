package com.alhasawi.acekuwait.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.databinding.LayoutConfirmedCartAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfirmedCartAdapter extends RecyclerView.Adapter<ConfirmedCartAdapter.ViewHolder> {
    List<ShoppingCartItem> cartedItemsList;
    DashboardActivity dashboardActivity;
    int cartCount = 0;

    public ConfirmedCartAdapter(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
        this.cartedItemsList = new ArrayList<>();
    }

    public abstract void onCartItemClicked(ShoppingCartItem cartItem);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutConfirmedCartAdapterItemBinding confirmedCartAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_confirmed_cart_adapter_item, parent, false);
        return new ViewHolder(confirmedCartAdapterItemBinding);
    }


    public void addAll(List<ShoppingCartItem> itemList) {
        cartedItemsList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.cartedItemsList = itemList;
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return cartedItemsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ShoppingCartItem shoppingCartItem = cartedItemsList.get(position);
            cartCount = shoppingCartItem.getQuantity();
            if (shoppingCartItem.getProduct().getDescriptions() != null && shoppingCartItem.getProduct().getDescriptions().size() > 0)
                holder.confirmedCartAdapterItemBinding.tvProductName.setText(shoppingCartItem.getProduct().getDescriptions().get(0).getProductName());
            //            if (shoppingCartItem.getProduct().getManufature() != null)
//                holder.cartAdapterItemBinding.tvBrand.setText(shoppingCartItem.getProduct().getManufature().getManufactureDescriptions().get(0).getName());

            if (shoppingCartItem.getProduct().getDiscountPercentage() != 0) {
                holder.confirmedCartAdapterItemBinding.tvOffer.setText("FLAT " + shoppingCartItem.getProduct().getDiscountPercentage() + "% OFF");
//                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getDiscountPrice());
            } else {
                holder.confirmedCartAdapterItemBinding.tvOffer.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getOriginalPrice());
            }
            String strDouble = String.format("%.3f", shoppingCartItem.getProduct().getOriginalPrice());
            holder.confirmedCartAdapterItemBinding.tvAmount.setText("KWD " + strDouble);
            Glide.with(dashboardActivity)
                    .load(shoppingCartItem.getProductConfigurable().getProductImages().get(0).getImageUrl())
                    .into(holder.confirmedCartAdapterItemBinding.imageViewProduct);

            holder.confirmedCartAdapterItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartItemClicked(shoppingCartItem);
                }
            });

            if (shoppingCartItem.getShippingMode() != null) {
                if (shoppingCartItem.getShippingMode().isPickup()) {
                    holder.confirmedCartAdapterItemBinding.cvStorePickup.setVisibility(View.VISIBLE);
                    holder.confirmedCartAdapterItemBinding.cvAvailabilityOptions.setVisibility(View.GONE);
                    holder.confirmedCartAdapterItemBinding.cvSelectedMode.setVisibility(View.GONE);
                    if (shoppingCartItem.getStore() != null) {
                        String storeDetails = "";
                        storeDetails = shoppingCartItem.getStore().getStoreName();
                        if (shoppingCartItem.getStore().getStoreAdress() != null)
                            storeDetails = storeDetails + ", " + shoppingCartItem.getStore().getStoreAdress();
                        holder.confirmedCartAdapterItemBinding.tvSelelctedStore.setText(storeDetails);
                    }
                } else {
                    holder.confirmedCartAdapterItemBinding.tvSelectedDeliveryMode.setText(shoppingCartItem.getShippingMode().getName());
                    holder.confirmedCartAdapterItemBinding.cvSelectedMode.setVisibility(View.VISIBLE);
                    holder.confirmedCartAdapterItemBinding.cvAvailabilityOptions.setVisibility(View.GONE);
                    holder.confirmedCartAdapterItemBinding.cvStorePickup.setVisibility(View.GONE);
                }
            } else {
                holder.confirmedCartAdapterItemBinding.cvAvailabilityOptions.setVisibility(View.VISIBLE);
                holder.confirmedCartAdapterItemBinding.cvSelectedMode.setVisibility(View.GONE);
                holder.confirmedCartAdapterItemBinding.cvStorePickup.setVisibility(View.GONE);
            }

            if (shoppingCartItem.getAddress() != null) {
                Address address = shoppingCartItem.getAddress();
                String addressString = address.getFirstName() + " " + address.getLastName() + "\n" + address.getStreet() + ", " + address.getBuildingType() + " " +
                        address.getBuildingNo() + ", " + address.getFlat() + ", " + address.getBlock() + ", " + address.getArea() + "\n" +
                        address.getMobile();
                holder.confirmedCartAdapterItemBinding.tvDeliverAddress.setText(addressString);
            }

            if (shoppingCartItem.getProduct().getProductOrigin() != null) {
                if (shoppingCartItem.getProduct().getProductOrigin().equalsIgnoreCase("VIRTUAL"))
                    holder.confirmedCartAdapterItemBinding.tvProductCategory.setText("BEYOND");
                else
                    holder.confirmedCartAdapterItemBinding.tvProductCategory.setText("ACE");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutConfirmedCartAdapterItemBinding confirmedCartAdapterItemBinding;

        public ViewHolder(@NonNull LayoutConfirmedCartAdapterItemBinding confirmedCartAdapterItemBinding) {
            super(confirmedCartAdapterItemBinding.getRoot());
            this.confirmedCartAdapterItemBinding = confirmedCartAdapterItemBinding;
        }
    }
}

