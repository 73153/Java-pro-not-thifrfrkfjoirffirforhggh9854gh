package com.df.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;

import com.androidquery.AQuery;
import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.DFImageFragment;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFCDisheDetailsActivity extends DFCustomerMasterActivity {

	private IjoomerTextView txtDishName;
	private IjoomerTextView txtRestaurantName;
	private IjoomerTextView txtLikeCount;
	private IjoomerTextView txtDishDescription;
	private IjoomerButton imgLike;
	private IjoomerTextView txtCall;
	private IjoomerButton imgMakeOrder;
	private IjoomerTextView txtProfile;

    private ViewPager viewPager;
    private int count;

	private HashMap<String, String> IN_DISH_DETAIL;
	private AQuery aQuery;

	private DFCDataprovider dataprovider;
    private ImagePageAdapter adapter;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_dish_detail;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

		imgLike = (IjoomerButton) findViewById(R.id.imgLike);
		txtCall = (IjoomerTextView) findViewById(R.id.txtCall);
		imgMakeOrder = (IjoomerButton) findViewById(R.id.imgMakeOrder);
		txtProfile = (IjoomerTextView) findViewById(R.id.txtProfile);

		txtDishName = (IjoomerTextView) findViewById(R.id.txtDishName);
		txtRestaurantName = (IjoomerTextView) findViewById(R.id.txtRestaurantName);
		txtLikeCount = (IjoomerTextView) findViewById(R.id.txtLikeCount);
		txtDishDescription = (IjoomerTextView) findViewById(R.id.txtDishDescription);
        txtDishDescription.setMovementMethod(new ScrollingMovementMethod());
		aQuery = new AQuery(this);
		dataprovider = new DFCDataprovider(this);
		getIntentData();
	}

	@Override
	public void prepareViews() {
        adapter = new ImagePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
		setDetails();
	}

	@Override
	public void setActionListeners() {

		txtCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + IN_DISH_DETAIL.get("phone")));
					startActivity(intent);
				} catch (Exception e) {
				}
			}
		});

		imgLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (IN_DISH_DETAIL.get("likedBefore").equals("1")) {
					IjoomerUtilities.getDFInfoDialog(DFCDisheDetailsActivity.this, "You have already given this dish a thumb up", getString(R.string.ok), new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				} else if (IN_DISH_DETAIL.get("orderedBefore").equals("0")) {
					IjoomerUtilities.getDFInfoDialog(DFCDisheDetailsActivity.this, "Sorry, you can only give a dish the thumbs up if you have ordered it before",
							getString(R.string.ok), new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
				} else {
					getConfirmDialog("Like Dish", "Give this dish a thumbs up?", "Yes", "No", false, new AlertMagnatic() {

						@Override
						public void PositiveMethod(DialogInterface dialog, int id) {
							showProgressDialog("Sending Like Request...", DFCDisheDetailsActivity.this);
							dataprovider.likeDish(IN_DISH_DETAIL.get("dishID"), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {

								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									hideProgressDialog();
									if (responseCode == 200) {
										IjoomerUtilities.getDFInfoDialog(DFCDisheDetailsActivity.this, "Dish Liked Successfully.", getString(R.string.ok),
												new CustomAlertNeutral() {

													@Override
													public void NeutralMethod() {
                                                        txtLikeCount.setText(""+((Integer.parseInt(IN_DISH_DETAIL.get("likes"))+1)));
                                                        dataprovider.updateLikeCount(IN_DISH_DETAIL.get("dishID"),txtLikeCount.getText().toString());
                                                        IN_DISH_DETAIL.put("likedBefore","1");
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

			}
		});

		imgMakeOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(DFCWillingToPayActivity.class, DFCDisheDetailsActivity.this, false, "IN_DISH_DETAIL", IN_DISH_DETAIL);
				} catch (Exception e) {
				}
			}
		});

		txtProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(DFCBusinessProfileActivity.class, DFCDisheDetailsActivity.this, false, "IN_RESTID", IN_DISH_DETAIL.get("restID"));
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
            count = IN_DISH_DETAIL.get("image").length()>0 && IN_DISH_DETAIL.get("image2").length()>0 ? 2 : 1;
		} catch (Exception e) {
		}
	}

	private void setDetails() {

		txtDishName.setText(IN_DISH_DETAIL.get("dishName"));
		txtRestaurantName.setText(IN_DISH_DETAIL.get("restName"));
		txtLikeCount.setText(IN_DISH_DETAIL.get("likes"));

        txtDishDescription.setText(Html.fromHtml(IN_DISH_DETAIL.get("description").replace("\n","<br>")));
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
