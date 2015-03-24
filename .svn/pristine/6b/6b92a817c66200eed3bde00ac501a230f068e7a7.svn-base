package com.df.seller;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

public class DFSOfferListActivity extends DFSellerMasterActivity {

	private ListView lstOffers;
	private IjoomerButton imgSwitch;

	private ArrayList<SmartListItem> listDataOffers = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder offerListAdapterWithHolder;
	private DFSDataprovider dataprovider;

	private ArrayList<HashMap<String, String>> IN_OFFERLIST;
    private Dialog dialog;

    @Override
	public int setLayoutId() {
		return R.layout.dfs_offer_list;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstOffers = (ListView) findViewById(R.id.lstOffers);
		imgSwitch = (IjoomerButton) findViewById(R.id.imgSwitch);
		dataprovider = new DFSDataprovider(this);
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {
		imgSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(DFSOfferMapActivity.class, DFSOfferListActivity.this, false, "IN_OFFERLIST", IN_OFFERLIST);
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
        try{
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(DFPushNotificationLuncherActivity.SENDREQUESTOFFER);
        }catch (Exception e){

        }

        if(offerListAdapterWithHolder!=null){
            getOffersList(false);
        }else{
            getOffersList(true);
        }
	}

	/**
	 * Class Methods
	 */

	public void getOffersList(boolean showProgress) {
        if(showProgress){
            showProgressDialog(getString(R.string.df_loading_offers), this);
        }
		dataprovider.getOffersList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
                IN_OFFERLIST = null;
				if (responseCode == 200) {
					if (data1 != null && data1.size() > 0) {
						IN_OFFERLIST = data1;
						prepareList(data1);
						offerListAdapterWithHolder = getListAdapter();
						lstOffers.setAdapter(offerListAdapterWithHolder);
					} else {
						listDataOffers.clear();
						offerListAdapterWithHolder = getListAdapter();
						lstOffers.setAdapter(offerListAdapterWithHolder);
                        IjoomerUtilities.getDFInfoDialog(DFSOfferListActivity.this, getString(R.string.df_no_offers_found), getString(R.string.ok),
                                new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {

                                    }
                                });
					}
				} else{
                    if(responseCode==204){
                        listDataOffers.clear();
                        offerListAdapterWithHolder = getListAdapter();
                        lstOffers.setAdapter(offerListAdapterWithHolder);
                        IjoomerUtilities.getDFInfoDialog(DFSOfferListActivity.this, getString(R.string.df_no_offers_found), getString(R.string.ok),
                                new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {

                                    }
                                });
                    }else{
                        responseMessageHandler(responseCode,false);
                    }
                }
			}
		});
	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataOffers.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfs_offer_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				obj.add(row);
				item.setValues(obj);
				listDataOffers.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfs_offer_list_item, listDataOffers, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtPersonName = (IjoomerTextView) v.findViewById(R.id.txtPersonName);
				holder.txtOrderDate = (IjoomerTextView) v.findViewById(R.id.txtOrderDate);
				holder.txtOrderDishName = (IjoomerTextView) v.findViewById(R.id.txtOrderDishName);
				holder.txtOrderAmount = (IjoomerTextView) v.findViewById(R.id.txtOrderAmount);
				holder.txtDeliveryRequired = (IjoomerTextView) v.findViewById(R.id.txtDeliveryRequired);
				holder.imgReject = (IjoomerButton) v.findViewById(R.id.imgReject);
				holder.imgCall = (IjoomerButton) v.findViewById(R.id.imgCall);
				holder.imgConfirm = (IjoomerButton) v.findViewById(R.id.imgConfirm);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				if (row.get("service").equalsIgnoreCase("delivery")) {
					holder.txtDeliveryRequired.setVisibility(View.VISIBLE);
				} else {
                    holder.txtDeliveryRequired.setVisibility(View.GONE);
				}

				holder.txtPersonName.setText(row.get("personName"));
                holder.txtOrderDate.setText(IjoomerUtilities.getDFDateString(row.get("timeStamp")));
//				holder.txtOrderDate.setText(row.get(row.get("date") + " @ " + row.get("time")));
				holder.txtOrderDishName.setText(row.get("dishName"));
				holder.txtOrderAmount.setText(getString(R.string.df_curency_sign) + row.get("price"));

				holder.txtDeliveryRequired.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeliveryOptionDialog(row.get("deliveryAddress"));
                    }
                });

				holder.imgCall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + row.get("personMobile")));
							startActivity(intent);
						} catch (Exception e) {
						}
					}
				});

				holder.imgConfirm.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

                        getConfirmDialog("Confirm Offer","Sure to confirm offer?","Yes","No",false,new AlertMagnatic() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                showProgressDialog("Confirming Offer..", DFSOfferListActivity.this);
                                dataprovider.acceptOffer(row.get("service"), row.get("offerID"), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {
                                            IjoomerUtilities.getDFInfoDialog(DFSOfferListActivity.this, "Offer Confirmed Successfully.", getString(R.string.ok), new CustomAlertNeutral() {

                                                @Override
                                                public void NeutralMethod() {
                                                    offerListAdapterWithHolder.remove(listDataOffers.get(position));
                                                    IN_OFFERLIST.remove(position);
                                                }
                                            });
                                        } else {
                                            responseMessageHandler(responseCode, false);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {

                            }
                        });

					}
				});

				holder.imgReject.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
                        getConfirmDialog("Reject Offer","Sure to reject offer?","Yes","No",false,new AlertMagnatic() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                showProgressDialog("Rejecting Offer..", DFSOfferListActivity.this);
                                dataprovider.rejectOffer(row.get("offerID"), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {
                                            IjoomerUtilities.getDFInfoDialog(DFSOfferListActivity.this, "Offer Rejected Successfully.", getString(R.string.ok), new CustomAlertNeutral() {

                                                @Override
                                                public void NeutralMethod() {
                                                    offerListAdapterWithHolder.remove(listDataOffers.get(position));
                                                    IN_OFFERLIST.remove(position);
                                                }
                                            });
                                        } else {
                                            responseMessageHandler(responseCode, false);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {

                            }
                        });

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

    public void showDeliveryOptionDialog(final String addresss) {

        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dfs_delivery_info);

        final IjoomerButton btnOk = (IjoomerButton) dialog.findViewById(R.id.btnOk);
        final IjoomerTextView txtAddress = (IjoomerTextView) dialog.findViewById(R.id.txtAddress);
        txtAddress.setText(addresss);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
