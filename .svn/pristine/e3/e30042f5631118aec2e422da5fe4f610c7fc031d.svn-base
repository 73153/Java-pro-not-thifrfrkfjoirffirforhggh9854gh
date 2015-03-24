package com.df.src;


import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.smart.framework.SmartApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tasol on 11/7/14.
 */


public class RequestTimer implements IjoomerSharedPreferences{

    private RequestTimer(){

    }

    public static long INTERVAL = 300000L;
    public static long EXPIRE = 900000L;
    public static long EXPIRE_INTERVAL = 600000L;

    private java.util.Timer timer;
    private long TIME_REMAINING = 0;
    private static RequestTimer requestTimer;

    private boolean wasAccepted;





    public long getTimeRemainig(){
        long startTime = SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getLong(SP_DF_REQ_TIMESTAMP,0);
        long endTime = System.currentTimeMillis();


        long diff = endTime-startTime;

        if(diff>0 && diff<INTERVAL){
            return  INTERVAL - diff;
        }else{
            return 0;
        }

    }

    public  static  boolean isOfferRunning(){


        long startTime = SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getLong(SP_DF_REQ_TIMESTAMP,0);
        long endTime = System.currentTimeMillis();


        long diff = endTime-startTime;

        if(diff>0 && diff<INTERVAL && !SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(SP_DF_REQ_WAS_CONFIRMED,false) ){
            return true;
        }

        return false;


    }

    public  static  boolean isOfferAccepted(){

        try{

            return SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getBoolean(SP_DF_REQ_WAS_ACCEPTED,false);

        }catch (Exception e){
            e.printStackTrace();
        }
        return  false;
    }


    public RequestTimerListener getRequestTimerListener() {
        return requestTimerListener;
    }

    public void setRequestTimerListener(RequestTimerListener requestTimerListener) {


        this.requestTimerListener = requestTimerListener;
        if(isOfferRunning()){
            TIME_REMAINING = getTimeRemainig();
            startTimer();
        }else{
            if(getRequestTimerListener()!=null){
                requestTimerListener.onTick(0,0,0);
            }
        }

    }

    private RequestTimerListener requestTimerListener;

    public interface RequestTimerListener{
        void onTick(int seconds, int minutes,int hour);
    }

    public static RequestTimer getInstance(){

        if(requestTimer==null){
            requestTimer = new RequestTimer();
        }
        return requestTimer;
    }

    public static void resetTimer(){

        if(requestTimer!=null){
            requestTimer.TIME_REMAINING=0;
            if(requestTimer.timer!=null){
                requestTimer.timer.cancel();
            }
            requestTimer.requestTimerListener=null;
            requestTimer.timer=null;
        }
    }

    private void startTimer() {

        if(timer==null){

            timer = new Timer();

            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    int seconds = (int) (TIME_REMAINING / 1000) % 60;
                    int minutes = (int) ((TIME_REMAINING / (1000 * 60)) % 60);
                    TIME_REMAINING = TIME_REMAINING - 1000;
                    if(getRequestTimerListener()!=null){
                        requestTimerListener.onTick(seconds,minutes,0);
                    }

                    if(TIME_REMAINING<=0){
                        requestTimerListener.onTick(0,0,0);
                        timer.cancel();
                        timer=null;
                        requestTimerListener=null;
                    }
                }
            }, 0, 1000);
        }

    }

}
