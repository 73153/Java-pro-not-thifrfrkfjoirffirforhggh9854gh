package com.df.customer;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFCProfileActivity extends DFCustomerMasterActivity {

	private DFCDataprovider dataprovider;
	private HashMap<String, String> userDetail;

	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;

	private String selectedImagePath = null;

	private DFCProfileAddressFragment profileAddressFragment;
	private DFCProfileDetailsFragment profileDetailsFragment;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_profile;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {
        getUserDetails();
	}

	@Override
	public void setActionListeners() {

	}


    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == PICK_IMAGE) {
				selectedImagePath = getAbsolutePath(data.getData());
			} else if (requestCode == CAPTURE_IMAGE) {
				selectedImagePath = getImagePath();
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
			if (selectedImagePath != null) {
				if (profileAddressFragment != null) {
					profileAddressFragment.setUseImagePreview(selectedImagePath);
				}
				if (profileDetailsFragment != null) {
					profileDetailsFragment.setUseImagePreview(selectedImagePath);
				}
			}
		}

	}

	/**
	 * Class Methods
	 */

	private void getUserDetails() {
		showProgressDialog(getString(R.string.df_loading_profile), this);
		dataprovider.getUserDetail(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					userDetail = data1.get(0);
                    if(IjoomerUtilities.isCustomerProfileComplete()){
                        goToProfileStats();
                    }else{
                        goToProfileAddress();
                    }
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

	public void goToProfileStats() {
		profileAddressFragment = null;
		profileDetailsFragment = null;
		addFragment(R.id.lnrFragment, new DFCProfileStatsFragment(userDetail));
	}

	public void goToProfileDetails() {
		profileDetailsFragment = new DFCProfileDetailsFragment(userDetail);
		addFragment(R.id.lnrFragment, profileDetailsFragment);
		profileAddressFragment = null;
	}

	public void goToProfileAddress() {
		profileAddressFragment = new DFCProfileAddressFragment(userDetail);
		addFragment(R.id.lnrFragment, profileAddressFragment);
		profileDetailsFragment = null;
	}

	public void saveUserDetail(HashMap<String, String> data) {

		if (data.containsKey("password1")) {
			showProgressDialog(getString(R.string.df_updating_profile), this);
			dataprovider.saveUserDetails(selectedImagePath, data.get("userName"), data.get("email"), data.get("mobile"), data.get("address1"), data.get("address2"),
					data.get("city"), data.get("pincode"), data.get("password1"), data.get("password2"), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							hideProgressDialog();
							if (responseCode == 200) {
								selectedImagePath = null;
								IjoomerUtilities.getDFInfoDialog(DFCProfileActivity.this, getString(R.string.df_profile_updated_successfully), getString(R.string.ok),
										new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {

											}
										});

								userDetail = data1.get(0);

                                if(userDetail.get("address1").toString().trim().length()>0 && userDetail.get("city").toString().trim().length()>0 && userDetail.get("pincode").toString().trim().length()>0){
                                    getSmartApplication().writeSharedPreferences(SP_DFC_PROFILE_COMPLETE,"1");
                                }
                                getSmartApplication().writeSharedPreferences(SP_DF_ADDRESS,userDetail.get("address1").toString().trim() + "\n" + userDetail.get("address2").toString().trim() + "\n" + userDetail.get("city").toString().trim() + "\n" +userDetail.get("pincode").toString().trim());
							} else {
								responseMessageHandler(responseCode, false);
							}
						}
					});
		} else {
			showProgressDialog(getString(R.string.df_updating_profile), this);
			dataprovider.saveUserDetails(selectedImagePath, data.get("userName"), data.get("email"), data.get("mobile"), data.get("address1"), data.get("address2"),
					data.get("city"), data.get("pincode"), null, null, new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							hideProgressDialog();
							if (responseCode == 200) {

								selectedImagePath = null;
								IjoomerUtilities.getDFInfoDialog(DFCProfileActivity.this, getString(R.string.df_profile_updated_successfully), getString(R.string.ok),
										new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {

											}
										});

								userDetail = data1.get(0);

                                if(userDetail.get("address1").toString().trim().length()>0 && userDetail.get("city").toString().trim().length()>0 && userDetail.get("pincode").toString().trim().length()>0){
                                    getSmartApplication().writeSharedPreferences(SP_DFC_PROFILE_COMPLETE,"1");
                                }
                                getSmartApplication().writeSharedPreferences(SP_DF_ADDRESS,userDetail.get("address1").toString().trim() + "\n" + userDetail.get("address2").toString().trim() + "\n" + userDetail.get("city").toString().trim() + "\n" +userDetail.get("pincode").toString().trim());

							} else {
								responseMessageHandler(responseCode, false);
							}
						}
					});
		}

	}

	public void changeUserImage() {
		selectImageDialog(new SelectImageDialogListner() {

			@Override
			public void onPhoneGallery() {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
			}

			@Override
			public void onCapture() {
				final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
				startActivityForResult(intent, CAPTURE_IMAGE);
			}
		});
	}

}
