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

public class DFSProfileAboutFragment extends SmartFragment {

	private ImageView imgBusiness;

	private IjoomerButton imgAbout;
	private IjoomerButton imgDetails;
	private IjoomerButton imgAddress;

	private IjoomerEditText edtAbout;

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

	public DFSProfileAboutFragment(HashMap<String, String> userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_profile_fragment_about;
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

		edtAbout = (IjoomerEditText) currentView.findViewById(R.id.edtAbout);

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

                if(edtAbout.getText().toString().trim().length()<=0){
                    edtAbout.setError(getString(R.string.validation_value_required));
                }else{
                    userDetail.put("description", edtAbout.getText().toString());

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
				((DFSProfileActivity) getActivity()).goToProfileAddress();
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void setDetails() {
		imgAddress.setBackgroundResource(R.drawable.dfs_profile_address_btn);
		imgDetails.setBackgroundResource(R.drawable.dfs_profile_details_btn);
		imgAbout.setBackgroundResource(R.drawable.dfs_profile_about_selected_btn);
		aQuery.id(imgBusiness).image(userDetail.get("image"), true, true, ((DFSProfileActivity) getActivity()).getDeviceWidth(), 0);
		edtAbout.setText(userDetail.get("description"));
	}

}
