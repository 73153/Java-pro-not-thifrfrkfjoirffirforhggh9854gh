package com.df.seller;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;

public class DFSEditDishesStep2Activity extends DFSellerMasterActivity {

	private IjoomerButton imgTakePhoto;
	private IjoomerButton imgUploadPhoto;

	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;

	private String selectedImagePath = "";

	@Override
	public int setLayoutId() {
		return R.layout.dfs_edit_dish_step2;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgTakePhoto = (IjoomerButton) findViewById(R.id.imgTakePhoto);
		imgUploadPhoto = (IjoomerButton) findViewById(R.id.imgUploadPhoto);

	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {

		imgTakePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
				startActivityForResult(intent, CAPTURE_IMAGE);
			}
		});
		imgUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == PICK_IMAGE) {
				selectedImagePath = getAbsolutePath(data.getData());
				returnDataToCallingActivity(selectedImagePath);
			} else if (requestCode == CAPTURE_IMAGE) {
				selectedImagePath = getImagePath();
				returnDataToCallingActivity(selectedImagePath);
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}

	}

	/**
	 * Class Methods
	 */

	private void returnDataToCallingActivity(String selectedImagePath) {
		Intent i = new Intent();
		i.putExtra("IN_IMAGE_PATH", selectedImagePath);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

}
