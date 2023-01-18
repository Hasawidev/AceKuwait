package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.RefundPaymentMode;
import com.alhasawi.acekuwait.databinding.LayoutRefundModeAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.List;

public class RefundModeAdapter extends RecyclerView.Adapter<RefundModeAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = -1;
    List<RefundPaymentMode> paymentModeList;
    Context context;

    public RefundModeAdapter(Context context, List<RefundPaymentMode> paymentModeList) {
        this.context = context;
        this.paymentModeList = paymentModeList;
    }

    public static void setsSelected(int sSelected) {
        RefundModeAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutRefundModeAdapterItemBinding layoutReturnAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_refund_mode_adapter_item, parent, false);
        return new ViewHolder(layoutReturnAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RefundPaymentMode paymentMode = paymentModeList.get(position);
        holder.layoutRefundModeAdapterItemBinding.tvTitle.setText(paymentMode.getTitle());
        holder.layoutRefundModeAdapterItemBinding.tvDescription.setText(paymentMode.getDescription());
        Drawable drawable = null;
        if (position == 0)
            drawable = context.getResources().getDrawable(R.drawable.dollar);
        else if (position == 1)
            drawable = context.getResources().getDrawable(R.drawable.wallet);
        else if (position == 2)
            drawable = context.getResources().getDrawable(R.drawable.knet_card);
        holder.layoutRefundModeAdapterItemBinding.imageView27.setImageDrawable(drawable);

        if (sSelected == position) {
            holder.layoutRefundModeAdapterItemBinding.radioButton2.setChecked(true);
        } else {
            holder.layoutRefundModeAdapterItemBinding.radioButton2.setChecked(false);
        }

        if (paymentMode.getTitle().equals("Store credit")) {
            holder.layoutRefundModeAdapterItemBinding.radioButton2.setEnabled(false);
            holder.layoutRefundModeAdapterItemBinding.cvDisable.setVisibility(View.VISIBLE);
        } else {
            holder.layoutRefundModeAdapterItemBinding.cvDisable.setVisibility(View.GONE);
            holder.layoutRefundModeAdapterItemBinding.radioButton2.setEnabled(true);
        }

        holder.layoutRefundModeAdapterItemBinding.radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sSelected = position;
                sClickListener.onItemClickListener(position, holder.itemView);
            }
        });
    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return paymentModeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutRefundModeAdapterItemBinding layoutRefundModeAdapterItemBinding;

        public ViewHolder(@NonNull LayoutRefundModeAdapterItemBinding layoutRefundModeAdapterItemBinding) {
            super(layoutRefundModeAdapterItemBinding.getRoot());
            this.layoutRefundModeAdapterItemBinding = layoutRefundModeAdapterItemBinding;
            layoutRefundModeAdapterItemBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }
}


