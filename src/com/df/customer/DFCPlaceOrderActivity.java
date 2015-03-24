package com.df.customer;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.df.src.RequestTimer;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.io.File;
import java.io.IOException;

public class DFCPlaceOrderActivity extends DFCustomerMasterActivity {

	private ImageView imgMic;
	private IjoomerButton imgBrowse;

	private MediaRecorder mRecorder = null;
	private String mFileName;
	private boolean isSpaceAvailable = true;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_place_order1;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgMic = (ImageView) findViewById(R.id.imgMic);
		imgBrowse = (IjoomerButton) findViewById(R.id.imgBrowse);
	}

	@Override
	public void prepareViews() {

        try{
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(DFPushNotificationLuncherActivity.OFFEREXPIRED);
            notificationManager.cancel(DFPushNotificationLuncherActivity.TIMEREXTENDED);

        }catch (Exception e){

        }


	}

    @Override
    protected void onResume() {
        super.onResume();

        if(!IjoomerUtilities.isCustomerProfileComplete()){

            IjoomerUtilities.getDFInfoDialog(DFCPlaceOrderActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                    loadNew(DFCProfileActivity.class, DFCPlaceOrderActivity.this, false);
                }
            });

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getBooleanExtra("IN_FROM_ALARM",false)){

            getSmartApplication().writeSharedPreferences(SP_DF_REQ_TIMESTAMP,new Long(0));

            IjoomerUtilities.getDFInfoDialog(this, "Sorry, no restaurants responded to your request \n Click here to upload a new one", getString(R.string.ok),
                    new CustomAlertNeutral() {

                        @Override
                        public void NeutralMethod() {

                        }
                    }
            );
        }
    }

    @Override
	public void setActionListeners() {

		imgBrowse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadNew(DFCChooseCuisineActivity.class, DFCPlaceOrderActivity.this, false);
			}
		});

		imgMic.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				imgMic.setImageResource(R.drawable.df_mic_cartoon_selected);
				startRecording();
				return true;
			}
		});
		imgMic.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					imgMic.setImageResource(R.drawable.df_mic_cartoon);
					stopRecording();

                    if (RequestTimer.isOfferRunning()){
                        IjoomerUtilities.getDFInfoDialog(DFCPlaceOrderActivity.this, "You already placed a voice offer, Please try after complete 5 minutes.", getString(R.string.ok), new CustomAlertNeutral() {

                            @Override
                            public void NeutralMethod() {
                            }
                        });
                    }else{
                        try {
                            loadNew(DFCPlaceOrderActivity2.class, DFCPlaceOrderActivity.this, false, "IN_FILENAME", mFileName);
                        } catch (Exception e) {
                        }
                    }

					mFileName = null;
				}
				return false;
			}
		});

	}

	/**
	 * Class Methods
	 */

	private void startRecording() {
		isSpaceAvailable = true;
		try {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mFileName = getRecordDefaultFileName();
			mRecorder.setOutputFile(mFileName);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			try {
				mRecorder.prepare();
			} catch (IOException e) {
				System.out.println("prepare() failed");
			}

			mRecorder.start();
		} catch (Exception e) {
			isSpaceAvailable = false;
			IjoomerUtilities.getDFInfoDialog(DFCPlaceOrderActivity.this, "Not Enough Space Available!", getString(R.string.ok), new CustomAlertNeutral() {

				@Override
				public void NeutralMethod() {
				}
			});

			return;
		}

	}

	/**
	 * this method used to stop recording.
	 */
	private void stopRecording() {
		try {
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * This method used to get record default file name.
	 * 
	 * @return represented {@link String}
	 */
	private String getRecordDefaultFileName() {
		File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "DreamyFlavours" + "/");
		if (!wallpaperDirectory.exists()) {
			wallpaperDirectory.mkdirs();
		}

		return wallpaperDirectory.getAbsolutePath() + File.separator + "iarecord" + ".3gp";
	}

    @Override
    public void onBackPressed() {

        getConfirmDialog(getString(R.string.app_name),getString(R.string.df_exit_app),getString(R.string.yes),getString(R.string.no),false,new AlertMagnatic() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                Intent intent = new Intent("clearStackActivity");
                intent.setType("text/plain");
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {

            }
        });

    }
}
