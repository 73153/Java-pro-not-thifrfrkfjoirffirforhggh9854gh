package com.df.src;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.df.customer.DFCPlaceOrderActivity;
import com.df.customer.DFCResponsesMapActivity;
import com.df.customer.DFCSelectPaymentOptionActivity;
import com.df.seller.DFSOfferListActivity;
import com.df.seller.DFSOrderListActivity;
import com.df.seller.DFSRequestListActivity;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.custom.interfaces.IjoomerKeys;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;

public class DFPushNotificationLuncherActivity extends IjoomerSuperMaster implements IjoomerKeys {

	private IjoomerTextView txtMessage;
	private IjoomerButton btnOk;
	private Bundle PUSH_BUNDLE;

	private int type;
	private String message;

	// customer

	public static final int ACCEPTOFFER = 101;
    public static final int REJECTOFFER = 102;
    public static final int ACCEPTREQUEST = 103;
    public static final int REJECTREQUEST = 104;

	// seller
    public static final int CONFIRMOFFER = 105;
    public static final int CONFIRMREQUEST = 106;
    public static final int SENDREQUESTOFFER = 107;
    public static final int SENDVOICEOFFER = 108;

    public static final int TIMEREXTENDED = 109;
    public static final int OFFEREXPIRED = 110;

	@Override
	public String[] setTabItemNames() {
		return new String[0];
	}

	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return new int[0];
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return new int[0];
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return new int[0];
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {

	}

	@Override
	public int setLayoutId() {
		return R.layout.df_push_luncher;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public void initComponents() {
		txtMessage = (IjoomerTextView) findViewById(R.id.txtMessage);
		btnOk = (IjoomerButton) findViewById(R.id.btnOk);
		getIntentData();
	}

	@Override
	public void prepareViews() {
		txtMessage.setText(message);

		if (type == ACCEPTOFFER) {

		} else if (type == REJECTOFFER) {

		} else if (type == ACCEPTREQUEST) {

		} else if (type == REJECTREQUEST) {

		} else if (type == CONFIRMOFFER) {

		} else if (type == CONFIRMREQUEST) {

		} else if (type == SENDREQUESTOFFER) {

		} else if (type == SENDVOICEOFFER) {

		}

	}

	@Override
	public void setActionListeners() {
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type == ACCEPTOFFER) {
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFCSelectPaymentOptionActivity.class);
					gotoIntent.putExtra("PUSH_BUNDLE", PUSH_BUNDLE);
					startActivity(gotoIntent);
				} else if (type == REJECTOFFER) {

                    try{
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(DFPushNotificationLuncherActivity.REJECTOFFER);
                    }catch (Exception e){

                    }

					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFCPlaceOrderActivity.class);
					startActivity(gotoIntent);
				} else if (type == ACCEPTREQUEST) {
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFCResponsesMapActivity.class);
					gotoIntent.putExtra("PUSH_BUNDLE", PUSH_BUNDLE);
					startActivity(gotoIntent);

				} else if (type == REJECTREQUEST) {
					Intent intent = new Intent("clearStackActivity");
					intent.setType("text/plain");
					sendBroadcast(intent);
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFCPlaceOrderActivity.class);
					startActivity(gotoIntent);
				} else if (type == CONFIRMOFFER) {
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFSOrderListActivity.class);
					startActivity(gotoIntent);
				} else if (type == CONFIRMREQUEST) {
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFSOrderListActivity.class);
					startActivity(gotoIntent);
				} else if (type == SENDREQUESTOFFER) {
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFSOfferListActivity.class);
					startActivity(gotoIntent);
				} else if (type == SENDVOICEOFFER) {
					Intent gotoIntent = new Intent(DFPushNotificationLuncherActivity.this, DFSRequestListActivity.class);
					startActivity(gotoIntent);
				} else if (type == TIMEREXTENDED) {

				} else if (type == OFFEREXPIRED) {
//					Intent intent = new Intent("clearStackActivity");
//					intent.setType("text/plain");
//					sendBroadcast(intent);
//					loadNew(DFCPlaceOrderActivity.class, DFPushNotificationLuncherActivity.this, false);
				}
				finish();
			}
		});
	}

	private void getIntentData() {
		PUSH_BUNDLE = getIntent().getBundleExtra("PUSH_BUNDLE");
		type = Integer.parseInt(PUSH_BUNDLE.getString("type"));
		message = PUSH_BUNDLE.getString("message");

	}

}
