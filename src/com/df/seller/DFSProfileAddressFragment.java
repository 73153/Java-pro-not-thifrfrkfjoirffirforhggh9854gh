package com.df.seller;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.smart.framework.SmartFragment;

import java.io.File;
import java.util.HashMap;

public class DFSProfileAddressFragment extends SmartFragment {

	private ImageView imgBusiness;

	private IjoomerButton imgAbout;
	private IjoomerButton imgDetails;
	private IjoomerButton imgAddress;

	private IjoomerEditText edtAddressLine1;
	private IjoomerEditText edtAddressLine2;
	private IjoomerEditText edtCity;
	private IjoomerEditText edtPostCode;

	private IjoomerButton btnSave;

	private HashMap<String, String> userDetail;

	private AQuery aQuery;

	public void setBusinessImagePreview(String selectedImage) {
		try {
            aQuery.id(imgBusiness).image(new File(selectedImage),((DFSProfileActivity) getActivity()).getDeviceWidth());
//			imgBusiness.setImageBitmap(BitmapFactory.decodeFile(selectedImage));
		} catch (Throwable e) {
		}
	}

	public DFSProfileAddressFragment(HashMap<String, String> userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_profile_fragment_address;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {

		aQuery = new AQuery(getActivity());

		imgBusiness = (ImageView) currentView.findViewById(R.id.imgBusiness);

		imgAbout = (IjoomerButton) currentView.findViewById(R.id.imgAbout);
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
				((DFSProfileActivity) getActivity()).goToProfileDetails();
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
		imgAddress.setBackgroundResource(R.drawable.dfs_profile_address_selected_btn);
		imgDetails.setBackgroundResource(R.drawable.dfs_profile_details_btn);
		imgAbout.setBackgroundResource(R.drawable.dfs_profile_about_btn);
		aQuery.id(imgBusiness).image(userDetail.get("image"), true, true, ((DFSProfileActivity) getActivity()).getDeviceWidth(), 0);
		edtAddressLine1.setText(userDetail.get("address1"));
		edtAddressLine2.setText(userDetail.get("address2"));
		edtCity.setText(userDetail.get("city"));
		edtPostCode.setText(userDetail.get("pincode"));

	}

}
