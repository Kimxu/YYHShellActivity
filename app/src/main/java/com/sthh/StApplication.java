package com.sthh;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.sthh.utils.GlobalUtils;

/**
 *
 * Created by kimxu on 16/1/7.
 */

public class StApplication extends Application{
    private StApplication mApplication;
    private StService mStService;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        bindStService();
        Log.e("Ip",GlobalUtils.getPhoneIp());

    }

    private void bindStService() {
        Intent it = new Intent(mApplication, StService.class);
        bindService(it, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                StService.ServiceBinder serviceBinder = (StService.ServiceBinder) service;
                serviceBinder.getService().setTransmissionCallback(new StService.DataTransmissionCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Service.BIND_AUTO_CREATE);
    }



}
