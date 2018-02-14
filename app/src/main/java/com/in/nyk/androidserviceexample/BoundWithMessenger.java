package com.in.nyk.androidserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

import java.lang.ref.WeakReference;

/**
 * Created by nikhilkanse on 14/02/18.
 */

public class BoundWithMessenger extends Service {

    private static String LOG_TAG = "BoundService";
    private static Chronometer mChronometer;
    static final int MSG_GET_TIMESTAMP = 1000;

    static class BoundServiceHandler extends Handler {
        private final WeakReference<BoundWithMessenger> mService;

        public BoundServiceHandler(BoundWithMessenger service) {
            mService = new WeakReference<BoundWithMessenger>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_TIMESTAMP:
                    long elapsedMillis = SystemClock.elapsedRealtime()
                            - mChronometer.getBase();
                    int hours = (int) (elapsedMillis / 3600000);
                    int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
                    int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
                    int millis = (int) (elapsedMillis - hours * 3600000 - minutes
                            * 60000 - seconds * 1000);
                    Messenger activityMessenger = msg.replyTo;
                    Bundle b = new Bundle();
                    b.putString("timestamp", hours + ":" + minutes + ":" + seconds
                            + ":" + millis);
                    Message replyMsg = Message.obtain(null, MSG_GET_TIMESTAMP);
                    replyMsg.setData(b);
                    try {
                        activityMessenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new BoundServiceHandler(this));


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
        mChronometer.stop();
    }
}
