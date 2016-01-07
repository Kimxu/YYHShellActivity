package com.sthh;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StService extends Service {
    private DataTransmissionCallback mTransmissionCallback;
    public class ServiceBinder extends Binder {
        private StService mService = null;

        public ServiceBinder(StService service) {

            mService = service;
        }

        public StService getService() {
            return mService;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new ServiceBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void setTransmissionCallback(DataTransmissionCallback transmissionCallback){
        mTransmissionCallback=transmissionCallback;
    }

    public interface DataTransmissionCallback{
        void onSuccess();
    }
}
