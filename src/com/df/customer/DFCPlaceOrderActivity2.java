package com.df.customer;

import android.app.Dialog;
import android.location.Address;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerAudioPlayer;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.smart.framework.CustomAlertNeutral;

import java.io.File;
import java.io.IOException;

public class DFCPlaceOrderActivity2 extends DFCustomerMasterActivity {

	private IjoomerButton imgListen;
	private IjoomerButton imgRecordAgain;
	private IjoomerButton imgNext;
	private ImageView imgMic;
	private IjoomerCheckBox chkDeliveryRequired;
    private LinearLayout lnrDeliveryRequired;
	private MediaRecorder mRecorder = null;
	private String mFileName = null;
	private boolean isSpaceAvailable = true;

	private String IN_FILENAME;
    private String deliveryAddress="";

    private Dialog dialog;

	private IjoomerAudioPlayer audioPlayer = new IjoomerAudioPlayer();

    @Override
	public int setLayoutId() {
		return R.layout.dfc_place_order2;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgMic = (ImageView) findViewById(R.id.imgMic);
		imgRecordAgain = (IjoomerButton) findViewById(R.id.imgRecordAgain);
		imgNext = (IjoomerButton) findViewById(R.id.imgNext);
		imgListen = (IjoomerButton) findViewById(R.id.imgListen);
		chkDeliveryRequired = (IjoomerCheckBox) findViewById(R.id.chkDeliveryRequired);
        lnrDeliveryRequired = (LinearLayout) findViewById(R.id.lnrDeliveryRequired);
		getIntentData();
	}

	@Override
	public void prepareViews() {
		audioPlayer.setMaxVolume(this);
	}

	@Override
	public void setActionListeners() {

        lnrDeliveryRequired.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chkDeliveryRequired.setChecked(true);
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }else{
                    showDeliveryOptionDialog();
                }
            }
        });


		imgNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mFileName != null) {
					IN_FILENAME = mFileName;
				}
				try {
					loadNew(DFCChooseCuisineActivity.class, DFCPlaceOrderActivity2.this, false, "IN_FILENAME", IN_FILENAME, "IN_SERVICE",
							chkDeliveryRequired.isChecked() ? "Delivery" : "Collection","IN_DELIVERY_ADDRESS",deliveryAddress);
				} catch (Exception e) {
				}

			}
		});
		imgListen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!audioPlayer.isPlaying()) {
					if (mFileName != null) {
						IN_FILENAME = mFileName;
					}
					audioPlayer.playAudio(IN_FILENAME);
				} else {
					audioPlayer.stopAudio();
				}
			}
		});

		imgRecordAgain.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				imgMic.setImageResource(R.drawable.df_mic_cartoon_selected);
				startRecording();
				return true;
			}
		});
		imgRecordAgain.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					imgMic.setImageResource(R.drawable.df_mic_cartoon);
					stopRecording();
				}
				return false;
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
				IjoomerUtilities.getDFInfoDialog(DFCPlaceOrderActivity2.this, "Audio Can No Be Played!", getString(R.string.ok), new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
					}
				});
			}

			@Override
			public void onComplete() {

			}
		});
	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			IN_FILENAME = getIntent().getStringExtra("IN_FILENAME");
		} catch (Exception e) {
		}
	}

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
		// String fileName;
		File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "iJoomerAdvance" + "/");
		if (!wallpaperDirectory.exists()) {
			wallpaperDirectory.mkdirs();
		}
		// if (wallpaperDirectory.listFiles() != null) {
		// fileName = "record" + wallpaperDirectory.listFiles().length;
		// } else {
		// fileName = "record" + 1;
		// }

		return wallpaperDirectory.getAbsolutePath() + File.separator + "iarecord" + ".3gp";
	}

    public void showDeliveryOptionDialog() {

        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dfc_select_delivery_option);

        final IjoomerCheckBox chkCurrentLocation = (IjoomerCheckBox) dialog.findViewById(R.id.chkCurrentLocation);
        final IjoomerCheckBox chkHomeAddress = (IjoomerCheckBox) dialog.findViewById(R.id.chkHomeAddress);
        final IjoomerEditText edtAddress = (IjoomerEditText) dialog.findViewById(R.id.edtAddress);
        final IjoomerButton btnOk = (IjoomerButton) dialog.findViewById(R.id.btnOk);
        final IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);

        chkHomeAddress.setChecked(true);
        edtAddress.setText(getSmartApplication().readSharedPreferences().getString(SP_DF_ADDRESS,""));
        chkCurrentLocation.setChecked(false);

        chkCurrentLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkCurrentLocation.isChecked()) {
                    chkCurrentLocation.setChecked(true);
                    chkHomeAddress.setChecked(false);
                    try{
                        Address address = IjoomerUtilities.getAddressFromLatLong(0,0);
                        edtAddress.setText(address.getAddressLine(0) + "," + address.getAddressLine(1) + "," + address.getAddressLine(2));
                    }catch (Exception e){
                        edtAddress.setText("");
                    }
                }else{
                    edtAddress.setText(getSmartApplication().readSharedPreferences().getString(SP_DF_ADDRESS,""));
                    chkCurrentLocation.setChecked(false);
                    chkHomeAddress.setChecked(true);
                }
            }
        });

        chkHomeAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkHomeAddress.isChecked()) {
                    chkCurrentLocation.setChecked(false);
                    chkHomeAddress.setChecked(true);
                    edtAddress.setText(getSmartApplication().readSharedPreferences().getString(SP_DF_ADDRESS,""));
                }else{
                    chkHomeAddress.setChecked(false);
                    chkCurrentLocation.setChecked(true);
                    try{
                        Address address = IjoomerUtilities.getAddressFromLatLong(0,0);
                        edtAddress.setText(address.getAddressLine(0) + "," + address.getAddressLine(1) + "," + address.getAddressLine(2));
                    }catch (Exception e){
                        edtAddress.setText("");
                    }
                }
            }
        });

        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(edtAddress.getText().toString().trim().length()>0){
                    deliveryAddress = edtAddress.getText().toString().trim();
                    chkDeliveryRequired.setChecked(true);
                    dialog.dismiss();
                }else{
                    edtAddress.setError(getString(R.string.validation_value_required));
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                chkDeliveryRequired.setChecked(false);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
