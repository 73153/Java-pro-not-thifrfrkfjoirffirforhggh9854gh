package com.df.customer;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidquery.AQuery;
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

public class DFCBrowseDishesActivity extends DFCustomerMasterActivity {

	private ListView lstDishes;

	private ArrayList<SmartListItem> listDataDishes = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder dishListAdapterWithHolder;
	private DFCDataprovider dataprovider;

	private String IN_CATID;
	private String IN_RESTID;

	private AQuery aQuery;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_browse_dishes;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstDishes = (ListView) findViewById(R.id.lstDishes);
		dataprovider = new DFCDataprovider(this);
		aQuery = new AQuery(this);
		getIntentData();
	}

	@Override
	public void prepareViews() {
		lstDishes.setAdapter(null);
		getDishesList();
	}

	@Override
	public void setActionListeners() {

	}

	/**
	 * Class methods
	 */

	private void getIntentData() {
		try {
			IN_CATID = getIntent().getStringExtra("IN_CATID");
			IN_RESTID = getIntent().getStringExtra("IN_RESTID");
		} catch (Exception e) {
		}
	}

	public void getDishesList() {
		showProgressDialog(getString(R.string.df_loading_dishes), this);

		if (IN_RESTID != null) {
			dataprovider.getRestaurantDishesList(IN_RESTID, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {

				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					hideProgressDialog();
					if (responseCode == 200) {
						prepareList(data1);
						dishListAdapterWithHolder = getListAdapter();
						lstDishes.setAdapter(dishListAdapterWithHolder);
					} else {
						responseMessageHandler(responseCode, false);
					}
				}
			});
		} else {
			dataprovider.getDishesList(IN_CATID, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {

				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					hideProgressDialog();
					if (responseCode == 200) {
						prepareList(data1);
						dishListAdapterWithHolder = getListAdapter();
						lstDishes.setAdapter(dishListAdapterWithHolder);
					} else {
						responseMessageHandler(responseCode, false);
					}
				}
			});
		}

	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataDishes.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfc_dish_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(data.get(i));
				item.setValues(obj);
				listDataDishes.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfc_dish_list_item, listDataDishes, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.txtDishName = (IjoomerTextView) v.findViewById(R.id.txtDishName);
				holder.txtRestaurantName = (IjoomerTextView) v.findViewById(R.id.txtRestaurantName);
				holder.txtMiles = (IjoomerTextView) v.findViewById(R.id.txtMiles);
				holder.imgDish = (ImageView) v.findViewById(R.id.imgDish);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				holder.txtDishName.setText(row.get("dishName"));
				holder.txtRestaurantName.setText(row.get("restName"));
				aQuery.id(holder.imgDish).image(row.get("image"), true, true, getDeviceWidth(), 0);
				holder.txtMiles.setText(IjoomerUtilities.calculateDistance(row.get("lat"), row.get("long"), getLatitude(), getLongitude()));

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							loadNew(DFCDisheDetailsActivity.class, DFCBrowseDishesActivity.this, false, "IN_DISH_DETAIL", row);
						} catch (Exception e) {
							e.printStackTrace();
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
						if (responseCode == 204) {
							finish();
						}
					}
				});

	}
}
