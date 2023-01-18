package com.alhasawi.acekuwait.ui.main.view;

import static com.alhasawi.acekuwait.ui.events.InAppEvents.logSearchProductEvent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.NavigationMenuItem;
import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductSearch;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.databinding.ActivityDashboardBinding;
import com.alhasawi.acekuwait.databinding.LayoutToastWishlistNotificationBinding;
import com.alhasawi.acekuwait.ui.base.AceHardware;
import com.alhasawi.acekuwait.ui.base.BaseActivity;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.HomeTabsPagerAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.SearchProductAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerItemClickListener;
import com.alhasawi.acekuwait.ui.main.view.checkout.CheckoutFragment;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;
import com.alhasawi.acekuwait.ui.main.view.checkout.OrderConfirmationFragment;
import com.alhasawi.acekuwait.ui.main.view.checkout.OrderSummaryFragment;
import com.alhasawi.acekuwait.ui.main.view.checkout.PaymentFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.home.BarcodeScanFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.home.CategoryFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.home.NotificationFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.home.NotifyMeFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.home.SearchFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.home.StoreFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.FilterFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.ProductDetailsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging.ProductListingFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.UserSettingsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.change_language.ChangeLanguageFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general.AboutUsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.UserAccountFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.WishListFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.loyalty.LoyaltyFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns.MyReturnsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history.OrderHistoryDetailFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history.OrderHistoryFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history.TrackOrderFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.profile.UserProfileFragment;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.DashboardViewModel;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.Connectivity;
import com.alhasawi.acekuwait.utils.ForceUpdateChecker;
import com.alhasawi.acekuwait.utils.LanguageHelper;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.ConfirmExitDialog;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.NoInternetDialog;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ForceUpdateChecker.OnUpdateNeededListener {

    public boolean zendeskMessagingEnabled = false;
    ActivityDashboardBinding activityDashboardBinding;
    ArrayList<NavigationMenuItem> menuItemArrayList = new ArrayList<>();
    Bundle dataBundle = new Bundle();
    SearchView searchView;
    MenuItem searchItem, wishlistItem, notificationItem, shareItem, bagItem, filterClearItem;
    ActionBar actionBar;
    boolean isUserLoggedin = false;
    String selectedLanguageId = "";
    HomeTabsPagerAdapter homeTabsPagerAdapter;
    private int cartCount = 0;
    private DashboardViewModel dashboardViewModel;
    private CharSequence mTitle;
    private HashMap<String, HomeResponse.Data> homeDataHashmap = new HashMap<>();
    private List<ProductSearch> productSearchList = new ArrayList<>();
    private SearchProductAdapter searchProductAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<Category> mainCategoryList = new ArrayList<>();
    private List<Category> allCategoryList = new ArrayList<>();
    private String searchQuery = "";
    private BadgeDrawable bottomBarBadgeDrawable;
    private String currentlyShowingProductId = "", currentlyShowingProductName = "";
    private NoInternetDialog noInternetDialog;
    private List<String> tabList = new ArrayList<>();

    @Override
    protected void setup() {
        AceHardware.getInstance().initAppLanguage(this);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        AceHardware.getInstance().initAppLanguage(this);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        setSupportActionBar(activityDashboardBinding.appBarMain.toolbar);
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        isUserLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        selectedLanguageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        if (selectedLanguageId.equals(""))
            selectedLanguageId = "en";
        LanguageHelper.changeLocale(getResources(), selectedLanguageId);


        try {
            Intent intent = getIntent();
            dataBundle = getIntent().getExtras();
            if (dataBundle != null) {
                String productId = dataBundle.containsKey("productID") ? dataBundle.getString("productID") : null;
                String categoryId = dataBundle.containsKey("categoryID") ? dataBundle.getString("categoryID") : null;
                String showReg = dataBundle.containsKey("showReg") ? dataBundle.getString("showReg") : null;
                if (productId != null) {
                    Log.e("productID", productId);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", productId);
                    handleActionMenuBar(false, false);
                    if (!productId.equals("")) {
                        replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);
                    }
                } else if (categoryId != null) {
                    Log.e("categoryID", categoryId);
                    Bundle bundle = new Bundle();
                    bundle.putString("category_id", categoryId);
                    handleActionMenuBar(true, true);
                    if (!categoryId.equals("")) {
                        replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);
                    }
                } else if (showReg != null) {
                    Log.e("showReg", showReg);

                    if (!showReg.equals("") && isUserLoggedin) {
                        handleActionMenuBar(true, false);
                        replaceFragment(R.id.fragment_replacer, new UserProfileFragment(), null, true, false);
                    }
                }
//                else {
//                    String login = dataBundle.getString("login");
//                    if (login.equals("login")) {
//                        Log.e("dash", "login extra");
//                        Intent signoutIntent = new Intent(this, SigninActivity.class);
//                        startActivity(signoutIntent);
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("");

        activityDashboardBinding.appBarMain.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        activityDashboardBinding.appBarMain.bottomNavigationView.setItemIconTintList(null);


        if (!Connectivity.isConnected(this)) {
            showNoInternetDialog();
        } else
            callHomeApi(selectedLanguageId);

        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
        preferenceHandler.saveData(PreferenceHandler.NOTIFICATION_STATUS, true);
        if (isUserLoggedin) {
            String productId = preferenceHandler.getData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
            if (!productId.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("product_object_id", productId);
                handleActionMenuBar(false, false);
                if (!productId.equals(""))
                    replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);
            }
        }
        parsingBranchDeepLink();
//        parsePushWooshDeeplinks();

        activityDashboardBinding.appBarMain.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callHomeApi(selectedLanguageId);

            }
        });

    }

    private void parsePushWooshDeeplinks() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
//        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
        try {
            String categoryId = data.getQueryParameter("category_id");
            if (!categoryId.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("category_id", categoryId);
                replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String productId = data.getQueryParameter("product_id");
            if (!productId.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("product_object_id", productId);
                replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String showReg = data.getQueryParameter("show_reg");
            if (showReg != null && !showReg.equals("")) {
                if (isUserLoggedin) {
                    handleActionMenuBar(true, false);
                    replaceFragment(R.id.fragment_replacer, new UserProfileFragment(), null, true, false);
                } else {
                    Intent siginInIntent = new Intent(DashboardActivity.this, SigninActivity.class);
                    startActivity(siginInIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }

    public void hideToolBar() {
        activityDashboardBinding.appBarMain.toolbar.setVisibility(View.GONE);
    }

    public void clearPreferences() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, false);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_CARTED, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_ITEM_TO_BE_WISHLISTED, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, false);
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CUSTOMER, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_NAME, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_CATEGORY_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_MAIN_CATEGORY_ID, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_NATIONALITY, "");
        preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, "");
        try {
//            Freshchat.resetUser(this);
            Log.e("signOut", "cleared");
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        activityDashboardBinding.appBarMain.recyclerSearchProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activityDashboardBinding.appBarMain.recyclerSearchProducts.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if (searchView != null) {
                        searchView.clearFocus();
                        searchItem.collapseActionView();
                    }
                    activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
                    ProductSearch selectedProduct = productSearchList.get(position);
                    String productName = "";
                    if (selectedLanguageId.equals("en"))
                        productName = selectedProduct.getNameEn();
                    else
                        productName = selectedProduct.getNameAr();
                    logSearchProductEvent(selectedProduct.getObjectID(), productName, searchQuery);
                    Bundle bundle = new Bundle();
                    bundle.putString("product_object_id", selectedProduct.getSku());
                    handleActionMenuBar(false, false);
                    replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        searchItem = menu.findItem(R.id.action_search);
        notificationItem = menu.findItem(R.id.action_notifications);
        wishlistItem = menu.findItem(R.id.action_wishlist);
        shareItem = menu.findItem(R.id.action_share);
        bagItem = menu.findItem(R.id.action_bag);
        filterClearItem = menu.findItem(R.id.action_filter_clear);
        filterClearItem.setVisible(false);
        shareItem.setVisible(false);
        bagItem.setVisible(false);
        searchItem.setVisible(true);
        wishlistItem.setVisible(true);
        notificationItem.setVisible(true);
//        searchView = (SearchView) searchItem.getActionView();
//        searchView.setQueryHint("What are you looking for?");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchQuery = query;
//                logSearchEvent(searchQuery);
//                searchView.clearFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                if (!query.equalsIgnoreCase("")) {
//                    if (activityDashboardBinding.appBarMain.fragmentReplaceSearchView.getVisibility() != View.VISIBLE) {
//                        activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.VISIBLE);
//                    }
//                    dashboardViewModel.searchProducts(query, selectedLanguageId).observe(DashboardActivity.this, searchProductListResponse -> {
//                        switch (searchProductListResponse.status) {
//                            case SUCCESS:
//                                if (searchProductListResponse.data.getStatusCode() == 200) {
//                                    productSearchList.clear();
//                                    productSearchList = searchProductListResponse.data.getData().getProductSearchs();
//                                    searchProductAdapter = new SearchProductAdapter(getApplicationContext(), productSearchList);
//                                    activityDashboardBinding.appBarMain.recyclerSearchProducts.setAdapter(searchProductAdapter);
//                                    searchProductAdapter.notifyDataSetChanged();
//                                } else {
//                                    GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), searchProductListResponse.data.getMessage());
//                                    generalDialog.show(getSupportFragmentManager(), "GENERAL_DIALOG");
//                                }
//
//                                break;
//                            case LOADING:
//                                break;
//                            case ERROR:
//                                GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), searchProductListResponse.message);
//                                generalDialog.show(getSupportFragmentManager(), "GENERAL_DIALOG");
//                                break;
//                        }
//                    });
//                }
//                return false;
//            }
//        });
//
//        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
//                return true;
//            }
//        });

        setCartBadges();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        switch (item.getItemId()) {
            case R.id.action_scan:
                hideToolBar();
                replaceFragment(R.id.fragment_replacer, new BarcodeScanFragment(), null, true, false);
                return true;
            case R.id.action_notifications:
                replaceFragment(R.id.fragment_replacer, new NotifyMeFragment(), null, true, false);
                handleActionMenuBar(true, false);
                return true;
            case R.id.action_search:
                replaceFragment(R.id.fragment_replacer, new SearchFragment(), null, true, false);
                return true;
            case R.id.action_share:
                return true;
            case R.id.action_wishlist:
                WishListFragment wishlistFragment = new WishListFragment();
                replaceFragment(R.id.fragment_replacer, wishlistFragment, null, true, false);
                handleActionMenuBar(true, false);
                return true;
            case R.id.action_filter_clear:
                BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_replacer_product);
                if (currentFragment instanceof FilterFragment) {
                    FilterFragment filterFragment = (FilterFragment) currentFragment;
                    filterFragment.resetilterData();
                }
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        handleActionBarIcons(true);
        activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
        BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_replacer);
        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
        showToolBar();
        switch (item.getItemId()) {

            case R.id.navigation_home:
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                handleActionMenuBar(true, true);
                return true;
            case R.id.navigation_stores:
                StoreFragment storeFragment = new StoreFragment();
                replaceFragment(R.id.fragment_replacer, storeFragment, null, true, false);
                handleActionMenuBar(true, true);
                return true;
            case R.id.navigation_categories:
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.VISIBLE);
                BaseFragment showingFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.framelayout_categories);
                if (showingFragment instanceof CategoryFragment) {

                } else {
                    replaceFragment(R.id.framelayout_categories, new CategoryFragment(), null, true, false);
                }
                return true;
            case R.id.navigation_cart:
                MyCart_1_Fragment myCartFragment = new MyCart_1_Fragment();
                if (isUserLoggedin)
                    handleActionMenuBar(true, false);
                else
                    handleActionMenuBar(true, true);
                replaceFragment(R.id.fragment_replacer, myCartFragment, null, true, false);
                return true;
            case R.id.navigation_profile:
                UserSettingsFragment userSettingsFragment;
                userSettingsFragment = new UserSettingsFragment();
                replaceFragment(R.id.fragment_replacer, userSettingsFragment, null, true, false);
                handleActionMenuBar(true, false);
                return true;
            default:
                return true;
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        try {
            mTitle = title;
            getSupportActionBar().setTitle(mTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleActionMenuBar(boolean showBottomNavigationMenu, boolean showToolbar) {
//         Showing back arrow icon. true means show and false means do not show
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        if (showBottomNavigationMenu)
            activityDashboardBinding.appBarMain.bottomNavigationView.setVisibility(View.VISIBLE);
        else
            activityDashboardBinding.appBarMain.bottomNavigationView.setVisibility(View.GONE);

        if (showToolbar)
            showToolBar();
        else
            hideToolBar();

    }

    @Override
    public void onBackPressed() {

        if (activityDashboardBinding.appBarMain.fragmentReplaceSearchView.getVisibility() == View.VISIBLE) {
            if (searchView != null) {
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
                searchView.clearFocus();
                activityDashboardBinding.appBarMain.toolbar.collapseActionView();
                activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
            }

        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() == 0) {
                ConfirmExitDialog confirmExitDialog = new ConfirmExitDialog(this);
                confirmExitDialog.show(getSupportFragmentManager(), "CONFIRM_EXIT_DIALOG");
                return;
            }

            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
            BaseFragment categoryFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.framelayout_categories);
            if (categoryFragment instanceof CategoryFragment && activityDashboardBinding.appBarMain.framelayoutCategories.getVisibility() == View.VISIBLE)
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);


            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_replacer);
            if (currentFragment == null)
                setTitle("");
            handleActionBarIcons(true);
            if (currentFragment instanceof NotifyMeFragment) {
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                handleActionMenuBar(true, true);
                handleActionBarIcons(true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof WishListFragment) {
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof MyCart_1_Fragment) {
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof StoreFragment) {
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof FilterFragment) {
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                handleAppBarForFilters(false);
            } else if (currentFragment instanceof MyReturnsFragment) {
                handleActionMenuBar(true, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof UserAccountFragment) {
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof UserSettingsFragment) {
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof LoyaltyFragment) {
                activityDashboardBinding.appBarMain.bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
                handleActionMenuBar(true, true);
                LoyaltyFragment loyaltyFragment = (LoyaltyFragment) currentFragment;
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof ChangeLanguageFragment) {
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof ProductDetailsFragment) {
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof PaymentFragment) {
                handleActionMenuBar(false, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderSummaryFragment) {
                handleActionMenuBar(false, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof CheckoutFragment) {
                handleActionMenuBar(false, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof UserProfileFragment) {
                handleActionMenuBar(true, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderHistoryFragment) {
                handleActionMenuBar(true, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof AboutUsFragment) {
                handleActionMenuBar(true, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
                activityDashboardBinding.appBarMain.toolbar.setVisibility(View.VISIBLE);
            } else if (currentFragment instanceof NotificationFragment) {
                handleActionMenuBar(true, true);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof TrackOrderFragment) {
                handleActionMenuBar(false, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderHistoryDetailFragment) {
                handleActionMenuBar(false, false);
                activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
            } else if (currentFragment instanceof OrderConfirmationFragment) {
//                OrderConfirmationFragment orderConfirmationFragment = (OrderConfirmationFragment) currentFragment;
//                orderConfirmationFragment.redirectToHome();
                clearTopFragments();
                onLocaleChanged();
            } else if (currentFragment instanceof ProductListingFragment) {
                setTitle("");
                handleActionMenuBar(true, true);
            } /*else if (currentFragment instanceof HomeFragment) {
                activityDashboardBinding.appBarMain.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }*/

            //Log.e("backStackCount", fm.getBackStackEntryCount() + "");
            try {
                if (fm.getBackStackEntryCount() == 1) {
                    activityDashboardBinding.appBarMain.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
            } catch (Exception e23) {
                e23.printStackTrace();
            }

        }
    }

    public void closeApp() {
        this.finishAffinity();
    }

    public void clearTopFragments() {
        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < fragmentCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
        activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.GONE);
        handleActionMenuBar(true, true);
        handleActionBarIcons(true);
    }


    public void hideSearchPage() {
        activityDashboardBinding.appBarMain.fragmentReplaceSearchView.setVisibility(View.GONE);
    }

    public void showCustomWislistToast(boolean isWishlisted) {
        LayoutInflater inflater = getLayoutInflater();
        LayoutToastWishlistNotificationBinding toastWishlistNotificationBinding = DataBindingUtil.inflate(inflater, R.layout.layout_toast_wishlist_notification, null, true);
        if (isWishlisted) {
            toastWishlistNotificationBinding.checkBoxWishlistStatus.setChecked(true);
            toastWishlistNotificationBinding.tvWishlistStatus.setText("Added to");
        } else {
            toastWishlistNotificationBinding.checkBoxWishlistStatus.setChecked(false);
            toastWishlistNotificationBinding.tvWishlistStatus.setText("Removed from");
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastWishlistNotificationBinding.getRoot());
        toast.show();
    }

    private void createDynamicHomeTabs(List<String> tabList, HashMap<String, HomeResponse.Data> homeDataHashmap) {
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
            String mainCategoryId = preferenceHandler.getData(PreferenceHandler.LOGIN_MAIN_CATEGORY_ID, "");
            if (mainCategoryId.equals("")) {
                if (homeDataHashmap != null) {
                    HomeResponse.Data data = homeDataHashmap.get("Ace");
                    mainCategoryId = data.getCategoryId();
                    preferenceHandler.saveData(PreferenceHandler.LOGIN_MAIN_CATEGORY_ID, mainCategoryId);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        homeTabsPagerAdapter = new HomeTabsPagerAdapter
                (this, getSupportFragmentManager(), tabList.size(), tabList, homeDataHashmap);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setAdapter(homeTabsPagerAdapter);
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.setOffscreenPageLimit(1);
        if (activityDashboardBinding.appBarMain.tabLayoutCategories.getTabCount() <= 5) {
            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_FIXED);
        } else {
            activityDashboardBinding.appBarMain.tabLayoutCategories.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        activityDashboardBinding.appBarMain.viewPagerHomeTabs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                activityDashboardBinding.appBarMain.viewPagerHomeTabs.setCurrentItem(position);
                String selctedProductOrigin = tabList.get(position);
                String categoryId = "";
                if (homeDataHashmap != null) {
                    HomeResponse.Data data = homeDataHashmap.get(selctedProductOrigin);
                    categoryId = data.getCategoryId();
                    String catName = data.getName();
                    try {
                        InAppEvents.logMainCategoryViewEvent(categoryId, catName);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                PreferenceHandler preferenceHandler = new PreferenceHandler(getBaseContext(), PreferenceHandler.TOKEN_LOGIN);
                preferenceHandler.saveData(PreferenceHandler.LOGIN_MAIN_CATEGORY_ID, categoryId);
                BaseFragment showingFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.framelayout_categories);
                if (showingFragment instanceof CategoryFragment) {
                    clearTopFragments();
                    activityDashboardBinding.appBarMain.framelayoutCategories.setVisibility(View.VISIBLE);
                    replaceFragment(R.id.framelayout_categories, new CategoryFragment(), null, true, false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        activityDashboardBinding.appBarMain.tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        activityDashboardBinding.appBarMain.tabLayoutCategories.setupWithViewPager(activityDashboardBinding.appBarMain.viewPagerHomeTabs);


    }

    public void onLocaleChanged() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        selectedLanguageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        if (selectedLanguageId.equals(""))
            selectedLanguageId = "en";
        LanguageHelper.changeLocale(getResources(), selectedLanguageId);
        Intent intent = getIntent();
        startActivity(intent);
        finish();
    }


    public void showMessageToast(String message, int duration) {
        Toast.makeText(DashboardActivity.this, message, duration).show();
    }

    public void handleSocialShare(boolean shouldShow) {
        if (shareItem != null) {
            if (shouldShow)
                shareItem.setVisible(true);
            else
                shareItem.setVisible(false);
        }
    }

    public void handleBag(boolean shouldShow) {
        if (bagItem != null) {
            if (shouldShow)
                bagItem.setVisible(true);
            else
                bagItem.setVisible(false);
        }
    }

    public void handleAppBarForFilters(boolean showFilter) {
        try {
            if (showFilter) {
                filterClearItem.setVisible(true);
                searchItem.setVisible(false);
                wishlistItem.setVisible(false);
                notificationItem.setVisible(false);
            } else {
                filterClearItem.setVisible(false);
                searchItem.setVisible(true);
                wishlistItem.setVisible(true);
                notificationItem.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleActionBarIcons(boolean showActionBaritems) {
        if (shareItem != null)
            shareItem.setVisible(false);
        if (bagItem != null)
            bagItem.setVisible(false);
        if (showActionBaritems) {
            if (searchItem != null)
                searchItem.setVisible(true);
            if (notificationItem != null)
                notificationItem.setVisible(true);
            if (wishlistItem != null)
                wishlistItem.setVisible(true);
        } else {
            if (searchItem != null)
                searchItem.setVisible(false);
            if (notificationItem != null)
                notificationItem.setVisible(false);
            if (wishlistItem != null)
                wishlistItem.setVisible(false);
        }
    }


    private void setAppBarBadge(int cartCount) {
        if (bagItem != null) {
            bagItem.setActionView(R.layout.layout_cart_badge);
            TextView textCartItemCount = (TextView) bagItem.getActionView().findViewById(R.id.cart_badge);
            if (textCartItemCount != null) {
                if (cartCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(cartCount, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
            FrameLayout fr_viewBag = (FrameLayout) bagItem.getActionView();

            fr_viewBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleActionMenuBar(false, false);
                    replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
                }
            });
        }


    }

    private void setCartBadges() {
        Menu menu = activityDashboardBinding.appBarMain.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.navigation_cart);
        bottomBarBadgeDrawable = activityDashboardBinding.appBarMain.bottomNavigationView.getOrCreateBadge(menuItem.getItemId());
        bottomBarBadgeDrawable.setBackgroundColor(getResources().getColor(R.color.ace_theme_color));
        bottomBarBadgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));
        bottomBarBadgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        setCartBadgeNumber(0);
        if (isUserLoggedin)
            callMyCartApi();
    }

    public void setCartBadgeNumber(int count) {
        if (count == 0) {
            bottomBarBadgeDrawable.clearNumber();
            bottomBarBadgeDrawable.setVisible(false);
        } else {
            cartCount = count;
            bottomBarBadgeDrawable.setNumber(count);
            bottomBarBadgeDrawable.setVisible(true);
        }
        setAppBarBadge(cartCount);
    }

    public int getCartCount() {
        return cartCount;
    }

    public void showToolBar() {
        activityDashboardBinding.appBarMain.toolbar.setVisibility(View.VISIBLE);
    }

    public void callMyCartApi() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessiontoken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        activityDashboardBinding.appBarMain.progressBar.setVisibility(View.VISIBLE);
        dashboardViewModel.getCartItems(userId, sessiontoken, selectedLanguageId).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:

                    if (cartResponse.data.getStatusCode() == 200) {
                        try {
                            int cartCount = cartResponse.data.getCartData().getItemCount();
                            setCartBadgeNumber(cartCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                        GeneralDialog generalDialog = new GeneralDialog("Error", cartResponse.data.getMessage());
//                        generalDialog.show(getSupportFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
//                    GeneralDialog generalDialog = new GeneralDialog("Error", cartResponse.message);
//                    generalDialog.show(getSupportFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            activityDashboardBinding.appBarMain.progressBar.setVisibility(View.GONE);
        });
    }

    public void setCurrentlyShowingProductId(String currentlyShowingProductId) {
        this.currentlyShowingProductId = currentlyShowingProductId;
    }

    public void setCurrentlyShowingProductName(String currentlyShowingProductName) {
        this.currentlyShowingProductName = currentlyShowingProductName;
    }


    public void showNoInternetDialog() {
        noInternetDialog = new NoInternetDialog();
        noInternetDialog.setClickListener(new NoInternetDialog.NoInternetDialogInterface() {
            @Override
            public void onCancel() {


            }

            @Override
            public void onRetry() {
                callHomeApi(selectedLanguageId);
            }
        });
        noInternetDialog.showDialog(this);
    }

    private void callHomeApi(String languageId) {
        activityDashboardBinding.appBarMain.progressBar.setVisibility(View.VISIBLE);
        dashboardViewModel.getHomeData(languageId).observe(this, homeResponseResource -> {
            switch (homeResponseResource.status) {
                case SUCCESS:
                    activityDashboardBinding.appBarMain.swipeToRefresh.setRefreshing(false);
                    if (homeResponseResource.data.getStatusCode() == 200) {
                        try {
                            if (homeResponseResource.data != null) {
                                HomeResponse homeResponse = homeResponseResource.data;
                                homeDataHashmap = homeResponse.getHomeUiData();
                                tabList = new ArrayList<>();
                                for (String key : homeDataHashmap.keySet()) {
                                    tabList.add(key);
                                }
                                createDynamicHomeTabs(tabList, homeDataHashmap);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), homeResponseResource.data.getMessage());
                        generalDialog.show(getSupportFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), homeResponseResource.message);
                    generalDialog.show(getSupportFragmentManager(), "GENERAL DIALOG");
                    break;
            }
            activityDashboardBinding.appBarMain.progressBar.setVisibility(View.GONE);
        });
    }

    public void clearBackStack() {
        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < (fragmentCount - 1); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onUpdateNeeded(String updateUrl) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        boolean hasUpdateAlreadyShown = preferenceHandler.getData(PreferenceHandler.HAS_UPDATE_DIALOG_SHOWN, false);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to experience new features")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferenceHandler.saveData(PreferenceHandler.HAS_UPDATE_DIALOG_SHOWN, true);
                                redirectStore(updateUrl);
                                InAppEvents.logAppUpdateEvent();
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferenceHandler.saveData(PreferenceHandler.HAS_UPDATE_DIALOG_SHOWN, true);
                                finish();
                            }
                        }).create();
        if (!hasUpdateAlreadyShown)
            dialog.show();
    }

    private void redirectStore(String updateUrl) {
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        final String appPackageName = getPackageName();
        try {
            Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            appStoreIntent.setPackage("com.android.vending");
            startActivity(appStoreIntent);
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
        }
    }


    private void parsingBranchDeepLink() {
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
            String productId = preferenceHandler.getData(PreferenceHandler.LOGIN_PRODUCT_ID, "");
            if (!productId.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("product_object_id", productId);
                replaceFragment(R.id.fragment_replacer, new ProductDetailsFragment(), bundle, true, false);
            }
            String subcateogryId = preferenceHandler.getData(PreferenceHandler.LOGIN_BRANCH_CATEGORY_ID, "");
            if (!subcateogryId.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("category_id", subcateogryId);
                replaceFragment(R.id.fragment_replacer, new ProductListingFragment(), bundle, true, false);
            }

//            String showReg = dataBundle.containsKey("showReg") ? dataBundle.getString("showReg") : null;
            boolean showRegPAge = preferenceHandler.getData(PreferenceHandler.SHOW_REG_PAGE, false);
            if (showRegPAge) {
                if (!isUserLoggedin) {
                    Intent siginInIntent = new Intent(DashboardActivity.this, SigninActivity.class);
                    startActivity(siginInIntent);
                } else {
                    handleActionMenuBar(true, false);
                    replaceFragment(R.id.fragment_replacer, new UserProfileFragment(), null, true, false);

                }

            }
            preferenceHandler.saveData(PreferenceHandler.SHOW_REG_PAGE, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}