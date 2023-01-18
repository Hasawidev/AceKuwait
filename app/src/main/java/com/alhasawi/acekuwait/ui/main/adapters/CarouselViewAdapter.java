package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutCarouselAdapterItemBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CarouselViewAdapter extends RecyclerView.Adapter<CarouselViewAdapter.ViewHolder> {

    private ArrayList<String> carouselImages;
    private Context context;


    public CarouselViewAdapter(Context context, ArrayList<String> images) {
        this.carouselImages = images;
        if (carouselImages == null)
            carouselImages = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCarouselAdapterItemBinding carouselAdapterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_carousel_adapter_item, parent, false);
        return new ViewHolder(carouselAdapterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Glide.with(context)
                    .load(carouselImages.get(position))
                    .into(holder.carouselAdapterItemBinding.imageItemCarousel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (carouselImages == null)
            return 0;
        else
            return carouselImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutCarouselAdapterItemBinding carouselAdapterItemBinding;

        public ViewHolder(@NonNull LayoutCarouselAdapterItemBinding carouselAdapterItemBinding) {
            super(carouselAdapterItemBinding.getRoot());
            this.carouselAdapterItemBinding = carouselAdapterItemBinding;
        }


    }
}
