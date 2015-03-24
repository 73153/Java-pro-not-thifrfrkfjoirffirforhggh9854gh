package com.df.seller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

public class DFSChooseCuisineActivity extends DFSellerMasterActivity {

	private ListView lstCuisine;
	private IjoomerButton imgSelect;

	private ArrayList<SmartListItem> listDataCuisine = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder cuisineListAdapterWithHolder;
	private DFCDataprovider dataprovider;
	private String selectedCuisine;
	private String selectedCuisineName;
	private String IN_CATID;

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_registration_step2;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstCuisine = (ListView) findViewById(R.id.lstCuisine);
		imgSelect = (IjoomerButton) findViewById(R.id.imgSelect);
		dataprovider = new DFCDataprovider(this);
		getIntentData();
	}

	@Override
	public void prepareViews() {
		lstCuisine.setAdapter(null);
		getCusineList();
	}

	@Override
	public void setActionListeners() {
		imgSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					if (selectedCuisine == null) {
						IjoomerUtilities.getDFInfoDialog(DFSChooseCuisineActivity.this, getString(R.string.df_please_select_your_cuisine), getString(R.string.ok),
								new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
					} else {
						Intent i = new Intent();
						i.putExtra("IN_CATNAME", selectedCuisineName);
						i.putExtra("IN_CATID", selectedCuisine);
						setResult(Activity.RESULT_OK, i);
						finish();
					}
				} catch (Exception e) {
				}
			}
		});

	}

	/**
	 * Class methods
	 */

	private void getIntentData() {
		IN_CATID = getIntent().getStringExtra("IN_CATID");
	}

	public void getCusineList() {
		showProgressDialog(getString(R.string.df_loading_cauisine), this);
		dataprovider.getCuiSineList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					hideProgressDialog();
					prepareList(data1);
					cuisineListAdapterWithHolder = getListAdapter();
					lstCuisine.setAdapter(cuisineListAdapterWithHolder);
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataCuisine.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfc_choose_cuisine_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				row.put("isChecked", "0");
				obj.add(row);
				item.setValues(obj);
				listDataCuisine.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfc_choose_cuisine_list_item, listDataCuisine, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.chkCuisine = (IjoomerCheckBox) v.findViewById(R.id.chkCuisine);
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				holder.chkCuisine.setText(row.get("name"));

				if (IN_CATID != null && selectedCuisine == null) {
					if (IN_CATID.equals(row.get("categoryID"))) {
						selectedCuisine = IN_CATID;
						selectedCuisineName = row.get("name");
						holder.chkCuisine.setChecked(true);
						updateList(position);
					}
				}
				if (row.get("isChecked").equals("1")) {
					holder.chkCuisine.setChecked(true);
				} else {
					holder.chkCuisine.setChecked(false);
				}

				holder.chkCuisine.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (holder.chkCuisine.isChecked()) {
							selectedCuisine = row.get("categoryID");
							selectedCuisineName = row.get("name");
							updateList(position);
						} else {
							selectedCuisine = null;
							selectedCuisineName = null;
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

	private void updateList(int pos) {
		int size = listDataCuisine.size();
		for (int i = 0; i < size; i++) {
			if (i == pos) {
				((HashMap<String, String>) listDataCuisine.get(i).getValues().get(0)).put("isChecked", "1");
			} else {
				((HashMap<String, String>) listDataCuisine.get(i).getValues().get(0)).put("isChecked", "0");
			}
		}
		cuisineListAdapterWithHolder.notifyDataSetChanged();
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
