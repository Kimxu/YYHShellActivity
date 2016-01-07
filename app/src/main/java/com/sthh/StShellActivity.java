package com.sthh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sthh.utils.PrefUtil;
import com.umeng.analytics.game.UMGameAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StShellActivity extends Activity {

    private static final int START_MAIN_ACTIVITY = 110;

    private boolean island = true;
    private String pkgName, clsName, theme, channel,linkText,linkUrl;
    private boolean linkShow, linkAction;
    private long delayTime;
    private Activity mActivity;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case START_MAIN_ACTIVITY:
                    UMGameAgent.onEvent(mActivity, "showSplash");
                    Intent intent = new Intent();
                    intent.setClassName(pkgName, clsName);
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.setFlags(0x10000000);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        UMGameAgent.setDebugMode(false);//设置输出运行时日志
        UMGameAgent.init(this);

        getMetaData();
        if (island) {
            //此为设置为横屏播放
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        launchView();
        start();
        myHandler.sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        UMGameAgent.onResume(this);
        if (island) {
            //此为设置为横屏播放
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMGameAgent.onPause(this);
    }

    private boolean isFirst() {
        String filname = "yyh.inject";
        boolean isFileexists = false;
        String buffInject = "Appchina Inject";
        File injectFile = new File(getFilesDir(), filname);
        isFileexists = injectFile.exists();
        if (!isFileexists) {
            if (!getFilesDir().exists()) {
                getFilesDir().mkdirs();
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(injectFile);
                out.write(buffInject.getBytes());
                out.close();
            } catch (IOException ignored) {

            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {

                    }
                }
            }
        }
        return !isFileexists;
    }

    private void start() {
        String d_d = "/data/data/" + pkgName;
        String a_d = "sdcard/Android/data/" + pkgName;
        String a_o = "sdcard/Android/obb/" + pkgName;
        Thread thread1 = new Thread(new MyThread(this, "copy.inject1", d_d));
        Thread thread2 = new Thread(new MyThread(this, "copy.inject2", a_d));
        Thread thread3 = new Thread(new MyThread(this, "copy.inject3", a_o));
        if (isFirst()) {
            thread1.start();
            thread2.start();
            thread3.start();
        }
    }

    private void getMetaData() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            island = info.metaData.getBoolean("landscape");
            theme = info.metaData.getString("theme", "light");
            pkgName = info.metaData.getString("pkgname");
            clsName = info.metaData.getString("clsname");
            linkShow = info.metaData.getBoolean("linkshow");
            linkAction = info.metaData.getBoolean("linkaction");
            delayTime = info.metaData.getInt("delaytime");
            linkText = info.metaData.getString("linktext");
            linkUrl = info.metaData.getString("linkurl");


            channel = getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA).metaData.getString("UMENG_CHANNEL");

            PrefUtil.init(mActivity);
            PrefUtil.putString(PrefUtil.ST_THEME, theme);
            PrefUtil.putBool(PrefUtil.ST_LINK_SHOW, linkShow);
            PrefUtil.putBool(PrefUtil.ST_LINK_ACTION, linkAction);
            PrefUtil.putLong(PrefUtil.ST_DELAY_TIME, delayTime);
            PrefUtil.putString(PrefUtil.ST_LINK_TEXT,linkText);
            PrefUtil.putString(PrefUtil.ST_LINK_URL,linkUrl);
        } catch (Exception e) {
            finish();
        }

    }

    private void launchView() {
        try {
            AssetManager am = this.getAssets();
            if (channel.equals("wandoujia")) {
                //删除掉爱吾的代码
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
//                RelativeLayout rl = new RelativeLayout(this);
//                rl.setBackgroundColor(Color.parseColor("#01a0e9"));
//                ImageView iv = new ImageView(this);
//                rl.addView(iv);
//                setContentView(rl, params);
//                String launch = "st_launch.jpg";
//                InputStream ins1 = am.open(launch);
//                iv.setImageDrawable(Drawable.createFromStream(ins1,null));
//                RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//                rlParams.width = 1000;
//                rlParams.height = 1000;
//                iv.setLayoutParams(rlParams);

                String launch = "st_launch.jpg";
                InputStream ins1 = am.open(launch);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
                RelativeLayout rl = new RelativeLayout(this);
//            RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(
//                    -2, -2);
//            rll.addRule(13);
                rl.setBackgroundDrawable(Drawable.createFromStream(ins1, null));
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://dwz.cn/2fNdMF");
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                });
                setContentView(rl, params);

            } else {
                String launch = "st_launch.jpg";
                InputStream ins1 = am.open(launch);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
                RelativeLayout rl = new RelativeLayout(this);
//            RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(
//                    -2, -2);
//            rll.addRule(13);
                rl.setBackgroundDrawable(Drawable.createFromStream(ins1, null));
                setContentView(rl, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

    }


    static class MyThread implements Runnable {
        String src, dest;// src 表示目标压缩文件，dest表示要复制到的目的地
        Context mContext;

        /**
         * 解压assets下文件到指定目录
         *
         * @param context
         * @param src     assets下的文件
         * @param dest    指定的目录
         */
        public MyThread(Context context, String src, String dest) {
            this.src = src;
            this.dest = dest;
            this.mContext = context;
        }

        @Override
        public void run() {
            try {
                unZip(mContext, src, dest, true);
            } catch (IOException e) {

            }

        }


        public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
            // 创建解压目标目录
            File file = new File(outputDirectory);
            // 如果目标目录不存在，则创建
            if (!file.exists()) {
                file.mkdirs();
            }
            // 打开压缩文件
            InputStream inputStream = context.getAssets().open(assetName);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            // 读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            // 使用1Mbuffer
            byte[] buffer = new byte[1024 * 1024];
            // 解压时字节计数
            int count = 0;
            // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
                // 如果是一个目录
                if (zipEntry.isDirectory()) {
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者是文件不存在
                    if (isReWrite || !file.exists()) {
                        file.mkdir();
                    }
                } else {
                    // 如果是文件
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者文件不存在，则解压文件
                    if (isReWrite || !file.exists()) {
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        while ((count = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, count);
                        }
                        fileOutputStream.close();
                    }
                }
                // 定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();
        }

    }

}
