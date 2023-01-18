package com.alhasawi.acekuwait.ui.main.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutAceHomeCarouselViewBinding;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.synnapps.carouselview.ImageListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeElementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public HomeSection homeSection;
    List<Element> sectionElementArrayList;
    List<String> imageList = new ArrayList<>();
    Context context;
    int SELECTED_VIEW_TYPE = 0;
    HomeSectionElementsAdapter.HomeItemClickListener homeItemClickListener;

    public HomeElementsAdapter(HomeSection homeSection, Context context, int VIEW_TYPE) {
        this.homeSection = homeSection;
        this.sectionElementArrayList = homeSection.getElements();
        this.context = context;
        this.SELECTED_VIEW_TYPE = VIEW_TYPE;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (SELECTED_VIEW_TYPE) {
//            case AppConstants.VIEW_TYPE_CATEGORY:
//                LayoutHomeCategoryViewBinding categoryViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_category_view, parent, false);
//                return new CategoryViewHolder(categoryViewBinding);
//            case AppConstants.VIEW_TYPE_BANNER:
//                LayoutHomeBannerViewBinding bannerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_banner_view, parent, false);
//                return new HomeSectionElementsAdapter.BannerViewHolder(bannerViewBinding);
//            case AppConstants.VIEW_TYPE_TEXTVIEW:
//                LayoutHomeTextviewBinding textviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_textview, parent, false);
//                return new HomeSectionElementsAdapter.TextviewHolder(textviewBinding);
//            case AppConstants.VIEW_TYPE_WISHLIST:
//                LayoutHomeWishlistViewBinding wishlistViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_wishlist_view, parent, false);
//                return new HomeSectionElementsAdapter.WishlistViewHolder(wishlistViewBinding);
//            case AppConstants.VIEW_TYPE_CART:
//                LayoutHomeCartViewBinding cartViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_cart_view, parent, false);
//                return new HomeSectionElementsAdapter.CartViewHolder(cartViewBinding);
//            case AppConstants.VIEW_TYPE_WISHLIST_CART:
//                LayoutHomeCartWishlistViewBinding wishlistCartViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_cart_wishlist_view, parent, false);
//                return new HomeSectionElementsAdapter.WishlistCartViewHolder(wishlistCartViewBinding);
//            case AppConstants.VIEW_TYPE_TIMER:
//                LayoutHomeTimerViewBinding timerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_timer_view, parent, false);
//                return new HomeSectionElementsAdapter.TimerViewHolder(timerViewBinding);
//            case AppConstants.VIEW_TYPE_STORE:
//                LayoutHomeFindStoreBinding storeBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_find_store, parent, false);
//                return new HomeSectionElementsAdapter.FindStoreViewHolder(storeBinding);
            case AppConstants.VIEW_TYPE_CAROUSEL:
                LayoutAceHomeCarouselViewBinding carouselViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_ace_home_carousel_view, parent, false);
                return new CarouselViewHolder(carouselViewBinding);
//                    @Override
//                    public void setFixedHeight() {
////                        ViewGroup.LayoutParams parentParams = parent.getLayoutParams();
////                        parentParams.height =500+
////                                ((RecyclerView) parent).computeVerticalScrollRange();
//////                                        + parent.getPaddingTop()
//////                                        + parent.getPaddingBottom();
////                        parent.setLayoutParams(parentParams);
//                    }
//                };
//            case AppConstants.VIEW_TYPE_BAG_VIEW:
//                LayoutHomeBagViewBinding bagViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_bag_view, parent, false);
//                return new HomeSectionElementsAdapter.BagViewHolder(bagViewBinding);
//            case AppConstants.VIEW_TYPE_UPCOMING:
//                LayoutUpcomingViewBinding upcomingViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_upcoming_view, parent, false);
//                return new HomeSectionElementsAdapter.UpcomingViewHolder(upcomingViewBinding);
//            case AppConstants.VIEW_TYPE_HERO_BANNER:
//                LayoutHomeHeroBannerViewBinding heroBannerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_hero_banner_view, parent, false);
//                return new HomeSectionElementsAdapter.HeroBannerViewHolder(heroBannerViewBinding);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (SELECTED_VIEW_TYPE) {
//            case AppConstants.VIEW_TYPE_CATEGORY:
//                HomeSectionElementsAdapter.CategoryViewHolder categoryViewHolder = (HomeSectionElementsAdapter.CategoryViewHolder) holder;
//                categoryViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_BANNER:
//                HomeSectionElementsAdapter.BannerViewHolder bannerViewHolder = (HomeSectionElementsAdapter.BannerViewHolder) holder;
//                bannerViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_TEXTVIEW:
//                HomeSectionElementsAdapter.TextviewHolder textviewHolder = (HomeSectionElementsAdapter.TextviewHolder) holder;
//                textviewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_WISHLIST:
//                HomeSectionElementsAdapter.WishlistViewHolder wishlistViewHolder = (HomeSectionElementsAdapter.WishlistViewHolder) holder;
//                wishlistViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_CART:
//                HomeSectionElementsAdapter.CartViewHolder cartViewHolder = (HomeSectionElementsAdapter.CartViewHolder) holder;
//                cartViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_WISHLIST_CART:
//                HomeSectionElementsAdapter.WishlistCartViewHolder wishlistCartViewHolder = (HomeSectionElementsAdapter.WishlistCartViewHolder) holder;
//                wishlistCartViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_TIMER:
//                HomeSectionElementsAdapter.TimerViewHolder timerViewHolder = (HomeSectionElementsAdapter.TimerViewHolder) holder;
//                timerViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_STORE:
//                HomeSectionElementsAdapter.FindStoreViewHolder findStoreViewHolder = (HomeSectionElementsAdapter.FindStoreViewHolder) holder;
//                findStoreViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_BAG_VIEW:
//                HomeSectionElementsAdapter.BagViewHolder bagViewHolder = (HomeSectionElementsAdapter.BagViewHolder) holder;
//                bagViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_HERO_BANNER:
//                HomeSectionElementsAdapter.HeroBannerViewHolder heroBannerViewHolder = (HomeSectionElementsAdapter.HeroBannerViewHolder) holder;
//                heroBannerViewHolder.onBind(position);
//                break;
//            case AppConstants.VIEW_TYPE_UPCOMING:
//                HomeSectionElementsAdapter.UpcomingViewHolder upcomingViewHolder = (HomeSectionElementsAdapter.UpcomingViewHolder) holder;
//                upcomingViewHolder.onBind(position);
//                break;
            case AppConstants.VIEW_TYPE_CAROUSEL:
                imageList = new ArrayList<>();
                for (int i = 0; i < sectionElementArrayList.size(); i++) {
                    imageList.add(sectionElementArrayList.get(i).getTitle().getLogo());
                }
                CarouselViewHolder carouselViewHolder = (CarouselViewHolder) holder;
                carouselViewHolder.onBind(position);
//                carouselViewHolder.setFixedHeight();
                break;
            default:
                break;
        }
    }

    public void setHomeItemClickListener(HomeSectionElementsAdapter.HomeItemClickListener homeItemClickListener) {
        this.homeItemClickListener = homeItemClickListener;
    }

    @Override
    public int getItemCount() {
        return sectionElementArrayList.size();
    }

    public interface HomeItemClickListener {
        void onItemClicked(Element element);

        void onWishlistClicked(RefObject refObject, boolean ischecked);

        void onCartClicked(RefObject refObject);

    }

    class CarouselViewHolder extends RecyclerView.ViewHolder {
        LayoutAceHomeCarouselViewBinding homeCarouselViewBinding;
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                try {
                    Glide.with(context)
                            .asBitmap()
//                        .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            .load(imageList.get(position))
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@Nullable Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    imageView.setAdjustViewBounds(true);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    imageView.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                imageView.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), "");
                    }
                });

            }
        };

        public CarouselViewHolder(@NonNull LayoutAceHomeCarouselViewBinding homeCarouselViewBinding) {
            super(homeCarouselViewBinding.getRoot());
            this.homeCarouselViewBinding = homeCarouselViewBinding;
        }

        public void onBind(int position) {
            homeCarouselViewBinding.carouselView.setImageListener(imageListener);
            if (imageList != null)
                homeCarouselViewBinding.carouselView.setPageCount(imageList.size());
            else
                homeCarouselViewBinding.carouselView.setPageCount(0);
            homeCarouselViewBinding.carouselView.setCurrentItem(position);
        }
    }
}
