package com.df.customer;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.HashMap;

public class DFCOrderListActivity extends DFCustomerMasterActivity {

	private ListView lstOrders;

	private ArrayList<SmartListItem> listDataOrders = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder orderListAdapterWithHolder;
	private DFCDataprovider dataprovider;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_order_list;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstOrders = (ListView) findViewById(R.id.lstOrders);
		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		getOrdersList();
	}

	/**
	 * Class Methods
	 */

	public void getOrdersList() {
		if (listDataOrders == null || listDataOrders.size() <= 0) {
			showProgressDialog(getString(R.string.df_loading_orders), this);
		}
		dataprovider.getOrdersList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					prepareList(data1);
					orderListAdapterWithHolder = getListAdapter();
					lstOrders.setAdapter(orderListAdapterWithHolder);
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataOrders.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfc_orders_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				obj.add(row);
				item.setValues(obj);
				listDataOrders.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfc_orders_list_item, listDataOrders, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtDfcRestaurantName = (IjoomerTextView) v.findViewById(R.id.txtDfcRestaurantName);
				holder.txtDfcOrderDate = (IjoomerTextView) v.findViewById(R.id.txtDfcOrderDate);
				holder.txtDfcOrderAmount = (IjoomerTextView) v.findViewById(R.id.txtDfcOrderAmount);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.txtDfcRestaurantName.setText(row.get("restName"));
//				holder.txtDfcOrderDate.setText(row.get("created"));
                holder.txtDfcOrderDate.setText(IjoomerUtilities.getDFDateString(row.get("timeStamp")));
				holder.txtDfcOrderAmount.setText(getString(R.string.df_curency_sign) + row.get("price"));

				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							loadNew(DFCOrderConfirmationAndDetailActivity.class, DFCOrderListActivity.this, false, "IN_DISH_DETAIL", row, "IN_FROM_ORDER_LIST", true);
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
