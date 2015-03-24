package com.df.src;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.df.customer.DFCPlaceOrderActivity;
import com.df.customer.data.provider.DFCDataprovider;
import com.df.seller.DFSMyAccountActivity;
import com.df.seller.data.provider.DFSDataprovider;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerRegistrationMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.oauth.IjoomerRegistration;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This Class Contains All Method Related To IjoomerRegistrationStep2Activity.
 * 
 * @author tasol
 * 
 */
public class IjoomerRegistrationStep2Activity extends IjoomerRegistrationMaster {

	private ListView lstCuisine;
	private IjoomerButton imgSelect;

	private ArrayList<SmartListItem> listDataCuisine = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder cuisineListAdapterWithHolder;
	private DFCDataprovider dataprovider;
	private HashMap<String, String> selectedCuisine = new HashMap<String, String>();

	private String IN_EMAIL;
	private String IN_USERNAME;
	private String IN_PASSWORD;
	private String IN_MONO;
	private String IN_USERTYPE;
    private boolean IN_ISFB;

	private String serveCategories = "";
	private String[] arr_serveCategory = null;

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_registration_step2;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lstCuisine = (ListView) findViewById(R.id.lstCuisine);
		imgSelect = (IjoomerButton) findViewById(R.id.imgSelect);
		dataprovider = new DFCDataprovider(this);
		getIntentData();

	}

	@Override
	public void prepareViews() {
		lstCuisine.setAdapter(null);
		if (IN_EMAIL == null) {
			try {
				arr_serveCategory = getSmartApplication().readSharedPreferences().getString(SP_SERVE_CATEGORIES, "").split(",");
			} catch (Exception e) {
			}
		}
		getCusineList();
	}

	@Override
	public void setActionListeners() {

		imgSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (selectedCuisine.size() <= 0) {
					IjoomerUtilities.getDFInfoDialog(IjoomerRegistrationStep2Activity.this, getString(R.string.df_please_select_your_cuisine), getString(R.string.ok),
							new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
				}else{
                    if (IN_EMAIL == null) {

                        Iterator it = selectedCuisine.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            System.out.println(pairs.getKey() + " = " + pairs.getValue());
                            serveCategories = serveCategories + pairs.getKey().toString() + ",";
                            it.remove();
                        }
                        serveCategories = serveCategories.substring(0, serveCategories.length() - 1);
                        showProgressDialog("Updating Categories...", IjoomerRegistrationStep2Activity.this);
                        new DFSDataprovider(IjoomerRegistrationStep2Activity.this).updateCatagories(serveCategories, new WebCallListener() {

                            @Override
                            public void onProgressUpdate(int progressCount) {

                            }

                            @Override
                            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                hideProgressDialog();
                                if (responseCode == 200) {
                                    IjoomerUtilities.getDFInfoDialog(IjoomerRegistrationStep2Activity.this, "Categories Updated Successfully.", getString(R.string.ok),
                                            new CustomAlertNeutral() {

                                                @Override
                                                public void NeutralMethod() {
                                                    getSmartApplication().writeSharedPreferences(SP_SERVE_CATEGORIES, serveCategories);
                                                    finish();
                                                }
                                            });
                                } else {
                                    responseMessageHandler(responseCode, false);
                                }
                            }
                        });
                    } else {

                        Iterator it = selectedCuisine.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            System.out.println(pairs.getKey() + " = " + pairs.getValue());
                            serveCategories = serveCategories + pairs.getKey().toString() + ",";
                            it.remove();
                        }
                        serveCategories = serveCategories.substring(0, serveCategories.length() - 1);

                        showProgressDialog(getString(R.string.dialog_loading_register_newuser), IjoomerRegistrationStep2Activity.this);
                        new IjoomerRegistration(IjoomerRegistrationStep2Activity.this).signUpNewUser(IN_USERNAME, IN_PASSWORD, IN_EMAIL, IN_MONO, IN_USERTYPE, serveCategories,IN_ISFB,
                                new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {
                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {

                                            if (IN_ISFB) {
                                                if (responseCode == 200) {

                                                    getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);

                                                    try {
                                                        if (!(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").equals(IN_EMAIL))) {
                                                            new IjoomerCaching(IjoomerRegistrationStep2Activity.this).resetDataBase();
                                                        }
                                                    } catch (Exception e) {

                                                    }
                                                    try {
                                                        getSmartApplication().writeSharedPreferences(SP_LOGIN_TYPE, ((JSONObject) data2).getString("userType"));
                                                        getSmartApplication().writeSharedPreferences(SP_REST_ID, ((JSONObject) data2).getString("restids"));
                                                    } catch (Exception e) {
                                                    }

                                                    try {
                                                        getSmartApplication().writeSharedPreferences(SP_LOGIN_TYPE, ((JSONObject) data2).getString("userType"));
                                                    } catch (Exception e) {
                                                    }

                                                    if (IjoomerApplicationConfiguration.getUserType() == IjoomerApplicationConfiguration.SELLER) {
                                                        try {
                                                            getSmartApplication().writeSharedPreferences(SP_DF_USERNAME, ((JSONObject) data2).getString("username"));
                                                            getSmartApplication().writeSharedPreferences(SP_DFS_PAYMENT_COMPLETE, ((JSONObject) data2).getString("isSetPaypal"));
                                                            getSmartApplication().writeSharedPreferences(SP_DFS_PROFILE_COMPLETE, ((JSONObject) data2).getString("isSetProfile"));

                                                            getSmartApplication().writeSharedPreferences(SP_SERVE_CATEGORIES, ((JSONObject) data2).getString("serveCategories"));
                                                            getSmartApplication().writeSharedPreferences(SP_NEW_MEAL_REQ,
                                                                    ((JSONObject) data2).getString("newMealReq").equals("1") ? true : false);
                                                            getSmartApplication().writeSharedPreferences(SP_NEW_DISH_OFFER,
                                                                    ((JSONObject) data2).getString("newDishOffer").equals("1") ? true : false);

                                                                Intent intent = new Intent("clearStackActivity");
                                                                intent.setType("text/plain");
                                                                sendBroadcast(intent);
                                                                loadNew(DFSMyAccountActivity.class, IjoomerRegistrationStep2Activity.this, true);

                                                        } catch (Exception e) {
                                                        }
                                                    } else {
                                                        try {
                                                            getSmartApplication().writeSharedPreferences(SP_DF_ADDRESS, ((JSONObject) data2).getString("address"));
                                                            getSmartApplication().writeSharedPreferences(SP_DF_USERNAME, ((JSONObject) data2).getString("username"));
                                                            getSmartApplication().writeSharedPreferences(SP_DF_SHARELINK, ((JSONObject) data2).getString("shareLink"));
                                                            getSmartApplication().writeSharedPreferences(SP_DF_DISH_NAME, ((JSONObject) data2).getString("dishName"));

                                                            getSmartApplication().writeSharedPreferences(SP_DFC_PROFILE_COMPLETE, ((JSONObject) data2).getString("isSetProfile"));

                                                            getSmartApplication().writeSharedPreferences(SP_ACCEPT_MEAL_REQ,
                                                                    ((JSONObject) data2).getString("acceptMealReq").equals("1") ? true : false);
                                                            getSmartApplication().writeSharedPreferences(SP_ACCEPT_DISH_OFFER,
                                                                    ((JSONObject) data2).getString("acceptDishOffer").equals("1") ? true : false);


                                                                Intent intent = new Intent("clearStackActivity");
                                                                intent.setType("text/plain");
                                                                sendBroadcast(intent);
                                                                loadNew(DFCPlaceOrderActivity.class, IjoomerRegistrationStep2Activity.this, true);

                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                } else {
                                                    responseMessageHandler(responseCode, true);
                                                }
                                            } else {
                                                IjoomerUtilities.getDFInfoDialog(IjoomerRegistrationStep2Activity.this, getString(R.string.registration_successfully), getString(R.string.ok),
                                                        new CustomAlertNeutral() {

                                                            @Override
                                                            public void NeutralMethod() {

                                                                Intent intent = new Intent("clearStackActivity");
                                                                intent.setType("text/plain");
                                                                sendBroadcast(intent);
                                                                IjoomerWebService.cookies = null;
                                                                loadNew(IjoomerLoginActivity.class, IjoomerRegistrationStep2Activity.this, true);
                                                            }
                                                        }
                                                );
                                            }



                                        } else {
                                            responseMessageHandler(responseCode, true);
                                        }
                                    }
                                });
                    }
                }


			}
		});

	}

	/**
	 * Class methods
	 */

	private void getIntentData() {
		try {
			IN_EMAIL = getIntent().getStringExtra("IN_EMAIL");
			IN_USERNAME = getIntent().getStringExtra("IN_USERNAME");
			IN_PASSWORD = getIntent().getStringExtra("IN_PASSWORD");
			IN_MONO = getIntent().getStringExtra("IN_MONO");
			IN_USERTYPE = getIntent().getStringExtra("IN_USERTYPE");
            IN_ISFB = getIntent().getBooleanExtra("IN_ISFB", false);
		} catch (Exception e) {
		}
	}

	public void getCusineList() {
		showProgressDialog(getString(R.string.df_loading_cauisine), this);
		dataprovider.getCuiSineList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					prepareList(data1);
					cuisineListAdapterWithHolder = getListAdapter();
					lstCuisine.setAdapter(cuisineListAdapterWithHolder);
				} else {
					responseMessageHandler(responseCode, false);
				}

			}
		});
	}

	private void prepareList(ArrayList<HashMap<String, String>> data) {

		if (data != null && data.size() > 0) {
			listDataCuisine.clear();
			int size = data.size();
			for (int i = 0; i < size; i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.dfc_choose_cuisine_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				HashMap<String, String> row = data.get(i);
				if (IN_EMAIL == null) {
					if (isCategoryExists(row.get("categoryID"))) {
						row.put("isChecked", "1");
						selectedCuisine.put(row.get("categoryID"), "");
					} else {
						row.put("isChecked", "0");
					}
				}
                obj.add(row);
				item.setValues(obj);
				listDataCuisine.add(item);
			}
		}
	}

	private boolean isCategoryExists(String cat) {
		if (arr_serveCategory != null) {
			for (int i = 0; i < arr_serveCategory.length; i++) {
				if (arr_serveCategory[i].equals(cat)) {
					return true;
				}
			}
		}
		return false;
	}

	public SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfc_choose_cuisine_list_item, listDataCuisine, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.chkCuisine = (IjoomerCheckBox) v.findViewById(R.id.chkCuisine);
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				holder.chkCuisine.setText(row.get("name"));

				if (IN_EMAIL == null) {
					if (row.get("isChecked").equals("1")) {
						holder.chkCuisine.setChecked(true);
					} else {
						holder.chkCuisine.setChecked(false);
					}
				}
				holder.chkCuisine.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (holder.chkCuisine.isChecked()) {
							selectedCuisine.put(row.get("categoryID"), "");
						} else {
							selectedCuisine.remove(row.get("categoryID"));
						}
						updateList(position, holder.chkCuisine.isChecked());
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

	private void updateList(int pos, boolean isChecked) {
		if (isChecked) {
			((HashMap<String, String>) listDataCuisine.get(pos).getValues().get(0)).put("isChecked", "1");
		} else {
			((HashMap<String, String>) listDataCuisine.get(pos).getValues().get(0)).put("isChecked", "0");
		}
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