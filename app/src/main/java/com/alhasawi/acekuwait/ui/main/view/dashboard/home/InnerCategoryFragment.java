package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.databinding.FragmentSubCategoriesBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.SubcategoryAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerItemClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class InnerCategoryFragment extends BaseFragment {
    FragmentSubCategoriesBinding fragmentSubCategoriesBinding;
    DashboardActivity dashboardActivity;
    Category mainCategory;
    ArrayList<Category> categoryList = new ArrayList<>();
    SubcategoryAdapter subCategoryAdapter;
    private String mainCatIdLog = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sub_categories;
    }

    @Override
    protected void setup() {

        fragmentSubCategoriesBinding = (FragmentSubCategoriesBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            Bundle bundle = getArguments();
            String categoryString = bundle.getString("category_string");
            Gson gson = new Gson();
            mainCategory = gson.fromJson(categoryString, (Type) Category.class);
            categoryList = (ArrayList<Category>) mainCategory.getCategories();
            subCategoryAdapter = new SubcategoryAdapter(getContext(), categoryList);
            fragmentSubCategoriesBinding.recyclerViewSubcategories.setLayoutManager(new LinearLayoutManager(getContext()));
            fragmentSubCategoriesBinding.recyclerViewSubcategories.setAdapter(subCategoryAdapter);
            fragmentSubCategoriesBinding.tvCategoryName.setText(mainCategory.getDescriptions().get(0).getCategoryName());
            mainCatIdLog = mainCategory.getDescriptions().get(0).getCategoryDescriptionId();

        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentSubCategoriesBinding.recyclerViewSubcategories.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    Category selectedSubcategory = categoryList.get(position);
                    redirectToProductListing(selectedSubcategory.getCategoryId(), selectedSubcategory.getDescriptions().get(0).getCategoryName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void redirectToProductListing(String categoryId, String categoryName) {
        Bundle bundle = new Bundle();
        bundle.putString("category_id", categoryId);
        bundle.putString("category_name", categoryName);
        dashboardActivity.handleActionMenuBar(true, false);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);
        try {
            InAppEvents.logCategoryViewEvent(mainCatIdLog, categoryId, categoryName);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }
}

