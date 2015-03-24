package com.df.seller;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.LinearLayout;

import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerAudioPlayer;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.CustomAlertNeutral;

import java.util.HashMap;

public class DFSOrderDetailsActivity extends DFSellerMasterActivity {

	private HashMap<String, String> IN_DISH_DETAIL;

	private IjoomerTextView txtPersonName;
	private IjoomerTextView txtPersonAddress1;
	private IjoomerTextView txtPersonAddress2;
	private IjoomerTextView txtDateTime;
	private IjoomerTextView txtDishPrice;
	private IjoomerTextView txtReference;
	private IjoomerTextView txtDishName;
	private IjoomerTextView txtCollectionOrDelivery;

    private LinearLayout lnrInfo;
    private IjoomerAudioPlayer audioPlayer = new IjoomerAudioPlayer();

	@Override
	public int setLayoutId() {
		return R.layout.dfs_order_details;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		getIntentData();
        lnrInfo = (LinearLayout) findViewById(R.id.lnrInfo);
		txtPersonName = (IjoomerTextView) findViewById(R.id.txtPersonName);
		txtPersonAddress1 = (IjoomerTextView) findViewById(R.id.txtPersonAddress1);
		txtPersonAddress2 = (IjoomerTextView) findViewById(R.id.txtPersonAddress2);
		txtDateTime = (IjoomerTextView) findViewById(R.id.txtDateTime);
		txtDishPrice = (IjoomerTextView) findViewById(R.id.txtDishPrice);
		txtReference = (IjoomerTextView) findViewById(R.id.txtReference);
		txtDishName = (IjoomerTextView) findViewById(R.id.txtDishName);
		txtCollectionOrDelivery = (IjoomerTextView) findViewById(R.id.txtCollectionOrDelivery);

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
                    IjoomerUtilities.getDFInfoDialog(DFSOrderDetailsActivity.this, "Audio Can No Be Played!", getString(R.string.ok), new CustomAlertNeutral() {

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

            lnrInfo.setOnClickListener(new View.OnClickListener() {
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
	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
		} catch (Exception e) {
		}
	}

	private void setDetails() {

		txtPersonName.setText(IN_DISH_DETAIL.get("personName"));
		txtPersonAddress1.setText(IN_DISH_DETAIL.get("personAddress1"));
		txtPersonAddress2.setText(IN_DISH_DETAIL.get("personAddress2"));
//		txtDateTime.setText(IN_DISH_DETAIL.get("date"));
        txtDateTime.setText(IjoomerUtilities.getDFDateString(IN_DISH_DETAIL.get("timeStamp")));
		txtReference.setText(IN_DISH_DETAIL.get("reference"));
		txtCollectionOrDelivery.setText(IN_DISH_DETAIL.get("service"));

		if (IN_DISH_DETAIL.get("dishID").equals("0")) {
			txtDishName.setText(IN_DISH_DETAIL.get("catName"));
		} else {
			txtDishName.setText(IN_DISH_DETAIL.get("dishName"));
		}
		txtDishPrice.setText(getString(R.string.df_curency_sign)+IN_DISH_DETAIL.get("price"));


        if (IN_DISH_DETAIL.get("service").equalsIgnoreCase("delivery")) {
            txtPersonAddress1.setText(IN_DISH_DETAIL.get("deliveryAddress"));
            txtPersonAddress2.setVisibility(View.GONE);
        }

	}

}
