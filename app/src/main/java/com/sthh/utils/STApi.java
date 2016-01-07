package com.sthh.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.sthh.widget.NotifyView;
import com.umeng.analytics.game.UMGameAgent;

/**
 * Created by xuzhiguo on 15/11/4.
 */
public class STApi {
    private String TAG = "STApi";

    private static SensorManagerHelper helper;
    private static boolean isVisibility = false;


    private static WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private static WindowManager mWindowManager;
    private static NotifyView mNotifyView;
    private static float x, y;

    /**
     * x y 都设置为0时，位置在屏幕右上角
     *
     * @param x
     * @param y
     */
    public static void open(final int x, final int y, final Activity mActivity) {
        if (helper == null) {
            helper = new SensorManagerHelper(mActivity, new ShakeListener() {

                @Override
                public void onStart() {
                    //Toast.makeText(mActivity, "打开了", Toast.LENGTH_SHORT).show();
                    isVisibility = false;
                }

                @Override
                public void onShake() {
                    if (!isVisibility) {
                        vibrate(mActivity, 200);
                        //Toast.makeText(mActivity, "你在摇哦", Toast.LENGTH_SHORT).show();
                        setmNotifyView(mActivity, x, y, true);
                    }
                    isVisibility = true;
                }

                @Override
                public void onStop() {
                    //Toast.makeText(mActivity, "关闭了", Toast.LENGTH_SHORT).show();
                    //移除悬浮窗口
                    if (mNotifyView != null && mWindowManager != null)
                        try {
                            mWindowManager.removeView(mNotifyView);
                        } catch (Exception e) {
                            //Log.i(TAG, "onStop error");
                        }
                    isVisibility = false;
                }
            });
            helper.start();
        }

    }

    private static Handler mHandler = new Handler();

    /**
     * x y 都设置为0时，位置在屏幕底部中间
     *
     * @param x
     * @param y
     */
    public static void show(final int x, final int y, final Activity mActivity) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotifyView notifyView = setTimerNotifyView(mActivity, x, y, true);
//                notifyView.setVisibility(View.GONE);
            }
        }, PrefUtil.getLong(PrefUtil.ST_DELAY_TIME, 30) * 1000);
    }

    public static void onCreate(Activity mActivity) {
        UMGameAgent.onEvent(mActivity, "stInit");
        if (PrefUtil.getBool(PrefUtil.ST_LINK_SHOW, false)) {
            show(100, 200, mActivity);
        }
    }

    public static void onResume(Activity mActivity) {

        UMGameAgent.onResume(mActivity);
        open(100, 200, mActivity);
    }


    public static void onPause(Activity mActivity) {
        UMGameAgent.onPause(mActivity);
        if (helper != null)
            helper.stop();
        helper = null;
    }

    private static NotifyView setTimerNotifyView(Activity mActivity, int sX, int sY, boolean hasClose) {
        UMGameAgent.onEvent(mActivity, "showDialogTimer");
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Log.i("mWindowManager", "mWindowManager--->" + mWindowManager);
        //设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        wmParams.token = mActivity.getWindow().getDecorView().getApplicationWindowToken();
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = sX;
        wmParams.y = sY;
        // 设置悬浮窗口长宽数据
        wmParams.width = dip2px(mActivity, 230);
        wmParams.height = dip2px(mActivity, 100);
        String theme = PrefUtil.getString(PrefUtil.ST_THEME, "light");
        NotifyView notifyView = new NotifyView(mActivity, theme, hasClose);
        mWindowManager.addView(notifyView, wmParams);
        return notifyView;
    }


    private static void setmNotifyView(final Activity mActivity, int sX, int sY, boolean hasClose) {
        UMGameAgent.onEvent(mActivity, "showDialog");
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Log.i("mWindowManager", "mWindowManager--->" + mWindowManager);
        //设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = sX;
        wmParams.y = sY;
        // 设置悬浮窗口长宽数据
        wmParams.width = dip2px(mActivity, 230);
        wmParams.height = dip2px(mActivity, 100);
        String theme = PrefUtil.getString(PrefUtil.ST_THEME, "light");
        mNotifyView = new NotifyView(mActivity, theme, hasClose);
        mNotifyView.setDissmissCallBack(new NotifyView.DissmissCallBack() {
            @Override
            public void onCallback() {
                isVisibility = false;
            }
        });
        //设置监听浮动窗口的触摸移动
        mNotifyView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getRawX();
                        y = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(event.getRawX() - x) > 10 || Math.abs(event.getRawY() - y) > 10) {
//                            Log.i("xxx", "onTouch x" + event.getX());
//                            Log.i("xxx", "onTouch y" + event.getY());
//                            Log.i("xxx", "onTouch rawx" + event.getRawX());
//                            Log.i("xxx", "onTouch rawy" + event.getRawY());
                            if (!(x == 0 && y == 0)) {

                                Rect rect = new Rect();
                                mActivity.getWindow().getDecorView()
                                        .getWindowVisibleDisplayFrame(rect);
//                            int naviWidth = mWindowManager.getDefaultDisplay().getWidth()
//                                    - rect.right;
//                            wmParams.x = (int) event.getRawX() - wmParams.width / 2
//                                    - naviWidth;
//                            wmParams.y = (int) event.getRawY() - rect.top
//                                    - wmParams.height / 2;
//                            android.util.Log.e("xxx","x0="+wmParams.x+",y0="+wmParams.y+",x="+(x-event.getX() )+",y="+(event.getY() - y));


                                wmParams.x = (int) (wmParams.x + (x - event.getRawX()));
                                wmParams.y = (int) (wmParams.y + (event.getRawY() - y));
                                mWindowManager.updateViewLayout(mNotifyView, wmParams);
                            }

                            x = event.getRawX();
                            y = event.getRawY();


                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        x = 0;
                        y = 0;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mWindowManager.addView(mNotifyView, wmParams);
    }

    public interface ShakeListener {
        void onStart();

        void onShake();

        void onStop();
    }

    public static int dip2px(Activity mActivity, float dpValue) {
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void vibrate(final Context activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
}
