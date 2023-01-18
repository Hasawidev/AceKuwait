package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.LayoutReturnReasonAdapterItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class AceReturnProductsAdapter extends RecyclerView.Adapter<AceReturnProductsAdapter.ViewHolder> {

    List<OrderProduct> productList = new ArrayList<>();
    List<String> returnReasons = new ArrayList<>();
    List<OrderProduct> selectedProductsForReturn = new ArrayList<>();
    ArrayList<Integer> quantityList = new ArrayList<>();
    Context context;

    public AceReturnProductsAdapter(Context context, List<OrderProduct> productList, List<String> returnReasons) {
        this.context = context;
        this.productList = productList;
        this.returnReasons = returnReasons;
    }

    public abstract void onReturnClicked(List<OrderProduct> selectedProductList);

    public abstract void onEditAddressClicked(Address address, OrderProduct product, int adapterPosition);

    public abstract void onAddNewAddressClicked(OrderProduct product, int adapterPosition);


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutReturnReasonAdapterItemBinding returnReasonAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_return_reason_adapter_item, parent, false);
        return new ViewHolder(returnReasonAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            OrderProduct product = productList.get(position);
            holder.returnReasonAdapterItemBinding.layoutProduct.tvProductName.setText(product.getProductName());
            String strDouble = String.format("%.3f", product.getOneTimePrice());
            holder.returnReasonAdapterItemBinding.layoutProduct.tvAmount.setText("KWD " + strDouble);
            Glide.with(context)
                    .load(product.getProductImage())
                    .into(holder.returnReasonAdapterItemBinding.layoutProduct.imageViewProduct);
            holder.returnReasonAdapterItemBinding.layoutProduct.tVProductColor.setText(product.getProductColorCode());
//            holder.returnReasonAdapterItemBinding.layoutProduct.tvProductOrigin.setText(product.getP);
            holder.returnReasonAdapterItemBinding.checkBoxSelectProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        product.setReturnQty(quantityList.get(holder.returnReasonAdapterItemBinding.layoutProduct.spinnerReturnQty.getSelectedItemPosition()));
                        product.setReturnReason(returnReasons.get(holder.returnReasonAdapterItemBinding.spinnerReturnReasons.getSelectedItemPosition()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (((AppCompatCheckBox) v).isChecked()) {
                        System.out.println("Checked");
                        if (!selectedProductsForReturn.contains(product)) {
                            product.setProductReturn(true);
                            selectedProductsForReturn.add(product);
                        }

                    } else {
                        System.out.println("Un-Checked");
                        if (selectedProductsForReturn.contains(product)) {
                            product.setProductReturn(false);
                            selectedProductsForReturn.remove(product);
                        }

                    }
                    onReturnClicked(selectedProductsForReturn);
                }
            });

            if (product.getAddress() != null) {
                holder.returnReasonAdapterItemBinding.tvUserName.setText(product.getAddress().getFirstName() + " " + product.getAddress().getLastName());
                String address = product.getAddress().getStreet() + ", " +
                        product.getAddress().getFlat() + " " + product.getAddress().getBlock() + ", "
                        + product.getAddress().getArea() + "\n" + product.getAddress().getMobile();
                holder.returnReasonAdapterItemBinding.tvAddress.setText(address);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, returnReasons);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.returnReasonAdapterItemBinding.spinnerReturnReasons.setAdapter(arrayAdapter);
            holder.returnReasonAdapterItemBinding.layoutProduct.tvItemQuantity.setText(product.getQuantity() + "");

            quantityList = new ArrayList<>();
            for (int i = 1; i <= product.getQuantity(); i++) {
                quantityList.add(i);
            }
            ArrayAdapter<Integer> qtyArrayAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, quantityList);
            qtyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.returnReasonAdapterItemBinding.layoutProduct.spinnerReturnQty.setAdapter(qtyArrayAdapter);
            holder.returnReasonAdapterItemBinding.tvEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditAddressClicked(product.getAddress(), product, position);
                }
            });
            holder.returnReasonAdapterItemBinding.tvAddNewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddNewAddressClicked(product, position);
                }
            });
            if (product.isProductReturn()) {
                holder.returnReasonAdapterItemBinding.spinnerReturnReasons.setVisibility(View.GONE);
                holder.returnReasonAdapterItemBinding.txtReturned.setText(context.getResources().getString(R.string.product_returned));
            } else {
                holder.returnReasonAdapterItemBinding.spinnerReturnReasons.setVisibility(View.VISIBLE);
                holder.returnReasonAdapterItemBinding.txtReturned.setText(context.getResources().getString(R.string.return_reason));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutReturnReasonAdapterItemBinding returnReasonAdapterItemBinding;

        public ViewHolder(@NonNull LayoutReturnReasonAdapterItemBinding returnReasonAdapterItemBinding) {
            super(returnReasonAdapterItemBinding.getRoot());
            this.returnReasonAdapterItemBinding = returnReasonAdapterItemBinding;
        }
    }
}
