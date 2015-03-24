package com.df.customer;

import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.smart.framework.SmartFragment;

import java.util.HashMap;

public class DFCProfileDetailsFragment extends SmartFragment {

//	private ImageView imgCustomer;

	private IjoomerButton imgStats;
	private IjoomerButton imgDetails;
	private IjoomerButton imgAddress;

	private IjoomerEditText edtUserName;
	private IjoomerEditText edtEmail;
	private IjoomerEditText edtMobile;
	private IjoomerEditText edtNewPassword;
	private IjoomerEditText edtConfirmPassword;

	private IjoomerButton btnSave;

	private AQuery aQuery;

	private HashMap<String, String> userDetail;

	public DFCProfileDetailsFragment(HashMap<String, String> userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfc_profile_fragment_details;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	public void setUseImagePreview(String selectedImage) {
		try {
//            aQuery.id(imgCustomer).image(new File(selectedImage),((DFCProfileActivity) getActivity()).getDeviceWidth());
//			imgCustomer.setImageBitmap(BitmapFactory.decodeFile(selectedImage));
		} catch (Throwable e) {
		}
	}

	@Override
	public void initComponents(View currentView) {

		aQuery = new AQuery(getActivity());
//		imgCustomer = (ImageView) currentView.findViewById(R.id.imgCustomer);

		imgStats = (IjoomerButton) currentView.findViewById(R.id.imgStats);
		imgDetails = (IjoomerButton) currentView.findViewById(R.id.imgDetails);
		imgAddress = (IjoomerButton) currentView.findViewById(R.id.imgAddress);

		edtUserName = (IjoomerEditText) currentView.findViewById(R.id.edtUserName);
		edtEmail = (IjoomerEditText) currentView.findViewById(R.id.edtEmail);
		edtMobile = (IjoomerEditText) currentView.findViewById(R.id.edtMobile);
		edtNewPassword = (IjoomerEditText) currentView.findViewById(R.id.edtNewPassword);
		edtConfirmPassword = (IjoomerEditText) currentView.findViewById(R.id.edtConfirmPassword);

		btnSave = (IjoomerButton) currentView.findViewById(R.id.btnSave);
	}

	@Override
	public void prepareViews(View currentView) {
		setDetails();
	}

	@Override
	public void setActionListeners(View currentView) {
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean validationFlag = true;

				if (edtUserName.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtUserName.setError(getString(R.string.validation_value_required));
				}
				if (edtEmail.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtEmail.setError(getString(R.string.validation_value_required));
				} else {
					if (!IjoomerUtilities.emailValidator(edtEmail.getText().toString().trim())) {
						validationFlag = false;
						edtEmail.setError(getString(R.string.validation_invalid_email));
					}
				}
				if (edtMobile.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtMobile.setError(getString(R.string.validation_value_required));
				}
				if (edtNewPassword.getText().toString().trim().length() > 0) {
					if (edtConfirmPassword.getText().toString().trim().length() <= 0) {
						validationFlag = false;
						edtConfirmPassword.setError(getString(R.string.validation_value_required));
					} else {
						if (!edtConfirmPassword.getText().toString().trim().equals(edtNewPassword.getText().toString().trim())) {
							validationFlag = false;
							edtConfirmPassword.setError(getString(R.string.validation_passwod_not_match));
						}
					}
				}

				if (edtConfirmPassword.getText().toString().trim().length() > 0) {
					if (edtNewPassword.getText().toString().trim().length() <= 0) {
						validationFlag = false;
						edtNewPassword.setError(getString(R.string.validation_value_required));
					} else {
						if (!edtConfirmPassword.getText().toString().trim().equals(edtNewPassword.getText().toString().trim())) {
							validationFlag = false;
							edtConfirmPassword.setError(getString(R.string.validation_passwod_not_match));
						}
					}
				}

				if (validationFlag) {
					userDetail.put("userName", edtUserName.getText().toString().trim());
					userDetail.put("email", edtEmail.getText().toString().trim());
					userDetail.put("mobile", edtMobile.getText().toString().trim());
					userDetail.put("password1", edtNewPassword.getText().toString().trim());
					userDetail.put("password2", edtConfirmPassword.getText().toString().trim());

					((DFCProfileActivity) getActivity()).saveUserDetail(userDetail);
				}
			}
		});

//		imgCustomer.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((DFCProfileActivity) getActivity()).changeUserImage();
//			}
//		});
		imgStats.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DFCProfileActivity) getActivity()).goToProfileStats();
			}
		});

		imgDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		imgAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((DFCProfileActivity) getActivity()).goToProfileAddress();
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void setDetails() {
		imgDetails.setBackgroundResource(R.drawable.df_profile_details_selected_btn);
//		aQuery.id(imgCustomer).image(userDetail.get("avatar"), true, true, ((DFCProfileActivity) getActivity()).getDeviceWidth(), 0);
		edtUserName.setText(userDetail.get("userName"));
		edtEmail.setText(userDetail.get("email"));
		edtMobile.setText(userDetail.get("mobile"));
	}
}
