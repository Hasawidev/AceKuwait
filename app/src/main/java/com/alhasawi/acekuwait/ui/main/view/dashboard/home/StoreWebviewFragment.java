package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentAceWebviewBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;

public class StoreWebviewFragment extends BaseFragment {

    FragmentAceWebviewBinding aceWebviewBinding;
    DashboardActivity dashboardActivity;
    String storeUrl = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_ace_webview;
    }

    @Override
    protected void setup() {

        aceWebviewBinding = (FragmentAceWebviewBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            Bundle bundle = getArguments();
            String storename = bundle.getString("store_name");
            storeUrl = bundle.getString("store_url");

            aceWebviewBinding.tvStoreName.setText(storename);
            setWebview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWebview() {

        aceWebviewBinding.webView.setWebViewClient(new WebViewClient() {
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        //Load url in webView
        aceWebviewBinding.webView.loadUrl(storeUrl);
        aceWebviewBinding.webView.getSettings().setJavaScriptEnabled(true);

    }
}
