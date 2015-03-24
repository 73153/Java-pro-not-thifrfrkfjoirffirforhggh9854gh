package com.df.seller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.DFImageFragment;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFSDisheDetailsActivity extends DFSellerMasterActivity {

	private IjoomerTextView txtDishName;
	private IjoomerTextView txtLikeCount;
	private IjoomerTextView txtDishDescription;
	private IjoomerButton imgEdit;
	private IjoomerButton imgImage;
	private IjoomerButton imgDelete;

    private ViewPager viewPager;
    private int count;

	private HashMap<String, String> IN_DISH_DETAIL;
	private static final int PICK_DISH_IMAGE = 5;
	private AQuery aQuery;

	private String selectedImagePath1 = "";
    private String selectedImagePath2 = "";

	private DFSDataprovider dataprovider;
    private ImagePageAdapter adapter;

    @Override
	public int setLayoutId() {
		return R.layout.dfs_dish_detail;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

		imgEdit = (IjoomerButton) findViewById(R.id.imgEdit);
		imgImage = (IjoomerButton) findViewById(R.id.imgImage);
		imgDelete = (IjoomerButton) findViewById(R.id.imgDelete);

		txtDishName = (IjoomerTextView) findViewById(R.id.txtDishName);
		txtLikeCount = (IjoomerTextView) findViewById(R.id.txtLikeCount);
		txtDishDescription = (IjoomerTextView) findViewById(R.id.txtDishDescription);
        txtDishDescription.setMovementMethod(new ScrollingMovementMethod());
		aQuery = new AQuery(this);
		dataprovider = new DFSDataprovider(this);

	}

	@Override
	public void prepareViews() {

	}

    @Override
    protected void onResume() {
        super.onResume();
        getIntentData();
        adapter = new ImagePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        setDetails();
    }

    @Override
	public void setActionListeners() {
		imgEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(DFSEditDishesStep1Activity.class, DFSDisheDetailsActivity.this, false, "IN_DISH_DETAIL", IN_DISH_DETAIL);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		imgImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(DFSDisheDetailsActivity.this, DFSEditDishesImageActivity.class);
                    intent.putExtra("IN_IMAGE1",IN_DISH_DETAIL.get("image"));
                    intent.putExtra("IN_IMAGE2",IN_DISH_DETAIL.get("image2"));
					startActivityForResult(intent, PICK_DISH_IMAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		imgDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

                getConfirmDialog("Delete Dish","Sure to delete this dish?","Yes","No",false,new AlertMagnatic() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        showProgressDialog(getString(R.string.df_sending_delete_request), DFSDisheDetailsActivity.this);
                        dataprovider.deleteDish(IN_DISH_DETAIL.get("dishID"), new WebCallListener() {

                            @Override
                            public void onProgressUpdate(int progressCount) {

                            }

                            @Override
                            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                hideProgressDialog();
                                if (responseCode == 200) {
                                    IjoomerUtilities.getDFInfoDialog(DFSDisheDetailsActivity.this, getString(R.string.df_dish_deleted_successfully), getString(R.string.ok),
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

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {

                    }
                });

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_DISH_IMAGE) {
				selectedImagePath1 = data.getStringExtra("IN_IMAGE_PATH1");
                selectedImagePath2 = data.getStringExtra("IN_IMAGE_PATH2");

                if(selectedImagePath1.length()>0 || selectedImagePath2.length()>0){

                    editDish();
                }
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {


		try {
            IN_DISH_DETAIL = dataprovider.getDishDetail(((HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL")).get("dishID")).get(0);
            count = IN_DISH_DETAIL.get("image").length()>0 && IN_DISH_DETAIL.get("image2").length()>0 ? 2 : 1;
			//IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
		} catch (Exception e) {
		}
	}

	private void setDetails() {

		txtDishName.setText(IN_DISH_DETAIL.get("dishName"));
		txtLikeCount.setText(IN_DISH_DETAIL.get("likes"));
		txtDishDescription.setText(Html.fromHtml(IN_DISH_DETAIL.get("description").replace("\n", "<br>")));
	}

	private void editDish() {
		showProgressDialog(getString(R.string.df_updating_dish_image), this);
		dataprovider.editDish(selectedImagePath1,selectedImagePath2, IN_DISH_DETAIL.get("dishID"), IjoomerApplicationConfiguration.getRestaurantId(), IN_DISH_DETAIL.get("catID"),
				IN_DISH_DETAIL.get("dishName"), IN_DISH_DETAIL.get("description"), IN_DISH_DETAIL.get("timeFrom"), IN_DISH_DETAIL.get("timeTo"), IN_DISH_DETAIL.get("price"), "1",
				new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						hideProgressDialog();
						if (responseCode == 200) {
							IjoomerUtilities.getDFInfoDialog(DFSDisheDetailsActivity.this, getString(R.string.df_dish_image_updated_successfully), getString(R.string.ok),
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

    private class ImagePageAdapter extends FragmentPagerAdapter {

        public ImagePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            if(pos==0){
                return new DFImageFragment(IN_DISH_DETAIL.get("image"));
            }else if(pos==1){
                return new DFImageFragment(IN_DISH_DETAIL.get("image2"));
            }else{
                return new DFImageFragment(IN_DISH_DETAIL.get("image"));
            }

        }

        @Override
        public int getCount() {
            return count;
        }

    }

}
