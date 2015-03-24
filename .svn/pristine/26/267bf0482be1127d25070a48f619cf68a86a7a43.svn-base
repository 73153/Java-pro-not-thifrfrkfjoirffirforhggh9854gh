package com.df.seller;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.IjoomerRegistrationStep2Activity;
import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;

public class DFSAppSettingsActivity extends DFSellerMasterActivity {

	private IjoomerButton imgNotifications;
	private IjoomerButton imgShare;
	private IjoomerButton imgAbout;
	private IjoomerButton imgTandC;
	private IjoomerButton imgLogout;
	private IjoomerButton imgSelectCatagory;
    private IjoomerButton bntGetInTouch;

    @Override
	public int setLayoutId() {
		return R.layout.dfs_app_settings;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgNotifications = (IjoomerButton) findViewById(R.id.imgNotifications);
		imgSelectCatagory = (IjoomerButton) findViewById(R.id.imgSelectCatagory);
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
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL,  new String[] {"hello@kravely.com"});

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

				loadNew(DFSSettingsNotificationActivity.class, DFSAppSettingsActivity.this, false);
			}
		});
		imgSelectCatagory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadNew(IjoomerRegistrationStep2Activity.class, DFSAppSettingsActivity.this, false);
			}
		});

		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadNew(DFSSettingsShareActivity.class, DFSAppSettingsActivity.this, false);
			}
		});
		imgAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadNew(DFSSettingsAboutActivity.class, DFSAppSettingsActivity.this, false);
			}
		});
		imgTandC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				loadNew(DFSSettingsTandCActivity.class, DFSAppSettingsActivity.this, false);
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
