package com.df.src;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.df.customer.AlarmManagerBroadcastReceiver;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;

/**
 * This Class Contains All Method Related To GCMIntentService.
 * 
 * @author tasol
 * 
 */
public class GcmIntentService extends IntentService implements IjoomerSharedPreferences{

	private static int count = 1;
    private int type =0;

    public static PushNotificationListener getPushNotificationListener() {
        return pushNotificationListener;
    }

    public static void setPushNotificationListener(PushNotificationListener pushNotificationListener) {
        GcmIntentService.pushNotificationListener = pushNotificationListener;
    }

    private static PushNotificationListener pushNotificationListener;

    public interface PushNotificationListener{
        void onPushReceived(Bundle bundle);
    }

	public GcmIntentService() {
		super(IjoomerApplicationConfiguration.getGCMProjectId());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		handleMessage(this, intent);
	}

	/**
	 * Class methods
	 */

	/**
	 * This methods used to handle push notification message.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param intent
	 *            represented {@link Intent}
	 */
	@SuppressWarnings("deprecation")
	private void handleMessage(Context mContext, Intent intent) {



		long when = System.currentTimeMillis();
		int icon = R.drawable.ijoomer_push_notification_icon;

		try {

			Bundle gcmData = intent.getExtras();

			NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent contentIntent = null;

			if (gcmData.getString("type").equals("backend")) {
				contentIntent = PendingIntent.getActivity(mContext, (int) (Math.random() * 100), new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
			} else {
                try{
                    type = Integer.parseInt(gcmData.getString("type"));
                }catch (Exception e){
                    type=0;
                }

                if (!(gcmData.getString("type").equals("104"))) {
                    Intent gotoIntent = new Intent(mContext, DFPushNotificationLuncherActivity.class);
                    gotoIntent.putExtra("PUSH_BUNDLE", gcmData);
                    contentIntent = PendingIntent.getActivity(mContext, (int) (Math.random() * 100), gotoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
			}
            if((gcmData.getString("type").equals("103"))){

                try{
                    AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(this, AlarmManagerBroadcastReceiver.class);
                    PendingIntent pi = PendingIntent.getBroadcast(this, 1010, alarmIntent, 0);
                    am.cancel(pi);
                }catch (Exception e){

                }

                SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SP_DF_REQ_WAS_ACCEPTED,true);
                editor.commit();

                try{
                    Intent intentResponses = new Intent("RESP");
                    intentResponses.setType("text/plain");
                    sendBroadcast(intentResponses);

                }catch (Exception e){

                }

            }


            SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);

            if(sharedPreferences.getString(SP_PASSWORD, "").length()>0){
                if (IjoomerApplicationConfiguration.getUserType() == IjoomerApplicationConfiguration.CUSTOMER && (type>100 && type<105)) {
                    if(!(gcmData.getString("type").equals("104"))){

                        Notification notification = new Notification(icon, gcmData.getString("message"), when);
                        notification.defaults |= Notification.DEFAULT_LIGHTS;
                        notification.defaults |= Notification.DEFAULT_VIBRATE;
                        notification.setLatestEventInfo(mContext, mContext.getString(R.string.app_name), intent.getExtras().getString("message"), contentIntent);
                        notification.flags = Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR;

                        notification.sound= Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.customer);

                        notificationManager.notify(type, notification);
                        count = count + 1;


                    }
                }else if (IjoomerApplicationConfiguration.getUserType() == IjoomerApplicationConfiguration.SELLER) {
                    Notification notification = new Notification(icon, gcmData.getString("message"), when);
                    notification.defaults |= Notification.DEFAULT_LIGHTS;
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                    notification.setLatestEventInfo(mContext, mContext.getString(R.string.app_name), intent.getExtras().getString("message"), contentIntent);
                    notification.flags = Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR;

                    notification.sound= Uri.parse("android.resource://"+mContext.getPackageName()+"/" + R.raw.seller);

                    notificationManager.notify(type, notification);
                    count = count + 1;
                }
            }


		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
