package com.df.customer;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.df.src.RequestTimer;
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

public class DFCChooseCuisineActivity extends DFCustomerMasterActivity {

	private ListView lstCuisine;
	private IjoomerButton imgSelect;

	private ArrayList<SmartListItem> listDataCuisine = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder cuisineListAdapterWithHolder;
	private DFCDataprovider dataprovider;
	private String selectedCuisine;

	private String IN_FILENAME;
	private String IN_FROM;
	private String IN_SERVICE;
    private String IN_DELIVERY_ADDRESS;


	@Override
	public int setLayoutId() {
		return R.layout.dfc_choose_cuisine;
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
						IjoomerUtilities.getDFInfoDialog(DFCChooseCuisineActivity.this, getString(R.string.df_please_select_your_cuisine), getString(R.string.ok),
								new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});

					} else {
						if (IN_FILENAME == null) {
							loadNew(DFCBrowseDishesActivity.class, DFCChooseCuisineActivity.this, false, "IN_CATID", selectedCuisine);
						} else {
							placeVoiceOrder();
						}
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
		try {
			IN_FILENAME = getIntent().getStringExtra("IN_FILENAME");
			IN_FROM = getIntent().getStringExtra("IN_FROM");
			IN_SERVICE = getIntent().getStringExtra("IN_SERVICE");
            IN_DELIVERY_ADDRESS = getIntent().getStringExtra("IN_DELIVERY_ADDRESS");

		} catch (Exception e) {
		}
	}

	public void getCusineList() {
		showProgressDialog(getString(R.string.df_loading_cauisine), this);
		dataprovider.getCuiSineList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				prepareList(data1);
				cuisineListAdapterWithHolder = getListAdapter();
				lstCuisine.setAdapter(cuisineListAdapterWithHolder);
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
							updateList(position);
						} else {
							selectedCuisine = null;
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

	private void placeVoiceOrder() {

		showProgressDialog(getString(R.string.df_sending_voice_order), this);
		dataprovider.sendOrderByVoice(IN_SERVICE, IN_FILENAME, selectedCuisine,IN_DELIVERY_ADDRESS, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, final ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {

                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(DFPushNotificationLuncherActivity.ACCEPTREQUEST);

                    long time = System.currentTimeMillis();

                    getSmartApplication().writeSharedPreferences(SP_DF_REQ_TIMESTAMP,new Long(time));
                    getSmartApplication().writeSharedPreferences(SP_DF_REQ_WAS_ACCEPTED,false);
                    getSmartApplication().writeSharedPreferences(SP_DF_REQ_WAS_CONFIRMED,false);
                    getSmartApplication().writeSharedPreferences(SP_DFC_LISTEN_COUNT,0);


                    setOnetimeTimer(time);

					IjoomerUtilities.getDFInfoDialog(DFCChooseCuisineActivity.this, getString(R.string.df_order_sent_successfully), getString(R.string.ok),
                            new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    try {
                                        loadNew(DFCResponsesMapActivity.class, DFCChooseCuisineActivity.this, false, "IN_OFFER_DETAILS", data1.get(0));
                                    } catch (Exception e) {
                                    }
                                }
                            }
                    );
				} else {

                    if (responseCode == 204) {
                        IjoomerUtilities.getDFInfoDialog(DFCChooseCuisineActivity.this, getString(R.string.df_no_meals_found), getString(R.string.ok), new CustomAlertNeutral() {
                            @Override
                            public void NeutralMethod() {
                                finish();
                            }
                        });
                    } else {
                        responseMessageHandler(responseCode, false);
                    }
				}

			}
		});
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

    public void setOnetimeTimer(long time) {
        AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1010, intent, 0);

        try{
            am.cancel(pi);
        }catch (Exception e){

        }
        am.set(AlarmManager.RTC_WAKEUP, time + (RequestTimer.INTERVAL), pi);
    }
}
