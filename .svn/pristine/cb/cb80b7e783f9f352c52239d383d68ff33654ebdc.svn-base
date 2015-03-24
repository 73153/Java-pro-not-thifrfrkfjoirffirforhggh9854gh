package com.df.customer;

import java.util.HashMap;

import android.view.View;

import com.df.src.R;
import com.ijoomer.customviews.IjoomerTextView;

public class DFCRewardsDetailActivity extends DFCustomerMasterActivity {

	private HashMap<String, String> IN_REWARD_DETAIL;

	private IjoomerTextView txtRewardTitle;
	private IjoomerTextView txtRewardCode;
	private IjoomerTextView txtRewardInfo;
	private IjoomerTextView txtRewardClaimInfo;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_rewards_detail;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		getIntentData();
		txtRewardTitle = (IjoomerTextView) findViewById(R.id.txtRewardTitle);
		txtRewardCode = (IjoomerTextView) findViewById(R.id.txtRewardCode);
		txtRewardInfo = (IjoomerTextView) findViewById(R.id.txtRewardInfo);
		txtRewardClaimInfo = (IjoomerTextView) findViewById(R.id.txtRewardClaimInfo);

	}

	@Override
	public void prepareViews() {
		setDetails();
	}

	@Override
	public void setActionListeners() {

	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			IN_REWARD_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
		} catch (Exception e) {
		}
	}

	private void setDetails() {
		txtRewardTitle.setText(IN_REWARD_DETAIL.get("rewardTitle"));
		txtRewardCode.setText(IN_REWARD_DETAIL.get("rewardCode"));
		txtRewardInfo.setText(IN_REWARD_DETAIL.get("rewardInfo"));
		txtRewardClaimInfo.setText(IN_REWARD_DETAIL.get("rewardClaimInfo"));
	}

}
