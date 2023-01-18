package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.LayoutReturnAdapterItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class ReturnProductsAdapter extends RecyclerView.Adapter<ReturnProductsAdapter.ViewHolder> {

    List<OrderProduct> productList = new ArrayList<>();
    List<String> returnReasons = new ArrayList<>();
    List<OrderProduct> selectedProductsForReturn = new ArrayList<>();
    ArrayList<Integer> quantityList = new ArrayList<>();
    Context context;

    public ReturnProductsAdapter(Context context, List<OrderProduct> productList, List<String> returnReasons) {
        this.context = context;
        this.productList = productList;
        this.returnReasons = returnReasons;
    }

    public abstract void onReturnClicked(List<OrderProduct> selectedProductList);


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutReturnAdapterItemBinding returnAdapterItepmBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_return_adapter_item, parent, false);
        return new ViewHolder(returnAdapterItepmBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            OrderProduct product = productList.get(position);
            holder.returnAdapterItemBinding.layoutProduct.tvProductName.setText(product.getProductName());
            String strDouble = String.format("%.3f", product.getOneTimePrice());
            holder.returnAdapterItemBinding.layoutProduct.tvAmount.setText("KWD " + strDouble);
            Glide.with(context)
                    .load(product.getProductImage())
                    .into(holder.returnAdapterItemBinding.layoutProduct.imageViewProduct);
            holder.returnAdapterItemBinding.layoutProduct.tVProductColor.setText(product.getProductColorCode());
//            holder.returnAdapterItemBinding.layoutProduct.tvProductSize.setText(product.getProductSize());
//            holder.returnAdapterItemBinding.checkBoxReturnProduct.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
////                        product.setReturnQty(quantityList.get(holder.returnAdapterItemBinding.layoutProduct.spinnerReturnQty.getSelectedItemPosition()));
////                        product.setReturnReason(returnReasons.get(holder.returnAdapterItemBinding.spinnerReturnReasons.getSelectedItemPosition()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (((AppCompatCheckBox) v).isChecked()) {
//                        System.out.println("Checked");
//                        if (!selectedProductsForReturn.contains(product)) {
////                            product.setProductReturn(true);
//                            selectedProductsForReturn.add(product);
//                        }
//
//                    } else {
//                        System.out.println("Un-Checked");
//                        if (selectedProductsForReturn.contains(product)) {
////                            product.setProductReturn(false);
//                            selectedProductsForReturn.remove(product);
//                        }
//
//                    }
//                    onReturnClicked(selectedProductsForReturn);
//                }
//            });

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, returnReasons);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            holder.returnAdapterItemBinding.spinnerReturnReasons.setAdapter(arrayAdapter);
//            holder.returnAdapterItemBinding.layoutProduct.tvItemQuantity.setText(product.getQuantity() + "");

            quantityList = new ArrayList<>();
            for (int i = 1; i <= product.getQuantity(); i++) {
                quantityList.add(i);
            }
            ArrayAdapter<Integer> qtyArrayAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, quantityList);
            qtyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            holder.returnAdapterItemBinding.layoutProduct.spinnerReturnQty.setAdapter(qtyArrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutReturnAdapterItemBinding returnAdapterItemBinding;

        public ViewHolder(@NonNull LayoutReturnAdapterItemBinding returnAdapterItemBinding) {
            super(returnAdapterItemBinding.getRoot());
            this.returnAdapterItemBinding = returnAdapterItemBinding;
        }
    }
}
