package com.df.customer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.df.src.R;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerButton;
import com.smart.framework.CustomAlertNeutral;

public abstract class DFCustomerMasterActivity extends IjoomerSuperMaster {

	private IjoomerButton txtHome;
	private IjoomerButton txtResponses;
	private IjoomerButton txtOrders;
	private IjoomerButton txtLoyalty;
	private IjoomerButton txtProfile;
	private IjoomerButton txtSettings;
	private IjoomerButton imgClose;
	private Dialog menuDialog;
	private LinearLayout lnrAnimate;

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	public void showMenuDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuDialog = new Dialog(DFCustomerMasterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                menuDialog.setContentView(R.layout.dfc_app_menu);

                txtHome = (IjoomerButton) menuDialog.findViewById(R.id.txtHome);
                txtResponses = (IjoomerButton) menuDialog.findViewById(R.id.txtResponses);
                txtOrders = (IjoomerButton) menuDialog.findViewById(R.id.txtOrders);
                txtLoyalty = (IjoomerButton) menuDialog.findViewById(R.id.txtLoyalty);
                txtProfile = (IjoomerButton) menuDialog.findViewById(R.id.txtProfile);
                txtSettings = (IjoomerButton) menuDialog.findViewById(R.id.txtSettings);
                imgClose = (IjoomerButton) menuDialog.findViewById(R.id.imgClose);

                lnrAnimate = (LinearLayout) menuDialog.findViewById(R.id.lnrAnimate);

                try{
                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_dialog);
                    lnrAnimate.startAnimation(slide);
                }catch (Throwable e){
                    e.printStackTrace();
                }


                imgClose.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        closeMenuDialog();
                    }
                });

                txtHome.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isCustomerProfileComplete()){
                            closeMenuDialog();
                            loadNew(DFCPlaceOrderActivity.class, DFCustomerMasterActivity.this, false);
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFCustomerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFCRewardPointsActivity.class, DFCustomerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });
                txtResponses.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isCustomerProfileComplete()){
                            closeMenuDialog();
                            loadNew(DFCResponsesMapActivity.class, DFCustomerMasterActivity.this, false);
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFCustomerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFCRewardPointsActivity.class, DFCustomerMasterActivity.this, false);
                                }
                            });
                        }


                    }
                });
                txtOrders.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if(IjoomerUtilities.isCustomerProfileComplete()){
                            closeMenuDialog();
                            loadNew(DFCOrderListActivity.class, DFCustomerMasterActivity.this, false);
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFCustomerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFCRewardPointsActivity.class, DFCustomerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });
                txtLoyalty.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isCustomerProfileComplete()){
                            closeMenuDialog();
                            loadNew(DFCRewardPointsActivity.class, DFCustomerMasterActivity.this, false);
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFCustomerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFCRewardPointsActivity.class, DFCustomerMasterActivity.this, false);
                                }
                            });
                        }


                    }
                });
                txtProfile.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        closeMenuDialog();
                        loadNew(DFCProfileActivity.class, DFCustomerMasterActivity.this, false);
                    }
                });
                txtSettings.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if(IjoomerUtilities.isCustomerProfileComplete()){
                            closeMenuDialog();
                            loadNew(DFCAppSettingsActivity.class, DFCustomerMasterActivity.this, false);
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFCustomerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFCProfileActivity.class, DFCustomerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });

                menuDialog.show();

                menuDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {

                        if (arg1== KeyEvent.KEYCODE_BACK) {
//					closeMenuDialog();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });


	}

	public void closeMenuDialog() {
		if (menuDialog != null && menuDialog.isShowing()) {
			Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_down);
			lnrAnimate.startAnimation(slide);
			slide.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					menuDialog.dismiss();
				}
			});

		}
	}

	public void selectImageDialog(final SelectImageDialogListner target) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.df_select_image_dialog);
		final IjoomerButton imgTakePhoto = (IjoomerButton) dialog.findViewById(R.id.imgTakePhoto);
		final IjoomerButton imgUploadPhoto = (IjoomerButton) dialog.findViewById(R.id.imgUploadPhoto);
		imgTakePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				target.onCapture();
				dialog.dismiss();

			}
		});
		imgUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				target.onPhoneGallery();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
