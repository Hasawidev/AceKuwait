package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductImage;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProdutImageViewPagerAdapter extends PagerAdapter {

    List<ProductImage> productImages;
    private DashboardActivity dashboardActivity;
    private LayoutInflater layoutInflater;

    public ProdutImageViewPagerAdapter(DashboardActivity context, List<ProductImage> productImageList) {
        this.dashboardActivity = context;
        this.productImages = productImageList;
    }

    @Override
    public int getCount() {
        return productImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) dashboardActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_view_pager_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(dashboardActivity)
                .load(productImages.get(position).getImageUrl())
                .placeholder(R.drawable.ace_watermark)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboardActivity, ProductDetailsFragment.ProductImageActivity.class);
                i.putExtra("image_url", productImages.get(position).getImageUrl());
                dashboardActivity.startActivity(i);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
