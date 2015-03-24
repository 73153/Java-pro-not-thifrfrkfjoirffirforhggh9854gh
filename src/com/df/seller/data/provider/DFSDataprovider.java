package com.df.seller.data.provider;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Methods Related To DFCustomer.
 * 
 * @author tasol
 * 
 */
public final class DFSDataprovider extends IjoomerResponseValidator implements IjoomerSharedPreferences {

	private static final String TBLCUISINE = "TBL_Cuisine";
	private static final String TBLCURRENTORDERS = "TBL_CurrentOrders";
	private static final String TBLPASTORDERS = "TBL_PastOrders";
	private static final String TBLOFFERS = "TBL_Offers";
	private static final String TBLREQUESTS = "TBL_Requests";
	private static final String TBLDISHES = "TBL_Dishes";
	private static final String TBLRESTAURANTS = "TBL_Restaurants";
	private static final String TBLACCOUNTDETAIL = "TBL_AccountDetail";

	private static final String SERVICE = "service";
	private static final String RESTID = "restid";
	private static final String REST_ID = "restID";
	private static final String DISHID = "dishid";
	private static final String CATID = "catid";
	private static final String OFFER_ID = "offerID";
	private static final String VOICEOFFERID = "voiceOfferID";
	private static final String ORDERID = "orderID";

	private static final String PAYPALID = "paypalID";
	private static final String SERVECATEGORY = "serveCategory";
	private static final String PAYPALPASSWORD = "paypalPassword";

	private static final String BUSINENAME = "busineName";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	private static final String ADDRESS1 = "address1";
	private static final String ADDRESS2 = "address2";
	private static final String CITY = "city";
	private static final String PINCODE = "pincode";
	private static final String PASSWORD1 = "password1";
	private static final String PASSWORD2 = "password2";
	private static final String ABOUT = "about";

	private static final String DISHNAME = "dishName";
	private static final String DESCRIPTION = "description";
	private static final String FROM = "timeFrom";
	private static final String TO = "timeTo";
	private static final String PRICE = "price";
	private static final String MESSAGE = "message";
	private static final String QUANTITY = "quantity";

	private static final String NEWMEALREQ = "newMealReq";
	private static final String NEWDISHOFFER = "newDishOffer";

	private final String LAT = "lat";
	private final String LONG = "long";

	public DFSDataprovider(Context mContext) {
		super(mContext);
		this.context = mContext;
	}

	private Context context;

	public void getCuiSineList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SERVECATEGORY);
				iw.addWSParam(EXTVIEW, CATEGORY);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						iw.getJsonObject().remove("total");
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLCUISINE);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getOffersList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETOFFERLIST);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(LAT, getLatitude());
					taskData.put(LONG, getLongitude());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						iw.getJsonObject().remove("total");
						IjoomerCaching ic = new IjoomerCaching(context);
						ic.cacheData(iw.getJsonObject(), true, TBLOFFERS);
						return new IjoomerCaching(context).getDataFromCache(TBLOFFERS, "select * from " + TBLOFFERS + " where status='Pending'");
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getCurrentOrdersList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETCURRENTORDERS);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						iw.getJsonObject().remove("total");
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLCURRENTORDERS);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getPastOrdersList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETPASTORDERS);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(LAT, getLatitude());
					taskData.put(LONG, getLongitude());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						iw.getJsonObject().remove("total");
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLPASTORDERS);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getRequestsList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETVOICEOFFERLIST);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(LAT, getLatitude());
					taskData.put(LONG, getLongitude());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLREQUESTS);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getRestaurantDishesList(final String restId, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, DISHES);
				iw.addWSParam(EXTVIEW, DISH);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(RESTID, restId);
					taskData.put(LAT, getLatitude());
					taskData.put(LONG, getLongitude());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						iw.getJsonObject().remove(TOTAL);
						iw.getJsonObject().remove("timeStamp");
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLDISHES);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getRestaurantDetail(final String restId, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, RESTAURANTDETAIL);
				iw.addWSParam(EXTVIEW, RESTAURANT);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(RESTID, restId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLRESTAURANTS);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void getAccountDetails(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, ACCOUNTDETAIL);
				iw.addWSParam(EXTVIEW, RESTAURANT);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(RESTID, IjoomerApplicationConfiguration.getRestaurantId());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLACCOUNTDETAIL);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void saveAccountDetails(final String newPaypalId,final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVEACCOUNT);
				iw.addWSParam(EXTVIEW, RESTAURANT);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(RESTID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(PAYPALID, newPaypalId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLACCOUNTDETAIL);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void startVoiceListening(final String voiceOfferID, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, STARTVOICELISTENING);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(VOICEOFFERID, voiceOfferID);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {

				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void stopVoiceListening(final String voiceOfferID, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, STOPVOICELISTENING);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(VOICEOFFERID, voiceOfferID);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {

				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void updateCatagories(final String categories, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVERESTAURANT);
				iw.addWSParam(EXTVIEW, RESTAURANT);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(RESTID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(SERVECATEGORY, categories);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLRESTAURANTS);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void addDish(final String imagePath1,final String imagePath2, final String restId, final String catId, final String dishName, final String description, final String from, final String to,
			final String price, final String quantity, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVEDISH);
				iw.addWSParam(EXTVIEW, DISH);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(DISHID, "0");
					taskData.put(RESTID, restId);
					taskData.put(CATID, catId);
					taskData.put(DISHNAME, dishName);
					taskData.put(DESCRIPTION, description);
					taskData.put(FROM, from);
					taskData.put(TO, to);
					taskData.put(PRICE, price);

				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);


                    ArrayList<HashMap<String,String>> filePaths = new ArrayList<HashMap<String, String>>();

                    if(imagePath1!=null && imagePath1.length()>0){
                        HashMap<String,String> img = new HashMap<String, String>();
                        img.put("image",imagePath1);
                        filePaths.add(img);
                    }
                    if(imagePath2!=null && imagePath2.length()>0){
                        HashMap<String,String> img2 = new HashMap<String, String>();
                        img2.put("image2",imagePath2);
                        filePaths.add(img2);
                    }

					iw.WSCall(filePaths, new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});


				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), false, TBLDISHES);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void editDish(final String imagePath1,final String imagePath2, final String dishId, final String restId, final String catId, final String dishName, final String description, final String from,
			final String to, final String price, final String quantity, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVEDISH);
				iw.addWSParam(EXTVIEW, DISH);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(DISHID, dishId);
					taskData.put(RESTID, restId);
					taskData.put(CATID, catId);
					taskData.put(DISHNAME, dishName);
					taskData.put(DESCRIPTION, description);
					taskData.put(FROM, from);
					taskData.put(TO, to);
					taskData.put(PRICE, price);

				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

                ArrayList<HashMap<String,String>> filePaths = new ArrayList<HashMap<String, String>>();

                if(imagePath1!=null && imagePath1.length()>0){
                    HashMap<String,String> img = new HashMap<String, String>();
                    img.put("image",imagePath1);
                    filePaths.add(img);
                }
                if(imagePath2!=null && imagePath2.length()>0){
                    HashMap<String,String> img2 = new HashMap<String, String>();
                    img2.put("image2",imagePath2);
                    filePaths.add(img2);
                }

                iw.WSCall(filePaths, new ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        if (num >= 100) {
                            target.onProgressUpdate(95);
                        } else {
                            target.onProgressUpdate((int) num);
                        }
                    }
                });

				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), false, TBLDISHES);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void saveBusinessDetails(final String imagePath, final String businessName, final String email, final String phone, final String address1, final String address2,
			final String city, final String pincode, final String password1, final String password2, final String about, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVERESTAURANT);
				iw.addWSParam(EXTVIEW, RESTAURANT);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(RESTID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(BUSINENAME, businessName);
					taskData.put(EMAIL, email);
					taskData.put(PHONE, phone);
					taskData.put(ABOUT, about);
					taskData.put(ADDRESS1, address1);
					taskData.put(ADDRESS2, address2);
					taskData.put(CITY, city);
					taskData.put(PINCODE, pincode);
					taskData.put(PASSWORD1, password1);
					taskData.put(PASSWORD2, password2);

				} catch (Throwable e) {
				}

				iw.addWSParam(TASKDATA, taskData);
				if (imagePath == null) {
					iw.WSCall(new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});

				} else {
					iw.WSCall(imagePath, new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});

				}
				if (validateResponse(iw.getJsonObject())) {
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLRESTAURANTS);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void deleteDish(final String dishId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, DELETEDISH);
				iw.addWSParam(EXTVIEW, DISH);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(DISHID, dishId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	public void deleteCurrentOrders(final String orderIDS, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, DELETEORDER);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(ORDERID, orderIDS);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					String arrOrderIds[] = orderIDS.split(",");
					String strOrderIds = "(";
					for (int i = 0; i < arrOrderIds.length; i++) {
						strOrderIds = strOrderIds + "'" + arrOrderIds[i] + "',";
					}
					strOrderIds = strOrderIds.substring(0, strOrderIds.length() - 1);
					strOrderIds += ")";

					new IjoomerCaching(context).deleteDataFromCache("DELETE FROM " + TBLCURRENTORDERS + " WHERE orderID IN " + strOrderIds + "");
					return new IjoomerCaching(context).getDataFromCache(TBLCURRENTORDERS);
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void deletePastOrders(final String orderIDS, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, DELETEORDER);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(ORDERID, orderIDS);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					String arrOrderIds[] = orderIDS.split(",");
					String strOrderIds = "(";
					for (int i = 0; i < arrOrderIds.length; i++) {
						strOrderIds = strOrderIds + "'" + arrOrderIds[i] + "',";
					}
					strOrderIds = strOrderIds.substring(0, strOrderIds.length() - 1);
					strOrderIds += ")";

					new IjoomerCaching(context).deleteDataFromCache("DELETE FROM " + TBLPASTORDERS + " WHERE orderID IN " + strOrderIds + "");
					return new IjoomerCaching(context).getDataFromCache(TBLPASTORDERS);
				}

				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void acceptOffer(final String service, final String offerId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, ACCEPTORDER);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(SERVICE, service);
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(OFFER_ID, offerId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					new IjoomerCaching(context).updateTable("update  " + TBLOFFERS + " SET status='Accepted' where offerID='" + offerId + "'");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	public void acceptRequest(final String service, final String voiceOfferId, final String price, final String message, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, ACCEPTREQUEST);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(SERVICE, service);
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(VOICEOFFERID, voiceOfferId);
					taskData.put(PRICE, price);
					taskData.put(MESSAGE, message);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					new IjoomerCaching(context).updateTable("update  " + TBLREQUESTS + " SET status='Accepted' where offerID='" + voiceOfferId + "'");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	public void rejectOffer(final String offerId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, REJECTORDER);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(OFFER_ID, offerId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					new IjoomerCaching(context).updateTable("update  " + TBLOFFERS + "  SET status='Rejected' where offerID='" + offerId + "'");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	public void rejectRequest(final String offerId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, REJECTTREQUEST);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, IjoomerApplicationConfiguration.getRestaurantId());
					taskData.put(VOICEOFFERID, offerId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
					new IjoomerCaching(context).updateTable("update  " + TBLREQUESTS + " SET status='Rejected' where offerID='" + offerId + "'");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	public void saveNotificationSettings(final boolean newMealReq, final boolean newDishOffer, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVESELLERNOTIFICATION);
				iw.addWSParam(EXTVIEW, USER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {

					taskData.put(NEWMEALREQ, newMealReq ? "1" : "0");
					taskData.put(NEWDISHOFFER, newDishOffer ? "1" : "0");
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
				}

				return null;
			}

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}


    public ArrayList<HashMap<String, String>> getDishDetail(String dishID){
        try{
            return new IjoomerCaching(context).getDataFromCache(TBLDISHES,"SELECT * from "+TBLDISHES+" WHERE dishID='"+dishID+"'");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
