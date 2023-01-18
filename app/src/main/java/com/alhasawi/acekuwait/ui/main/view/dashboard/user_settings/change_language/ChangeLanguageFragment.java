package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.change_language;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.AppLanguage;
import com.alhasawi.acekuwait.databinding.FragmentChangeLanguageBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.LanguageAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.PreferenceHandler;

import java.util.ArrayList;

public class ChangeLanguageFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentChangeLanguageBinding fragmentChangeLanguageBinding;
    LanguageAdapter languageAdapter;
    AppLanguage selectedLanguage;
    DashboardActivity dashboardActivity;
    private ArrayList<AppLanguage> languageArrayList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_change_language;
    }

    @Override
    protected void setup() {
        fragmentChangeLanguageBinding = (FragmentChangeLanguageBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        languageArrayList.clear();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String langugeId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        if (langugeId.equals("en")) {
            languageArrayList.add(new AppLanguage("en", "English", true));
            languageArrayList.add(new AppLanguage("ar", "Arabic", false));
            selectedLanguage = languageArrayList.get(0);
        } else if (langugeId.equals("ar")) {
            languageArrayList.add(new AppLanguage("en", "English", false));
            languageArrayList.add(new AppLanguage("ar", "Arabic", true));
            selectedLanguage = languageArrayList.get(1);
        } else {
            languageArrayList.add(new AppLanguage("en", "English", true));
            languageArrayList.add(new AppLanguage("ar", "Arabic", false));
            selectedLanguage = languageArrayList.get(0);
        }


        languageAdapter = new LanguageAdapter(dashboardActivity, languageArrayList);
        languageAdapter.setOnItemClickListener(this);
        fragmentChangeLanguageBinding.recyclerViewLanguage.setLayoutManager(new LinearLayoutManager(dashboardActivity, LinearLayoutManager.VERTICAL, false));
        fragmentChangeLanguageBinding.recyclerViewLanguage.setAdapter(languageAdapter);

        fragmentChangeLanguageBinding.btnProceedLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (selectedLanguage != null)
//                    LanguageHelper.changeLocale(dashboardActivity.getResources(), selectedLanguage.getId());
                preferenceHandler.saveData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, selectedLanguage.getId());
                dashboardActivity.onLocaleChanged();
            }
        });

        fragmentChangeLanguageBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    @Override
    public void onItemClickListener(int position, View view) {
        selectedLanguage = languageArrayList.get(position);
        languageAdapter.selectedItem();
    }


}
