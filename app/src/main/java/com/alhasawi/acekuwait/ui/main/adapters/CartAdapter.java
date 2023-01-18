package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.databinding.LayoutCartAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<ShoppingCartItem> cartedItemsList;
    DashboardActivity dashboardActivity;
    int cartCount = 0;

    public CartAdapter(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
        this.cartedItemsList = new ArrayList<>();
    }

    public abstract void onItemDeleteClicked(ShoppingCartItem cartItem);

    public abstract void onDeliveryOptionsClicked(ShoppingCartItem cartItem, int adapterPosition);

    public abstract void onApplianceProtectionClicked(ShoppingCartItem cartItem, int adapterPosition);

    public abstract void cartItemsUpdated(ShoppingCartItem cartItem, int quantity, boolean isQuantityIncreased);

    public abstract void onCartItemClicked(ShoppingCartItem cartItem);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCartAdapterItemBinding cartAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_cart_adapter_item, parent, false);
        return new ViewHolder(cartAdapterItemBinding);
    }


    public void addAll(List<ShoppingCartItem> itemList) {
        cartedItemsList = new ArrayList<>();
        if (itemList != null && itemList.size() > 0) {
            this.cartedItemsList = itemList;
        }
        notifyDataSetChanged();
    }

    public void removeOneItem(ShoppingCartItem cartItem, int position) {
        if (cartedItemsList.contains(cartItem)) {
            cartedItemsList.remove(cartItem);
        }
        notifyItemRemoved(position);
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
            try {
                holder.cartAdapterItemBinding.tvProductName.setText(shoppingCartItem.getProduct().getDescriptions().get(0).getProductName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (shoppingCartItem.getQuantity() != null)
                holder.cartAdapterItemBinding.tvItemCount.setText(cartCount + "");
//            if (shoppingCartItem.getProduct().getManufature() != null)
//                holder.cartAdapterItemBinding.tvBrand.setText(shoppingCartItem.getProduct().getManufature().getManufactureDescriptions().get(0).getName());

            if (shoppingCartItem.getProduct().getDiscountPercentage() != 0) {
                holder.cartAdapterItemBinding.tvOffer.setText("FLAT " + shoppingCartItem.getProduct().getDiscountPercentage() + "% OFF");
//                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getDiscountPrice());
            } else {
                holder.cartAdapterItemBinding.tvOffer.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.tvOriginalPrice.setText("KWD " + shoppingCartItem.getProduct().getOriginalPrice());
            }
            String strDouble = String.format("%.3f", shoppingCartItem.getProduct().getOriginalPrice());
            holder.cartAdapterItemBinding.tvAmount.setText("KWD " + strDouble);
            Glide.with(dashboardActivity)
                    .load(shoppingCartItem.getProductConfigurable().getProductImages().get(0).getImageUrl())
                    .into(holder.cartAdapterItemBinding.imageViewProduct);

            holder.cartAdapterItemBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartItemClicked(shoppingCartItem);
                }
            });

            holder.cartAdapterItemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemDeleteClicked(shoppingCartItem);
                }
            });
            holder.cartAdapterItemBinding.cvAvailabilityOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeliveryOptionsClicked(shoppingCartItem, position);
                }
            });

            holder.cartAdapterItemBinding.cvSelectedMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeliveryOptionsClicked(shoppingCartItem, position);
                }
            });
            holder.cartAdapterItemBinding.cvApplianceProtection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onApplianceProtectionClicked(shoppingCartItem, position);
                }
            });

//            if (shoppingCartItem.isOutOfStock()) {
//                holder.cartRecyclerItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
//                holder.cartRecyclerItemBinding.txtAmountToPay.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.tvAmountToPay.setVisibility(View.GONE);
//            } else {
//                holder.cartRecyclerItemBinding.tvOutOfStock.setVisibility(View.GONE);
//                holder.cartRecyclerItemBinding.txtAmountToPay.setVisibility(View.VISIBLE);
//                holder.cartRecyclerItemBinding.tvAmountToPay.setVisibility(View.VISIBLE);
//            }
            holder.cartAdapterItemBinding.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartCount = Integer.parseInt(holder.cartAdapterItemBinding.tvItemCount.getText().toString());
                    if (shoppingCartItem.getProductConfigurable().getQuantity() >= 1 && cartCount < shoppingCartItem.getProductConfigurable().getQuantity()) {
                        if (cartCount >= 0)
                            cartCount++;
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog(dashboardActivity.getResources().getString(R.string.error), dashboardActivity.getResources().getString(R.string.max_qty_reached));
                            generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    holder.cartAdapterItemBinding.tvItemCount.setText(cartCount + "");
                    cartItemsUpdated(shoppingCartItem, cartCount, true);
                }
            });
            holder.cartAdapterItemBinding.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartCount = Integer.parseInt(holder.cartAdapterItemBinding.tvItemCount.getText().toString());
                    if (cartCount > 1) {
                        cartCount--;
                        holder.cartAdapterItemBinding.tvItemCount.setText(cartCount + "");
                        cartItemsUpdated(shoppingCartItem, cartCount, false);
                    }
                }
            });
            if (shoppingCartItem.getShippingMode() != null) {
                if (shoppingCartItem.getShippingMode().isPickup()) {
                    holder.cartAdapterItemBinding.cvStorePickup.setVisibility(View.VISIBLE);
                    holder.cartAdapterItemBinding.cvAvailabilityOptions.setVisibility(View.GONE);
                    holder.cartAdapterItemBinding.cvSelectedMode.setVisibility(View.GONE);
                    if (shoppingCartItem.getStore() != null) {
                        String storeDetails = "";
                        storeDetails = shoppingCartItem.getStore().getStoreName();
                        if (shoppingCartItem.getStore().getStoreAdress() != null)
                            storeDetails = storeDetails + ", " + shoppingCartItem.getStore().getStoreAdress();
                        holder.cartAdapterItemBinding.tvSelelctedStore.setText(storeDetails);
                    }
                } else {
                    holder.cartAdapterItemBinding.tvSelectedDeliveryMode.setText(shoppingCartItem.getShippingMode().getName());
                    holder.cartAdapterItemBinding.cvSelectedMode.setVisibility(View.VISIBLE);
                    holder.cartAdapterItemBinding.cvAvailabilityOptions.setVisibility(View.GONE);
                    holder.cartAdapterItemBinding.cvStorePickup.setVisibility(View.GONE);
//                    if (shoppingCartItem.getAddress() != null) {
//                        Address address = shoppingCartItem.getAddress();
//                        String addressString = address.getFirstName() + " " + address.getLastName() + "\n" + address.getStreet() + ", " + address.getBuildingType() + " " +
//                                address.getBuildingNo() + ", " + address.getFlat() + ", " + address.getBlock() + ", " + address.getArea() + "\n" +
//                                address.getMobile();
//                        if (!addressString.equalsIgnoreCase(""))
//                            holder.cartAdapterItemBinding.tvDeliverAddress.setText(addressString);
//                        else
//                            holder.cartAdapterItemBinding.tvDeliverAddress.setText(dashboardActivity.getResources().getString(R.string.click_to_see_delivery));

//                    } else {
//                        holder.cartAdapterItemBinding.tvDeliverAddress.setText(dashboardActivity.getResources().getString(R.string.click_to_see_delivery));
//                    }
                }
            } else {


                holder.cartAdapterItemBinding.cvAvailabilityOptions.setVisibility(View.VISIBLE);
                holder.cartAdapterItemBinding.cvSelectedMode.setVisibility(View.GONE);
                holder.cartAdapterItemBinding.cvStorePickup.setVisibility(View.GONE);
            }

            if (shoppingCartItem.getAddress() != null) {
                Address address = shoppingCartItem.getAddress();
                String addressString = address.getFirstName() + " " + address.getLastName() + "\n" + address.getStreet() + ", " + address.getBuildingType() + " " +
                        address.getBuildingNo() + ", " + address.getFlat() + ", " + address.getBlock() + ", " + address.getArea() + "\n" +
                        address.getMobile();
                if (!addressString.equalsIgnoreCase(""))
                    holder.cartAdapterItemBinding.tvDeliverAddress.setText(addressString);
                else
                    holder.cartAdapterItemBinding.tvDeliverAddress.setText(dashboardActivity.getResources().getString(R.string.click_to_see_delivery));
            } else {
                holder.cartAdapterItemBinding.tvDeliverAddress.setText(dashboardActivity.getResources().getString(R.string.click_to_see_delivery));
            }
            if (shoppingCartItem.getProduct().getProductOrigin() != null) {
                if (shoppingCartItem.getProduct().getProductOrigin().equalsIgnoreCase("VIRTUAL"))
                    holder.cartAdapterItemBinding.tvProductCategory.setText("BEYOND");
                else
                    holder.cartAdapterItemBinding.tvProductCategory.setText("ACE");
            }
            if (shoppingCartItem.isOutOfStock())
                holder.cartAdapterItemBinding.tvOutOfStock.setVisibility(View.VISIBLE);
            else
                holder.cartAdapterItemBinding.tvOutOfStock.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(int position) {
        cartedItemsList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ShoppingCartItem item, int position) {
        cartedItemsList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCartAdapterItemBinding cartAdapterItemBinding;

        public ViewHolder(@NonNull LayoutCartAdapterItemBinding cartAdapterItemBinding) {
            super(cartAdapterItemBinding.getRoot());
            this.cartAdapterItemBinding = cartAdapterItemBinding;
        }
    }
}
