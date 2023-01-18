package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.StoreInventory;
import com.alhasawi.acekuwait.databinding.LayoutStorelistAdapterItemBinding;

import java.util.ArrayList;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    ArrayList<StoreInventory> storeArrayList;
    Context context;

    public StoreListAdapter(Context context, ArrayList<StoreInventory> stores) {
        this.context = context;
        this.storeArrayList = stores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStorelistAdapterItemBinding storelistAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_storelist_adapter_item, parent, false);
        return new ViewHolder(storelistAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.storelistAdapterItemBinding.tvStoreName.setText(storeArrayList.get(position).getStore().getStoreName());
            if (storeArrayList.get(position).getQty() == 0) {
                holder.storelistAdapterItemBinding.tvStoreStock.setText("No");
                holder.storelistAdapterItemBinding.tvStoreStock.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder.storelistAdapterItemBinding.tvStoreStock.setText("Yes");
                holder.storelistAdapterItemBinding.tvStoreStock.setTextColor(context.getResources().getColor(R.color.green));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutStorelistAdapterItemBinding storelistAdapterItemBinding;

        public ViewHolder(@NonNull LayoutStorelistAdapterItemBinding layoutStoreAdapterItemBinding) {
            super(layoutStoreAdapterItemBinding.getRoot());
            this.storelistAdapterItemBinding = layoutStoreAdapterItemBinding;
        }

    }
}
