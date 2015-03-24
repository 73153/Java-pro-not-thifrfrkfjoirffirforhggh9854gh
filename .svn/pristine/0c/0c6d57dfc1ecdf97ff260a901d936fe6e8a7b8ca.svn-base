package com.df.src;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ijoomer.common.classes.IjoomerSplashMaster;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.page.indicator.CirclePageIndicator;

/**
 * This Class Contains All Method Related To IjoomerSplashActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerSplashActivity extends IjoomerSplashMaster {


    private ViewPager viewPager;
    private IjoomerTextView txtLogin;
    private CirclePageIndicator indicator;
    private ImagePageAdapter adapter;

    /**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		IjoomerApplicationConfiguration.setDefaultConfiguration(this);
		return R.layout.ijoomer_splash;
	}

	@Override
	public void initComponents() {
        if(getSmartApplication().readSharedPreferences().getString(SP_PASSWORD, "").length()>0){
            loadNew(IjoomerLoginActivity.class,IjoomerSplashActivity.this,true);
        }else{
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(3);
            indicator = (CirclePageIndicator) findViewById(R.id.indicator);
            txtLogin = (IjoomerTextView) findViewById(R.id.txtLogin);
        }

    }

	@Override
	public void prepareViews() {

        try{
            adapter = new ImagePageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);

            indicator.setPageColor(Color.TRANSPARENT);
            indicator.setStrokeColor(Color.parseColor(getString(R.color.df_reward_detail_color)));
            indicator.setStrokeWidth(convertSizeToDeviceDependent(1));
            indicator.setRadius(convertSizeToDeviceDependent(5));
            indicator.setFillColor(Color.parseColor(getString(R.color.df_reward_detail_color)));
            indicator.setViewPager(viewPager, 0);
            indicator.setSnap(true);

        }catch (Exception e){
            e.printStackTrace();
        }

	}


    @Override
	public void setActionListeners() {
        try{
            txtLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadNew(IjoomerLoginActivity.class,IjoomerSplashActivity.this,false);
                    finish();
                }
            });

        }catch (Exception e){

        }


	}

	@Override
	public View setLayoutView() {
		return null;
	}


    private class ImagePageAdapter extends FragmentPagerAdapter {

        public ImagePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {

            switch (pos){
                case 0:
                    return new DFSplashFragment(pos);
                case 1:
                    return new DFSplashFragment(pos);
                case 2:
                    return new DFSplashFragment(pos);
                default:
                    return new DFSplashFragment(0);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }


}