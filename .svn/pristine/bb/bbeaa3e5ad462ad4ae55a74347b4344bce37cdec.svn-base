package com.df.src;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;


@SuppressLint("ValidFragment")
public class DFImageFragment extends SmartFragment implements IjoomerSharedPreferences {

	private AQuery androidQuery;
	private String imgPath;
    private ImageView imgDish;
	private ProgressBar pbrLoading;

	public DFImageFragment(String imgPath) {
		this.imgPath = imgPath;
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.df_image_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		androidQuery = new AQuery(getActivity());
        pbrLoading = (ProgressBar) currentView.findViewById(R.id.pbrLoading);
        imgDish = (ImageView) currentView.findViewById(R.id.imgDish);
	}

	@Override
	public void prepareViews(View currentView) {
        pbrLoading.setVisibility(View.VISIBLE);
        androidQuery.id(imgDish).image(imgPath, true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0,new BitmapAjaxCallback(){
            @Override
            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                super.callback(url, iv, bm, status);
                if(bm!=null){
                    imgDish.setImageBitmap(bm);
                }
                pbrLoading.setVisibility(View.GONE);
            }
        });
	}

	@Override
	public void setActionListeners(View currentView) {
	}

	/**
	 * Class methods
	 */


}
