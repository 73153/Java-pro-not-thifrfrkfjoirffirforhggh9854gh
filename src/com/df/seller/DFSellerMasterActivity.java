package com.df.seller;

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

public abstract class DFSellerMasterActivity extends IjoomerSuperMaster {

	private IjoomerButton txtRequests;
	private IjoomerButton txtOffers;
	private IjoomerButton txtOrders;
	private IjoomerButton txtMyDishes;
	private IjoomerButton txtProfile;
	private IjoomerButton txtAccount;
	private IjoomerButton txtSettings;
	private IjoomerButton imgClose;
	private Dialog menuDialog;
	private LinearLayout lnrAnimate;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

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
                menuDialog = new Dialog(DFSellerMasterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                menuDialog.setContentView(R.layout.dfs_app_menu);

                txtRequests = (IjoomerButton) menuDialog.findViewById(R.id.txtRequests);
                txtOffers = (IjoomerButton) menuDialog.findViewById(R.id.txtOffers);
                txtOrders = (IjoomerButton) menuDialog.findViewById(R.id.txtOrders);
                txtMyDishes = (IjoomerButton) menuDialog.findViewById(R.id.txtMyDishes);
                txtProfile = (IjoomerButton) menuDialog.findViewById(R.id.txtProfile);
                txtAccount = (IjoomerButton) menuDialog.findViewById(R.id.txtAccount);
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

                txtRequests.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isSellerProfileComplete()){

                            if(IjoomerUtilities.isSellerPaymentDetailComplete()){
                                closeMenuDialog();
                                loadNew(DFSRequestListActivity.class, DFSellerMasterActivity.this, false);
                            }else{
                                IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_payment_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {
                                        closeMenuDialog();
                                        loadNew(DFSMyAccountActivity.class, DFSellerMasterActivity.this, false);
                                    }
                                });

                            }
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFSProfileActivity.class, DFSellerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });
                txtOffers.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isSellerProfileComplete()){

                            if(IjoomerUtilities.isSellerPaymentDetailComplete()){
                                closeMenuDialog();
                                loadNew(DFSOfferListActivity.class, DFSellerMasterActivity.this, false);
                            }else{
                                IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_payment_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {
                                        closeMenuDialog();
                                        loadNew(DFSMyAccountActivity.class, DFSellerMasterActivity.this, false);
                                    }
                                });

                            }
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFSProfileActivity.class, DFSellerMasterActivity.this, false);
                                }
                            });
                        }


                    }
                });
                txtOrders.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isSellerProfileComplete()){

                            if(IjoomerUtilities.isSellerPaymentDetailComplete()){
                                closeMenuDialog();
                                loadNew(DFSOrderListActivity.class, DFSellerMasterActivity.this, false);
                            }else{
                                IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_payment_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {
                                        closeMenuDialog();
                                        loadNew(DFSMyAccountActivity.class, DFSellerMasterActivity.this, false);
                                    }
                                });

                            }
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFSProfileActivity.class, DFSellerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });
                txtMyDishes.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        if(IjoomerUtilities.isSellerProfileComplete()){

                            if(IjoomerUtilities.isSellerPaymentDetailComplete()){
                                closeMenuDialog();
                                loadNew(DFSMyDishesListActivity.class, DFSellerMasterActivity.this, false);
                            }else{
                                IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_payment_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {
                                        closeMenuDialog();
                                        loadNew(DFSMyAccountActivity.class, DFSellerMasterActivity.this, false);
                                    }
                                });

                            }
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFSProfileActivity.class, DFSellerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });
                txtProfile.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        closeMenuDialog();
                        loadNew(DFSProfileActivity.class, DFSellerMasterActivity.this, false);
                    }
                });

                txtAccount.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        closeMenuDialog();
                        loadNew(DFSMyAccountActivity.class, DFSellerMasterActivity.this, false);
                    }
                });

                txtSettings.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if(IjoomerUtilities.isSellerProfileComplete()){

                            if(IjoomerUtilities.isSellerPaymentDetailComplete()){
                                closeMenuDialog();
                                loadNew(DFSAppSettingsActivity.class, DFSellerMasterActivity.this, false);
                            }else{
                                IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_payment_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {
                                        closeMenuDialog();
                                        loadNew(DFSMyAccountActivity.class, DFSellerMasterActivity.this, false);
                                    }
                                });

                            }
                        }else{
                            IjoomerUtilities.getDFInfoDialog(DFSellerMasterActivity.this,getString(R.string.df_complete_profile_detail), getString(R.string.ok), new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    closeMenuDialog();
                                    loadNew(DFSProfileActivity.class, DFSellerMasterActivity.this, false);
                                }
                            });
                        }

                    }
                });

                menuDialog.show();

                menuDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {

                        if (arg1 == KeyEvent.KEYCODE_BACK) {
                            // closeMenuDialog();
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
