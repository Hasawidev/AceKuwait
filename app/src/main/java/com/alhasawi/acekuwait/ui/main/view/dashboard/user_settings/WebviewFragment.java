package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentWebviewBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;

public class WebviewFragment extends BaseFragment {
    FragmentWebviewBinding fragmentWebviewBinding;
    String url = "";
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void setup() {
        fragmentWebviewBinding = (FragmentWebviewBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            url = bundle.getString("url");
            fragmentWebviewBinding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                //Show loader on url load
                @Override
                public void onLoadResource(WebView view, String url) {

                }

                @Override
                public void onPageFinished(WebView view, String url) {

                }
            });

            fragmentWebviewBinding.webView.loadUrl(url);
            fragmentWebviewBinding.webView.getSettings().setJavaScriptEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
