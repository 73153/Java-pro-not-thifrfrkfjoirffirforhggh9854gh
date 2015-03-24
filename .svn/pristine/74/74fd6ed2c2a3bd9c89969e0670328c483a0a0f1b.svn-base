package com.df.customer;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.R;
import com.ijoomer.common.classes.IjoomerFacebookSharingActivity;
import com.ijoomer.customviews.IjoomerButton;

public class DFCSettingsShareActivity extends DFCustomerMasterActivity {

	private IjoomerButton imgEmail;
	private IjoomerButton imgFacebook;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_settings_share;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgEmail = (IjoomerButton) findViewById(R.id.imgEmail);
		imgFacebook = (IjoomerButton) findViewById(R.id.imgFacebook);
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {
		imgEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
                //String message = getSmartApplication().readSharedPreferences().getString(SP_DF_USERNAME,"") +" just bought a dish using " + getSmartApplication().readSharedPreferences().getString(SP_DF_SHARELINK,"");

                String message = "I found this great App called Kravely. It allows you to request food to be delivered. You can download the App for Android and iPhone http://www.kravely.com";

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_SUBJECT, "Kravely");
                i.putExtra(
                        Intent.EXTRA_TEXT,
                        Html.fromHtml(message));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    ting(getString(R.string.share_email_no_client));
                }
			}
		});

		imgFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

                    try {
                        loadNew(IjoomerFacebookSharingActivity.class, DFCSettingsShareActivity.this,false);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
            }

		});
	}

}
