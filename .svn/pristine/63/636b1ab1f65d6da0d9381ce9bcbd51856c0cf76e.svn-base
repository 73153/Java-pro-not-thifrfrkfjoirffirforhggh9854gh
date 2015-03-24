package com.df.seller;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker;

import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DFSAddDishesSetp1Activity extends DFSellerMasterActivity {

	private IjoomerEditText edtDishName;
	private IjoomerEditText edtDishCatagory;
	private IjoomerEditText edtDishFrom;
	private IjoomerEditText edtDishTo;
	private IjoomerEditText edtDishAbout;
	private IjoomerButton imgNext;

	private IjoomerCheckBox chkAvailableAllTime;

	private String selectedImagePath1 = "";
    private String selectedImagePath2 = "";

	private static final int PICK_CATAGORY = 1;

	private static final int PICK_DISH_IMAGE = 5;

	private DFSDataprovider dataprovider;

	private String IN_CATNAME;
	private String IN_CATID;

	private int hour;
	private int minute;

	@Override
	public int setLayoutId() {
		return R.layout.dfs_add_dish_step1;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		chkAvailableAllTime = (IjoomerCheckBox) findViewById(R.id.chkAvailableAllTime);
		edtDishName = (IjoomerEditText) findViewById(R.id.edtDishName);
		edtDishCatagory = (IjoomerEditText) findViewById(R.id.edtDishCatagory);
		edtDishFrom = (IjoomerEditText) findViewById(R.id.edtDishFrom);
		edtDishTo = (IjoomerEditText) findViewById(R.id.edtDishTo);
		edtDishAbout = (IjoomerEditText) findViewById(R.id.edtDishAbout);
		imgNext = (IjoomerButton) findViewById(R.id.imgNext);
		dataprovider = new DFSDataprovider(this);
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {
		chkAvailableAllTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					edtDishFrom.setVisibility(View.GONE);
					edtDishTo.setVisibility(View.GONE);
					edtDishFrom.setText("00:00");
					edtDishTo.setText("23:59");
				} else {
					edtDishFrom.setVisibility(View.VISIBLE);
					edtDishTo.setVisibility(View.VISIBLE);
					edtDishFrom.setText("");
					edtDishTo.setText("");
				}
			}
		});

		edtDishCatagory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DFSAddDishesSetp1Activity.this, DFSChooseCuisineActivity.class);
				intent.putExtra("IN_CATID", IN_CATID);
				startActivityForResult(intent, PICK_CATAGORY);
			}
		});

		edtDishFrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					hour = Integer.parseInt(edtDishFrom.getText().toString().trim().split(":")[0]);
					minute = Integer.parseInt(edtDishFrom.getText().toString().trim().split(":")[1]);
				} catch (Exception e) {
					Calendar date = Calendar.getInstance();
					hour = date.get(Calendar.HOUR);
					minute = date.get(Calendar.MINUTE);
				}
				getTimeDialog(hour, minute, new CustomClickListner() {

					@Override
					public void onClick(String value) {
						edtDishFrom.setText(value);
					}
				});
			}
		});
		edtDishTo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					hour = Integer.parseInt(edtDishTo.getText().toString().trim().split(":")[0]);
					minute = Integer.parseInt(edtDishTo.getText().toString().trim().split(":")[1]);
				} catch (Exception e) {
					Calendar date = Calendar.getInstance();
					hour = date.get(Calendar.HOUR);
					minute = date.get(Calendar.MINUTE);
				}
				getTimeDialog(hour, minute, new CustomClickListner() {

					@Override
					public void onClick(String value) {
						edtDishTo.setText(value);
					}
				});
			}
		});
		imgNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				boolean validationFlag = true;

				if (edtDishName.getText().toString().trim().length() <=0) {
					edtDishName.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtDishCatagory.getText().toString().trim().length() <=0) {
					edtDishCatagory.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtDishFrom.getText().toString().trim().length() <= 0) {
					edtDishFrom.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtDishTo.getText().toString().trim().length() <= 0) {
					edtDishTo.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtDishAbout.getText().toString().trim().length() <= 0) {
					edtDishAbout.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}

				if (validationFlag) {
					try {
						Intent intent = new Intent(DFSAddDishesSetp1Activity.this, DFSAddDishesImageActivity.class);
						startActivityForResult(intent, PICK_DISH_IMAGE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_CATAGORY) {
				IN_CATNAME = data.getStringExtra("IN_CATNAME");
				IN_CATID = data.getStringExtra("IN_CATID");
				edtDishCatagory.setText(IN_CATNAME);
			} else if (requestCode == PICK_DISH_IMAGE) {
                try{
                    selectedImagePath1 = data.getStringExtra("IN_IMAGE_PATH1");
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    selectedImagePath2 = data.getStringExtra("IN_IMAGE_PATH2");
                }catch (Exception e){
                    e.printStackTrace();
                }

				addDish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Class Methods
	 */

	public void getTimeDialog(final int hour, final int minute, final CustomClickListner target) {

		TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Calendar date = Calendar.getInstance();
				date.set(Calendar.HOUR_OF_DAY, hourOfDay);
				date.set(Calendar.MINUTE, minute);
				String dateString = (("" + hourOfDay).length() == 2 ? ("" + hourOfDay) : ("0" + hourOfDay)) + ":" + (("" + minute).length() == 2 ? ("" + minute) : ("0" + minute));
				target.onClick(dateString);
			}
		}, hour, minute, true);

		timeDialog.show();

	}

	private void addDish() {
		showProgressDialog(getString(R.string.df_uploading_new_dish), this);
		dataprovider.addDish(selectedImagePath1,selectedImagePath2, IjoomerApplicationConfiguration.getRestaurantId(), IN_CATID, edtDishName.getText().toString(), edtDishAbout.getText().toString(),
				edtDishFrom.getText().toString(), edtDishTo.getText().toString(), "0", "1", new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						hideProgressDialog();
						if (responseCode == 200) {
							IjoomerUtilities.getDFInfoDialog(DFSAddDishesSetp1Activity.this, getString(R.string.df_dish_sdded_successfully), getString(R.string.ok),
									new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
                                            finish();
										}
									});


						} else {
							responseMessageHandler(responseCode, false);
						}
					}
				});
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}
}
