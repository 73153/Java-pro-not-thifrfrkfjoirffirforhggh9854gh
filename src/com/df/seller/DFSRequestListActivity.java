package com.df.seller;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerAudioPlayer;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DFSRequestListActivity extends DFSellerMasterActivity {

	private ListView lstRequests;
	private IjoomerButton imgSwitch;

	private ArrayList<SmartListItem> listDataRequest = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder requestListAdapterWithHolder;
	private DFSDataprovider dataprovider;
	private IjoomerAudioPlayer audioPlayer = new IjoomerAudioPlayer();
	private ArrayList<HashMap<String, String>> IN_REQUESTLIST;
	private HashMap<String, String> clickdRow;



    private Timer timer;
    private int delay =3000;
    private int interval = 15000;


	@Override
	public int setLayoutId() {
		return R.layout.dfs_requests_list;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstRequests = (ListView) findViewById(R.id.lstRequests);
		imgSwitch = (IjoomerButton) findViewById(R.id.imgSwitch);
		dataprovider = new DFSDataprovider(this);
	}

	@Override
	public void prepareViews() {

		audioPlayer.setMaxVolume(this);
	}


    @Override
    protected void onPause() {
        super.onPause();
        stopPoling();
    }

    @Override
	protected void onResume() {
		super.onResume();
        try{
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(DFPushNotificationLuncherActivity.SENDVOICEOFFER);
        }catch (Exception e){

        }
        if(IjoomerUtilities.isSellerProfileComplete()){

            if(!IjoomerUtilities.isSellerPaymentDetailComplete()){
                IjoomerUtilities.getDFInfoDialog(DFSRequestListActivity.this,getString(R.string.df_complete_payment_detail), getString(R.string.ok), new CustomAlertNeutral() {

                    @Override
                    public void NeutralMethod() {
                        loadNew(DFSMyAccountActivity.class, DFSRequestListActivity.this, false);
                    }
                });

            }else{
                if(requestListAdapterWithHolder!=null){
                    getRequestsList(false,true);
                    startPoling();
                }else{
                    getRequestsList(true,true);
                    startPoling();
                }
            }
        }else{
            IjoomerUtilities.getDFInfoDialog(DFSRequestListActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                    loadNew(DFSProfileActivity.class, DFSRequestListActivity.this, false);
                }
            });
        }

	}

	@Override
	public void setActionListeners() {
		imgSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(DFSRequestMapActivity.class, DFSRequestListActivity.this, false, "IN_REQUESTLIST", IN_REQUESTLIST);
				} catch (Exception e) {
				}
			}
		});
		audioPlayer.setAudioListener(new AudioListener() {

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
				IjoomerUtilities.getDFInfoDialog(DFSRequestListActivity.this, "Audio Can No Be Played!", getString(R.string.ok), new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
					}
				});
			}

			@Override
			public void onComplete() {
				if (clickdRow != null) {
					stopVoiceListening(clickdRow);
				}
			}
		});
	}

	/**
	 * Class Methods
	 */

	public void getRequestsList(final boolean showLoading,final boolean showMsg) {
        if(showLoading){
            showProgressDialog(getString(R.string.df_loading_requests), this);
        }
		dataprovider.getRequestsList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
                IN_REQUESTLIST = null;
				if (responseCode == 200) {
					IN_REQUESTLIST = data1;
					prepareList(data1);
					requestListAdapterWithHolder = getListAdapter();
					lstRequests.setAdapter(requestListAdapterWithHolder);
				} else {
					listDataRequest.clear();
					requestListAdapterWithHolder = getListAdapter();
					lstRequests.setAdapter(requestListAdapterWithHolder);
                    if(responseCode==204){
                        if(showMsg){
                            IjoomerUtilities.getDFInfoDialog(DFSRequestListActivity.this, getString(R.string.df_no_request_found), getString(R.string.ok),
                                    new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {

                                        }
                                    });
                        }
                    }else{
                        responseMessageHandler(responseCode, false);
                    }
				}
			}
		});
	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataRequest.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfs_request_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				obj.add(row);
				item.setValues(obj);
				listDataRequest.add(item);
			}
		} else {
			listDataRequest.clear();
			requestListAdapterWithHolder = getListAdapter();
			lstRequests.setAdapter(requestListAdapterWithHolder);
		}
	}

    public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfs_request_list_item, listDataRequest, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtTimeAdded = (IjoomerButton) v.findViewById(R.id.txtTimeAdded);
				holder.txtCallUser = (IjoomerButton) v.findViewById(R.id.txtCallUser);
				holder.txtRequestAccept = (IjoomerButton) v.findViewById(R.id.txtRequestAccept);
				holder.imgRequestListen = (IjoomerButton) v.findViewById(R.id.imgRequestListen);
				holder.txtRequestReject = (IjoomerButton) v.findViewById(R.id.txtRequestReject);
                holder.btnFlag = (IjoomerButton) v.findViewById(R.id.btnFlag);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.txtTimeAdded.setText(getString(R.string.df_added) + " " + IjoomerUtilities.getDFTimeString(row.get("timeStamp")));

                holder.btnFlag.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = "Reported Username : "+row.get("personName")+"\n My Username: "+getSmartApplication().readSharedPreferences().getString(SP_DF_USERNAME,"")+"\n\n Please explain the reason for reporting this user below. One of our customer service team will take appropriate action within the next 24 hours.";
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/html");
                        i.putExtra(Intent.EXTRA_EMAIL,  new String[] {"hello@kravely.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Report");
                        i.putExtra(
                                Intent.EXTRA_TEXT,
                                Html.fromHtml(message));
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            ting(getString(R.string.share_email_no_client));
                        }
                    }
                });

				holder.imgRequestListen.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
                        if(!getSmartApplication().readSharedPreferences().getBoolean(row.get("offerID"),false)){

                            if (!audioPlayer.isPlaying()) {
                                startVoiceListening(row);
                            } else {
                                audioPlayer.stopAudio();
                            }
                        }else{
                            if (!audioPlayer.isPlaying()) {
                                audioPlayer.playAudio(row.get("voice"));
                            } else {
                                audioPlayer.stopAudio();
                            }
                        }


					}
				});
				holder.txtCallUser.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + row.get("personMobile")));
							startActivity(intent);
						} catch (Exception e) {
						}
					}
				});
				holder.txtRequestAccept.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							loadNew(DFSConfirmOrderActivity.class, DFSRequestListActivity.this, false, "IN_ORDER_DETAIL", row);
						} catch (Exception e) {
						}
					}
				});
				holder.txtRequestReject.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

                        getConfirmDialog("Reject Request.","Sure to reject request?","Yes","No",false,new AlertMagnatic() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                showProgressDialog("Rejecting Request..", DFSRequestListActivity.this);
                                dataprovider.rejectRequest(row.get("offerID"), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {
                                            IjoomerUtilities.getDFInfoDialog(DFSRequestListActivity.this, "Request Rejected Successfully.", getString(R.string.ok),
                                                    new CustomAlertNeutral() {

                                                        @Override
                                                        public void NeutralMethod() {
                                                            requestListAdapterWithHolder.remove(listDataRequest.get(position));
                                                            IN_REQUESTLIST.remove(position);
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

	private void startVoiceListening(final HashMap<String, String> row) {

		showProgressDialog("Sending Listing Request...", this);
		dataprovider.startVoiceListening(row.get("offerID"), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					clickdRow = row;
                    getSmartApplication().writeSharedPreferences(row.get("offerID"),true);
                    if (!audioPlayer.isPlaying()) {
						audioPlayer.playAudio(row.get("voice"));
					} else {
						audioPlayer.stopAudio();
					}
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	private void stopVoiceListening(final HashMap<String, String> row) {
        clickdRow = null;
		showProgressDialog("Sending Listen Complete Request...", this);
		dataprovider.stopVoiceListening(row.get("offerID"), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				hideProgressDialog();
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


    private void startPoling(){

        if(timer==null){
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getRequestsList(false,false);
                        }
                    });
                }
            },delay,interval);
        }
    }

    private void stopPoling(){

        try{
            if(timer!=null){
                timer.cancel();
                timer=null;
            }
        }catch (Exception e){

        }
    }
}
