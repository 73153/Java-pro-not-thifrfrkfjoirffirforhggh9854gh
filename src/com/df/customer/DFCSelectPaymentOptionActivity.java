package com.df.customer;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerFacebookSharingActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFCSelectPaymentOptionActivity extends DFCustomerMasterActivity {

	private IjoomerTextView txtRestaurantName;
	private IjoomerTextView txtDishName;
	private IjoomerTextView txtPrice;

	private IjoomerButton imgPayWithCard;
	private IjoomerButton imgPayWithCash;

	private HashMap<String, String> IN_DISH_DETAIL;

	private Bundle PUSH_BUNDLE;

	private DFCDataprovider dataprovider;

    private static final int FACEBOOK_SHARE=1010;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_select_payment_option;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		txtRestaurantName = (IjoomerTextView) findViewById(R.id.txtRestaurantName);
		txtDishName = (IjoomerTextView) findViewById(R.id.txtDishName);
		txtPrice = (IjoomerTextView) findViewById(R.id.txtPrice);

		imgPayWithCard = (IjoomerButton) findViewById(R.id.imgPayWithCard);
		imgPayWithCash = (IjoomerButton) findViewById(R.id.imgPayWithCash);

		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {
		getIntentData();
		if (PUSH_BUNDLE != null) {
			IN_DISH_DETAIL = dataprovider.getOfferDetail(PUSH_BUNDLE.getString("id")).get(0);
		}
		setOrderDetails();
	}

	@Override
	public void setActionListeners() {
		imgPayWithCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					loadNew(DFCAddTipActivity.class, DFCSelectPaymentOptionActivity.this, false, "IN_DISH_DETAIL", IN_DISH_DETAIL);
				} catch (Exception e) {
				}
			}
		});

		imgPayWithCash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showProgressDialog("Confirming Order...", DFCSelectPaymentOptionActivity.this);
				dataprovider.payOrder(IN_DISH_DETAIL.get("restID"),IN_DISH_DETAIL.get("offerID"), "cash", IN_DISH_DETAIL.get("dishID").equals("0") ? true : false, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, final ArrayList<HashMap<String, String>> data1, Object data2) {
						hideProgressDialog();
						if (responseCode == 200) {
                            IN_DISH_DETAIL = data1.get(0);
                            try{
                                if(IN_DISH_DETAIL.get("dishID").equals("0")){
                                    getSmartApplication().writeSharedPreferences(SP_DF_REQ_TIMESTAMP,new Long(0));
                                    getSmartApplication().writeSharedPreferences(SP_DF_REQ_WAS_CONFIRMED,true);
                                    getSmartApplication().writeSharedPreferences(SP_DFC_LISTEN_COUNT,0);

                                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(DFPushNotificationLuncherActivity.ACCEPTREQUEST);
                                }else{
                                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancel(DFPushNotificationLuncherActivity.ACCEPTOFFER);
                                }

                            }catch (Exception e){

                            }

							IjoomerUtilities.getDFInfoDialog(DFCSelectPaymentOptionActivity.this, "Transaction completed successfully", getString(R.string.ok), new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {


                                    getConfirmDialog("Facebook Share","Would you like to share on Facebook?","Yes","No",false,new AlertMagnatic() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(DFCSelectPaymentOptionActivity.this,IjoomerFacebookSharingActivity.class);

                                            if(IN_DISH_DETAIL.get("dishID").equals("0")){
                                                String message = getSmartApplication().readSharedPreferences().getString(SP_DF_USERNAME,"") + " just ordered a meal from "+IN_DISH_DETAIL.get("restName")+" using Kravely. http://www.kravely.com to learn more.";
                                                intent.putExtra("IN_MESSAGE",message);
                                                intent.putExtra("IN_NAME",getString(R.string.app_name));
                                                intent.putExtra("IN_CAPTION","");
                                                intent.putExtra("IN_DESCRIPTION","Dish Call App.");
                                                intent.putExtra("IN_PICTURE","http://www.kravely.com/kravely_logo.png");
                                                intent.putExtra("IN_LINK","http://www.kravely.com");
                                            }else{
                                                String message = getSmartApplication().readSharedPreferences().getString(SP_DF_USERNAME,"") + " just ordered "+IN_DISH_DETAIL.get("dishName")+"  from "+IN_DISH_DETAIL.get("restName")+" using Kravely. http://www.kravely.com to learn more.";
                                                intent.putExtra("IN_MESSAGE",message);
                                                intent.putExtra("IN_NAME",getString(R.string.app_name));
                                                intent.putExtra("IN_CAPTION","");
                                                intent.putExtra("IN_DESCRIPTION","Dish Call App.");
                                                intent.putExtra("IN_PICTURE",IN_DISH_DETAIL.get("image"));
                                                intent.putExtra("IN_LINK","http://www.kravely.com");
                                            }
                                            startActivityForResult(intent,FACEBOOK_SHARE);

                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            try {
                                                loadNew(DFCOrderConfirmationAndDetailActivity.class, DFCSelectPaymentOptionActivity.this, true, "IN_DISH_DETAIL", IN_DISH_DETAIL);
                                            } catch (Exception e) {
                                            }
                                        }
                                    });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FACEBOOK_SHARE) {
                try {
                    loadNew(DFCOrderConfirmationAndDetailActivity.class, DFCSelectPaymentOptionActivity.this, true, "IN_DISH_DETAIL", IN_DISH_DETAIL);
                } catch (Exception e) {
                }
            }
        }
    }

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			PUSH_BUNDLE = getIntent().getBundleExtra("PUSH_BUNDLE");
		} catch (Exception e) {
		}
		try {
			IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
		} catch (Exception e) {
		}
	}

	private void setOrderDetails() {
		txtRestaurantName.setText(IN_DISH_DETAIL.get("restName"));
		if (IN_DISH_DETAIL.get("dishID").equals("0")) {
			txtDishName.setText(IN_DISH_DETAIL.get("catName"));
		} else {
			txtDishName.setText(IN_DISH_DETAIL.get("dishName"));
		}
		txtPrice.setText(getString(R.string.df_curency_sign) + (IN_DISH_DETAIL.get("price")));
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}
}
