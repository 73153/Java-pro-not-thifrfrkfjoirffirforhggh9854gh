package com.df.customer;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class DFCSettingsNotificationActivity extends DFCustomerMasterActivity {

	private IjoomerCheckBox chkMealRequest;
	private IjoomerCheckBox chkDishRequest;
	private IjoomerButton imgSave;

	private DFCDataprovider dataprovider;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_settings_notification;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgSave = (IjoomerButton) findViewById(R.id.imgSave);
		chkMealRequest = (IjoomerCheckBox) findViewById(R.id.chkMealRequest);
		chkDishRequest = (IjoomerCheckBox) findViewById(R.id.chkDishRequest);
		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {
		chkMealRequest.setChecked(getSmartApplication().readSharedPreferences().getBoolean(SP_ACCEPT_MEAL_REQ, false));
		chkDishRequest.setChecked(getSmartApplication().readSharedPreferences().getBoolean(SP_ACCEPT_DISH_OFFER, false));
	}

	@Override
	public void setActionListeners() {
		imgSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showProgressDialog("Updating Notification Settings...", DFCSettingsNotificationActivity.this);
				dataprovider.saveNotificationSettings(chkMealRequest.isChecked(), chkDishRequest.isChecked(), new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						hideProgressDialog();
						if (responseCode == 200) {
							IjoomerUtilities.getDFInfoDialog(DFCSettingsNotificationActivity.this, "Settings Updated Successfully.", getString(R.string.ok),
									new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											getSmartApplication().writeSharedPreferences(SP_ACCEPT_MEAL_REQ, chkMealRequest.isChecked());
											getSmartApplication().writeSharedPreferences(SP_ACCEPT_DISH_OFFER, chkDishRequest.isChecked());
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
