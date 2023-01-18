package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductSearch;
import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductCategory;
import com.alhasawi.acekuwait.data.api.response.SearchResponse;
import com.alhasawi.acekuwait.databinding.FragmentSearchResultBinding;
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

public class SearchResultFragment extends BaseFragment {
    FragmentSearchResultBinding fragmentSearchResultBinding;
    DashboardActivity dashboardActivity;
    SearchViewModel searchViewModel;
    List<ProductSearch> productSearchList = new ArrayList<>();
    List<ProductSearch> initialSearchedProductList = new ArrayList<>();
    SearchProductAdapter searchProductAdapter;
    SearchCategoryAdapter searchCategoryAdapter;
    String languageId = "", category = "";
    List<SearchProductCategory> searchProductCategoryList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void setup() {
        fragmentSearchResultBinding = (FragmentSearchResultBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        fragmentSearchResultBinding.recyclerviewSearchResultsProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        try {
            String query = "";
            if (getArguments() != null)
                query = getArguments().getString("query");
            if (!query.equals(""))
                callSearchApi(query, category);
            else {
                fragmentSearchResultBinding.cvNoData.setVisibility(View.VISIBLE);
                fragmentSearchResultBinding.btnViewAllProducts.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentSearchResultBinding.recyclerviewSearchResultsProducts.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {

                    ProductSearch selectedProduct = productSearchList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", selectedProduct.getSku());
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);
//                    getParentFragmentManager().popBackStackImmediate();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        fragmentSearchResultBinding.recyclerviewCategories.setLayoutManager(new LinearLayoutManager(dashboardActivity,
                LinearLayoutManager.VERTICAL, false));
        fragmentSearchResultBinding.btnViewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    redirectToProductListingFragment(getAllSearchedProductIds());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fragmentSearchResultBinding.recyclerviewCategories.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
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

    private void callSearchApi(String query, String category) {
        productSearchList = new ArrayList<>();
        searchProductCategoryList = new ArrayList<>();
        if (searchProductAdapter != null)
            searchProductAdapter.notifyDataSetChanged();
        if (searchProductCategoryList != null)
            searchProductAdapter.notifyDataSetChanged();
        fragmentSearchResultBinding.progressBar.setVisibility(View.VISIBLE);
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
                                fragmentSearchResultBinding.cvNoData.setVisibility(View.VISIBLE);
                            } else {
                                fragmentSearchResultBinding.btnViewAllProducts.setVisibility(View.VISIBLE);
                            }

//                            searchProductAdapter = new SearchProductAdapter(getContext(), productSearchList);
//                            fragmentSearchResultBinding.recyclerviewSearchResultsProducts.setAdapter(searchProductAdapter);

                            if (searchProductListResponse.data.getData() != null) {
                                try {
                                    HashMap<String, String> categoriesMap = searchProductListResponse.data.getData().getCategoriesMap();
                                    HashMap<String, List<String>> productsByCategory = searchProductListResponse.data.getData().getProductByCategoryMap();
                                    if (categoriesMap != null && productsByCategory != null) {
                                        searchProductCategoryList = new ArrayList<>();
                                        searchProductCategoryList = getProdutCategories(categoriesMap, productsByCategory);
                                        if (searchProductCategoryList != null && searchProductCategoryList.size() > 0) {
//                                            searchCategoryAdapter = new SearchCategoryAdapter(dashboardActivity, searchProductCategoryList);
//                                            fragmentSearchResultBinding.recyclerviewCategories.setAdapter(searchCategoryAdapter);

                                        }


                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                fragmentSearchResultBinding.cvNoData.setVisibility(View.VISIBLE);
                                fragmentSearchResultBinding.btnViewAllProducts.setVisibility(View.GONE);
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
                    fragmentSearchResultBinding.cvNoData.setVisibility(View.VISIBLE);
                    GeneralDialog generalDialog = new GeneralDialog("Error", searchProductListResponse.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            fragmentSearchResultBinding.progressBar.setVisibility(View.GONE);

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

    public void setQuery(String searchQuery) {
        searchProductCategoryList = new ArrayList<>();
        productSearchList = new ArrayList<>();
        fragmentSearchResultBinding.btnViewAllProducts.setVisibility(View.GONE);
        searchCategoryAdapter.notifyDataSetChanged();
        searchProductAdapter.notifyDataSetChanged();
        callSearchApi(searchQuery, category);
    }
}
