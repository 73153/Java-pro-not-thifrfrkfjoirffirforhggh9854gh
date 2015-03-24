package com.ijoomer.common.classes;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.df.src.R;
import com.smart.framework.CustomAlertNeutral;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;


/**
 * This Class Contains All Method Related To FacebookSharingActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerFacebookSharingActivity extends IjoomerSuperMaster {

    private SimpleFacebook simpleFacebook;
    private SimpleFacebookConfiguration.Builder simpleFacebookConfigurationBuilder;
    private SimpleFacebookConfiguration simpleFacebookConfiguration;

    private LinearLayout lnrPbr;

    private String IN_MESSAGE;
    private String IN_CAPTION;
    private String IN_NAME;
    private String IN_PICTURE;
    private String IN_LINK;
    private String IN_DESCRIPTION;
    @Override
    public String[] setTabItemNames() {
        return new String[0];
    }

    @Override
    public int setTabBarDividerResId() {
        return 0;
    }

    @Override
    public int setTabItemLayoutId() {
        return 0;
    }

    @Override
    public int[] setTabItemOnDrawables() {
        return new int[0];
    }

    @Override
    public int[] setTabItemOffDrawables() {
        return new int[0];
    }

    @Override
    public int[] setTabItemPressDrawables() {
        return new int[0];
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public int setLayoutId() {
        return R.layout.facebook_main;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public int setHeaderLayoutId() {
        return 0;
    }

    @Override
    public int setFooterLayoutId() {
        return 0;
    }

    @Override
    public void initComponents() {
        lnrPbr = (LinearLayout) findViewById(R.id.lnrPbr);

        simpleFacebook = SimpleFacebook.getInstance(this);
        simpleFacebookConfigurationBuilder = new SimpleFacebookConfiguration.Builder();
        simpleFacebookConfigurationBuilder.setAppId(getString(R.string.facebook_app_id));
        simpleFacebookConfigurationBuilder.setPermissions(new Permission[]{Permission.PUBLISH_ACTION});
        simpleFacebookConfigurationBuilder.setNamespace("kravely");
        simpleFacebookConfiguration = simpleFacebookConfigurationBuilder.build();
        SimpleFacebook.setConfiguration(simpleFacebookConfiguration);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        simpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void prepareViews() {
        getIntentData();
        if(simpleFacebook.isLogin()){
            publishFeed();
        }else{
            simpleFacebook.login(new OnLoginListener());
        }

    }

    private void getIntentData(){
        IN_MESSAGE = getIntent().getStringExtra("IN_MESSAGE")==null ?"":getIntent().getStringExtra("IN_MESSAGE");
        IN_CAPTION = getIntent().getStringExtra("IN_CAPTION")==null ?"":getIntent().getStringExtra("IN_CAPTION");
        IN_DESCRIPTION = getIntent().getStringExtra("IN_DESCRIPTION")==null ?"":getIntent().getStringExtra("IN_DESCRIPTION");
        IN_NAME = getIntent().getStringExtra("IN_NAME")==null ?"":getIntent().getStringExtra("IN_NAME");
        IN_PICTURE = getIntent().getStringExtra("IN_PICTURE")==null ?"":getIntent().getStringExtra("IN_PICTURE");
        IN_LINK = getIntent().getStringExtra("IN_LINK")==null ?"":getIntent().getStringExtra("IN_LINK");
    }

    @Override
    public void setActionListeners() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleFacebook = SimpleFacebook.getInstance(this);
    }

    private void publishFeed(){
        Feed.Builder builder = new Feed.Builder();
        if(IN_MESSAGE!=null && IN_MESSAGE.length()>0){

            builder.setMessage(IN_DESCRIPTION)
                    .setName(IN_NAME)
                    .setCaption(IN_CAPTION)
                    .setDescription(IN_MESSAGE)
                    .setPicture(IN_PICTURE)
                    .setLink(IN_LINK)
                    .build();

        }else{
            builder.setMessage("Dish Call App.")
                    .setName(getString(R.string.app_name))
                    .setCaption("")
                    .setDescription("I found this great App called Kravely. It allows you to request food to be delivered. You can download the App for Android and iPhone http://www.kravely.com/")
                    .setPicture("http://www.kravely.com/kravely_logo.png")
                    .setLink("http://www.kravely.com")
                    .build();
        }


        final Feed feed = builder.build();

        simpleFacebook.publish(feed,true, new onPublishListener());

    }

    class OnLoginListener implements com.sromku.simple.fb.listeners.OnLoginListener{


        @Override
        public void onLogin() {
            publishFeed();
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {

        }

        @Override
        public void onThinking() {

        }

        @Override
        public void onException(Throwable throwable) {

        }

        @Override
        public void onFail(String reason) {

        }
    }

    class onPublishListener extends OnPublishListener {
        @Override
        public void onComplete(String response) {
            super.onComplete(response);
            lnrPbr.setVisibility(View.GONE);
            IjoomerUtilities.getDFInfoDialog(IjoomerFacebookSharingActivity.this,getString(R.string.facebook_share_success), getString(R.string.ok), new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                    Intent i = new Intent();
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            });

        }
        @Override
        public void onFail(String reason) {
            super.onFail(reason);
            lnrPbr.setVisibility(View.GONE);
            IjoomerUtilities.getDFInfoDialog(IjoomerFacebookSharingActivity.this,reason, getString(R.string.ok), new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                    Intent i = new Intent();
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            });

        }

        @Override
        public void onException(Throwable throwable) {
            super.onException(throwable);
            lnrPbr.setVisibility(View.GONE);
        }
        @Override
        public void onThinking() {
            super.onThinking();


        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
