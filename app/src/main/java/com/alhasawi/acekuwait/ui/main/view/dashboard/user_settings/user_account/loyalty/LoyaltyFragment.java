package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.loyalty;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.User;
import com.alhasawi.acekuwait.databinding.FragmentLoyaltyBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class LoyaltyFragment extends BaseFragment {
    FragmentLoyaltyBinding fragmentLoyaltyBinding;
    DashboardActivity dashboardActivity;
    String cardNumber = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_loyalty;
    }

    @Override
    protected void setup() {

        fragmentLoyaltyBinding = (FragmentLoyaltyBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            String customerObjString = preferenceHandler.getData(PreferenceHandler.LOGIN_CUSTOMER, "");
            Gson gson = new Gson();
            User user = gson.fromJson(customerObjString, User.class);
            if (user != null) {
                fragmentLoyaltyBinding.tvName.setText(user.getCustomerFirstName() + " " + user.getCustomerLastName());
                cardNumber = user.getLoyalty().getCardNumber();
                fragmentLoyaltyBinding.tvCardNumber.setText(cardNumber);
                fragmentLoyaltyBinding.tvPoints.setText(user.getLoyalty().getPoints() + " PTS");
                String loyalityJson = gson.toJson(user.getLoyalty());
                InAppEvents.logUserLoyalityProperty(loyalityJson);
                InAppEvents.logUserLoyalityTag(user.getLoyalty().getPoints());
                if (!cardNumber.equals(""))
                    generateBarcode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentLoyaltyBinding.cardFlipview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentLoyaltyBinding.cardFlipview.flipTheView(true);

            }
        });
        fragmentLoyaltyBinding.btnPurchaseMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.clearTopFragments();
            }
        });
        fragmentLoyaltyBinding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });


    }

    private void generateBarcode() {
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(cardNumber, BarcodeFormat.CODE_128, 400, 100, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.WHITE : Color.BLACK);
                }
            }
            fragmentLoyaltyBinding.layoutBackcard.imageViewBarcode.setImageBitmap(bitmap);
            fragmentLoyaltyBinding.layoutBackcard.tvBarcodeCardNumber.setText(cardNumber);
        } catch (Exception e) {

        }
    }

}
