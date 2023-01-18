package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.databinding.LayoutAddressAdapterItemBinding;
import com.alhasawi.acekuwait.databinding.LayoutAddressBookAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.utils.AppConstants;

import java.util.ArrayList;

public abstract class ShippingAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    ArrayList<Address> addressArrayList;
    Context context;
    private int ADDRESS_VIEW_TYPE;


    public ShippingAddressAdapter(Context context, ArrayList<Address> addresses, int VIEW_TYPE) {
        this.context = context;
        this.addressArrayList = addresses;
        this.ADDRESS_VIEW_TYPE = VIEW_TYPE;
    }

    public static void setsSelected(int sSelected) {
        ShippingAddressAdapter.sSelected = sSelected;
    }

    public abstract void onEditClicked(Address address);

    public abstract void onDeleteClicked(Address address);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (ADDRESS_VIEW_TYPE) {
            case AppConstants.ADDRESS_VIEW_TYPE_ADDRESSBOOK:
                LayoutAddressBookAdapterItemBinding addressBookAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_address_book_adapter_item, parent, false);
                return new AddressbookViewHolder(addressBookAdapterItemBinding);
            case AppConstants.ADDRESS_VIEW_TYPE_CHECKOUT:
                LayoutAddressAdapterItemBinding addressAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_address_adapter_item, parent, false);
                return new CheckoutAddressViewHolder(addressAdapterItemBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Address addressItem = addressArrayList.get(position);
        String firstName = "", lastName = "";
        switch (ADDRESS_VIEW_TYPE) {

            case AppConstants.ADDRESS_VIEW_TYPE_ADDRESSBOOK:
                AddressbookViewHolder addressbookViewHolder = (AddressbookViewHolder) holder;
                addressbookViewHolder.addressBookAdapterItemBinding.tvBuildingType.setText(addressItem.getBuildingType());
                addressbookViewHolder.addressBookAdapterItemBinding.tvStreet.setText(addressItem.getFlat() + ", " +
                        addressItem.getBuildingNo() + ", " +
                        addressItem.getBlock());
                addressbookViewHolder.addressBookAdapterItemBinding.tvCountry.setText(addressItem.getCountry());
                String address = addressItem.getStreet() + " " + addressItem.getArea();
                addressbookViewHolder.addressBookAdapterItemBinding.tvAddress.setText(address);
                addressbookViewHolder.addressBookAdapterItemBinding.tvPhoneNumber.setText(addressItem.getMobile());
                addressbookViewHolder.addressBookAdapterItemBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditClicked(addressItem);
                    }
                });
                if (sSelected == position) {
                    addressbookViewHolder.addressBookAdapterItemBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.ace_theme_red_outlined_rounded_rectangle));
                } else {
                    addressbookViewHolder.addressBookAdapterItemBinding.cvBackground.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle));
                }
                addressbookViewHolder.addressBookAdapterItemBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteClicked(addressItem);
                    }
                });
                break;
            case AppConstants.ADDRESS_VIEW_TYPE_CHECKOUT:
                CheckoutAddressViewHolder checkoutAddressViewHolder = (CheckoutAddressViewHolder) holder;
//                checkoutAddressViewHolder.addressAdapterItemBinding.tvStreet.setText(addressItem.getStreet());
//                checkoutAddressViewHolder.addressAdapterItemBinding.tvArea.setText(addressItem.getArea());
                if (addressItem.getFirstName().equals(null) || addressItem.getFirstName().equalsIgnoreCase("null") || addressItem.getFirstName() == null)
                    firstName = "";
                else
                    firstName = addressItem.getFirstName();
                if (addressItem.getLastName().equals(null) || addressItem.getLastName().equalsIgnoreCase("null") || addressItem.getLastName() == null)
                    lastName = "";
                else
                    lastName = addressItem.getLastName();
                checkoutAddressViewHolder.addressAdapterItemBinding.tvAddressType.setText(addressItem.getBuildingType());
                checkoutAddressViewHolder.addressAdapterItemBinding.tvMobile.setText(addressItem.getMobile());
                checkoutAddressViewHolder.addressAdapterItemBinding.tvName.setText(firstName + " " + lastName);
//                checkoutAddressViewHolder.addressAdapterItemBinding.tvFlat.setText(addressItem.getFlat() + " " + addressItem.getBlock());
                checkoutAddressViewHolder.addressAdapterItemBinding.tvCountry.setText(addressItem.getCountry());
                if (sSelected == position) {
                    checkoutAddressViewHolder.addressAdapterItemBinding.cvBackgroundAddress.setBackground(context.getResources().getDrawable(R.drawable.ace_red_outlined_rounded_rectangle_12dp));
                    checkoutAddressViewHolder.addressAdapterItemBinding.radioButtonSelectAddress.setChecked(true);
                } else {
                    checkoutAddressViewHolder.addressAdapterItemBinding.cvBackgroundAddress.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_12dp));
                    checkoutAddressViewHolder.addressAdapterItemBinding.radioButtonSelectAddress.setChecked(false);
                }

                checkoutAddressViewHolder.addressAdapterItemBinding.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditClicked(addressItem);
                    }
                });
                checkoutAddressViewHolder.addressAdapterItemBinding.radioButtonSelectAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sSelected = position;
                        sClickListener.onItemClickListener(position, holder.itemView);
                    }
                });
                checkoutAddressViewHolder.addressAdapterItemBinding.imageViewDeleteAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteClicked(addressItem);
                    }
                });
                break;
        }


    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class CheckoutAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutAddressAdapterItemBinding addressAdapterItemBinding;

        public CheckoutAddressViewHolder(@NonNull LayoutAddressAdapterItemBinding addressAdapterItemBinding) {
            super(addressAdapterItemBinding.getRoot());
            this.addressAdapterItemBinding = addressAdapterItemBinding;
            addressAdapterItemBinding.cvBackgroundAddress.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }


    public class AddressbookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutAddressBookAdapterItemBinding addressBookAdapterItemBinding;

        public AddressbookViewHolder(@NonNull LayoutAddressBookAdapterItemBinding addressBookAdapterItemBinding) {
            super(addressBookAdapterItemBinding.getRoot());
            this.addressBookAdapterItemBinding = addressBookAdapterItemBinding;
            addressBookAdapterItemBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}



