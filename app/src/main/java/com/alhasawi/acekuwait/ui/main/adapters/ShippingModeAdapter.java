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
import com.alhasawi.acekuwait.databinding.LayoutShippingModeAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.ArrayList;

public class ShippingModeAdapter extends RecyclerView.Adapter<ShippingModeAdapter.ViewHolder> {
    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    ArrayList<ShippingMode> shippingModeArrayList;
    Context context;

    public ShippingModeAdapter(Context context, ArrayList<ShippingMode> shippingModeArrayList) {
        this.context = context;
        this.shippingModeArrayList = shippingModeArrayList;
    }

    public static void setsSelected(int sSelected) {
        ShippingModeAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutShippingModeAdapterItemBinding shippingModeAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_shipping_mode_adapter_item, parent, false);
        return new ViewHolder(shippingModeAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShippingMode shippingModeItem = shippingModeArrayList.get(position);
        holder.shippingModeAdapterItemBinding.tvName.setText(shippingModeItem.getName());
        holder.shippingModeAdapterItemBinding.tvShippingModeDetails.setText(shippingModeItem.getDescription());
        if (sSelected == position) {
            holder.shippingModeAdapterItemBinding.cvBackgroundShippingMode.setBackground(context.getResources().getDrawable(R.drawable.ace_red_outlined_rounded_rectangle_12dp));
            holder.shippingModeAdapterItemBinding.radioButtonSelectShippingMode.setChecked(true);
        } else {
            holder.shippingModeAdapterItemBinding.cvBackgroundShippingMode.setBackground(context.getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_12dp));
            holder.shippingModeAdapterItemBinding.radioButtonSelectShippingMode.setChecked(false);
        }

        holder.shippingModeAdapterItemBinding.radioButtonSelectShippingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sSelected = position;
                sClickListener.onItemClickListener(position, holder.itemView);
            }
        });

    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shippingModeArrayList.size();
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutShippingModeAdapterItemBinding shippingModeAdapterItemBinding;

        public ViewHolder(@NonNull LayoutShippingModeAdapterItemBinding shippingModeAdapterItemBinding) {
            super(shippingModeAdapterItemBinding.getRoot());
            this.shippingModeAdapterItemBinding = shippingModeAdapterItemBinding;
            shippingModeAdapterItemBinding.cvBackgroundShippingMode.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), view);
        }

    }
}



