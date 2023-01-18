package com.alhasawi.acekuwait.ui.main.view.home;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutHomeBagViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeBannerViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeCarouselViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeCartViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeCartWishlistViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeCategoryViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeFindStoreBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeHeroBannerViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeTextviewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeTimerViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutHomeWishlistViewBinding;
import com.alhasawi.acekuwait.databinding.LayoutUpcomingViewBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.DateTimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.synnapps.carouselview.ImageListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.Suppress;

public class HomeSectionElementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    private static RecyclerviewSingleChoiceClickListener sClickListener;
//    private static int sSelected = 0;
    public HomeSection homeSection;
    List<Element> sectionElementArrayList;
    List<String> imageList = new ArrayList<>();
    DashboardActivity context;
    int SELECTED_VIEW_TYPE = 0;
    HomeItemClickListener homeItemClickListener;
    String headerText = "";


    public HomeSectionElementsAdapter(HomeSection homeSection, DashboardActivity context, int VIEW_TYPE, String mHeaderText) {
        this.homeSection = homeSection;
        this.sectionElementArrayList = homeSection.getElements();
        this.context = context;
        this.SELECTED_VIEW_TYPE = VIEW_TYPE;
        this.headerText = mHeaderText;
    }

//    public static void setsSelected(int sSelected) {
//        sSelected = sSelected;
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (SELECTED_VIEW_TYPE) {
            case AppConstants.VIEW_TYPE_CATEGORY:
                LayoutHomeCategoryViewBinding categoryViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_category_view, parent, false);
                return new CategoryViewHolder(categoryViewBinding);
            case AppConstants.VIEW_TYPE_BANNER:
                LayoutHomeBannerViewBinding bannerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_banner_view, parent, false);
                return new BannerViewHolder(bannerViewBinding);
            case AppConstants.VIEW_TYPE_TEXTVIEW:
                LayoutHomeTextviewBinding textviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_textview, parent, false);
                return new TextviewHolder(textviewBinding);
            case AppConstants.VIEW_TYPE_WISHLIST:
//                LayoutHomeWishlistViewBinding wishlistViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_wishlist_view, parent, false);
//                return new WishlistViewHolder(wishlistViewBinding);
            case AppConstants.VIEW_TYPE_CART:
//                LayoutHomeCartViewBinding cartViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_cart_view, parent, false);
//                return new CartViewHolder(cartViewBinding);
            case AppConstants.VIEW_TYPE_WISHLIST_CART:
                LayoutHomeCartWishlistViewBinding wishlistCartViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_cart_wishlist_view, parent, false);
                return new WishlistCartViewHolder(wishlistCartViewBinding);
            case AppConstants.VIEW_TYPE_TIMER:
                LayoutHomeTimerViewBinding timerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_timer_view, parent, false);
                return new TimerViewHolder(timerViewBinding);
            case AppConstants.VIEW_TYPE_STORE:
                LayoutHomeFindStoreBinding storeBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_find_store, parent, false);
                return new FindStoreViewHolder(storeBinding);
            case AppConstants.VIEW_TYPE_CAROUSEL:
                LayoutHomeCarouselViewBinding carouselViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_carousel_view, parent, false);
//                new PagerSnapHelper().attachToRecyclerView(carouselViewBinding.recylcerviewCarousels);
                return new CarouselViewHolder(carouselViewBinding);
            case AppConstants.VIEW_TYPE_BAG_VIEW:
                LayoutHomeBagViewBinding bagViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_bag_view, parent, false);
                return new BagViewHolder(bagViewBinding);
            case AppConstants.VIEW_TYPE_UPCOMING:
                LayoutUpcomingViewBinding upcomingViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_upcoming_view, parent, false);
                return new UpcomingViewHolder(upcomingViewBinding);
            case AppConstants.VIEW_TYPE_HERO_BANNER:
                LayoutHomeHeroBannerViewBinding heroBannerViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_home_hero_banner_view, parent, false);
                return new HeroBannerViewHolder(heroBannerViewBinding);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (SELECTED_VIEW_TYPE) {
            case AppConstants.VIEW_TYPE_CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                categoryViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_BANNER:
                BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
                bannerViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_TEXTVIEW:
                TextviewHolder textviewHolder = (TextviewHolder) holder;
                textviewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_WISHLIST:
//                WishlistViewHolder wishlistViewHolder = (WishlistViewHolder) holder;
//                wishlistViewHolder.onBind(position);
//                break;
            case AppConstants.VIEW_TYPE_CART:
//                CartViewHolder cartViewHolder = (CartViewHolder) holder;
//                cartViewHolder.onBind(position);
//                break;
            case AppConstants.VIEW_TYPE_WISHLIST_CART:
                WishlistCartViewHolder wishlistCartViewHolder = (WishlistCartViewHolder) holder;
                wishlistCartViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_TIMER:
                TimerViewHolder timerViewHolder = (TimerViewHolder) holder;
                timerViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_STORE:
                FindStoreViewHolder findStoreViewHolder = (FindStoreViewHolder) holder;
                findStoreViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_BAG_VIEW:
                BagViewHolder bagViewHolder = (BagViewHolder) holder;
                bagViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_HERO_BANNER:
                HeroBannerViewHolder heroBannerViewHolder = (HeroBannerViewHolder) holder;
                heroBannerViewHolder.onBind(position);
//                ConstraintLayout.LayoutParams params = new
//                        ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                // Set the height by params
//                params.height=homeSection.getDimension().getHeight();
//                // set height of RecyclerView
//              heroBannerViewHolder.heroBannerViewBinding.cardContainer.setLayoutParams(params);


                break;
            case AppConstants.VIEW_TYPE_UPCOMING:
                UpcomingViewHolder upcomingViewHolder = (UpcomingViewHolder) holder;
                upcomingViewHolder.onBind(position);
                break;
            case AppConstants.VIEW_TYPE_CAROUSEL:
                imageList = new ArrayList<>();
                for (int i = 0; i < sectionElementArrayList.size(); i++) {
                    imageList.add(sectionElementArrayList.get(i).getTitle().getLogo());
                }
                CarouselViewHolder carouselViewHolder = (CarouselViewHolder) holder;
                carouselViewHolder.onBind(position);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (SELECTED_VIEW_TYPE == AppConstants.VIEW_TYPE_CAROUSEL) {
            return 1;
        }
        return sectionElementArrayList.size();
    }

    public int getWidthForGivenGrid(int grid) {
        int width = 0;
        try {
            DisplayMetrics outMetrics = new DisplayMetrics();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                Display display = context.getDisplay();
                display.getRealMetrics(outMetrics);
            } else {
                @Suppress(names = "DEPRECATION")
                Display display = context.getWindowManager().getDefaultDisplay();
                display.getRealMetrics(outMetrics);
            }

            float scWidth = outMetrics.widthPixels;

            width = (int) scWidth;
            if (grid == 1 || grid == 0) {
                width = (int) scWidth;
            } else if (grid == 2) {
                width = (width / homeSection.getDimension().getGrid()) - 20;
            } else if (grid == 3) {
                width = width / grid - 20;
            } else if (grid == 4) {
                width = (width / 2) - 70;
            } else {
                width = 250;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public int getHeight(int grid) {
        int height = 0;
        Display display = context.getDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getRealMetrics(outMetrics);
        float scWidth = outMetrics.widthPixels;
        height = (int) (scWidth * 0.4f);
        return height;
    }

//    public void setOnItemClickListener(RecyclerviewSingleChoiceClickListener clickListener) {
//        sClickListener = clickListener;
//    }

//    public void selectedItem() {
//        notifyDataSetChanged();
//    }

    public void setHomeItemClickListener(HomeItemClickListener homeItemClickListener) {
        this.homeItemClickListener = homeItemClickListener;
    }

    public void processTimer(Element element, LayoutHomeTimerViewBinding timerViewBinding) {
        String startString = element.getTimer().getStarts_at();
        String endString = element.getTimer().getEnds_at();

        if (!startString.equals("") && !endString.equals("")) {
            Date startDate = DateTimeUtils.convertToUTCDate(startString);
            Date endDate = DateTimeUtils.convertToUTCDate(endString);
            Date currentDate = DateTimeUtils.getCurrentUTCDate();

//            boolean isDealStarted=DateTimeUtils.isThisDateWithinCertainRange(currentDate.toString(),
//                    startDate,endDate);
//            if(isDealStarted){
            long difference_In_Time
                    = endDate.getTime() - currentDate.getTime();
            new CountDownTimer(difference_In_Time, 1000) {
                public void onTick(long millisUntilFinished) {
                    // Used for formatting digit to be in 2 digits only
                    NumberFormat f = new DecimalFormat("00");
                    long hour = (millisUntilFinished / 3600000) % 24;
                    long min = (millisUntilFinished / 60000) % 60;
                    long sec = (millisUntilFinished / 1000) % 60;
                    timerViewBinding.tvTimer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                }

                // When the task is over it will print 00:00:00 there
                public void onFinish() {
                    timerViewBinding.tvTimer.setText("00:00:00");
                }
            }.start();

        }
//        }

    }

    public interface HomeItemClickListener {
        void onItemClicked(HomeSection homeSection, Element element, String headerText);

        void onWishlistClicked(RefObject refObject, boolean ischecked);

        void onCartClicked(RefObject refObject);

    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeCategoryViewBinding homeCategoryViewBinding;

        public CategoryViewHolder(@NonNull LayoutHomeCategoryViewBinding homeCategoryViewBinding) {
            super(homeCategoryViewBinding.getRoot());
            this.homeCategoryViewBinding = homeCategoryViewBinding;
        }

        public void onBind(int position) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                Display display = context.getDisplay();
                display.getRealMetrics(outMetrics);
            } else {
                @Suppress(names = "DEPRECATION")
                Display display = context.getWindowManager().getDefaultDisplay();
                display.getRealMetrics(outMetrics);
            }

            float scWidth = outMetrics.widthPixels;
            //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (scWidth / 2), LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (scWidth / 3.5), LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(16, 0, 8, 0);
            homeCategoryViewBinding.getRoot().setLayoutParams(lp);

            homeCategoryViewBinding.cvContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), headerText);
                }
            });
            Glide.with(context).load(sectionElementArrayList.get(position).getTitle().getLogo()).into(homeCategoryViewBinding.imageViewCategory);
            homeCategoryViewBinding.tvCategoryName.setText(sectionElementArrayList.get(position).getTitle().getTitleName());
//            int gridHeight= (int) ( homeSection.getDimension().getHeight() * context.getResources().getDisplayMetrics().density);
//            int gridHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, homeSection.getDimension().getHeight(), context.getResources().getDisplayMetrics());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(homeSection.getDimension().getWidth(), gridHeight);
//            lp.setMargins(0, 0, 16, 0);
//            homeCategoryViewBinding.getRoot().setLayoutParams(lp);
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeBannerViewBinding homeBannerViewBinding;

        public BannerViewHolder(@NonNull LayoutHomeBannerViewBinding homeBannerViewBinding) {
            super(homeBannerViewBinding.getRoot());
            this.homeBannerViewBinding = homeBannerViewBinding;
        }

        public void onBind(int position) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(1), homeSection.getDimension().getHeight() * 2 + 200);
            lp.setMargins(16, 0, 0, 0);
            homeBannerViewBinding.getRoot().setLayoutParams(lp);
            Element element = sectionElementArrayList.get(position);
            Glide.with(context)
                    .load(element.getTitle().getLogo())
                    .into(homeBannerViewBinding.imageViewBanner);
            homeBannerViewBinding.imageViewBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, element, headerText);
                }
            });
        }
    }

    class BagViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeBagViewBinding bagViewBinding;

        public BagViewHolder(@NonNull LayoutHomeBagViewBinding bagViewBinding) {
            super(bagViewBinding.getRoot());
            this.bagViewBinding = bagViewBinding;
        }

        public void onBind(int position) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(4), LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(16, 0, 0, 0);
            bagViewBinding.getRoot().setLayoutParams(lp);
            Element element = sectionElementArrayList.get(position);
            Glide.with(context)
                    .load(element.getTitle().getLogo())
                    .into(bagViewBinding.imageViewBanner);
            bagViewBinding.cardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, element, headerText);
                }
            });

        }
    }

    class HeroBannerViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeHeroBannerViewBinding heroBannerViewBinding;

        public HeroBannerViewHolder(@NonNull LayoutHomeHeroBannerViewBinding heroBannerViewBinding) {
            super(heroBannerViewBinding.getRoot());
            this.heroBannerViewBinding = heroBannerViewBinding;
        }

        public void onBind(int position) {
            Element element = sectionElementArrayList.get(position);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(1), homeSection.getDimension().getHeight() + 300);
            lp.setMargins(0, 0, 0, 0);
            heroBannerViewBinding.getRoot().setLayoutParams(lp);
            Glide.with(context)
                    .load(element.getTitle().getLogo())
                    .into(heroBannerViewBinding.imageViewBanner);
            heroBannerViewBinding.imageViewBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, element, headerText);
                }
            });
        }
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder {
        LayoutUpcomingViewBinding layoutUpcomingViewBinding;

        public UpcomingViewHolder(@NonNull LayoutUpcomingViewBinding layoutUpcomingViewBinding) {
            super(layoutUpcomingViewBinding.getRoot());
            this.layoutUpcomingViewBinding = layoutUpcomingViewBinding;
        }

        public void onBind(int position) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                Display display = context.getDisplay();
                display.getRealMetrics(outMetrics);
            } else {
                @Suppress(names = "DEPRECATION")
                Display display = context.getWindowManager().getDefaultDisplay();
                display.getRealMetrics(outMetrics);
            }

            float scWidth = outMetrics.widthPixels;
            //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (scWidth / 2), LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (scWidth / 2.2), LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(16, 0, 0, 0);
            layoutUpcomingViewBinding.getRoot().setLayoutParams(lp);
            Element element = sectionElementArrayList.get(position);
            Glide.with(context)
                    .load(element.getTitle().getLogo())
                    .into(layoutUpcomingViewBinding.imageViewBanner);
            layoutUpcomingViewBinding.cardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, element, headerText);
                }
            });
        }
    }

    class WishlistViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeWishlistViewBinding homeWishlistViewBinding;

        public WishlistViewHolder(@NonNull LayoutHomeWishlistViewBinding homeWishlistViewBinding) {
            super(homeWishlistViewBinding.getRoot());
            this.homeWishlistViewBinding = homeWishlistViewBinding;
        }

        public void onBind(int position) {
            Element element = sectionElementArrayList.get(position);
            int grid = 0;
            if (homeSection.getElements().size() == 1)
                grid = 4;
            else if (homeSection.getElements().size() >= 2)
                grid = 4;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(grid), LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(16, 0, 0, 0);
            homeWishlistViewBinding.getRoot().setLayoutParams(lp);

            Glide.with(context).load(element.getTitle().getLogo()).into(homeWishlistViewBinding.imageViewProduct);
            homeWishlistViewBinding.tvProductName.setText(element.getTitle().getTitleName());
            if (element.getTitle().getRefObject() != null) {
                String strDouble = String.format("%.3f", element.getTitle().getRefObject().getOriginalPrice());
                homeWishlistViewBinding.tvOriginalPrice.setText("KWD " + strDouble);
            }

            homeWishlistViewBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), headerText);
                }
            });
            if (element.getTitle().getRefObject() != null) {
                if (element.getTitle().getRefObject().isWishlist())
                    homeWishlistViewBinding.radioButtonWishlist.setChecked(true);
                else
                    homeWishlistViewBinding.radioButtonWishlist.setChecked(false);
            }
            homeWishlistViewBinding.radioButtonWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (element.getTitle().getType().equalsIgnoreCase("p"))
                        homeItemClickListener.onWishlistClicked(element.getTitle().getRefObject(), homeWishlistViewBinding.radioButtonWishlist.isChecked());
                }
            });

            if (element.getTitle().getRefObject() != null) {
                if (element.getTitle().getRefObject().getDiscountPercentage() == 0)
                    homeWishlistViewBinding.tvRibbon.setVisibility(View.GONE);
                else
                    homeWishlistViewBinding.tvRibbon.setText(element.getTitle().getRefObject().getDiscountPercentage() + "% OFF");
            }
        }
    }

    class TextviewHolder extends RecyclerView.ViewHolder {
        LayoutHomeTextviewBinding homeTextviewBinding;

        public TextviewHolder(@NonNull LayoutHomeTextviewBinding homeTextviewBinding) {
            super(homeTextviewBinding.getRoot());
            this.homeTextviewBinding = homeTextviewBinding;
        }

        public void onBind(int position) {
            Element element = sectionElementArrayList.get(position);
            homeTextviewBinding.tvTextName.setText(element.getTitle().getTitleName());
            homeTextviewBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), headerText);
                }
            });
        }
    }


    class FindStoreViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeFindStoreBinding homeFindStoreBinding;

        public FindStoreViewHolder(@NonNull LayoutHomeFindStoreBinding homeFindStoreBinding) {
            super(homeFindStoreBinding.getRoot());
            this.homeFindStoreBinding = homeFindStoreBinding;
        }

        public void onBind(int position) {
            Element element = sectionElementArrayList.get(position);
        }
    }

    class WishlistCartViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeCartWishlistViewBinding homeWishlistCartViewBinding;

        public WishlistCartViewHolder(@NonNull LayoutHomeCartWishlistViewBinding homeWishlistCartViewBinding) {
            super(homeWishlistCartViewBinding.getRoot());
            this.homeWishlistCartViewBinding = homeWishlistCartViewBinding;
        }

        public void onBind(int position) {
            int grid = 0;
            if (homeSection.getElements().size() == 1)
                grid = 4;
            else if (homeSection.getElements().size() >= 2)
                grid = 4;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(grid), LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(16, 0, 8, 0);
            homeWishlistCartViewBinding.getRoot().setLayoutParams(lp);
            Element element = sectionElementArrayList.get(position);
            Glide.with(context).load(element.getTitle().getLogo()).into(homeWishlistCartViewBinding.imageViewProduct);


            if (element.getTitle().getRefObject() != null) {
                try {
                    RefObject refObject = element.getTitle().getRefObject();
                    if (refObject.getDescriptions() != null && refObject.getDescriptions().size() > 0)
                        homeWishlistCartViewBinding.tvProductName.setText(refObject.getDescriptions().get(0).getProductName() + "\n");
                    String strOriginalPrice = String.format("%.3f", refObject.getOriginalPrice());
                    if (element.getTitle().getRefObject().getDiscountPrice() != null && element.getTitle().getRefObject().getDiscountPrice() > 0) {
                        homeWishlistCartViewBinding.tvOriginalPrice.setVisibility(View.VISIBLE);
                        String strDiscounted = String.format("%.3f", element.getTitle().getRefObject().getDiscountPrice());
                        homeWishlistCartViewBinding.tvOurPrice.setText("KWD " + strOriginalPrice);
                        homeWishlistCartViewBinding.tvOriginalPrice.setText("KWD " + strDiscounted);
                        homeWishlistCartViewBinding.tvOriginalPrice.setPaintFlags(homeWishlistCartViewBinding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        homeWishlistCartViewBinding.tvOurPrice.setText("KWD " + strOriginalPrice);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            homeWishlistCartViewBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), headerText);
                }
            });
            if (element.getTitle().getRefObject() != null) {
                if (element.getTitle().getRefObject().isWishlist())
                    homeWishlistCartViewBinding.radioButtonWishlist.setChecked(true);
                else
                    homeWishlistCartViewBinding.radioButtonWishlist.setChecked(false);
            }
            homeWishlistCartViewBinding.radioButtonWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (element.getTitle().getType().equalsIgnoreCase("p"))
                        homeItemClickListener.onWishlistClicked(element.getTitle().getRefObject(), homeWishlistCartViewBinding.radioButtonWishlist.isChecked());
                }
            });
            homeWishlistCartViewBinding.imageButtonCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (element.getTitle().getType().equalsIgnoreCase("p"))
                        homeItemClickListener.onCartClicked(element.getTitle().getRefObject());
                }
            });

//
            if (element.getTitle().getRefObject() != null && !element.getTitle().getRefObject().isAvailable()) {
                homeWishlistCartViewBinding.tvOutOfStock.setVisibility(View.VISIBLE);
                homeWishlistCartViewBinding.tvRibbon.setVisibility(View.GONE);
            } else {
                homeWishlistCartViewBinding.tvOutOfStock.setVisibility(View.GONE);

            }
            if (element.getTitle().getRefObject() != null)
                if (element.getTitle().getRefObject().isAvailable() && element.getTitle().getRefObject().getDiscountPercentage() > 0) {
                    homeWishlistCartViewBinding.tvRibbon.setText("");
                    String text = homeWishlistCartViewBinding.tvRibbon.getText().toString();
                    String newText = "";
                    newText = new StringBuilder(text).append("S").toString();
                    newText = new StringBuilder(newText).append("\n").toString();
                    newText = new StringBuilder(newText).append("A").toString();
                    newText = new StringBuilder(newText).append("\n").toString();
                    newText = new StringBuilder(newText).append("L").toString();
                    newText = new StringBuilder(newText).append("\n").toString();
                    newText = new StringBuilder(newText).append("E").toString();

                    homeWishlistCartViewBinding.tvRibbon.setText(newText);
                    homeWishlistCartViewBinding.tvRibbon.setVisibility(View.VISIBLE);
                } else {
                    homeWishlistCartViewBinding.tvRibbon.setVisibility(View.GONE);
                }

//            if (element.getTitle().getRefObject() != null) {
//                if (element.getTitle().getRefObject().getDiscountPercentage() == 0)
//                    homeWishlistCartViewBinding.tvRibbon.setVisibility(View.GONE);
//                else
//                    homeWishlistCartViewBinding.tvRibbon.setText(element.getTitle().getRefObject().getDiscountPercentage() + "% OFF");

        }
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeCartViewBinding homeCartViewBinding;

        public CartViewHolder(@NonNull LayoutHomeCartViewBinding homeCartViewBinding) {
            super(homeCartViewBinding.getRoot());
            this.homeCartViewBinding = homeCartViewBinding;
        }

        public void onBind(int position) {
            int grid = 0;
            if (homeSection.getElements().size() == 1)
                grid = 4;
            else if (homeSection.getElements().size() >= 2)
                grid = 4;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(grid), LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(16, 0, 0, 0);
            homeCartViewBinding.getRoot().setLayoutParams(lp);
            Element element = sectionElementArrayList.get(position);
            homeCartViewBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), headerText);
                }
            });
            homeCartViewBinding.tvProductName.setText(element.getTitle().getTitleName());

            Glide.with(context).load(element.getTitle().getLogo()).into(homeCartViewBinding.imageViewProduct);

            if (element.getTitle().getRefObject() != null) {
                String strDouble = String.format("%.3f", element.getTitle().getRefObject().getOriginalPrice());
                homeCartViewBinding.tvOriginalPrice.setText("KWD " + strDouble);
            }

            homeCartViewBinding.imageButtonCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (element.getTitle().getType().equalsIgnoreCase("p"))
                        homeItemClickListener.onCartClicked(element.getTitle().getRefObject());
                }
            });

        }
    }

    class CarouselViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeCarouselViewBinding homeCarouselViewBinding;
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(context)
                        .asBitmap()
                        .override(ViewGroup.LayoutParams.MATCH_PARENT, homeSection.getDimension().getContainerHeight())
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

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeItemClickListener.onItemClicked(homeSection, sectionElementArrayList.get(position), headerText);
                    }
                });

            }
        };


        public CarouselViewHolder(@NonNull LayoutHomeCarouselViewBinding homeCarouselViewBinding) {
            super(homeCarouselViewBinding.getRoot());
            this.homeCarouselViewBinding = homeCarouselViewBinding;
        }

        public void onBind(int position) {
//
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(1), (int) context.getResources().getDimension(R.dimen._300dp));
            lp.setMargins(0, 0, 0, 0);
            homeCarouselViewBinding.getRoot().setLayoutParams(lp);

            /*ArrayList<SlideModel> slideList = new ArrayList<SlideModel>();
            for (int i = 0; i < imageList.size(); i ++) {
                slideList.add(new SlideModel(imageList.get(i), null));
            }

            homeCarouselViewBinding.carouselView.setImageList(slideList);

            homeCarouselViewBinding.carouselView.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemSelected(int i) {
                    homeItemClickListener.onItemClicked(sectionElementArrayList.get(position));
                }
            });*/


            homeCarouselViewBinding.carouselView.setImageListener(imageListener);
            if (imageList != null)
                homeCarouselViewBinding.carouselView.setPageCount(imageList.size());
            else
                homeCarouselViewBinding.carouselView.setPageCount(0);
            homeCarouselViewBinding.carouselView.setCurrentItem(position);

//
//            homeCarouselViewBinding.recylcerviewCarousels.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            CarouselViewAdapter carouselViewAdapter = new CarouselViewAdapter(context, (ArrayList<String>) imageList);
//            homeCarouselViewBinding.recylcerviewCarousels.setAdapter(carouselViewAdapter);
//            if (imageList.size() > 1) {
//                homeCarouselViewBinding.recylcerviewCarousels.setOnFlingListener(null);
////                homeCarouselViewBinding.recylcerviewCarousels.setNestedScrollingEnabled(false);
//                homeCarouselViewBinding.recylcerviewCarousels.setHasFixedSize(true);
//                final int radius = context.getResources().getDimensionPixelSize(R.dimen.radius);
//                final int dotsHeight = context.getResources().getDimensionPixelSize(R.dimen.dots_height);
//                final int color = ContextCompat.getColor(context, R.color.ace_theme_color);
//                homeCarouselViewBinding.recylcerviewCarousels.addItemDecoration(new DotsIndicatorDecoration(radius, radius * 4, dotsHeight, color, color));
//
//            }

//            SliderAdapter adapter = new SliderAdapter(context);
//            adapter.renewItems(imageList);
//
//            homeCarouselViewBinding.imageSlider.setSliderAdapter(adapter);
//
//            homeCarouselViewBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
////            homeCarouselViewBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
////            homeCarouselViewBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
////            homeCarouselViewBinding.imageSlider.setIndicatorSelectedColor(con);
////            homeCarouselViewBinding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
//            homeCarouselViewBinding.imageSlider.setScrollTimeInSec(3); //set scroll delay in seconds :
////            homeCarouselViewBinding.imageSlider.startAutoCycle();


        }

    }

    class TimerViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeTimerViewBinding homeTimerViewBinding;

        public TimerViewHolder(@NonNull LayoutHomeTimerViewBinding homeTimerViewBinding) {
            super(homeTimerViewBinding.getRoot());
            this.homeTimerViewBinding = homeTimerViewBinding;
        }

        public void onBind(int position) {
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthForGivenGrid(4), (int) context.getResources().getDimension(R.dimen._200dp));
//            lp.setMargins(0, 0, 0, 0);
//            homeTimerViewBinding.getRoot().setLayoutParams(lp);
            Element element = sectionElementArrayList.get(position);
            Glide.with(context).load(element.getTitle().getLogo()).into(homeTimerViewBinding.imageView);

            processTimer(element, homeTimerViewBinding);
            homeTimerViewBinding.cvBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeItemClickListener.onItemClicked(homeSection, element, headerText);
                }
            });
        }

    }
}


