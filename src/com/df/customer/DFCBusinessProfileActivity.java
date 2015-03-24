package com.df.customer;

import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.androidquery.AQuery;
import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

public class DFCBusinessProfileActivity extends DFCustomerMasterActivity {

	private ImageView imgRestaurant;
	private LinearLayout lnrBusinessAbout;
	private IjoomerTextView txtRestaurantNameAbt;
	private IjoomerButton imgLike;
	private IjoomerTextView txtLikeCount;
	private IjoomerTextView txtDescription;

	private LinearLayout lnrBusinessAddress;
	private IjoomerTextView txtRestaurantNameAddr;
	private IjoomerTextView txtAddress1;
	private IjoomerTextView txtAddress2;
	private IjoomerTextView txtCity;
	private IjoomerTextView txtPhoneNo;
	private IjoomerTextView txtEmail;
	private IjoomerTextView txtWebsite;

	private IjoomerButton imgAbout;
	private IjoomerButton imgAddress;
	private IjoomerButton imgDishes;

	private ListView lstDishes;
	private ScrollView scrollAddressAbout;
	private LinearLayout lnrDish;

	private ArrayList<SmartListItem> listDataDishes = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder dishListAdapterWithHolder;

	private int ABOUT = 1;
	private int ADDRESS = 2;
	private int DISHES = 3;
	private int CURRENT = ABOUT;

	private String IN_RESTID;
	private HashMap<String, String> restaurantDetail;

	private DFCDataprovider dataprovider;
	private AQuery aQuery;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_business_profile;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lnrDish = (LinearLayout) findViewById(R.id.lnrDish);
		lstDishes = (ListView) findViewById(R.id.lstDishes);
		scrollAddressAbout = (ScrollView) findViewById(R.id.scrollAddressAbout);
		imgRestaurant = (ImageView) findViewById(R.id.imgRestaurant);
		lnrBusinessAbout = (LinearLayout) findViewById(R.id.lnrBusinessAbout);
		txtRestaurantNameAbt = (IjoomerTextView) findViewById(R.id.txtRestaurantNameAbt);
		imgLike = (IjoomerButton) findViewById(R.id.imgLike);
		txtLikeCount = (IjoomerTextView) findViewById(R.id.txtLikeCount);
		txtDescription = (IjoomerTextView) findViewById(R.id.txtDescription);

		lnrBusinessAddress = (LinearLayout) findViewById(R.id.lnrBusinessAddress);
		txtRestaurantNameAddr = (IjoomerTextView) findViewById(R.id.txtRestaurantNameAddr);
		txtAddress1 = (IjoomerTextView) findViewById(R.id.txtAddress1);
		txtAddress2 = (IjoomerTextView) findViewById(R.id.txtAddress2);
		txtCity = (IjoomerTextView) findViewById(R.id.txtCity);
		txtPhoneNo = (IjoomerTextView) findViewById(R.id.txtPhoneNo);
		txtEmail = (IjoomerTextView) findViewById(R.id.txtEmail);
		txtWebsite = (IjoomerTextView) findViewById(R.id.txtWebsite);

		imgAbout = (IjoomerButton) findViewById(R.id.imgAbout);
		imgAddress = (IjoomerButton) findViewById(R.id.imgAddress);
		imgDishes = (IjoomerButton) findViewById(R.id.imgDishes);

		dataprovider = new DFCDataprovider(this);
		aQuery = new AQuery(this);
	}

	@Override
	public void prepareViews() {
		getIntentData();
		getRestaurantDetail();
	}

	@Override
	public void setActionListeners() {

		imgAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (CURRENT != ABOUT) {
					showAbout();
				}
			}
		});

		imgAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CURRENT != ADDRESS) {
					showAddress();
				}
			}
		});

		imgDishes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CURRENT != DISHES) {
					showDishes();
				}
				// try {
				// loadNew(DFCBrowseDishesActivity.class,
				// DFCBusinessProfileActivity.this, false, "IN_RESTID",
				// IN_RESTID);
				// } catch (Exception e) {
				// }
			}
		});

		imgLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (restaurantDetail.get("likedBefore").equals("1")) {
					IjoomerUtilities.getDFInfoDialog(DFCBusinessProfileActivity.this, "You have already given this restaurant a thumb up", getString(R.string.ok),
							new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
				} else if (restaurantDetail.get("orderedBefore").equals("0")) {
					IjoomerUtilities.getDFInfoDialog(DFCBusinessProfileActivity.this, "Sorry, you can only give a restaurant the thumbs up if you have ordered it before",
							getString(R.string.ok), new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
				} else {
					getConfirmDialog("Like Restaurant", "Give this restaurant a thumbs up?", "Yes", "No", false, new AlertMagnatic() {

						@Override
						public void PositiveMethod(DialogInterface dialog, int id) {

							showProgressDialog(getString(R.string.df_sending_like_request), DFCBusinessProfileActivity.this);
							dataprovider.likeRestaurant(restaurantDetail.get("restID"), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {

								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									hideProgressDialog();
									if (responseCode == 200) {
										IjoomerUtilities.getDFInfoDialog(DFCBusinessProfileActivity.this, getString(R.string.df_restaurant_liked_uccessfully),
												getString(R.string.ok), new CustomAlertNeutral() {

													@Override
													public void NeutralMethod() {
                                                        restaurantDetail.put("likedBefore","1");
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

	}

	/**
	 * Class Methods
	 */

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

	private void getIntentData() {
		try {
			IN_RESTID = getIntent().getStringExtra("IN_RESTID");
		} catch (Exception e) {
		}
	}

	private void getRestaurantDetail() {
		showProgressDialog(getString(R.string.df_loading_restaurant_details), this);
		dataprovider.getRestaurantDetail(IN_RESTID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					if (data1 != null) {
						restaurantDetail = data1.get(0);
						if (CURRENT == ABOUT) {
							showAbout();
						} else if (CURRENT == ADDRESS) {
							showAddress();
						} else if (CURRENT == DISHES) {
							showDishes();
						}
						aQuery.id(imgRestaurant).image(restaurantDetail.get("image"), true, true, getDeviceWidth(), 0);
					} else {

					}
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	private void showAbout() {
		CURRENT = ABOUT;
		scrollAddressAbout.setVisibility(View.VISIBLE);
		lnrDish.setVisibility(View.GONE);
		lnrBusinessAbout.setVisibility(View.VISIBLE);
		lnrBusinessAddress.setVisibility(View.GONE);
		imgAbout.setBackgroundResource(R.drawable.df_business_about_selected_img);
		imgAddress.setBackgroundResource(R.drawable.df_business_address_img);
		imgDishes.setBackgroundResource(R.drawable.df_business_dish_img);

		txtRestaurantNameAbt.setText(restaurantDetail.get("restName"));
		txtLikeCount.setText(restaurantDetail.get("likes"));
//		txtDescription.setText(Html.fromHtml(restaurantDetail.get("description")));
        txtDescription.setText(Html.fromHtml(restaurantDetail.get("description").replace("\n","<br>")));

	}

	private void showAddress() {
		CURRENT = ADDRESS;
		scrollAddressAbout.setVisibility(View.VISIBLE);
		lnrDish.setVisibility(View.GONE);
		lnrBusinessAbout.setVisibility(View.GONE);
		lnrBusinessAddress.setVisibility(View.VISIBLE);
		imgAbout.setBackgroundResource(R.drawable.df_business_about_img);
		imgAddress.setBackgroundResource(R.drawable.df_business_address_selected_img);
		imgDishes.setBackgroundResource(R.drawable.df_business_dish_img);

		txtRestaurantNameAddr.setText(restaurantDetail.get("restName"));
		txtAddress1.setText(restaurantDetail.get("address1"));
		txtAddress2.setText(restaurantDetail.get("address2"));
		txtCity.setText(restaurantDetail.get("city"));
		txtPhoneNo.setText(restaurantDetail.get("phone"));
		txtEmail.setText(restaurantDetail.get("email"));
		txtWebsite.setText(restaurantDetail.get("website"));

	}

	private void showDishes() {
		CURRENT = DISHES;
		scrollAddressAbout.setVisibility(View.GONE);
		lnrDish.setVisibility(View.VISIBLE);
		lnrBusinessAbout.setVisibility(View.GONE);
		lnrBusinessAddress.setVisibility(View.VISIBLE);
		imgAbout.setBackgroundResource(R.drawable.df_business_about_img);
		imgAddress.setBackgroundResource(R.drawable.df_business_address_img);
		imgDishes.setBackgroundResource(R.drawable.df_business_dish_selected_img);

		getDishesList();
	}

	public void getDishesList() {
		showProgressDialog(getString(R.string.df_loading_dishes), this);
		dataprovider.getRestaurantDishesList(IN_RESTID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					prepareList(data1);
					dishListAdapterWithHolder = getListAdapter();
					lstDishes.setAdapter(dishListAdapterWithHolder);
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});

	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataDishes.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfs_dish_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(data.get(i));
				item.setValues(obj);
				listDataDishes.add(item);
			}
		}
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfs_dish_list_item, listDataDishes, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {


                holder.txtDishName = (IjoomerTextView) v.findViewById(R.id.txtDishName);
                holder.imgDish = (ImageView) v.findViewById(R.id.imgDish);
                holder.txtLikeCount=(IjoomerTextView) v.findViewById(R.id.txtLikeCount);


                final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
                holder.txtDishName.setText(row.get("dishName"));
                holder.txtLikeCount.setText(row.get("likes"));

                aQuery.id(holder.imgDish).image(row.get("image"), true, true, getDeviceWidth(), 0);

                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            loadNew(DFCDisheDetailsActivity.class, DFCBusinessProfileActivity.this, false, "IN_DISH_DETAIL", row);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return v;

			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

		});
		return listAdapterWithHolder;
	}

}
