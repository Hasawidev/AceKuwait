package com.alhasawi.acekuwait.ui.main.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.NavigationMenuItem;
import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductListResponse;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.data.api.response.MainCategoryResponse;
import com.alhasawi.acekuwait.data.repository.DynamicDataRepository;
import com.alhasawi.acekuwait.data.repository.ProductRepository;
import com.alhasawi.acekuwait.utils.PreferenceHandler;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {
    ArrayList<NavigationMenuItem> menuItemArrayList;
    ProductRepository productRepository;
    DynamicDataRepository dynamicDataRepository;

    public DashboardViewModel() {
        this.productRepository = new ProductRepository();
        dynamicDataRepository = new DynamicDataRepository();
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String query, String language) {
        return productRepository.searchProducts(query, language);
    }

    public ArrayList<NavigationMenuItem> getMenuItemsList(Context context) {
        if (menuItemArrayList == null)
            menuItemArrayList = new ArrayList<>();
        PreferenceHandler preferenceHandler = new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN);
        boolean isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_HOME, context.getResources().getString(R.string.menu_home), context.getResources().getDrawable(R.drawable.home_side_menu), true));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_NOTIFICATION, context.getResources().getString(R.string.menu_notifications), context.getResources().getDrawable(R.drawable.notification_side_menu), true));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_FAQ, context.getResources().getString(R.string.faq), context.getResources().getDrawable(R.drawable.conversation), true));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_ABOUT_US, context.getResources().getString(R.string.about_us), context.getResources().getDrawable(R.drawable.question), true));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_PRIVACY_POLICY, context.getResources().getString(R.string.privacy_policy), context.getResources().getDrawable(R.drawable.contract), true));
//        menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_CONTACT_US, context.getResources().getString(R.string.contact_us), context.getResources().getDrawable(R.drawable.headset), true));
//        if (isLoggedIn) {
//            menuItemArrayList.add(new NavigationMenuItem(AppConstants.ID_MENU_SIGNOUT, context.getResources().getString(R.string.menu_signout), context.getResources().getDrawable(R.drawable.logout), true));
//        }
        return menuItemArrayList;
    }

    public MutableLiveData<Resource<MainCategoryResponse>> getMainCateogries(String language) {
        return dynamicDataRepository.getMainCategories(language);
    }


    public MutableLiveData<Resource<CartResponse>> getCartItems(String userID, String sessiontoken, String language) {
        return productRepository.getCartItems(userID, sessiontoken, language, false);
    }

    public MutableLiveData<Resource<HomeResponse>> getHomeData(String languageId) {
        return dynamicDataRepository.getHomeData(languageId);
    }

}
