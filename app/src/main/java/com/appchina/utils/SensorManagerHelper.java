package com.appchina.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.appchina.widget.NotifyView;

import java.util.ArrayList;
import java.util.List;

public class SensorManagerHelper implements SensorEventListener {

    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 3000;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 50;
    // 传感器管理器
    private SensorManager sensorManager;
    // 传感器
    private Sensor sensor;
    // 重力感应监听器
    private OnShakeListener onShakeListener;
    // 上下文对象context
    private Activity context;
    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;
    // 上次检测时间
    private long lastUpdateTime;

    private List<Integer> shakeCounts;
    private final int shakeNum = 5;
    private String TAG = "SensorManagerHelper";


    private boolean isStart =false;
    // 构造器
    public SensorManagerHelper(final Activity activity) {
        // 获得监听对象
        this.context = activity;
        shakeCounts = new ArrayList<>();
        onShakeListener = new OnShakeListener() {
            @Override
            public void onShake() {
                if (isStart){
                    vibrate(context, 200);
                    Toast.makeText(context, "你在摇哦", Toast.LENGTH_SHORT).show();
                    init();
                    isStart=false;
                }
            }
        };
        start();
    }
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private NotifyView mNotifyView;
    private float x,y;
    private void init() {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Log.i("mWindowManager", "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
        // 设置悬浮窗口长宽数据
        wmParams.width = dip2px(230);
        wmParams.height = dip2px(100);
        String theme = PrefUtil.getString(PrefUtil.ST_THEME, "light");
        mNotifyView = new NotifyView(context, theme);
        mNotifyView.setDissmissCallBack(new NotifyView.DissmissCallBack() {
            @Override
            public void onCallback() {
                isStart=true;
            }
        });
        //设置监听浮动窗口的触摸移动
        mNotifyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x=event.getX();
                        y=event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getX()-x)>10||Math.abs(event.getY()-y)>10) {
                            Log.i(TAG, "onTouch x" + event.getX());
                            Log.i(TAG, "onTouch y" + event.getY());
                            Log.i(TAG, "onTouch rawx" + event.getRawX());
                            Log.i(TAG, "onTouch rawy" + event.getRawY());
                            Rect rect = new Rect();
                            context.getWindow().getDecorView()
                                    .getWindowVisibleDisplayFrame(rect);
                            int naviWidth = mWindowManager.getDefaultDisplay().getWidth()
                                    - rect.right;
                            wmParams.x = (int) event.getRawX() - wmParams.width / 2
                                    - naviWidth;
                            wmParams.y = (int) event.getRawY() - rect.top
                                    - wmParams.height / 2;
                            mWindowManager.updateViewLayout(mNotifyView, wmParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mWindowManager.addView(mNotifyView, wmParams);
    }

    // 开始
    public void start() {
        isStart=true;
        // 获得传感器管理器
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            // 获得重力传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // 注册
            if (sensor != null) {
                sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    // 停止检测
    public void stop() {
        Toast.makeText(context, "关闭了", Toast.LENGTH_SHORT).show();
        //移除悬浮窗口
        mWindowManager.removeView(mNotifyView);
        sensorManager.unregisterListener(this);
    }

    // 摇晃监听接口
    public interface OnShakeListener {
        public void onShake();
    }

    // 设置重力感应监听器
    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    /*
     * (non-Javadoc)
     * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
     * .Sensor, int)
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    /*
     * 重力感应器感应获得变化数据
     * android.hardware.SensorEventListener#onSensorChanged(android.hardware
     * .SensorEvent)
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;
        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;
        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        // 达到速度阀值，发出提示
        if (speed >= SPEED_SHRESHOLD) {
            if (shakeIsDone()) {
                onShakeListener.onShake();

            }
        } else {
            shakeCounts.clear();
        }

    }

    /**
     * 摇晃次数到达三次
     *
     * @return
     */
    private boolean shakeIsDone() {
        int shakecurrent = shakeCounts.size();
        if (shakeNum == shakecurrent) {
            shakeCounts.clear();
            return true;
        } else {
            shakeCounts.add(1);
            return false;
        }
    }

    public static void vibrate(final Context activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}