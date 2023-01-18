package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.AceStore;
import com.alhasawi.acekuwait.databinding.LayoutStoreAdapterItemBinding;

import java.util.ArrayList;

public abstract class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    ArrayList<AceStore> storeArrayList;
    Context context;


    public StoreAdapter(Context context, ArrayList<AceStore> stores) {
        this.context = context;
        this.storeArrayList = stores;
    }

    public abstract void onStoreClicked(AceStore aceStore);

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStoreAdapterItemBinding storeAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_store_adapter_item, parent, false);
        return new ViewHolder(storeAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            AceStore store = storeArrayList.get(position);
            if (store.getStoreName() != null && !store.getStoreName().equals(""))
                holder.layoutStoreAdapterItemBinding.tvStoreName.setText(store.getStoreName());
            if (store.getStoreAdress() != null && !store.getStoreAdress().equals(""))
                holder.layoutStoreAdapterItemBinding.tvAddress.setText(store.getStoreAdress());
            if (store.getContact() != null && !store.getStoreAdress().equals(""))
                holder.layoutStoreAdapterItemBinding.tvContact.setText(store.getContact());

            holder.layoutStoreAdapterItemBinding.imageButtonProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStoreClicked(store);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutStoreAdapterItemBinding layoutStoreAdapterItemBinding;

        public ViewHolder(@NonNull LayoutStoreAdapterItemBinding layoutStoreAdapterItemBinding) {
            super(layoutStoreAdapterItemBinding.getRoot());
            this.layoutStoreAdapterItemBinding = layoutStoreAdapterItemBinding;
        }

    }
}
