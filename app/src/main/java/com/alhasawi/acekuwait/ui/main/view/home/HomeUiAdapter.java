package com.alhasawi.acekuwait.ui.main.view.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutHomeUiAdapterItemBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.AppConstants;

import java.util.ArrayList;

public class HomeUiAdapter extends RecyclerView.Adapter<HomeUiAdapter.ViewHolder> {
    HomeSection currentHomeSection;
    HomeSectionElementsAdapter sectionElementsAdapter;
    int VIEW_TYPE = 0;
    HomeSectionElementsAdapter.HomeItemClickListener homeItemClickListener;
    private DashboardActivity context;
    private ArrayList<HomeSection> homeSectionList;
    private ArrayList<Element> selectedElementsList = new ArrayList<>();

    public HomeUiAdapter(DashboardActivity context, ArrayList<HomeSection> homeSections, HomeSectionElementsAdapter.HomeItemClickListener listener) {
        this.homeItemClickListener = listener;
        this.context = context;
        if (homeSections != null && homeSections.size() > 0)
            this.homeSectionList = homeSections;
        else
            homeSectionList = new ArrayList<>();

    }

    @NonNull
    @Override
    public HomeUiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutHomeUiAdapterItemBinding homeUiAdapterItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_home_ui_adapter_item, null, false);
        return new ViewHolder(homeUiAdapterItemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (homeSectionList.size() > 0) {
            try {
                currentHomeSection = homeSectionList.get(position);
                VIEW_TYPE = getVIEW_TYPE(currentHomeSection.getView().getTag());
                String headerText = "";
                if (currentHomeSection.getName() == null || currentHomeSection.getName().equals("")) {
                    headerText = "";
                } else {
                    headerText = currentHomeSection.getName().toString();
                }
                if (VIEW_TYPE == AppConstants.VIEW_TYPE_UPCOMING) {
                    holder.homeUiAdapterItemBinding.tvSectionHeadingTop.setVisibility(View.GONE);
                    holder.homeUiAdapterItemBinding.view47.setVisibility(View.VISIBLE);
                    holder.homeUiAdapterItemBinding.tvUpcomingProducts.setVisibility(View.VISIBLE);
                    if (currentHomeSection.getName() != null && !currentHomeSection.getName().equals(""))
                        holder.homeUiAdapterItemBinding.tvUpcomingProducts.setText(currentHomeSection.getName());
                } else {
                    holder.homeUiAdapterItemBinding.view47.setVisibility(View.GONE);
                    holder.homeUiAdapterItemBinding.tvUpcomingProducts.setVisibility(View.GONE);
                    if (currentHomeSection.getName() == null || currentHomeSection.getName().equals("")) {
                        holder.homeUiAdapterItemBinding.tvSectionHeadingTop.setVisibility(View.GONE);
                    } else {
                        holder.homeUiAdapterItemBinding.tvSectionHeadingTop.setVisibility(View.VISIBLE);
                        holder.homeUiAdapterItemBinding.tvSectionHeadingTop.setText(currentHomeSection.getName());
                    }

                }
                selectedElementsList = (ArrayList<Element>) currentHomeSection.getElements();
                sectionElementsAdapter = new HomeSectionElementsAdapter(currentHomeSection, context, VIEW_TYPE, headerText);
                sectionElementsAdapter.setHomeItemClickListener(homeItemClickListener);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL,
                        false);


//                LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false){
//
//                    @Override
//                    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//
//                        View view = recycler.getViewForPosition(position);
//                        if (view != null) {
//                            measureChild(view, widthSpec, heightSpec);
//                            //int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                            int measuredHeight = currentHomeSection.getDimension().getContainerHeight();
////                            int showHeight = measuredHeight * state.getItemCount();
////                            if(state.getItemCount() >= 5){
////                                showHeight = measuredHeight * 5;
////                            }
//                            setMeasuredDimension(widthSpec, measuredHeight);
//                        }
//                    }
//                };
//                sectionElementsAdapter.setOnItemClickListener(this);
                if (VIEW_TYPE == AppConstants.VIEW_TYPE_CAROUSEL) {
                    LinearLayoutManager lm = new LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL,
                            true) {
                        @Override
                        public boolean canScrollHorizontally() {
                            return false;
                        }
                    };
                    holder.homeUiAdapterItemBinding.recyclerviewHomeSectionElements.setLayoutManager(lm);
                } else {
                    holder.homeUiAdapterItemBinding.recyclerviewHomeSectionElements.setLayoutManager(layoutManager);
                }
                holder.homeUiAdapterItemBinding.recyclerviewHomeSectionElements.setAdapter(sectionElementsAdapter);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, currentHomeSection.getDimension().getContainerHeight());
//                holder.homeUiAdapterItemBinding.getRoot().setLayoutParams(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeSectionList.size();
    }

//    @Override
//    public void onItemClickListener(int position, View view) {
//        sectionElementsAdapter.selectedItem();
//        Element element = selectedElementsList.get(position);
//        onSectionElementClicked(currentHomeSection, element);
//    }

    public int getVIEW_TYPE(String view_tag) {
        switch (view_tag) {
            case "category_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_CATEGORY;
                return VIEW_TYPE;
            case "banner_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_BANNER;
                return VIEW_TYPE;
            case "text_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_TEXTVIEW;
                return VIEW_TYPE;
            case "wishlist_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_WISHLIST;
                return VIEW_TYPE;
            case "cart_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_CART;
                return VIEW_TYPE;
            case "wishlist_cart_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_WISHLIST_CART;
                return VIEW_TYPE;
            case "carousel_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_CAROUSEL;
                return VIEW_TYPE;
            case "store_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_STORE;
                return VIEW_TYPE;
            case "timer_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_TIMER;
                return VIEW_TYPE;
            case "bag_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_BAG_VIEW;
                return VIEW_TYPE;
            case "herobanner_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_HERO_BANNER;
                return VIEW_TYPE;
            case "upcoming_view":
                VIEW_TYPE = AppConstants.VIEW_TYPE_UPCOMING;
                return VIEW_TYPE;
        }
        return VIEW_TYPE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutHomeUiAdapterItemBinding homeUiAdapterItemBinding;

        public ViewHolder(@NonNull LayoutHomeUiAdapterItemBinding homeUiAdapterItemBinding) {
            super(homeUiAdapterItemBinding.getRoot());
            this.homeUiAdapterItemBinding = homeUiAdapterItemBinding;
        }
    }


}
