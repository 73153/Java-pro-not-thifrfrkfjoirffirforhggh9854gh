package com.df.customer;

import java.util.ArrayList;
import java.util.HashMap;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class DFCRewardsListActivity extends DFCustomerMasterActivity {

	private ListView lstRewards;

	private ArrayList<SmartListItem> listDataRewards = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder rewardListAdapterWithHolder;
	private DFCDataprovider dataprovider;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_rewards_list;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstRewards = (ListView) findViewById(R.id.lstRewards);
		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {
		getRewards();
	}

	@Override
	public void setActionListeners() {

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
	 * Class Methods
	 */

	public void getRewards() {
		if (listDataRewards == null || listDataRewards.size() <= 0) {
			showProgressDialog(getString(R.string.df_loading_rewards), this);
		}
		dataprovider.getRewards(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					prepareList(data1);
					rewardListAdapterWithHolder = getListAdapter();
					lstRewards.setAdapter(rewardListAdapterWithHolder);
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataRewards.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfc_rewards_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				obj.add(row);
				item.setValues(obj);
				listDataRewards.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfc_rewards_list_item, listDataRewards, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtRewardTitle = (IjoomerTextView) v.findViewById(R.id.txtRewardTitle);
				holder.txtRewardCode = (IjoomerTextView) v.findViewById(R.id.txtRewardCode);
				holder.txtRewardDate = (IjoomerTextView) v.findViewById(R.id.txtRewardDate);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.txtRewardTitle.setText(row.get("rewardTitle"));
				holder.txtRewardCode.setText(row.get("rewardCode"));
				holder.txtRewardDate.setText(row.get("rewardDate"));

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							loadNew(DFCRewardsDetailActivity.class, DFCRewardsListActivity.this, false, "IN_REWARD_DETAIL", row);
						} catch (Exception e) {
						}
					}
				});
				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

		});
		return listAdapterWithHolder;
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
