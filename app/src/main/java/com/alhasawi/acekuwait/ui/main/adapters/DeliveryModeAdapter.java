package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.databinding.LayoutDeliveryModeAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.List;

public class DeliveryModeAdapter extends RecyclerView.Adapter<DeliveryModeAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    List<ShippingMode> deliveryModeList;
    Context context;

    public DeliveryModeAdapter(Context context, List<ShippingMode> deliveryModes) {
        this.context = context;
        this.deliveryModeList = deliveryModes;
    }

    public static void setsSelected(int sSelected) {
        DeliveryModeAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutDeliveryModeAdapterItemBinding deliveryModeAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_delivery_mode_adapter_item, parent, false);
        return new ViewHolder(deliveryModeAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShippingMode deliveryMode = deliveryModeList.get(position);

        if (sSelected == position) {
            holder.deliveryModeAdapterItemBinding.radiobutton.setChecked(true);
        } else {
            holder.deliveryModeAdapterItemBinding.radiobutton.setChecked(false);
        }

        holder.deliveryModeAdapterItemBinding.radiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sSelected = position;
                sClickListener.onItemClickListener(position, holder.itemView);
            }
        });
        holder.deliveryModeAdapterItemBinding.tvName.setText(deliveryMode.getName());
        holder.deliveryModeAdapterItemBinding.tvContent.setText(deliveryMode.getDescription());
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return deliveryModeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutDeliveryModeAdapterItemBinding deliveryModeAdapterItemBinding;

        public ViewHolder(@NonNull LayoutDeliveryModeAdapterItemBinding deliveryModeAdapterItemBinding) {
            super(deliveryModeAdapterItemBinding.getRoot());
            this.deliveryModeAdapterItemBinding = deliveryModeAdapterItemBinding;
            deliveryModeAdapterItemBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }
}


