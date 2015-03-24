package com.df.customer;

import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.smart.framework.SmartFragment;

import java.util.HashMap;

public class DFCProfileAddressFragment extends SmartFragment {

//	private ImageView imgCustomer;

	private IjoomerButton imgStats;
	private IjoomerButton imgDetails;
	private IjoomerButton imgAddress;

	private IjoomerEditText edtAddressLine1;
	private IjoomerEditText edtAddressLine2;
	private IjoomerEditText edtCity;
	private IjoomerEditText edtPostCode;

	private IjoomerButton btnSave;

	private HashMap<String, String> userDetail;

	private AQuery aQuery;

	public void setUseImagePreview(String selectedImage) {
		try {
//            aQuery.id(imgCustomer).image(new File(selectedImage),((DFCProfileActivity) getActivity()).getDeviceWidth());
//			imgCustomer.setImageBitmap(BitmapFactory.decodeFile(selectedImage));
		} catch (Throwable e) {
		}
	}

	public DFCProfileAddressFragment(HashMap<String, String> userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfc_profile_fragment_address;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {

		aQuery = new AQuery(getActivity());

//		imgCustomer = (ImageView) currentView.findViewById(R.id.imgCustomer);

		imgStats = (IjoomerButton) currentView.findViewById(R.id.imgStats);
		imgDetails = (IjoomerButton) currentView.findViewById(R.id.imgDetails);
		imgAddress = (IjoomerButton) currentView.findViewById(R.id.imgAddress);

		edtAddressLine1 = (IjoomerEditText) currentView.findViewById(R.id.edtAddressLine1);
		edtAddressLine2 = (IjoomerEditText) currentView.findViewById(R.id.edtAddressLine2);
		edtCity = (IjoomerEditText) currentView.findViewById(R.id.edtCity);
		edtPostCode = (IjoomerEditText) currentView.findViewById(R.id.edtPostCode);

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

                boolean validation=true;

                if(edtAddressLine1.getText().toString().trim().length()<=0){
                    validation=false;
                    edtAddressLine1.setError(getString(R.string.validation_value_required));
                }

                if(edtCity.getText().toString().trim().length()<=0){
                    validation=false;
                    edtCity.setError(getString(R.string.validation_value_required));
                }
                if(edtPostCode.getText().toString().trim().length()<=0){
                    validation=false;
                    edtPostCode.setError(getString(R.string.validation_value_required));
                }

                if(validation){
                    edtAddressLine1.setError(null);
                    edtCity.setError(null);
                    edtPostCode.setError(null);

                    userDetail.put("address1", edtAddressLine1.getText().toString());
                    userDetail.put("address2", edtAddressLine2.getText().toString());
                    userDetail.put("city", edtCity.getText().toString());
                    userDetail.put("pincode", edtPostCode.getText().toString());

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
				((DFCProfileActivity) getActivity()).goToProfileDetails();
			}
		});

		imgAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	/**
	 * Class Methods
	 */

	private void setDetails() {
		imgAddress.setBackgroundResource(R.drawable.df_profile_adress_selected_btn);
//		aQuery.id(imgCustomer).image(userDetail.get("avatar"), true, true, ((DFCProfileActivity) getActivity()).getDeviceWidth(), 0);
		edtAddressLine1.setText(userDetail.get("address1"));
		edtAddressLine2.setText(userDetail.get("address2"));
		edtCity.setText(userDetail.get("city"));
		edtPostCode.setText(userDetail.get("pincode"));
	}

}
