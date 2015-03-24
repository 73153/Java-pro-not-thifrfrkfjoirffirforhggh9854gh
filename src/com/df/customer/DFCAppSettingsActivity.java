package com.df.customer;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;

public class DFCAppSettingsActivity extends DFCustomerMasterActivity {

	private IjoomerButton imgNotifications;
	private IjoomerButton imgShare;
	private IjoomerButton imgAbout;
	private IjoomerButton imgTandC;
	private IjoomerButton imgLogout;
    private IjoomerButton bntGetInTouch;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_app_settings;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgNotifications = (IjoomerButton) findViewById(R.id.imgNotifications);
		imgShare = (IjoomerButton) findViewById(R.id.imgShare);
		imgAbout = (IjoomerButton) findViewById(R.id.imgAbout);
		imgTandC = (IjoomerButton) findViewById(R.id.imgTandC);
		imgLogout = (IjoomerButton) findViewById(R.id.imgLogout);

        bntGetInTouch = (IjoomerButton) findViewById(R.id.bntGetInTouch);
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {

        bntGetInTouch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@kravely.email"});

                try {
                    startActivity(Intent.createChooser(i, "Get In Touch..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    ting(getString(R.string.share_email_no_client));
                }
            }
        });

		imgNotifications.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				loadNew(DFCSettingsNotificationActivity.class, DFCAppSettingsActivity.this, false);
			}
		});
		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadNew(DFCSettingsShareActivity.class, DFCAppSettingsActivity.this, false);
			}
		});
		imgAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadNew(DFCSettingsAboutActivity.class, DFCAppSettingsActivity.this, false);
			}
		});
		imgTandC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				loadNew(DFCSettingsTandCActivity.class, DFCAppSettingsActivity.this, false);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kravely.com/index.php/t-c"));
                startActivity(browserIntent);
			}

		});
		imgLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				logout();
			}
		});
	}

}
