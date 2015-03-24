package com.df.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
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
import com.ijoomer.customviews.IjoomerAudioPlayer;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
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

public class DFSRequestMapActivity extends DFSellerMasterActivity implements OnInfoWindowClickListener {

    private OnInfoWindowElemTouchListener btnRequestAcceptListener;
    private OnInfoWindowElemTouchListener btnRequestRejectListener;
    private OnInfoWindowElemTouchListener btnCallUserListener;
    private OnInfoWindowElemTouchListener btnRequestListenListener;
    private OnInfoWindowElemTouchListener btnFlagListenListener;


    private ViewGroup infoWindow;
    private MapWrapperLayout mapWrapperLayout;
    private HashMap<String, String> clickdRow;

    private IjoomerTextView txtTimeAdded;
    private IjoomerButton btnRequestAccept;
    private IjoomerButton btnRequestReject;
    private IjoomerButton btnCallUser;
    private IjoomerButton btnRequestListen;
    private IjoomerButton btnFlag;

    private GoogleMap googleMap;
    private Builder builder = LatLngBounds.builder();

    private IjoomerButton imgSwitch;
    private DFSDataprovider dataprovider;
    private IjoomerAudioPlayer audioPlayer = new IjoomerAudioPlayer();
    private ArrayList<HashMap<String, String>> IN_REQUESTLIST;

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

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.dfs_requests_map;
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
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 40));

        androidQuery = new AQuery(this);
        dataprovider = new DFSDataprovider(this);

        try {
            IN_REQUESTLIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_REQUESTLIST");
        } catch (Exception e) {
        }
        markerHashMap = new HashMap<Marker, HashMap<String, String>>();

    }

    @Override
    public void prepareViews() {
        audioPlayer.setMaxVolume(this);
        infoWindow = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dfs_request_map_item, null);
        txtTimeAdded = (IjoomerTextView) infoWindow.findViewById(R.id.txtTimeAdded);
        btnRequestAccept = (IjoomerButton) infoWindow.findViewById(R.id.btnRequestAccept);
        btnRequestReject = (IjoomerButton) infoWindow.findViewById(R.id.btnRequestReject);
        btnCallUser = (IjoomerButton) infoWindow.findViewById(R.id.btnCallUser);
        btnRequestListen = (IjoomerButton) infoWindow.findViewById(R.id.btnRequestListen);
        btnFlag = (IjoomerButton) infoWindow.findViewById(R.id.btnFlag);


        this.btnFlagListenListener = new OnInfoWindowElemTouchListener(btnFlag) {

            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                final HashMap<String, String> data = markerHashMap.get(marker);
                String message = "Reported Username : "+data.get("personName")+"\n My Username: "+getSmartApplication().readSharedPreferences().getString(SP_DF_USERNAME,"")+"\n\n Please explain the reason for reporting this user below. One of our customer service team will take appropriate action within the next 24 hours.";
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL,  new String[] {"hello@kravely.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Report");
                i.putExtra(
                        Intent.EXTRA_TEXT,
                        Html.fromHtml(message));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    ting(getString(R.string.share_email_no_client));
                }

            }
        };
        this.btnFlag.setOnTouchListener(btnFlagListenListener);


        this.btnRequestAcceptListener = new OnInfoWindowElemTouchListener(btnRequestAccept) {

            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                final HashMap<String, String> data = markerHashMap.get(marker);
                try {
                    loadNew(DFSConfirmOrderActivity.class, DFSRequestMapActivity.this, false, "IN_ORDER_DETAIL", data);
                } catch (Exception e) {
                }

            }
        };
        this.btnRequestAccept.setOnTouchListener(btnRequestAcceptListener);

        this.btnRequestRejectListener = new OnInfoWindowElemTouchListener(btnRequestAccept) {

            @Override
            protected void onClickConfirmed(View v, final Marker marker) {

                final HashMap<String, String> data = markerHashMap.get(marker);

                getConfirmDialog("Reject Request.","Sure to reject request?","Yes","No",false,new AlertMagnatic() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rejectRequest(data,marker);
                            }
                        });

                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {

                    }
                });
            }
        };
        this.btnRequestReject.setOnTouchListener(btnRequestRejectListener);

        this.btnCallUserListener = new OnInfoWindowElemTouchListener(btnCallUser) {

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
        this.btnCallUser.setOnTouchListener(btnCallUserListener);

        this.btnRequestListenListener = new OnInfoWindowElemTouchListener(btnRequestListen) {

            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                final HashMap<String, String> data = markerHashMap.get(marker);

                if(!getSmartApplication().readSharedPreferences().getBoolean(data.get("offerID"),false)){
                    if (!audioPlayer.isPlaying()) {
                        startVoiceListening(data);
                    } else {
                        audioPlayer.stopAudio();
                    }
                }else{
                    if (!audioPlayer.isPlaying()) {
                        audioPlayer.playAudio(data.get("voice"));
                    } else {
                        audioPlayer.stopAudio();
                    }
                }

            }
        };
        this.btnRequestListen.setOnTouchListener(btnRequestListenListener);

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

        audioPlayer.setAudioListener(new AudioListener() {

            @Override
            public void onReportClicked() {
            }

            @Override
            public void onPrepared() {
            }

            @Override
            public void onPlayClicked(boolean isplaying) {
            }

            @Override
            public void onError() {
                IjoomerUtilities.getDFInfoDialog(DFSRequestMapActivity.this, "Audio Can No Be Played!", getString(R.string.ok), new CustomAlertNeutral() {

                    @Override
                    public void NeutralMethod() {
                    }
                });
            }

            @Override
            public void onComplete() {
                if (clickdRow != null) {
                    stopVoiceListening(clickdRow);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {

    }

    /**
     * Class methods
     */

    private void rejectRequest(final HashMap<String, String> data,final Marker marker){
        showProgressDialog("Rejecting Request..", DFSRequestMapActivity.this);
        dataprovider.rejectOffer(data.get("offerID"), new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {

            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                hideProgressDialog();
                if (responseCode == 200) {
                    IjoomerUtilities.getDFInfoDialog(DFSRequestMapActivity.this, "Request Rejected Successfully.", getString(R.string.ok), new CustomAlertNeutral() {

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

    private void startVoiceListening(final HashMap<String, String> row) {

        showProgressDialog("Sending Listing Request...", this);
        dataprovider.startVoiceListening(row.get("offerID"), new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {

            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                hideProgressDialog();
                if (responseCode == 200) {
                    clickdRow = row;
                    getSmartApplication().writeSharedPreferences(row.get("offerID"),true);
                    if (!audioPlayer.isPlaying()) {
                        audioPlayer.playAudio(row.get("voice"));
                    } else {
                        audioPlayer.stopAudio();
                    }
                } else {
                    responseMessageHandler(responseCode, false);
                }
            }
        });
    }

    private void stopVoiceListening(final HashMap<String, String> row) {
        showProgressDialog("Sending Listen Complete Request...", this);
        dataprovider.stopVoiceListening(row.get("offerID"), new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {
            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                hideProgressDialog();
                clickdRow = null;
            }
        });
    }


    /**
     * This method used to populate map.
     */
    private void populateMap() {

        if (IN_REQUESTLIST != null && IN_REQUESTLIST.size() > 0) {

            try {
                googleMap.setInfoWindowAdapter(new InfoAdapter());
                googleMap.setOnInfoWindowClickListener(this);

                for (HashMap<String, String> mapItem : IN_REQUESTLIST) {
                    placeMarker(mapItem);
                }

                LatLngBounds bounds = builder.build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void refreshMap() {

        if (markerHashMap != null && markerHashMap.size() > 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(markerHashMap.entrySet().iterator().next().getKey().getPosition()));
//            markerHashMap.entrySet().iterator().next().getKey().showInfoWindow();
        } else {
            IjoomerUtilities.getDFInfoDialog(DFSRequestMapActivity.this, getString(R.string.df_no_request_found), getString(R.string.ok),
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
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.df_map_circle))
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

            tv = new TextView(DFSRequestMapActivity.this);
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

                txtTimeAdded.setText(getString(R.string.df_added) + " " + IjoomerUtilities.getDFTimeString(row.get("timeStamp")));
                btnCallUserListener.setMarker(marker);
                btnRequestAcceptListener.setMarker(marker);
                btnRequestListenListener.setMarker(marker);
                btnRequestRejectListener.setMarker(marker);
                btnFlagListenListener.setMarker(marker);
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

}
