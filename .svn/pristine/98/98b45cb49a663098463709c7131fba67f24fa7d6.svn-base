package com.df.customer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.df.src.R;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;

/**
 * Created by tasol on 19/7/14.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver implements IjoomerSharedPreferences {
    @Override
    public void onReceive(Context context, Intent intent) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);


        if(sharedPreferences.getLong(SP_DF_REQ_TIMESTAMP,new Long(0))>0){

            if(!sharedPreferences.getBoolean(SP_DF_REQ_WAS_ACCEPTED,false) && !sharedPreferences.getBoolean(SP_DF_REQ_WAS_CONFIRMED,false)){

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(SP_DF_REQ_TIMESTAMP,new Long(0));
                editor.commit();
                Intent intent1 = new Intent();
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setClass(context, DFCPlaceOrderActivity.class);
                intent1.putExtra("IN_FROM_ALARM", true);
                context.startActivity(intent1);
            }
        }
    }
}
