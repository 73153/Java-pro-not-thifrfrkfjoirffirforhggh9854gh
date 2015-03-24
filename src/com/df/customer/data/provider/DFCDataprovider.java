package com.df.customer.data.provider;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerResponseValidator;
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
public final class DFCDataprovider extends IjoomerResponseValidator implements IjoomerSharedPreferences {

	private static final String TBLCUISINE = "TBL_Cuisine";
	private static final String TBLDISHES = "TBL_Dishes";
	private static final String TBLRESTAURANTS = "TBL_Restaurants";
	private static final String TBLOFFERS = "TBL_Offers";
	private static final String TBLREQUESTS = "TBL_Requests";
	private static final String TBLRESPONSES = "TBL_Responses";
	private static final String TBLREWARDS = "TBL_Rewards";
	private static final String TBLORDERS = "TBL_Orders";
	private static final String TBLUSERS = "TBL_Users";

	private static final String CATEGORYID = "categoryID";

	private static final String RESTID = "restid";
	private static final String DISHID = "dishid";

	private static final String ACCEPTMEALREQ = "acceptMealReq";
	private static final String ACCEPTDISHOFFER = "acceptDishOffer";

	private static final String SERVICE = "service";
	private static final String REST_ID = "restID";
	private static final String DISH_ID = "dishID";
	private static final String CAT_ID = "catID";
	private static final String OFFERID = "offerID";
    private static final String DELIVERYADDRESS = "deliveryAddress";
	private static final String PAYMENTTYPE = "paymentType";
	private static final String ISVOICE = "isVoice";

	private static final String PRICE = "price";

	private final String LAT = "lat";
	private final String LONG = "long";

	private static final String USERNAME = "userName";
	private static final String EMAIL = "email";
	private static final String PHONE = "phone";
	private static final String ADDRESS1 = "address1";
	private static final String ADDRESS2 = "address2";
	private static final String CITY = "city";
	private static final String PINCODE = "pincode";
	private static final String PASSWORD1 = "password1";
	private static final String PASSWORD2 = "password2";

	public DFCDataprovider(Context mContext) {
		super(mContext);
		this.context = mContext;
	}

	private Context context;

	public ArrayList<HashMap<String, String>> getVoiceOfferDetail() {

		try {
			return new IjoomerCaching(context).getDataFromCache(TBLREQUESTS, "SELECT * FROM TBL_Requests where voiceOfferID in (SELECT max(voiceOfferID) FROM TBL_Requests)");
		} catch (Exception e) {
		}
		return null;
	}

	public void getListenCount(final String offerID, final WebCallListener target) {

		new AsyncTask<Void, Void, Object>() {

			@Override
			protected Object doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETLISTINGCOUNT);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(OFFERID, offerID);
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
					return iw.getJsonObject();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void getCuiSineList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, CATEGORIES);
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

	public void getDishesList(final String catId, final WebCallListener target) {

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
					taskData.put(CATEGORYID, catId);
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

	public void getRestaurantDishesList(final String restId, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			protected void onPreExecute() {
				ArrayList<HashMap<String, String>> data = null;
				try {
					data = new IjoomerCaching(context).getDataFromCache(TBLDISHES, "SELECT * FROM " + TBLDISHES + " WHERE restID='" + restId + "'");
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (data != null && data.size() > 0) {
					target.onCallComplete(200, getErrorMessage(), data, null);
				}
			};

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, DISHES);
				iw.addWSParam(EXTVIEW, DISH);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REST_ID, restId);
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
						ic.cacheData(iw.getJsonObject(), true, TBLDISHES);

						return new IjoomerCaching(context).getDataFromCache(TBLDISHES, "SELECT * FROM " + TBLDISHES + " WHERE restID='" + restId + "'");
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

	public void likeDish(final String dishId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, LIKEDISH);
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

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	public void saveNotificationSettings(final boolean acceptMealReq, final boolean acceptDishOffer, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SAVECUSTOMERNOTIFICATION);
				iw.addWSParam(EXTVIEW, USER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {

					taskData.put(ACCEPTMEALREQ, acceptMealReq ? "1" : "0");
					taskData.put(ACCEPTDISHOFFER, acceptDishOffer ? "1" : "0");
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

	public void likeRestaurant(final String restId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, LIKERESTAURANT);
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

	public void getUserDetail(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, USERDETAIL);
				iw.addWSParam(EXTVIEW, USER);
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
                        IjoomerCaching ic = new IjoomerCaching(context);
                        ic.cacheData(iw.getJsonObject().getJSONObject("userDetail").getJSONArray("favouriteDishes"), true, TBLDISHES);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLUSERS);
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

	public void getResponses(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETRESPONSES);
				iw.addWSParam(EXTVIEW, ORDER);
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
						IjoomerCaching ic = new IjoomerCaching(context);
						ic.cacheData(iw.getJsonObject(), true, TBLRESPONSES);
						return new IjoomerCaching(context).getDataFromCache(TBLRESPONSES);
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

	public void getRewards(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, GETREWARDS);
				iw.addWSParam(EXTVIEW, REWARD);
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
						IjoomerCaching ic = new IjoomerCaching(context);
						ic.cacheData(iw.getJsonObject(), true, TBLREWARDS);
						return new IjoomerCaching(context).getDataFromCache(TBLREWARDS);
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

	public void saveUserDetails(final String imagePath, final String userName, final String email, final String phone, final String address1, final String address2,
			final String city, final String pincode, final String password1, final String password2, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, UPDATEUSER);
				iw.addWSParam(EXTVIEW, USER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(USERNAME, userName);
					taskData.put(EMAIL, email);
					taskData.put(PHONE, phone);
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
						iw.getJsonObject().remove("total");
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), true, TBLUSERS);
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

	public ArrayList<HashMap<String, String>> getOfferDetail(String offerId) {
		try {
			return new IjoomerCaching(context).getDataFromCache(TBLOFFERS, "select * from " + TBLOFFERS + " where offerID='" + offerId + "'");
		} catch (Exception e) {
		}
		return null;
	}

	public void removeOrder(String orderID, boolean isVoice) {
		try {
			IjoomerCaching ic = new IjoomerCaching(context);
			if (isVoice) {
				ic.deleteDataFromCache("DELETE FROM " + TBLREQUESTS + " where voiceOfferID='" + orderID + "'");
			} else {
				ic.deleteDataFromCache("DELETE FROM " + TBLOFFERS + " where offerID='" + orderID + "'");
			}
		} catch (Exception e) {
		}
	}

	public void sendOrderToRestaurant(final String service, final String restId, final String dishId, final String cattId, final String price,final String deliveryAddress, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SENDORDERTORESTAURANTS);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(SERVICE, service);
                    taskData.put(DELIVERYADDRESS, deliveryAddress);
					taskData.put(REST_ID, restId);
					taskData.put(CAT_ID, cattId);
					taskData.put(DISH_ID, dishId);
					taskData.put(PRICE, price);
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
					iw.getJsonObject().remove("pushNotificationData");
					try {
						IjoomerCaching ic = new IjoomerCaching(context);
						return ic.cacheData(iw.getJsonObject(), false, TBLOFFERS);
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

	public void sendOrderByVoice(final String service, final String voicePath, final String cattId,final String deliveryAddress, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, SENDORDERBYVOICE);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(SERVICE, service);
					taskData.put(DELIVERYADDRESS, deliveryAddress);
                    taskData.put(CAT_ID, cattId);
					taskData.put(LAT, getLatitude());
					taskData.put(LONG, getLongitude());
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(voicePath, new ProgressListener() {

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

	public void getOrdersList(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, MYCONFIRMORDER);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);

				JSONObject taskData = new JSONObject();
				try {
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
						ic.cacheData(iw.getJsonObject(), true, TBLORDERS);
						return new IjoomerCaching(context).getDataFromCache(TBLORDERS);
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

	public void payOrder(final String restID,final String orderID, final String paymentType, final boolean isVoice, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(EXTTASK, PAYORDER);
				iw.addWSParam(EXTVIEW, ORDER);
				iw.addWSParam(EXTNAME, DREAMYFLAVOURS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(OFFERID, orderID);
					taskData.put(PAYMENTTYPE, paymentType);
					taskData.put(ISVOICE, isVoice ? "1" : "0");
                    taskData.put(REST_ID,restID);
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
					IjoomerCaching ic = new IjoomerCaching(context);
					if (isVoice) {
						ic.deleteDataFromCache("DELETE FROM " + TBLREQUESTS + " where voiceOfferID='" + orderID + "'");
					} else {
						ic.deleteDataFromCache("DELETE FROM " + TBLOFFERS + " where offerID='" + orderID + "'");
					}
					return ic.parseData(iw.getJsonObject());
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

    public HashMap<String,String> getDishDetail(String dishId){

        try{
            return new IjoomerCaching(context).getDataFromCache(TBLDISHES,"Select * from "+TBLDISHES+" where dishID='"+dishId+"'").get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateLikeCount(String dishId,String count){

        try{
             new IjoomerCaching(context).updateTable("UPDATE "+TBLDISHES+ " set likes="+count+" where dishID='"+dishId+"'");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
