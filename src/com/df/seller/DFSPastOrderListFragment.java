package com.df.seller;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ListView;

import com.df.seller.DFSOrderListActivity.ActivityEventListener;
import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DFSPastOrderListFragment extends SmartFragment {

	private ListView lstOrders;

	private ArrayList<SmartListItem> listDataOrders = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder ordersListAdapterWithHolder;
	private DFSDataprovider dataprovider;

	private HashMap<String, String> selectedToDelete = new HashMap<String, String>();

	private boolean isListInDeleteMode = false;

	public DFSPastOrderListFragment() {
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_current_past_order_list_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {

		lstOrders = (ListView) currentView.findViewById(R.id.lstOrders);
		dataprovider = new DFSDataprovider(getActivity());

	}

	@Override
	public void prepareViews(View currentView) {

		getPastOrdersList();

	}

	@Override
	public void setActionListeners(View currentView) {

		((DFSOrderListActivity) getActivity()).setActivityEventListener(new ActivityEventListener() {

			@Override
			public boolean onBackPressed() {
				if (isListInDeleteMode) {
					isListInDeleteMode = false;
					selectedToDelete = new HashMap<String, String>();
					ordersListAdapterWithHolder.notifyDataSetChanged();
				} else {
					isListInDeleteMode = true;
				}
				return !isListInDeleteMode;
			}

			@Override
			public void onDeletePressed() {
				if (listDataOrders == null || listDataOrders.size() <= 0) {
					((DFSOrderListActivity) getActivity()).ting("Nothing To Delete!");
				} else {
					if (isListInDeleteMode) {
						if (selectedToDelete.size() <= 0) {
							((DFSOrderListActivity) getActivity()).ting("Please Selecte an Order to Delete!");
						} else {
                            ((SmartActivity)getActivity()).getConfirmDialog("Delete Orders", "Sure to delete?", "Yes", "No", false, new AlertMagnatic() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    deleteOrders();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });
						}
					} else {
						((DFSOrderListActivity) getActivity()).ting("Long Press Item to Delete.");
					}
				}
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void deleteOrders() {
		String strOrderIds = "";
		Iterator it = selectedToDelete.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			strOrderIds += pairs.getKey().toString() + ",";
		}
		strOrderIds = strOrderIds.substring(0, strOrderIds.length() - 1);
		((DFSOrderListActivity) getActivity()).showProgressDialog("Deleting Orders...", getActivity());
		dataprovider.deletePastOrders(strOrderIds, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, final ArrayList<HashMap<String, String>> data1, Object data2) {
				((DFSOrderListActivity) getActivity()).hideProgressDialog();

				if (responseCode == 200) {
					isListInDeleteMode = false;
					IjoomerUtilities.getDFInfoDialog(getActivity(), "Delete Done!", getString(R.string.ok), new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {
							if (data1 == null || data1.size() <= 0) {
								lstOrders.setAdapter(null);
								IjoomerUtilities.getDFInfoDialog(getActivity(), "All Orders Deleted!", getString(R.string.ok), new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});

							} else {
								prepareList(data1);
								ordersListAdapterWithHolder = getListAdapter();
								lstOrders.setAdapter(ordersListAdapterWithHolder);
							}
						}
					});
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	public void getPastOrdersList() {
		((DFSOrderListActivity) getActivity()).showProgressDialog(getString(R.string.df_loading_past_orders), getActivity());
		dataprovider.getPastOrdersList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				((DFSOrderListActivity) getActivity()).hideProgressDialog();
				if (responseCode == 200) {
					prepareList(data1);
					ordersListAdapterWithHolder = getListAdapter();
					lstOrders.setAdapter(ordersListAdapterWithHolder);
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
				item.setItemLayout(R.layout.dfs_orders_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				obj.add(row);
				item.setValues(obj);
				listDataOrders.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.dfs_orders_list_item, listDataOrders, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtDFSPersonName = (IjoomerTextView) v.findViewById(R.id.txtDFSPersonName);
				holder.txtDFSOrderDate = (IjoomerTextView) v.findViewById(R.id.txtDFSOrderDate);
				holder.txtDFSOrderAmount = (IjoomerTextView) v.findViewById(R.id.txtDFSOrderAmount);
				holder.chkDelete = (IjoomerCheckBox) v.findViewById(R.id.chkDelete);
				holder.chkDelete.setChecked(false);

				if (isListInDeleteMode) {
					holder.chkDelete.setVisibility(View.VISIBLE);
				} else {
					holder.chkDelete.setVisibility(View.GONE);
				}

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				if (selectedToDelete.size() > 0 && selectedToDelete.containsKey(row.get("orderID"))) {
					holder.chkDelete.setChecked(true);
				}

				holder.txtDFSPersonName.setText(row.get("personName"));
//				holder.txtDFSOrderDate.setText(row.get("date"));
                holder.txtDFSOrderDate.setText(IjoomerUtilities.getDFDateString(row.get("timeStamp")));
				holder.txtDFSOrderAmount.setText(getString(R.string.df_curency_sign) + row.get("price"));
				v.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						isListInDeleteMode = true;
						ordersListAdapterWithHolder.notifyDataSetChanged();
						return true;
					}
				});
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (isListInDeleteMode) {
							if (holder.chkDelete.isChecked()) {
								holder.chkDelete.setChecked(false);
								selectedToDelete.remove(row.get("orderID"));
							} else {
								holder.chkDelete.setChecked(true);
								selectedToDelete.put(row.get("orderID"), "");
							}
						} else {
							try {
								((SmartActivity) getActivity()).loadNew(DFSOrderDetailsActivity.class, getActivity(), false, "IN_DISH_DETAIL", row);
							} catch (Exception e) {
							}
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

		IjoomerUtilities.getDFInfoDialog(getActivity(), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getString(R.string.ok), new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

}
