package com.alhasawi.acekuwait.ui.main.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.StoreInventory;
import com.alhasawi.acekuwait.databinding.LayoutStoreSlotAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;

import java.util.Calendar;
import java.util.List;

public class PickupStoresAdapter extends RecyclerView.Adapter<PickupStoresAdapter.ViewHolder> {

    private static RecyclerviewSingleChoiceClickListener sClickListener;
    private static int sSelected = 0;
    List<StoreInventory> storeList;
    StoreInventory selectedStore;
    DatePickerDialog datePickerDialog;
    Context context;
    private String selectedDob = "";
    private StoreSelectedListener storeSelectedListener;

    public PickupStoresAdapter(Activity context, List<StoreInventory> stores, StoreSelectedListener listener) {
        this.context = context;
        this.storeList = stores;
        this.storeSelectedListener = listener;
    }

    public static void setsSelected(int sSelected) {
        PickupStoresAdapter.sSelected = sSelected;
    }

    @NonNull
    @Override
    public PickupStoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStoreSlotAdapterItemBinding storeSlotAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_store_slot_adapter_item, parent, false);
        return new ViewHolder(storeSlotAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PickupStoresAdapter.ViewHolder holder, int position) {
        selectedStore = storeList.get(position);

        if (sSelected == position) {
            holder.storeSlotAdapterItemBinding.radiobutton.setChecked(true);
        } else {
            holder.storeSlotAdapterItemBinding.radiobutton.setChecked(false);
        }


        holder.storeSlotAdapterItemBinding.radiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sSelected = position;
                sClickListener.onItemClickListener(position, holder.itemView);
            }
        });
        holder.storeSlotAdapterItemBinding.imageViewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(holder);
            }
        });
        holder.storeSlotAdapterItemBinding.imageViewEditTimeslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.storeSlotAdapterItemBinding.edtSlot.setEnabled(true);
            }
        });
        holder.storeSlotAdapterItemBinding.edtSlot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.storeSlotAdapterItemBinding.radiobutton.isChecked())
                    storeSelectedListener.onStoreSelected(selectedStore, selectedDob, holder.storeSlotAdapterItemBinding.edtSlot.getText().toString());
            }
        });
        holder.storeSlotAdapterItemBinding.tvName.setText(selectedStore.getStore().getStoreName());
        holder.storeSlotAdapterItemBinding.tvContent.setText(selectedStore.getStore().getStoreName());

    }

    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
        sClickListener = clickListener;
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    private void showDatePickerDialog(ViewHolder holder) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (holder.storeSlotAdapterItemBinding.radiobutton.isChecked())
                            storeSelectedListener.onStoreSelected(selectedStore, holder.storeSlotAdapterItemBinding.tvSelectedDate.getText().toString(), holder.storeSlotAdapterItemBinding.edtSlot.getText().toString());
                        holder.storeSlotAdapterItemBinding.tvSelectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        selectedDob = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public interface StoreSelectedListener {
        void onStoreSelected(StoreInventory storeInventory, String pickupdate, String slot);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutStoreSlotAdapterItemBinding storeSlotAdapterItemBinding;

        public ViewHolder(@NonNull LayoutStoreSlotAdapterItemBinding storeSlotAdapterItemBinding) {
            super(storeSlotAdapterItemBinding.getRoot());
            this.storeSlotAdapterItemBinding = storeSlotAdapterItemBinding;
            storeSlotAdapterItemBinding.cvBackground.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sSelected = getAdapterPosition();
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }
}


