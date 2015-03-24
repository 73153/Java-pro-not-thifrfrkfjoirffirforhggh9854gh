package com.df.customer;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerFacebookSharingActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.util.HashMap;

public class DFCPaymentGatewayActivity extends DFCustomerMasterActivity {

	private WebView web;
	private ProgressBar pbrLoading;
	private ImageView imgClose;
	private String IN_PAYMENT_URL;
	private HashMap<String, String> IN_DISH_DETAIL;

	private DFCDataprovider dataprovider;

    private static final int FACEBOOK_SHARE=1010;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_payment_gateway;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		dataprovider = new DFCDataprovider(this);
		getIntentData();
		web = (WebView) findViewById(R.id.web);
		pbrLoading = (ProgressBar) findViewById(R.id.pbrLoading);
		imgClose = (ImageView) findViewById(R.id.imgClose);

	}

	@Override
	public void prepareViews() {
		web.setWebViewClient(new DFCWebClient());
		WebSettings settings = web.getSettings();
		settings.setAllowFileAccess(true);
		settings.setJavaScriptEnabled(true);
		settings.setPluginState(WebSettings.PluginState.ON);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		try {
			settings.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		} catch (Throwable e) {
		}
		settings.setBuiltInZoomControls(true);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			settings.setDisplayZoomControls(false);
		}
		showProgressDialog();

		web.loadUrl(IN_PAYMENT_URL);
	}

	@Override
	public void setActionListeners() {
		imgClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress > 0) {
					showProgressDialog();
				}
				if (newProgress >= 100) {
					hideProgressDialog();
				}
			}

		});
	}

	/**
	 * Class Methods
	 */

	private class DFCWebClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, final String url) {

			System.out.println("url : " + url);

			if (url.contains("success=true")) {

				IjoomerUtilities.getDFInfoDialog(DFCPaymentGatewayActivity.this, "Transaction completed successfully.", getString(R.string.ok),
						new CustomAlertNeutral() {

							@Override
							public void NeutralMethod() {
								try {

                                    try{
                                        if(IN_DISH_DETAIL.get("dishID").equals("0")){
                                            getSmartApplication().writeSharedPreferences(SP_DFC_LISTEN_COUNT,0);
                                            getSmartApplication().writeSharedPreferences(SP_DF_REQ_TIMESTAMP,new Long(0));
                                            getSmartApplication().writeSharedPreferences(SP_DF_REQ_WAS_CONFIRMED,true);
                                            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.cancel(DFPushNotificationLuncherActivity.ACCEPTREQUEST);
                                        }else{
                                            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.cancel(DFPushNotificationLuncherActivity.ACCEPTOFFER);
                                        }

                                    }catch (Exception e){

                                    }

									dataprovider.removeOrder(IN_DISH_DETAIL.get("orderID"), IN_DISH_DETAIL.get("dishID").equals("0") ? true : false);
									Uri uri = Uri.parse(url);
									String reference = uri.getQueryParameter("reference");
                                    String service = uri.getQueryParameter("service");
									IN_DISH_DETAIL.put("reference", reference);



                                    getConfirmDialog("Facebook Share","Would you like to share on Facebook?","Yes","No",false,new AlertMagnatic() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(DFCPaymentGatewayActivity.this,IjoomerFacebookSharingActivity.class);

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
                                                loadNew(DFCOrderConfirmationAndDetailActivity.class, DFCPaymentGatewayActivity.this, true, "IN_DISH_DETAIL", IN_DISH_DETAIL);
                                            } catch (Exception e) {
                                            }
                                        }
                                    });





								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});
			} else if (url.contains("success=false")) {
				IjoomerUtilities.getDFInfoDialog(DFCPaymentGatewayActivity.this, "Payment Error.", getString(R.string.ok), new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						finish();
					}
				});
			}
			return super.shouldOverrideUrlLoading(view, url);

		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FACEBOOK_SHARE) {
                try {
                    loadNew(DFCOrderConfirmationAndDetailActivity.class, DFCPaymentGatewayActivity.this, true, "IN_DISH_DETAIL", IN_DISH_DETAIL);
                } catch (Exception e) {
                }
            }
        }
    }


	private void getIntentData() {
		IN_PAYMENT_URL = getIntent().getStringExtra("IN_PAYMENT_URL");
		IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
	}

	public void showProgressDialog() {

		try {
			if (pbrLoading.getVisibility() == View.GONE) {
				pbrLoading.setVisibility(View.VISIBLE);
			}
		} catch (Throwable e) {
		}
	}

	public void hideProgressDialog() {
		try {
			pbrLoading.setVisibility(View.GONE);
		} catch (Throwable e) {
		}

	}

	@Override
	public void onBackPressed() {
		if (web.canGoBack()) {
			web.goBack();
		} else {
			super.onBackPressed();
		}
	}

	// try {
	// loadNew(DFCOrderConfirmationActivity.class, DFCAddTipActivity.this,
	// false, "IN_DISH_DETAIL", IN_DISH_DETAIL);
	// } catch (Exception e) {
	// }
}
