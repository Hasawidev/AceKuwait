package com.alhasawi.acekuwait.ui.main.view.dashboard.product;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.FilterAttributeValues;
import com.alhasawi.acekuwait.databinding.FragmentFilterBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.FilterOptionAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.FilterViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.ProgressBarDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    public List<FilterAttributeValues> selectedFilters = new ArrayList<>();
    public List<FilterAttributeValues> selectedBrandFilters = new ArrayList<>();
    public List<FilterAttributeValues> selectedColorFilters = new ArrayList<>();
    public List<FilterAttributeValues> selectedSizeFilters = new ArrayList<>();
    DashboardActivity dashboardActivity;
    List<FilterAttributeValues> filterAttributeValuesList = new ArrayList<>();
    FilterViewModel filterViewModel;
    FragmentFilterBinding fragmentFilterBinding;
    FilterOptionAdapter filterOptionAdapter;
    //    FilterValueAdapter filterValueAdapter;
    ArrayList<String> filterKeysList = new ArrayList<>();
    Map<String, List<FilterAttributeValues>> filterAttributeMap;
    ProgressBarDialog progressBarDialog;
    JSONArray filterArray = null, brandArray = null, colorArray = null, sizeArray = null;
    JSONObject homeProductPayload;
    Bundle filterAnalyticsBundle = new Bundle();
    int filterAppliedCount = 0;
    private String selectedCategoryId = "", selectedSorting = "", inputPayload = "";
    private String isPreference = "yes";
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_filter;
    }

    @Override
    protected void setup() {
        fragmentFilterBinding = (FragmentFilterBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(true);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        filterViewModel = new ViewModelProvider(dashboardActivity).get(FilterViewModel.class);
        fragmentFilterBinding.recyclerFilterOptions.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentFilterBinding.recyclerFilterValues.setLayoutManager(new LinearLayoutManager(getActivity()));

        intitializeFilterAdapters();

        try {
            Bundle bundle = getArguments();
            selectedCategoryId = bundle.getString("category_id");
            inputPayload = bundle.getString("input_payload");
            callFilterApi(inputPayload);
//            loadFilterData(filterArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentFilterBinding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (selectedFilters.size() == 0 && selectedSizeFilters.size() == 0 && selectedColorFilters.size() == 0 && selectedBrandFilters.size() == 0)
                        setPreviouslyAppliedFilters();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filterAppliedCount++;

                filterArray = new JSONArray();
                if (selectedFilters != null) {
                    for (int i = 0; i < selectedFilters.size(); i++) {
                        filterArray.put(selectedFilters.get(i).getId());

                    }
                }
                brandArray = new JSONArray();
                if (selectedBrandFilters != null)
                    for (int i = 0; i < selectedBrandFilters.size(); i++) {
                        brandArray.put(selectedBrandFilters.get(i).getId());
                    }
                else
                    brandArray = null;
                colorArray = new JSONArray();
                if (selectedColorFilters != null)
                    for (int i = 0; i < selectedColorFilters.size(); i++) {
                        colorArray.put(selectedColorFilters.get(i).getId());
                    }
                else
                    colorArray = null;
                sizeArray = new JSONArray();
                if (selectedSizeFilters != null)
                    for (int i = 0; i < selectedSizeFilters.size(); i++) {
                        sizeArray.put(selectedSizeFilters.get(i).getId());
                    }
                else
                    sizeArray = null;
                Gson gson = new Gson();
                String appliedFilterValues = gson.toJson(filterArray);
                filterAnalyticsBundle.putString("filter_values", appliedFilterValues);
                try {
                    filterAnalyticsBundle.putString("selected_brands", gson.toJson(brandArray));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                returnFilterValues(filterArray, brandArray, colorArray, sizeArray);
            }
        });

//        fragmentFilterBinding.btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                for (int i = 0; i < selectedFilters.size(); i++) {
////                    selectedFilters.get(i).setSelected(false);
////                }
////                filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
////                loadFilterData(null);
//                resetilterData();
//            }
//        });
        fragmentFilterBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    private void intitializeFilterAdapters() {
        filterOptionAdapter = new FilterOptionAdapter(getActivity(), fragmentFilterBinding.recyclerFilterValues) {
            @Override
            public void onFilterSelected(List<FilterAttributeValues> filterAttributeValues, List<FilterAttributeValues> selectedBrands, List<FilterAttributeValues> selectedColors, List<FilterAttributeValues> selectedSizes) {
                selectedFilters = filterAttributeValues;
                selectedBrandFilters = selectedBrands;
                selectedColorFilters = selectedColors;
                selectedSizeFilters = selectedSizes;
                try {
                    String inputPayload = addInputParams();
                    callFilterApi(inputPayload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        filterOptionAdapter.setOnItemClickListener(this);
        fragmentFilterBinding.recyclerFilterOptions.setAdapter(filterOptionAdapter);
    }

    public void loadFilterData(JSONArray filterArray) {
        fragmentFilterBinding.progressBar.setVisibility(View.VISIBLE);
//        sharedHomeViewModel.getProductsInfo(selectedCategoryId, "0" +
//                "", filterArray).observe(dashboardActivity, productResponse -> {
//            switch (productResponse.status) {
//                case SUCCESS:
//                    filterKeysList.clear();
//                    filterAttributeMap = productResponse.data.getData().getFilterAttributes();
//                    filterKeysList = new ArrayList<>(filterAttributeMap.keySet());
//                    filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    Toast.makeText(dashboardActivity, productResponse.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//
//            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
//        });

//        sharedHomeViewModel.getFilterAttributeMap().observe(getActivity(), stringListMap -> {
//            filterKeysList.clear();
//            filterAttributeMap = stringListMap;
//            filterKeysList = new ArrayList<>(filterAttributeMap.keySet());
//            filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
//            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleAppBarForFilters(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        dashboardActivity.handleActionMenuBar(true, true);
        dashboardActivity.handleActionBarIcons(true);
        dashboardActivity.handleAppBarForFilters(false);
    }

    public void resetilterData() {
        filterArray = new JSONArray();
        brandArray = new JSONArray();
        colorArray = new JSONArray();
        sizeArray = new JSONArray();
        returnFilterValues(filterArray, brandArray, colorArray, sizeArray);
    }

    private void returnFilterValues(JSONArray filterArray, JSONArray brandArray, JSONArray colorArray, JSONArray sizeArray) {
        ProductListingFragment productListingFragment = (ProductListingFragment) getTargetFragment();
        productListingFragment.setFilterData(filterArray, brandArray, colorArray, sizeArray, isPreference);

        dashboardActivity.getSupportFragmentManager().popBackStackImmediate();

    }

    @Override
    public void onItemClickListener(int position, View view) {
        filterOptionAdapter.selectedItem();
        try {
            filterAnalyticsBundle.putString("filter_key", filterKeysList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPreviouslyAppliedFilters() {
        selectedFilters = new ArrayList<>();
        selectedSizeFilters = new ArrayList<>();
        selectedColorFilters = new ArrayList<>();
        selectedBrandFilters = new ArrayList<>();
        if (filterAttributeMap != null) {
            for (Map.Entry<String, List<FilterAttributeValues>> entry : filterAttributeMap.entrySet())
                if (entry.getKey().equalsIgnoreCase("brand"))
                    selectedBrandFilters = getCheckedFilterValues(entry.getValue());
                else if (entry.getKey().equalsIgnoreCase("color"))
                    selectedColorFilters = getCheckedFilterValues(entry.getValue());
                else if (entry.getKey().equalsIgnoreCase("size"))
                    selectedSizeFilters = getCheckedFilterValues(entry.getValue());
                else
                    selectedFilters.addAll(getCheckedFilterValues(entry.getValue()));

        }
    }

    private List<FilterAttributeValues> getCheckedFilterValues(List<FilterAttributeValues> filterAttributeValues) {
        List<FilterAttributeValues> checkedList = new ArrayList<>();
        for (int i = 0; i < filterAttributeValues.size(); i++) {
            if (filterAttributeValues.get(i).isChecked())
                checkedList.add(filterAttributeValues.get(i));
        }
        return checkedList;
    }

    private void callFilterApi(String inputPayload) {
        fragmentFilterBinding.progressBar.setVisibility(View.VISIBLE);
        filterViewModel.getFilter(inputPayload, languageId).observe(dashboardActivity, filterResponseResource -> {
            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
            switch (filterResponseResource.status) {
                case SUCCESS:
                    if (filterResponseResource.data.getStatusCode() == 200) {
                        if (filterResponseResource.data.getData() != null) {
                            filterKeysList.clear();
                            filterAttributeMap = filterResponseResource.data.getData().getFilterAttributes();
                            filterKeysList = new ArrayList<>(filterAttributeMap.keySet());
                            filterOptionAdapter.addAll(filterAttributeMap, filterKeysList);
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", filterResponseResource.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", filterResponseResource.message);
                        generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

            fragmentFilterBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private String addInputParams() {

        Map<String, Object> jsonParams = new ArrayMap<>();
        try {
            JSONObject inputPayloadObj = new JSONObject(inputPayload);

            if (inputPayloadObj != null) {
                JSONArray homeAttributeIds = inputPayloadObj.getJSONArray("attributeIds");
                if (selectedFilters.size() > 0) {
                    for (int i = 0; i < selectedFilters.size(); i++) {
                        if (homeAttributeIds.length() == 0)
                            homeAttributeIds.put(selectedFilters.get(i).getId());
                        else
                            for (int j = 0; j < homeAttributeIds.length(); j++) {
                                if (!homeAttributeIds.get(j).equals(selectedFilters.get(i).getId()))
                                    homeAttributeIds.put(selectedFilters.get(i).getId());
                                else
                                    homeAttributeIds.remove(j);
                            }
                    }
                }
                JSONArray homeBrandIds = inputPayloadObj.getJSONArray("brandIds");
                if (selectedBrandFilters.size() > 0) {
                    for (int i = 0; i < selectedBrandFilters.size(); i++) {
                        if (homeBrandIds.length() == 0)
                            homeBrandIds.put(selectedBrandFilters.get(i).getId());
                        else
                            for (int j = 0; j < homeBrandIds.length(); j++) {
                                if (!homeBrandIds.get(j).equals(selectedBrandFilters.get(i).getId()))
                                    homeBrandIds.put(selectedBrandFilters.get(i).getId());
                                else
                                    homeBrandIds.remove(j);
                            }
                    }
                }

                JSONArray homeSizes = inputPayloadObj.getJSONArray("sizes");

                if (selectedSizeFilters.size() > 0) {
                    for (int i = 0; i < selectedSizeFilters.size(); i++) {
                        if (homeSizes.length() == 0)
                            homeSizes.put(selectedSizeFilters.get(i).getId());
                        else
                            for (int j = 0; j < homeSizes.length(); j++) {
                                if (!homeSizes.get(j).equals(selectedSizeFilters.get(i).getId()))
                                    homeSizes.put(selectedSizeFilters.get(i).getId());
                                else
                                    homeSizes.remove(j);
                            }
                    }
                }

                JSONArray homeColors = inputPayloadObj.getJSONArray("colors");
                if (selectedColorFilters.size() > 0) {
                    for (int i = 0; i < selectedColorFilters.size(); i++) {
                        if (homeColors.length() == 0)
                            homeColors.put(selectedColorFilters.get(i).getId());
                        else
                            for (int j = 0; j < homeColors.length(); j++) {
                                if (!homeColors.get(j).equals(selectedColorFilters.get(i).getId()))
                                    homeColors.put(selectedColorFilters.get(i).getId());
                                else
                                    homeColors.remove(j);
                            }
                    }
                }
                JSONArray categoryIds = inputPayloadObj.getJSONArray("categoryIds");
                JSONArray productIds = inputPayloadObj.getJSONArray("productIds");
                String userId = inputPayloadObj.getString("customerId");
                String sort = inputPayloadObj.getString("sorting");

                jsonParams.put("categoryIds", categoryIds);
                jsonParams.put("attributeIds", homeAttributeIds);
                jsonParams.put("brandIds", homeBrandIds);
                jsonParams.put("productIds", productIds);
                jsonParams.put("sorting", sort);
                jsonParams.put("customerId", userId);
                jsonParams.put("sizes", homeSizes);
                jsonParams.put("colors", homeColors);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String input = (new JSONObject(jsonParams)).toString();
        return input;
    }

}
