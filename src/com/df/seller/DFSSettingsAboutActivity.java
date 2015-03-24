package com.df.seller;

import android.view.View;
import android.widget.TextView;

import com.df.src.R;

public class DFSSettingsAboutActivity extends DFSellerMasterActivity{

    private TextView txtAboutHeader;
    private TextView txtAbout;

    private String aboutHeader = "Kravely is the next generation takeaway app. Your Food, Your Price.";

    private String aboutContent = "Kravely is a next generation takeaway app. We connect you with local restaurants, takeaways and chefs. You can browse through the dishes or simply tap record and your voice order gets sent to the relevant restaurants in your area. Then all you have to do is tell us how much you want to pay, that's right, YOU tell us what you want to pay and not the other way around!\n" +
            "\n" +
            "Here's how it works!\n" +
            "\n" +
            "Step 1. Using our app, decide what type of food you want. We offer a wide variety of food from Indian to Chinese, and a whole range of world foods.\n" +
            "\n" +
            "Step 2. Tap record and our app records your order.\n" +
            "\n" +
            "Step 3. Make an offer! With this feature YOU decide what you are willing to pay.\n" +
            "\n" +
            "Step 4. Your order along with your offer gets sent to various restaurants in your area, just wait for one of them to accept your offer and your food will soon be on its way. It's as easy as that!\n" +
            "\n" +
            "We feel this allows you, the customer, to get the food you want for a price that suits you. And our app allows you to order your food from wherever you are, whether you're still in work or on the way home.";
    @Override
    public int setLayoutId() {
        return R.layout.dfc_settings_about;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents() {
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAboutHeader = (TextView) findViewById(R.id.txtAboutHeader);

        txtAboutHeader.setText(aboutHeader);
        txtAbout.setText(aboutContent);
    }

    @Override
    public void prepareViews() {
    }

    @Override
    public void setActionListeners() {

    }

}
