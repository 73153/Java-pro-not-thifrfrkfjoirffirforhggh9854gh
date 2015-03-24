package com.df.seller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.MapWrapperLayout;
import com.ijoomer.customviews.OnInfoWindowElemTouchListener;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.InfoWindowAdapter;
import pl.mg6.android.maps.extensions.GoogleMap.OnInfoWindowClickListener;
import pl.mg6.android.maps.extensions.Marker;

public class DFSOfferMapActivity extends DFSellerMasterActivity implements OnInfoWindowClickListener {

	private OnInfoWindowElemTouchListener btnRejectListener;
	private OnInfoWindowElemTouchListener btnCallListener;
	private OnInfoWindowElemTouchListener btnConfirmListener;
	private OnInfoWindowElemTouchListener btnDeliveryListener;

	private ViewGroup infoWindow;
	private MapWrapperLayout mapWrapperLayout;

	private IjoomerTextView txtPersonName;
	private IjoomerTextView txtOrderDate;
	private IjoomerTextView txtOrderDishName;
	private IjoomerTextView txtOrderAmount;
	private IjoomerButton btnReject;
	private IjoomerButton btnCall;
	private IjoomerButton btnConfirm;
	private IjoomerButton btnDeliveryRequired;

	private GoogleMap googleMap;
    private Builder builder = LatLngBounds.builder();

	private IjoomerButton imgSwitch;
	private DFSDataprovider dataprovider;
	private ArrayList<HashMap<String, String>> IN_OFFERLIST;

	private AQuery androidQuery;
	private HashMap<Marker, HashMap<String, String>> markerHashMap;
	Bitmap bitmapCreate = null;
	Bitmap bitmapScale = null;

	// 180 - 8*8,160 - 9*9,144 - 10*10,120 - 12*12 and 96 - 15*15 grids
	// respectively on zoom level 2.
	private final double[] CLUSTER_SIZES = new double[] { 180, 160, 144, 120, 96 };

	private MutableData[] dataArray = { new MutableData(6, new LatLng(-50, 0)), new MutableData(28, new LatLng(-52, 1)), new MutableData(496, new LatLng(-51, -2)), };
	private Handler handler = new Handler();
	private Runnable dataUpdater = new Runnable() {

		@Override
		public void run() {
			for (MutableData data : dataArray) {
				data.value = 7 + 3 * data.value;
			}
			onDataUpdate();
			handler.postDelayed(this, 1000);
		}
	};
    private Dialog dialog;

    /**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.dfs_offer_map;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initComponents() {

		imgSwitch = (IjoomerButton) findViewById(R.id.imgSwitch);
		googleMap = getMapView();
        googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
		mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 40));

		androidQuery = new AQuery(this);
		dataprovider = new DFSDataprovider(this);
		try {
			IN_OFFERLIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_OFFERLIST");
		} catch (Exception e) {
		}
		markerHashMap = new HashMap<Marker, HashMap<String, String>>();

	}

	@Override
	public void prepareViews() {
		infoWindow = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dfs_offer_map_item, null);
		txtPersonName = (IjoomerTextView) infoWindow.findViewById(R.id.txtPersonName);
		txtOrderDate = (IjoomerTextView) infoWindow.findViewById(R.id.txtOrderDate);
		txtOrderDishName = (IjoomerTextView) infoWindow.findViewById(R.id.txtOrderDishName);
		txtOrderAmount = (IjoomerTextView) infoWindow.findViewById(R.id.txtOrderAmount);
		btnDeliveryRequired = (IjoomerButton) infoWindow.findViewById(R.id.btnDeliveryRequired);
		btnReject = (IjoomerButton) infoWindow.findViewById(R.id.btnReject);
		btnCall = (IjoomerButton) infoWindow.findViewById(R.id.btnCall);
		btnConfirm = (IjoomerButton) infoWindow.findViewById(R.id.btnConfirm);

		this.btnDeliveryListener = new OnInfoWindowElemTouchListener(btnDeliveryRequired) {

			@Override
			protected void onClickConfirmed(View v, Marker marker) {
                final HashMap<String, String> data = markerHashMap.get(marker);
                showDeliveryOptionDialog(data.get("deliveryAddress"));
			}
		};
		this.btnDeliveryRequired.setOnTouchListener(btnDeliveryListener);

		this.btnRejectListener = new OnInfoWindowElemTouchListener(btnReject) {

			@Override
			protected void onClickConfirmed(View v, final Marker marker) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final HashMap<String, String> data = markerHashMap.get(marker);

                        getConfirmDialog("Reject Offer","Sure to reject offer?","Yes","No",false,new AlertMagnatic() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                showProgressDialog("Rejecting Offer..", DFSOfferMapActivity.this);
                                dataprovider.rejectOffer(data.get("offerID"), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {
                                            IjoomerUtilities.getDFInfoDialog(DFSOfferMapActivity.this, "Offer Rejected Successfully.", getString(R.string.ok), new CustomAlertNeutral() {

                                                @Override
                                                public void NeutralMethod() {
                                                    marker.remove();
                                                    markerHashMap.remove(marker);
                                                    refreshMap();
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
		};
		this.btnReject.setOnTouchListener(btnRejectListener);

		this.btnCallListener = new OnInfoWindowElemTouchListener(btnCall) {

			@Override
			protected void onClickConfirmed(View v, Marker marker) {
				final HashMap<String, String> data = markerHashMap.get(marker);
				try {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.get("personMobile")));
					startActivity(intent);
				} catch (Exception e) {
				}

			}
		};
		this.btnCall.setOnTouchListener(btnCallListener);

		this.btnConfirmListener = new OnInfoWindowElemTouchListener(btnConfirm) {

			@Override
			protected void onClickConfirmed(View v, final Marker marker) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final HashMap<String, String> data = markerHashMap.get(marker);

                        getConfirmDialog("Confirm Offer","Sure to confirm offer?","Yes","No",false,new AlertMagnatic() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                showProgressDialog("Confirming Offer..", DFSOfferMapActivity.this);
                                dataprovider.acceptOffer(data.get("service"), data.get("offerID"), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {
                                            IjoomerUtilities.getDFInfoDialog(DFSOfferMapActivity.this, "Offer Confirmed Successfully.", getString(R.string.ok), new CustomAlertNeutral() {

                                                @Override
                                                public void NeutralMethod() {
                                                    marker.remove();
                                                    markerHashMap.remove(marker);
                                                    refreshMap();
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
		};
		this.btnConfirm.setOnTouchListener(btnConfirmListener);

		populateMap();
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.post(dataUpdater);
	}

	@Override
	protected void onPause() {
		super.onPause();
		handler.removeCallbacks(dataUpdater);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (marker.isCluster()) {
			List<Marker> markers = marker.getMarkers();
			Builder builder = LatLngBounds.builder();
			for (Marker m : markers) {
				builder.include(m.getPosition());
			}
			LatLngBounds bounds = builder.build();
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));
		}

	}

	@Override
	public void setActionListeners() {
		imgSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class methods
	 */



	/**
	 * This method used to populate map.
	 */
	private void populateMap() {

		if (IN_OFFERLIST != null && IN_OFFERLIST.size() > 0) {

			try {

                googleMap.setInfoWindowAdapter(new InfoAdapter());
                googleMap.setOnInfoWindowClickListener(this);

				for (HashMap<String, String> mapItem : IN_OFFERLIST) {
					placeMarker(mapItem);
				}

                LatLngBounds bounds = builder.build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));


			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			IjoomerUtilities.getDFInfoDialog(this, "No Offers Left!", getString(R.string.ok), new CustomAlertNeutral() {

				@Override
				public void NeutralMethod() {
					finish();
				}
			});

		}
	}

	private void refreshMap() {

		if (markerHashMap != null && markerHashMap.size() > 0) {
			  googleMap.animateCamera(CameraUpdateFactory.newLatLng(markerHashMap.entrySet().iterator().next().getKey().getPosition()));
//            markerHashMap.entrySet().iterator().next().getKey().showInfoWindow();
		} else {
            IjoomerUtilities.getDFInfoDialog(DFSOfferMapActivity.this, getString(R.string.df_no_offers_found), getString(R.string.ok),
                    new CustomAlertNeutral() {

                        @Override
                        public void NeutralMethod() {

                        }
                    });
		}
	}

	/**
	 * This method used to place marker on map.
	 * 
	 * @param markerData
	 *            represented markar data.
	 */
	private void placeMarker(final HashMap<String, String> markerData) {
        MarkerOptions markerOptions =new MarkerOptions().title(markerData.get("dishName")).icon(BitmapDescriptorFactory.fromResource(R.drawable.df_map_circle))
                .position(new LatLng(Double.parseDouble(markerData.get("lat")), Double.parseDouble(markerData.get("long"))));

        markerHashMap.put(
                googleMap.addMarker(markerOptions), markerData);

        builder.include(markerOptions.getPosition());

	}

	/**
	 * This method used to on map data update.
	 */
	private void onDataUpdate() {
		Marker m = googleMap.getMarkerShowingInfoWindow();
		if (m != null && !m.isCluster() && m.getData() instanceof MutableData) {
			m.showInfoWindow();
		}
	}

	/**
	 * Custom marker info adapter.
	 * 
	 * @author tasol
	 * 
	 */
	class InfoAdapter implements InfoWindowAdapter {

		private TextView tv;
		{

			tv = new TextView(DFSOfferMapActivity.this);
			tv.setTextColor(Color.BLACK);
		}

		private Collator collator = Collator.getInstance();
		private Comparator<Marker> comparator = new Comparator<Marker>() {
			public int compare(Marker lhs, Marker rhs) {
				String leftTitle = lhs.getTitle();
				String rightTitle = rhs.getTitle();
				if (leftTitle == null && rightTitle == null) {
					return 0;
				}
				if (leftTitle == null) {
					return 1;
				}
				if (rightTitle == null) {
					return -1;
				}
				return collator.compare(leftTitle, rightTitle);
			}
		};

		@Override
		public View getInfoContents(Marker marker) {
			if (marker.isCluster()) {
				List<Marker> markers = marker.getMarkers();
				int i = 0;
				String text = "";
				while (i < 3 && markers.size() > 0) {
					Marker m = Collections.min(markers, comparator);
					String title = m.getTitle();
					if (title == null) {
						break;
					}
					text += title + "\n";
					markers.remove(m);
					i++;
				}
				if (text.length() == 0) {
					text = "Markers with mutable data";
				} else if (markers.size() > 0) {
					text += "and " + markers.size() + " more...";
				} else {
					text = text.substring(0, text.length() - 1);
				}
				tv.setText(text);
				return tv;
			} else {
				Object data = marker.getData();
				if (data instanceof MutableData) {
					MutableData mutableData = (MutableData) data;
					tv.setText("Value: " + mutableData.value);
					return tv;
				} else {
					return null;
				}
			}
		}

		@Override
		public View getInfoWindow(Marker marker) {
			if (!marker.isCluster()) {
				final HashMap<String, String> row = markerHashMap.get(marker);

				txtPersonName.setText(row.get("personName"));
                txtOrderDate.setText(IjoomerUtilities.getDFDateString(row.get("timeStamp")));
//				txtOrderDate.setText(row.get(row.get("date") + " @ " + row.get("time")));
				txtOrderDishName.setText(row.get("dishName"));
				txtOrderAmount.setText(getString(R.string.df_curency_sign) + row.get("price"));

				if (row.get("service").equalsIgnoreCase("delivery")) {
					btnDeliveryRequired.setVisibility(View.VISIBLE);
				} else {
                    btnDeliveryRequired.setVisibility(View.GONE);
				}
				btnCallListener.setMarker(marker);
				btnConfirmListener.setMarker(marker);
				btnRejectListener.setMarker(marker);
				btnDeliveryListener.setMarker(marker);

				mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);

				return infoWindow;
			} else {
				return null;
			}

		}

	}

	/**
	 * Inner class
	 * 
	 * @author tasol
	 * 
	 */
	private class MutableData {

		private int value;
		@SuppressWarnings("unused")
		private LatLng position;

		public MutableData(int value, LatLng position) {
			this.value = value;
			this.position = position;
		}
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
    public void showDeliveryOptionDialog(final String addresss) {

        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dfs_delivery_info);

        final IjoomerButton btnOk = (IjoomerButton) dialog.findViewById(R.id.btnOk);
        final IjoomerTextView txtAddress = (IjoomerTextView) dialog.findViewById(R.id.txtAddress);
        txtAddress.setText(addresss);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
