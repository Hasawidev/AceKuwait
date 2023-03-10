package com.alhasawi.acekuwait.ui.main.view.dashboard.product;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentSortBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.SortAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.SharedHomeViewModel;

import java.util.ArrayList;

public class SortFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    DashboardActivity dashboardActivity;
    SharedHomeViewModel sharedHomeViewModel;
    FragmentSortBinding fragmentSortBinding;
    SortAdapter sortAdapter;
    String selectedSort = "", isPreference = "yes";
    ArrayList<String> sortList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sort;
    }

    @Override
    protected void setup() {
        fragmentSortBinding = (FragmentSortBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(true);
        sharedHomeViewModel = new ViewModelProvider(getActivity()).get(SharedHomeViewModel.class);
        try {
            Bundle bundle = getArguments();
            sortList = bundle.getStringArrayList("sort_string");
            sortAdapter = new SortAdapter(sortList);
            sortAdapter.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentSortBinding.recyclerviewSort.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentSortBinding.recyclerviewSort.setAdapter(sortAdapter);
        fragmentSortBinding.imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
        try {


//            sortAdapter = new SortAdapter() {
//                @Override
//                public void onSortSelected(String sort) {
//                    selectedSort = sort;
//                    returnSortValues();
//                    getParentFragmentManager().popBackStackImmediate();
//                }
//            };
//            sharedHomeViewModel.getSortString().observe(getActivity(), list -> {
//                if (list != null) {
//                    sortAdapter.addAll(list);
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void returnSortValues() {
        ProductListingFragment productListingFragment = (ProductListingFragment) getTargetFragment();
        productListingFragment.setSortData(selectedSort, isPreference);
        dashboardActivity.getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onItemClickListener(int position, View view) {
        sortAdapter.selectedItem();
        selectedSort = sortList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("sort_value", selectedSort);
        returnSortValues();
    }
}
