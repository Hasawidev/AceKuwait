package com.alhasawi.acekuwait.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.NavigationMenuItem;
import com.alhasawi.acekuwait.databinding.LayoutNavigationDrawerItemBinding;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.PreferenceHandler;

import java.util.List;

public abstract class NavigationDrawerAdapter extends ArrayAdapter<NavigationMenuItem> {

    Context context;
    List<NavigationMenuItem> menuItemList;

    public NavigationDrawerAdapter(@NonNull Context context, int resource, @NonNull List<NavigationMenuItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.menuItemList = objects;
    }

    public abstract void onNotificationStatusChanged(boolean isNotificationEnabled);

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutNavigationDrawerItemBinding navigationDrawerItemBinding = DataBindingUtil.
                inflate(inflater, R.layout.layout_navigation_drawer_item, null, false);
        navigationDrawerItemBinding.tvNavDrawerItemName.setText(menuItemList.get(position).getName());
        if (menuItemList.get(position).isEnabled())
            navigationDrawerItemBinding.tvNavDrawerItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_blue));
        else
            navigationDrawerItemBinding.tvNavDrawerItemName.setTextColor(context.getResources().getColor(R.color.txt_clr_grey));
        if (menuItemList.get(position).get_ID() == AppConstants.ID_MENU_NOTIFICATION) {
            navigationDrawerItemBinding.imageView3.setVisibility(View.GONE);
            navigationDrawerItemBinding.swOnOff.setVisibility(View.VISIBLE);
        }
        navigationDrawerItemBinding.imageView.setImageDrawable(menuItemList.get(position).getDrawable()
        );
        PreferenceHandler preferenceHandler = new PreferenceHandler(context, PreferenceHandler.TOKEN_LOGIN);
        boolean isNotificationEnabled = preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false);
        if (isNotificationEnabled)
            navigationDrawerItemBinding.swOnOff.setChecked(true);
        else
            navigationDrawerItemBinding.swOnOff.setChecked(false);
        navigationDrawerItemBinding.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onNotificationStatusChanged(isChecked);
            }
        });
        return navigationDrawerItemBinding.getRoot();

    }

}
