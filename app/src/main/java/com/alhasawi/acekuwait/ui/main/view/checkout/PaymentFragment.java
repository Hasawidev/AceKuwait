package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.PaymentMode;
import com.alhasawi.acekuwait.databinding.FragmentPaymentNewBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.listeners.PaymentSuccessListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.PaymentViewModel;
import com.alhasawi.acekuwait.utils.LanguageHelper;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.FailedPaymentDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class PaymentFragment extends BaseFragment implements PaymentSuccessListener {
    FragmentPaymentNewBinding fragmentPaymentNewBinding;
    PaymentViewModel paymentViewModel;
    PaymentMode selectedPaymentMode;
    double totalPrice = 0;
    DashboardActivity dashboardActivity;
    PaymentSuccessListener paymentSuccessListener;
    CheckoutFragment checkoutFragmentInstance;

    public PaymentFragment(CheckoutFragment checkoutFragmentInstance) {
        this.checkoutFragmentInstance = checkoutFragmentInstance;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_new;
    }

    @Override
    protected void setup() {
        fragmentPaymentNewBinding = (FragmentPaymentNewBinding) viewDataBinding;
        paymentViewModel = new ViewModelProvider(getActivity()).get(PaymentViewModel.class);
        paymentSuccessListener = this;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        try {
            Bundle bundle = getArguments();
            String paymentModeString = bundle.getString("payment_mode");
            if (bundle.containsKey("total_price"))
                totalPrice = bundle.getDouble("total_price");
            Gson gson = new Gson();
            selectedPaymentMode = gson.fromJson(paymentModeString, PaymentMode.class);
            paymentViewModel.setSelectedPaymentMode(selectedPaymentMode);
            if (selectedPaymentMode.isPostPay()) {
                Bundle paymentbundle = new Bundle();
                String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
                paymentbundle.putString("payment", paymentJson);
                paymentbundle.putString("payment_url", "");
                paymentbundle.putString("paymentId", null);
                dashboardActivity.clearBackStack();
                dashboardActivity.handleActionMenuBar(false, true);
                PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                String selectedLanguageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
                if (selectedLanguageId.equals(""))
                    selectedLanguageId = "en";
                LanguageHelper.changeLocale(getResources(), selectedLanguageId);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderConfirmationFragment(checkoutFragmentInstance), paymentbundle, false, false);
            } else
                callPaymentApi(selectedPaymentMode);
            try {
                InAppEvents.logPaymentMethodEvent(selectedPaymentMode);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callPaymentApi(PaymentMode selectedPaymentMode) {

        FailedPaymentDialog failedPaymentDialog = new FailedPaymentDialog(dashboardActivity);
        fragmentPaymentNewBinding.webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(selectedPaymentMode.getFailedUrl())) {
                    view.destroy();
                    failedPaymentDialog.show(getParentFragmentManager(), "PAYMENT_FAILED");
                    try {
                        InAppEvents.logOrderFailedEvent(
                                InAppEvents.cartItemsList,
                                InAppEvents.cartTotalAmount

                        );
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    try {
                        InAppEvents.logAddPaymentInfoFirebaseEvent(
                                selectedPaymentMode,
                                false
                        );
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
//                else if (url.contains(selectedPaymentMode.getSuccessUrl())) {
//                    logAddPaymentInfoEvent(selectedPaymentMode, true);
//                    logAddPaymentInfoFirebaseEvent(selectedPaymentMode, true);
//
//                    Bundle paymentBundle = new Bundle();
//                    Gson gson = new Gson();
//                    String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
//                    paymentBundle.putString("payment", paymentJson);
//                    paymentBundle.putString("payment_url", url);
//                    paymentBundle.putString("paymentId", "");
//                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderFragment(), paymentBundle, true, false);
//
//                }
                else {
                    view.loadUrl(url);
                }

                return true;
            }

            //Show loader on url load
            @Override
            public void onLoadResource(WebView view, String url) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                fragmentPaymentNewBinding.progressBar.setVisibility(View.GONE);
//                try {
//
//                    if (url.contains(selectedPaymentMode.getFailedUrl())) {
//                        failedPaymentDialog.show(getParentFragmentManager(), "PAYMENT_FAILED");
//                        logAddPaymentInfoEvent(selectedPaymentMode, false);
//                        logAddPaymentInfoFirebaseEvent(selectedPaymentMode, false);
//                    } else
                if (url.contains(selectedPaymentMode.getSuccessUrl())) {
                    view.destroy();
//                        fragmentPaymentNewBinding.webviewPayment.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

//                        fragmentPaymentNewBinding.webviewPayment.loadUrl("javascript:HtmlViewer.showHTML" +
//                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                    Bundle paymentBundle = new Bundle();
                    Gson gson = new Gson();
                    String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
                    paymentBundle.putString("payment", paymentJson);
                    paymentBundle.putString("payment_url", url);
                    paymentBundle.putString("paymentId", "");


                    dashboardActivity.clearBackStack();
                    dashboardActivity.handleActionMenuBar(false, true);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderConfirmationFragment(checkoutFragmentInstance), paymentBundle, false, false);

                }
//
//                } catch (Exception exception) {
//                    failedPaymentDialog.show(getParentFragmentManager(), "PAYMENT_FAILED");
//                    exception.printStackTrace();
//                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                fragmentPaymentNewBinding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        //Load url in webView
        fragmentPaymentNewBinding.webviewPayment.loadUrl(selectedPaymentMode.getPaymentUrl());
        fragmentPaymentNewBinding.webviewPayment.getSettings().setJavaScriptEnabled(true);
//        fragmentPaymentNewBinding.webviewPayment.getSettings().setSupportZoom(true);
//        fragmentPaymentNewBinding.webviewPayment.getSettings().setBuiltInZoomControls(true);
//        fragmentPaymentNewBinding.webviewPayment.addJavascriptInterface(new MyJavaScriptInterface(dashboardActivity), "HtmlViewer");

    }


    @Override
    public void onPaymentSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray invoiceTransactions = data.getJSONArray("InvoiceTransactions");
            JSONObject invoiceTransactionObj = invoiceTransactions.getJSONObject(0);
            String paymentId = invoiceTransactionObj.getString("PaymentId");
            Gson gson = new Gson();
            String paymentJson = gson.toJson(selectedPaymentMode, PaymentMode.class);
            Bundle paymentBundle = new Bundle();
            paymentBundle.putString("payment", paymentJson);
            paymentBundle.putString("payment_id", paymentId);
            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            String selectedLanguageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
            if (selectedLanguageId.equals(""))
                selectedLanguageId = "en";
            LanguageHelper.changeLocale(getResources(), selectedLanguageId);
            dashboardActivity.clearBackStack();
            dashboardActivity.handleActionMenuBar(false, true);
            dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderConfirmationFragment(checkoutFragmentInstance), paymentBundle, false, false);
            try {
                InAppEvents.logAddPaymentInfoFirebaseEvent(
                        selectedPaymentMode,
                        true
                );
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            try {
                InAppEvents.logPaymentInfo(
                        totalPrice,
                        selectedPaymentMode.getName()
                );
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyJavaScriptInterface {

        private DashboardActivity dashboardActivity;

        MyJavaScriptInterface(DashboardActivity dashboardActivity) {
            this.dashboardActivity = dashboardActivity;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            try {
                int startingIndex = html.indexOf("{");
                int closingIndex = html.indexOf("statusCode");
                String responseString = html.substring(startingIndex, closingIndex + 16);
                paymentSuccessListener.onPaymentSuccess(responseString);
                try {
                    InAppEvents.logAddPaymentInfoFirebaseEvent(
                            selectedPaymentMode,
                            true
                    );
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
