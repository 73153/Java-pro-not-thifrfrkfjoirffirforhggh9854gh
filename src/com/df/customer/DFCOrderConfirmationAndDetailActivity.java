package com.df.customer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerAudioPlayer;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.CustomAlertNeutral;

import java.util.HashMap;

public class DFCOrderConfirmationAndDetailActivity extends DFCustomerMasterActivity {

	private HashMap<String, String> IN_DISH_DETAIL;
	private boolean IN_FROM_ORDER_LIST;

	private ImageView imgBanner;
	private IjoomerTextView txtRestaurantName;
	private IjoomerTextView txtRestaurantAddress1;
	private IjoomerTextView txtRestaurantAddress2;
	private IjoomerTextView txtDateTime;
	private IjoomerTextView txtDishPrice;
	private IjoomerTextView txtReference;
	private IjoomerTextView txtDishName;
	private IjoomerTextView txtCollectionOrDelivery;

    private LinearLayout lnrInfo;
    private IjoomerAudioPlayer audioPlayer = new IjoomerAudioPlayer();
	private IjoomerButton imgBack;


	@Override
	public int setLayoutId() {
		return R.layout.dfc_order_confirmation;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		getIntentData();
        lnrInfo = (LinearLayout) findViewById(R.id.lnrInfo);
		imgBanner = (ImageView) findViewById(R.id.imgBanner);
		txtRestaurantName = (IjoomerTextView) findViewById(R.id.txtRestaurantName);
		txtRestaurantAddress1 = (IjoomerTextView) findViewById(R.id.txtRestaurantAddress1);
		txtRestaurantAddress2 = (IjoomerTextView) findViewById(R.id.txtRestaurantAddress2);
		txtDateTime = (IjoomerTextView) findViewById(R.id.txtDateTime);
		txtDishPrice = (IjoomerTextView) findViewById(R.id.txtDishPrice);
		txtReference = (IjoomerTextView) findViewById(R.id.txtReference);
		txtDishName = (IjoomerTextView) findViewById(R.id.txtDishName);
		txtCollectionOrDelivery = (IjoomerTextView) findViewById(R.id.txtCollectionOrDelivery);

		imgBack = (IjoomerButton) findViewById(R.id.imgBack);

	}

	@Override
	public void prepareViews() {
		setDetails();
	}

	@Override
	public void setActionListeners() {
        if(IN_DISH_DETAIL.get("dishID").equals("0")){

            audioPlayer.setAudioListener(new IjoomerAudioPlayer.AudioListener() {

                @Override
                public void onReportClicked() {
                }

                @Override
                public void onPrepared() {
                }

                @Override
                public void onPlayClicked(boolean isplaying) {
                }

                @Override
                public void onError() {
                    lnrInfo.getBackground().clearColorFilter();
                    lnrInfo.invalidate();
                    IjoomerUtilities.getDFInfoDialog(DFCOrderConfirmationAndDetailActivity.this, "Audio Can No Be Played!", getString(R.string.ok), new CustomAlertNeutral() {

                        @Override
                        public void NeutralMethod() {
                        }
                    });
                }

                @Override
                public void onComplete() {
                    lnrInfo.getBackground().clearColorFilter();
                    lnrInfo.invalidate();
                }
            });

            lnrInfo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!audioPlayer.isPlaying()) {
                        lnrInfo.getBackground().setColorFilter(Color.parseColor("#80FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        lnrInfo.invalidate();
                        audioPlayer.playAudio(IN_DISH_DETAIL.get("dishName"));
                    } else {
                        lnrInfo.getBackground().clearColorFilter();
                        lnrInfo.invalidate();
                        audioPlayer.stopAudio();
                    }
                }
            });

        }
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!IN_FROM_ORDER_LIST) {
					Intent intent = new Intent("clearStackActivity");
					intent.setType("text/plain");
					sendBroadcast(intent);
					loadNew(DFCPlaceOrderActivity.class, DFCOrderConfirmationAndDetailActivity.this, true);
				} else {
					finish();
				}
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
			IN_FROM_ORDER_LIST = getIntent().getBooleanExtra("IN_FROM_ORDER_LIST", false);
		} catch (Exception e) {
		}
	}

	private void setDetails() {
		if (IN_FROM_ORDER_LIST) {
			imgBanner.setImageResource(R.drawable.df_order_detail_banner);
		}
		txtRestaurantName.setText(IN_DISH_DETAIL.get("restName"));
		txtRestaurantAddress1.setText(IN_DISH_DETAIL.get("address1"));
		txtRestaurantAddress2.setText(IN_DISH_DETAIL.get("address2"));

//		txtDateTime.setText(IN_DISH_DETAIL.get("created"));
        try{
            txtDateTime.setText(IjoomerUtilities.getDFDateString(IN_DISH_DETAIL.get("timeStamp")));
        }catch (Exception e){

        }

		txtReference.setText(IN_DISH_DETAIL.get("reference"));
        txtCollectionOrDelivery.setText(IN_DISH_DETAIL.get("service"));

		if (IN_DISH_DETAIL.get("dishID").equals("0")) {
			txtDishName.setText(IN_DISH_DETAIL.get("catName"));
		} else {
			txtDishName.setText(IN_DISH_DETAIL.get("dishName"));
		}
		txtDishPrice.setText(getString(R.string.df_curency_sign) + IN_DISH_DETAIL.get("price"));
	}

}
