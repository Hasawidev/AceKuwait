package com.alhasawi.acekuwait.ui.main.view.signin.select_language;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.AppLanguage;
import com.alhasawi.acekuwait.databinding.ActivitySelectLanguageBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;
import com.alhasawi.acekuwait.ui.main.adapters.LanguageAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.LanguageHelper;
import com.alhasawi.acekuwait.utils.PreferenceHandler;

import java.util.ArrayList;

public class SelectLanguageActivity extends BaseActivity implements RecyclerviewSingleChoiceClickListener {
    ActivitySelectLanguageBinding activitySelectLanguageBinding;
    LanguageAdapter languageAdapter;
    AppLanguage selectedLanguage;
    private ArrayList<AppLanguage> languageArrayList = new ArrayList<>();

    @Override
    protected void setup() {
        activitySelectLanguageBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_language);
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        preferenceHandler.saveData(PreferenceHandler.HAS_LANGUAGE_PAGE_SHOWN, true);
        String langugeId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        if (langugeId.equals("en")) {
            languageArrayList.add(new AppLanguage("en", "English", false));
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


        languageAdapter = new LanguageAdapter(this, languageArrayList);
        languageAdapter.setOnItemClickListener(this);
        activitySelectLanguageBinding.recyclerViewLanguage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitySelectLanguageBinding.recyclerViewLanguage.setAdapter(languageAdapter);

        activitySelectLanguageBinding.btnProceedLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLanguage != null)
                    LanguageHelper.changeLocale(getResources(), selectedLanguage.getId());
                preferenceHandler.saveData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, selectedLanguage.getId());

                Intent homeIntent = new Intent(SelectLanguageActivity.this, DashboardActivity.class);
                homeIntent.putExtras(getIntent());
                startActivity(homeIntent);
                finish();

            }
        });

    }

    @Override
    public void onItemClickListener(int position, View view) {
        selectedLanguage = languageArrayList.get(position);
        languageAdapter.selectedItem();
    }
}
