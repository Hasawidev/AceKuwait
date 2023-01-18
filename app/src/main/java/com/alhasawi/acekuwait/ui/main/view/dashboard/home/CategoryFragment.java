package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.databinding.FragmentCategoriesBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.CategoryAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.CategoryViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CategoryFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentCategoriesBinding fragmentCategoriesBinding;
    CategoryViewModel categoryViewModel;
    String selectedMainCategoryId = "";
    DashboardActivity dashboardActivity;
    ArrayList<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private String mainCatIdLog = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_categories;
    }

    @Override
    protected void setup() {
        fragmentCategoriesBinding = (FragmentCategoriesBinding) viewDataBinding;
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionMenuBar(true, true);
        dashboardActivity.handleActionBarIcons(true);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String categoryId = preferenceHandler.getData(PreferenceHandler.LOGIN_MAIN_CATEGORY_ID, "");
        String languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        mainCatIdLog = categoryId;
        callOriginCategory(categoryId, languageId);
    }

//    private void callCategoryApi() {
//        fragmentCategoriesBinding.progressBar.setVisibility(View.VISIBLE);
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        fragmentCategoriesBinding.recyclerViewCategories.setLayoutManager(horizontalLayoutManager);
//
//        categoryViewModel.getCategoryTree().observe(this, mainCategoryResponseResource -> {
//            switch (mainCategoryResponseResource.status) {
//                case SUCCESS:
//                    categories = (ArrayList<Category>) mainCategoryResponseResource.data.getMainCategories();
//                    categoryAdapter = new CategoryAdapter(getActivity(), categories);
//                    categoryAdapter.setOnItemClickListener(this);
//                    fragmentCategoriesBinding.recyclerViewCategories.setAdapter(categoryAdapter);
//
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    Toast.makeText(dashboardActivity, mainCategoryResponseResource.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            fragmentCategoriesBinding.progressBar.setVisibility(View.GONE);
//        });
//
//    }

    private void callOriginCategory(String selectedMainCategoryId, String languageId) {
        fragmentCategoriesBinding.progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentCategoriesBinding.recyclerViewCategories.setLayoutManager(horizontalLayoutManager);

        categoryViewModel.getCategoriesByOrigin(selectedMainCategoryId, languageId).observe(this, originCategoryResponseResource -> {
            switch (originCategoryResponseResource.status) {
                case SUCCESS:
                    try {

                        categories = (ArrayList<Category>) originCategoryResponseResource.data.getData().getCategoryList();
                        categoryAdapter = new CategoryAdapter(getActivity(), categories);
                        categoryAdapter.setOnItemClickListener(this);
                        fragmentCategoriesBinding.recyclerViewCategories.setAdapter(categoryAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), originCategoryResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentCategoriesBinding.progressBar.setVisibility(View.GONE);
        });

    }


    @Override
    public void onItemClickListener(int position, View view) {
        try {
            categoryAdapter.selectedItem();
            selectedMainCategoryId = categories.get(position).getCategoryId();
            String mainCategoryName = categories.get(position).getDescriptions().get(0).getCategoryName();
            if (categories.get(position).getCategories().size() == 0) {
                redirectToProductListing(selectedMainCategoryId, mainCategoryName);
            } else {
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String categoryString = gson.toJson(categories.get(position));
                bundle.putString("category_string", categoryString);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new SubcategoryFragment(), bundle, true, false);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onResume() {
        super.onResume();
        CategoryAdapter.setsSelected(0);
    }
}

