package com.df.customer;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;

public class DFCRewardPointsActivity extends DFCustomerMasterActivity {

	private IjoomerTextView txtRewardCount;
	private IjoomerTextView txtRewardActivationCount;
	private IjoomerButton txtMyRewards;

	private IjoomerButton imgClaim;

	private ImageView imgReward1;
	private ImageView imgReward2;
	private ImageView imgReward3;
	private ImageView imgReward4;
	private ImageView imgReward5;
	private ImageView imgReward6;
	private ImageView imgReward7;
	private ImageView imgReward8;
	private ImageView imgReward9;
	private ImageView imgReward10;

	private ImageView[] arrImgRewards = new ImageView[10];

	private int rewardPoints;
	private int rewardPointsForActivation = 10;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_reward_points;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		rewardPoints = Integer.parseInt(getSmartApplication().readSharedPreferences().getString(SP_REWARD_POINTS, "0"));
		txtRewardCount = (IjoomerTextView) findViewById(R.id.txtRewardCount);
		txtRewardActivationCount = (IjoomerTextView) findViewById(R.id.txtRewardActivationCount);
		txtMyRewards = (IjoomerButton) findViewById(R.id.txtMyRewards);

		imgClaim = (IjoomerButton) findViewById(R.id.imgClaim);

		imgReward1 = (ImageView) findViewById(R.id.imgReward1);
		imgReward2 = (ImageView) findViewById(R.id.imgReward2);
		imgReward3 = (ImageView) findViewById(R.id.imgReward3);
		imgReward4 = (ImageView) findViewById(R.id.imgReward4);
		imgReward5 = (ImageView) findViewById(R.id.imgReward5);
		imgReward6 = (ImageView) findViewById(R.id.imgReward6);
		imgReward7 = (ImageView) findViewById(R.id.imgReward7);
		imgReward8 = (ImageView) findViewById(R.id.imgReward8);
		imgReward9 = (ImageView) findViewById(R.id.imgReward9);
		imgReward10 = (ImageView) findViewById(R.id.imgReward10);

		arrImgRewards[0] = imgReward1;
		arrImgRewards[1] = imgReward2;
		arrImgRewards[2] = imgReward3;
		arrImgRewards[3] = imgReward4;
		arrImgRewards[4] = imgReward5;
		arrImgRewards[5] = imgReward6;
		arrImgRewards[6] = imgReward7;
		arrImgRewards[7] = imgReward8;
		arrImgRewards[8] = imgReward9;
		arrImgRewards[9] = imgReward10;
	}

	@Override
	public void prepareViews() {
		txtRewardCount.setText("" + rewardPoints);
		txtRewardActivationCount.setText("" + (rewardPointsForActivation - rewardPoints)+ " points");

		if (rewardPointsForActivation - rewardPoints == 0) {
			imgClaim.setVisibility(View.VISIBLE);
		} else {
			imgClaim.setVisibility(View.GONE);
		}

		for (int i = 0; i < rewardPoints; i++) {
			arrImgRewards[i].setImageResource(R.drawable.df_rewardpoints_on);
		}
	}

	@Override
	public void setActionListeners() {
		imgClaim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadNew(DFCScratchCardActivity.class, DFCRewardPointsActivity.this, false);
			}
		});

		txtMyRewards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadNew(DFCRewardsListActivity.class, DFCRewardPointsActivity.this, false);
			}
		});
	}

}
