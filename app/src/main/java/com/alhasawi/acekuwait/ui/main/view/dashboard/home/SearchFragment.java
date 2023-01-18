package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import static com.alhasawi.acekuwait.ui.events.InAppEvents.logSearchEvent;
import static com.alhasawi.acekuwait.ui.events.InAppEvents.logSearchProductEvent;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductSearch;
import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductCategory;
import com.alhasawi.acekuwait.data.api.response.SearchResponse;
import com.alhasawi.acekuwait.databinding.FragmentSearchBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.SearchCategoryAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.SearchProductAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerItemClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.SearchViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends BaseFragment {
    FragmentSearchBinding fragmentSearchBinding;
    DashboardActivity dashboardActivity;
    SearchViewModel searchViewModel;
    List<ProductSearch> productSearchList = new ArrayList<>();
    List<ProductSearch> initialSearchedProductList = new ArrayList<>();
    SearchProductAdapter searchProductAdapter;
    SearchCategoryAdapter searchCategoryAdapter;
    String languageId = "", category = "";
    List<SearchProductCategory> searchProductCategoryList = new ArrayList<>();
    private String searchQuery = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void setup() {

        fragmentSearchBinding = (FragmentSearchBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        handleSearchView();
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        fragmentSearchBinding.layoutSearchResult.recyclerviewSearchResultsProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        searchProductAdapter = new SearchProductAdapter(dashboardActivity);
        fragmentSearchBinding.layoutSearchResult.recyclerviewSearchResultsProducts.setAdapter(searchProductAdapter);
        fragmentSearchBinding.layoutSearchResult.cvNoData.setVisibility(View.VISIBLE);

        fragmentSearchBinding.layoutSearchResult.recyclerviewSearchResultsProducts.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {

                    ProductSearch selectedProduct = productSearchList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", selectedProduct.getSku());
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);
//                    getParentFragmentManager().popBackStackImmediate();
                    try {
                        logSearchProductEvent(selectedProduct.getObjectID(), selectedProduct.getNameEn(), searchQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        fragmentSearchBinding.layoutSearchResult.recyclerviewCategories.setLayoutManager(new LinearLayoutManager(dashboardActivity,
                LinearLayoutManager.VERTICAL, false));
        searchCategoryAdapter = new SearchCategoryAdapter(dashboardActivity);
        fragmentSearchBinding.layoutSearchResult.recyclerviewCategories.setAdapter(searchCategoryAdapter);

        fragmentSearchBinding.layoutSearchResult.btnViewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    redirectToProductListingFragment(getAllSearchedProductIds());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fragmentSearchBinding.layoutSearchResult.recyclerviewCategories.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    SearchProductCategory category = searchProductCategoryList.get(position);
                    List<String> productIds = category.getProductList();
                    redirectToProductListingFragment(productIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
    //        setupViewPager(fragmentSearchBinding.viewPager);
//        fragmentSearchBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                loadSearchResultFragment();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        loadSearchResultFragment();
//        fragmentSearchBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                fragmentSearchBinding.viewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


//    private void loadSearchResultFragment() {
//        Bundle bundle = new Bundle();
//        bundle.putString("query", searchQuery);
//        dashboardActivity.replaceFragment(R.id.frameLayout, new SearchResultFragment(), bundle, false, false);
//    }

//    private void callSearchApi() {
//        BaseFragment currentFragment = (BaseFragment) getParentFragmentManager().findFragmentById(R.id.frameLayout);
//        SearchResultFragment searchResultFragment = (SearchResultFragment) currentFragment;
//        searchResultFragment.setQuery(searchQuery);
//
//    }

    private void handleSearchView() {
        fragmentSearchBinding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                logSearchEvent(searchQuery);
                fragmentSearchBinding.searchview.clearFocus();
                callSearchApi(searchQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.equalsIgnoreCase("")) {
                    searchQuery = query;
                    callSearchApi(searchQuery);
                }
                return false;
            }
        });

    }

    private void callSearchApi(String query) {
        if (!query.equals(""))
            callSearchApi(query, category);
        else {
            fragmentSearchBinding.layoutSearchResult.cvNoData.setVisibility(View.VISIBLE);
            fragmentSearchBinding.layoutSearchResult.btnViewAllProducts.setVisibility(View.GONE);
        }
    }

    private void callSearchApi(String query, String category) {
//        fragmentSearchResultBinding.progressBar.setVisibility(View.VISIBLE);
        productSearchList = new ArrayList<>();
        searchProductCategoryList = new ArrayList<>();
        searchCategoryAdapter.addAll(searchProductCategoryList);
        searchProductAdapter.addAll(productSearchList);
        searchViewModel.getSearchResults(query, category, languageId).observe(this, searchProductListResponse -> {
            switch (searchProductListResponse.status) {
                case SUCCESS:
                    if (searchProductListResponse.data.getStatusCode() == 200) {
                        if (searchProductListResponse != null) {
                            SearchResponse productListResponse = searchProductListResponse.data;
                            initialSearchedProductList = productListResponse.getData().getProductSearchList();
                            if (initialSearchedProductList != null && initialSearchedProductList.size() > 0) {
                                for (int i = 0; i < initialSearchedProductList.size(); i++) {
                                    if (i <= 3)
                                        productSearchList.add(initialSearchedProductList.get(i));
                                    else
                                        break;
                                }
                            }
                            if (productSearchList.size() == 0) {
                                fragmentSearchBinding.layoutSearchResult.cvNoData.setVisibility(View.VISIBLE);
                            } else {
                                fragmentSearchBinding.layoutSearchResult.cvNoData.setVisibility(View.GONE);
                                fragmentSearchBinding.layoutSearchResult.btnViewAllProducts.setVisibility(View.VISIBLE);
                            }
                            searchProductAdapter.addAll(productSearchList);
                            searchProductAdapter.notifyDataSetChanged();
                            if (searchProductListResponse.data.getData() != null) {
                                try {
                                    HashMap<String, String> categoriesMap = searchProductListResponse.data.getData().getCategoriesMap();
                                    HashMap<String, List<String>> productsByCategory = searchProductListResponse.data.getData().getProductByCategoryMap();
                                    if (categoriesMap != null && productsByCategory != null) {
                                        searchProductCategoryList = new ArrayList<>();
                                        searchProductCategoryList = getProdutCategories(categoriesMap, productsByCategory);
                                        if (searchProductCategoryList != null && searchProductCategoryList.size() > 0) {
                                            searchCategoryAdapter.addAll(searchProductCategoryList);
                                            searchCategoryAdapter.notifyDataSetChanged();
                                        }


                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                fragmentSearchBinding.layoutSearchResult.cvNoData.setVisibility(View.VISIBLE);
                                fragmentSearchBinding.layoutSearchResult.btnViewAllProducts.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", searchProductListResponse.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    fragmentSearchBinding.layoutSearchResult.cvNoData.setVisibility(View.VISIBLE);
                    GeneralDialog generalDialog = new GeneralDialog("Error", searchProductListResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
//            f.progressBar.setVisibility(View.GONE);

        });
    }

    private List<SearchProductCategory> getProdutCategories(HashMap<String, String> categories, HashMap<String, List<String>> productsByCategory) {
        List<SearchProductCategory> searchProductCategoryList = new ArrayList<>();
        for (String key : categories.keySet()) {
            SearchProductCategory searchProductCategory = new SearchProductCategory(key, categories.get(key), productsByCategory.get(key));
            searchProductCategoryList.add(searchProductCategory);
        }
        return searchProductCategoryList;
    }

    private void redirectToProductListingFragment(List<String> productIds) {
        JSONArray productIdsArray = new JSONArray();
        if (productIds != null)
            for (int i = 0; i < productIds.size(); i++) {
                productIdsArray.put(productIds.get(i));
            }
        else
            productIdsArray = null;
        Bundle bundle = new Bundle();
        bundle.putString("product_ids", new Gson().toJson(productIdsArray));
        dashboardActivity.handleActionMenuBar(true, false);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);
    }

    private List<String> getAllProductIdsByCategory() {
        List<String> productIds = new ArrayList<>();
        if (searchProductCategoryList != null && searchProductCategoryList.size() > 0) {
            for (int i = 0; i < searchProductCategoryList.size(); i++) {
                productIds.addAll(searchProductCategoryList.get(i).getProductList());
            }
        }
        return productIds;
    }

    private List<String> getAllSearchedProductIds() {
        List<String> productIds = new ArrayList<>();
        if (initialSearchedProductList != null && initialSearchedProductList.size() > 0) {
            for (int i = 0; i < initialSearchedProductList.size(); i++) {
                productIds.add(initialSearchedProductList.get(i).getObjectID());
            }
        }
        return productIds;
    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getParentFragmentManager());
//        adapter.addFragment(new SearchResultFragment(), "");
//        adapter.addFragment(new SearchResultFragment(), "");
//        viewPager.setAdapter(adapter);
//    }

//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Fragment fragment = mFragmentList.get(position);
//            Bundle bundle = new Bundle();
//            bundle.putString("query", searchQuery);
//            fragment.setArguments(bundle);
//            return fragment;
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String name) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(name);
//
//        }
//    }

}
