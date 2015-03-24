package com.df.seller;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.smart.framework.SmartFragment;

import java.io.File;
import java.util.HashMap;

public class DFSProfileDetailsFragment extends SmartFragment {

	private ImageView imgBusiness;

	private IjoomerButton imgAbout;
	private IjoomerButton imgDetails;
	private IjoomerButton imgAddress;

	private IjoomerEditText edtBusinessName;
	private IjoomerEditText edtEmail;
	private IjoomerEditText edtMobile;
	private IjoomerEditText edtNewPassword;
	private IjoomerEditText edtConfirmPassword;

	private IjoomerButton btnSave;

	private AQuery aQuery;

	private HashMap<String, String> userDetail;

	public DFSProfileDetailsFragment(HashMap<String, String> userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_profile_fragment_details;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	public void setBusinessImagePreview(String selectedImage) {
		try {
            aQuery.id(imgBusiness).image(new File(selectedImage),((DFSProfileActivity) getActivity()).getDeviceWidth());
//			imgBusiness.setImageBitmap(BitmapFactory.decodeFile(selectedImage));
		} catch (Throwable e) {
		}
	}

	@Override
	public void initComponents(View currentView) {

		aQuery = new AQuery(getActivity());
		imgBusiness = (ImageView) currentView.findViewById(R.id.imgBusiness);

		imgAbout = (IjoomerButton) currentView.findViewById(R.id.imgAbout);
		imgDetails = (IjoomerButton) currentView.findViewById(R.id.imgDetails);
		imgAddress = (IjoomerButton) currentView.findViewById(R.id.imgAddress);

		edtBusinessName = (IjoomerEditText) currentView.findViewById(R.id.edtBusinessName);
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

				if (edtBusinessName.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtBusinessName.setError(getString(R.string.validation_value_required));
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
					userDetail.put("restName", edtBusinessName.getText().toString().trim());
					userDetail.put("email", edtEmail.getText().toString().trim());
					userDetail.put("phone", edtMobile.getText().toString().trim());
					userDetail.put("password1", edtNewPassword.getText().toString().trim());
					userDetail.put("password2", edtConfirmPassword.getText().toString().trim());

					((DFSProfileActivity) getActivity()).saveBusinessDetail(userDetail);
				}
			}
		});

		imgBusiness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((DFSProfileActivity) getActivity()).changeBusinessImage();
			}
		});
		imgAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DFSProfileActivity) getActivity()).goToProfileAbout();
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
				((DFSProfileActivity) getActivity()).goToProfileAddress();
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void setDetails() {
		imgAddress.setBackgroundResource(R.drawable.dfs_profile_address_btn);
		imgDetails.setBackgroundResource(R.drawable.dfs_profile_details_selected_btn);
		imgAbout.setBackgroundResource(R.drawable.dfs_profile_about_btn);
		aQuery.id(imgBusiness).image(userDetail.get("image"), true, true, ((DFSProfileActivity) getActivity()).getDeviceWidth(), 0);
		edtBusinessName.setText(userDetail.get("restName"));
		edtEmail.setText(userDetail.get("email"));
		edtMobile.setText(userDetail.get("phone"));
		
	}
}
