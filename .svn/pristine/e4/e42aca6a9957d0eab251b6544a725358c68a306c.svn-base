package com.df.customer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.df.src.RequestTimer;
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
import com.smart.framework.CustomAlertNeutral;

import org.json.JSONObject;

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

public class DFCResponsesMapActivity extends DFCustomerMasterActivity implements OnInfoWindowClickListener {

    private OnInfoWindowElemTouchListener btnRequestAcceptListener;
    private OnInfoWindowElemTouchListener btnProfileListener;
    private ViewGroup infoWindow;

    private MapWrapperLayout mapWrapperLayout;
    private ProgressBar pbrLoading;

    private GoogleMap googleMap;

    private ArrayList<HashMap<String, String>> IN_MAPLIST;
    private DFCDataprovider dataprovider;

    private Builder builder = LatLngBounds.builder();

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

    private IjoomerTextView txtRestaurantListens;
    private IjoomerTextView txtTimer;
    private HashMap<String, String> IN_OFFER_DETAILS;

    private IjoomerButton btnRequestAccept;
    private IjoomerButton btnProfile;
    private IjoomerTextView txtRestaurantName;
    private IjoomerButton btnAmount;
    private RequestTimer requestTimer;

    private ResponseReceiver responseReceiver;

    @Override
    public int setLayoutId() {
        return R.layout.dfc_response_map;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents() {
        responseReceiver = new ResponseReceiver();
        registerReceiver(responseReceiver, IntentFilter.create("RESP", "text/plain"));

        requestTimer = RequestTimer.getInstance();

        pbrLoading = (ProgressBar) findViewById(R.id.pbrLoading);
        androidQuery = new AQuery(this);
        dataprovider = new DFCDataprovider(this);
        IN_MAPLIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_MAPLIST");
        markerHashMap = new HashMap<Marker, HashMap<String, String>>();
        try {
            getIntentData();

            txtRestaurantListens = (IjoomerTextView) findViewById(R.id.txtRestaurantListens);
            txtTimer = (IjoomerTextView) findViewById(R.id.txtTimer);
            googleMap = getMapView();
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
            mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 106));
        } catch (Exception e) {
        }

    }

    @Override
    public void prepareViews() {



        infoWindow = (ViewGroup) LayoutInflater.from(DFCResponsesMapActivity.this).inflate(R.layout.dfc_responses_map_item, null);
        btnRequestAccept = (IjoomerButton) infoWindow.findViewById(R.id.btnRequestAccept);
        btnProfile = (IjoomerButton) infoWindow.findViewById(R.id.btnProfile);
        txtRestaurantName = (IjoomerTextView) infoWindow.findViewById(R.id.txtRestaurantName);
        btnAmount = (IjoomerButton) infoWindow.findViewById(R.id.btnAmount);

        this.btnRequestAcceptListener = new OnInfoWindowElemTouchListener(btnRequestAccept) {

            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                final HashMap<String, String> data = markerHashMap.get(marker);
                try {
                    loadNew(DFCSelectPaymentOptionActivity.class, DFCResponsesMapActivity.this, false, "IN_DISH_DETAIL", data);
                } catch (Exception e) {
                }

            }
        };
        this.btnRequestAccept.setOnTouchListener(btnRequestAcceptListener);

        this.btnProfileListener = new OnInfoWindowElemTouchListener(btnProfile) {

            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                final HashMap<String, String> data = markerHashMap.get(marker);
                try {
                    loadNew(DFCBusinessProfileActivity.class, DFCResponsesMapActivity.this, false, "IN_RESTID", data.get("restID"));
                } catch (Exception e) {
                }


            }
        };
        this.btnProfile.setOnTouchListener(btnProfileListener);

        try {
//			txtRestaurantListens.setText(0 + " listens");
            txtRestaurantListens.setText(getSmartApplication().readSharedPreferences().getInt(SP_DFC_LISTEN_COUNT,0) + " listens");

        } catch (Exception e) {
        }

        try{
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude())), 15.0f));
        }catch (Exception e){

        }
    }

    @Override
    public void setActionListeners() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        requestTimer.setRequestTimerListener(new RequestTimer.RequestTimerListener() {
            @Override
            public void onTick(final int seconds, final int minutes, final int hour) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        txtTimer.setText(minutes + "m " + seconds + "s" + " remaining");

                        if(!RequestTimer.isOfferRunning() && getSmartApplication().readSharedPreferences().getLong(SP_DF_REQ_TIMESTAMP,0)>0 && !RequestTimer.isOfferAccepted()){
                            getSmartApplication().writeSharedPreferences(SP_DF_REQ_TIMESTAMP,new Long(0));
                            IjoomerUtilities.getDFInfoDialog(DFCResponsesMapActivity.this, "Sorry, no restaurants responded to your request \n Click here to upload a new one", getString(R.string.ok),
                                    new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {

                                            loadNew(DFCPlaceOrderActivity.class, DFCResponsesMapActivity.this, false);
                                        }
                                    }
                            );

                        }
                    }
                });



            }
        });

        if(RequestTimer.isOfferRunning()){
            getListenCount();
        }

        getResponses();
        handler.post(dataUpdater);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(responseReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RequestTimer.resetTimer();
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
        } else {
            marker.hideInfoWindow();
            return;
        }

    }

    /**
     * Class Methods
     */

    private void getListenCount() {

        try {
            pbrLoading.setVisibility(View.GONE);
            dataprovider.getListenCount(IN_OFFER_DETAILS.get("voiceOfferID"), new WebCallListener() {

                @Override
                public void onProgressUpdate(int progressCount) {

                }

                @Override
                public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                    pbrLoading.setVisibility(View.GONE);
                    if (responseCode == 200) {
                        try {
                            JSONObject obj = (JSONObject) data2;
                            getSmartApplication().writeSharedPreferences(SP_DFC_LISTEN_COUNT,Integer.parseInt(obj.getString("listenCount")));
                            txtRestaurantListens.setText(obj.getString("listenCount") + " listens");

                        } catch (Exception e) {
                        }
                        if(RequestTimer.isOfferRunning()){
                            getListenCount();
                        }
                    }
                }
            });
        } catch (Exception e) {
            pbrLoading.setVisibility(View.GONE);
        }

    }

    private void getResponses() {
        pbrLoading.setVisibility(View.VISIBLE);
        dataprovider.getResponses(new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {

            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                pbrLoading.setVisibility(View.GONE);
                if (responseCode == 200) {
                    IN_MAPLIST = data1;
                    if(IN_MAPLIST!=null && IN_MAPLIST.size()>0){
                        getSmartApplication().writeSharedPreferences(SP_DF_REQ_WAS_ACCEPTED,true);
                    }
                    populateMap();
                } else {

                    if(responseCode!=204){
                        responseMessageHandler(responseCode, false);
                    }

                    if(responseCode==204){
                        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(DFPushNotificationLuncherActivity.ACCEPTREQUEST);
                    }
                }
            }
        });
    }



    /**
     * This method used to populate map.
     */
    private void populateMap() {
        try {
            if (IN_MAPLIST != null && IN_MAPLIST.size() > 0) {

                try {

                    for (HashMap<String, String> mapItem : IN_MAPLIST) {
                        placeMarker(mapItem);
                    }

                    builder.include(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.dfc_map_pin)).position(
                            new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude())).getPosition());

                    LatLngBounds bounds = builder.build();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));

                    googleMap.setInfoWindowAdapter(new InfoAdapter());
                    googleMap.setOnInfoWindowClickListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
        }

    }

    /**
     * This method used to place marker on map.
     *
     * @param markerData
     *            represented markar data.
     */
    private void placeMarker(final HashMap<String, String> markerData) {

        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.dfc_map_pin)).position(
                new LatLng(Double.parseDouble(markerData.get("lat")), Double.parseDouble(markerData.get("long"))));

        markerHashMap.put(
                googleMap.addMarker(markerOptions), markerData);

        builder.include(markerOptions.getPosition());


    }

    /**
     * This method used to on map data update.
     */
    private void onDataUpdate() {
        try {
            Marker m = googleMap.getMarkerShowingInfoWindow();
            if (m != null && !m.isCluster() && m.getData() instanceof MutableData) {
                m.showInfoWindow();
            }
        } catch (Exception e) {
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

            tv = new TextView(DFCResponsesMapActivity.this);
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
                final HashMap<String, String> data = markerHashMap.get(marker);
                txtRestaurantName.setText(data.get("restName"));
                btnAmount.setText(getString(R.string.df_curency_sign) + data.get("price"));

                btnRequestAcceptListener.setMarker(marker);
                btnProfileListener.setMarker(marker);

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



    private void getIntentData() {
        try {
            IN_OFFER_DETAILS = dataprovider.getVoiceOfferDetail().get(0);

        } catch (Exception e) {
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


    private final class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getResponses();
                }
            });
        }
    }





}
