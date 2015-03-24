package com.df.seller;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class DFSSettingsNotificationActivity extends DFSellerMasterActivity {

	private IjoomerCheckBox chkMealOffer;
	private IjoomerCheckBox chkDishOffer;
	private IjoomerButton imgSave;
	private DFSDataprovider dataprovider;

	@Override
	public int setLayoutId() {
		return R.layout.dfs_settings_notification;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgSave = (IjoomerButton) findViewById(R.id.imgSave);
		chkMealOffer = (IjoomerCheckBox) findViewById(R.id.chkMealOffer);
		chkDishOffer = (IjoomerCheckBox) findViewById(R.id.chkDishOffer);
		dataprovider = new DFSDataprovider(this);
	}

	@Override
	public void prepareViews() {

		chkMealOffer.setChecked(getSmartApplication().readSharedPreferences().getBoolean(SP_NEW_MEAL_REQ, false));
		chkDishOffer.setChecked(getSmartApplication().readSharedPreferences().getBoolean(SP_NEW_DISH_OFFER, false));
	}

	@Override
	public void setActionListeners() {
		imgSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showProgressDialog("Updating Notification Settings...", DFSSettingsNotificationActivity.this);
				dataprovider.saveNotificationSettings(chkMealOffer.isChecked(), chkDishOffer.isChecked(), new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						hideProgressDialog();
						if (responseCode == 200) {
							IjoomerUtilities.getDFInfoDialog(DFSSettingsNotificationActivity.this, "Settings Updated Successfully.", getString(R.string.ok),
									new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											getSmartApplication().writeSharedPreferences(SP_NEW_MEAL_REQ, chkMealOffer.isChecked());
											getSmartApplication().writeSharedPreferences(SP_NEW_DISH_OFFER, chkDishOffer.isChecked());
											finish();
										}
									});
						} else {
							responseMessageHandler(responseCode, false);
						}
					}
				});
			}
		});
	}

	/**
	 * Class Methods
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

}
