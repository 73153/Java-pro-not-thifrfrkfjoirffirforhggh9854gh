package com.df.src;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.smart.framework.SmartFragment;



@SuppressLint("ValidFragment")
public class DFSplashFragment extends SmartFragment implements IjoomerSharedPreferences {

    private AQuery androidQuery;
    private int imgResource;
    private ImageView imgSplash;
    private ImageView imgSplash2;
    private Animation myFadeInAnimation;
    private boolean isVisible;
    public DFSplashFragment(int imgResource) {
        this.imgResource = imgResource;
    }

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.df_splash_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {
        androidQuery = new AQuery(getActivity());
        imgSplash = (ImageView) currentView.findViewById(R.id.imgSplash);
        imgSplash2 = (ImageView) currentView.findViewById(R.id.imgSplash2);
    }

    @Override
    public void prepareViews(View currentView) {
        setImageSequence();


    }

    @Override
    public void setActionListeners(View currentView) {
    }


    public void animateImage(){

        if(isVisible){
            try{
                switch (imgResource){
                    case 0:
                        androidQuery.id(imgSplash2).image((R.drawable.b1));
                        break;
                    case 1:
                        androidQuery.id(imgSplash2).image((R.drawable.b2));
                        break;
                    case 2:
                        androidQuery.id(imgSplash2).image((R.drawable.b3));
                        break;
                }
                myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                imgSplash2.startAnimation(myFadeInAnimation);

                myFadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }catch (Throwable e){
            }
        }

    }


    public void setImageSequence(){

        switch (imgResource){
            case 0:
                androidQuery.id(imgSplash).image((R.drawable.a1));
                break;
            case 1:
                androidQuery.id(imgSplash).image((R.drawable.a2));
                break;
            case 2:
                androidQuery.id(imgSplash).image((R.drawable.a3));
                break;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        if(menuVisible){
            isVisible = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateImage();
                }
            },1500);
        }else{
            try{
                isVisible = false;
                imgSplash2.setImageResource(0);
                myFadeInAnimation.cancel();
            }catch (Throwable e){
                e.printStackTrace();
            }

        }

    }


}
