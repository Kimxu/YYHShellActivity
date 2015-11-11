package com.appchina.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 提示框
 * Created by xuzhiguo on 15/11/6.
 */
public class NotifyView extends RelativeLayout {

    private DissmissCallBack dissmissCallBack;
    private String TAG = "NotifyView";
    private ImageView mIvLogo;
    private ImageView mIvCancel;
    private TextView mTvContent;
    private String textColor;

    public NotifyView(Context context, String theme,boolean hasClose) {
        super(context);
        init(context, theme,hasClose);
    }


    private void init(final Context context, String theme,boolean hasClose) {
        mIvLogo = new ImageView(context);
        mIvCancel = new ImageView(context);
        mTvContent = new TextView(context);
        mIvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(GONE);
                if (dissmissCallBack != null) {
                    dissmissCallBack.onCallback();
                }
            }
        });
        if (!hasClose)
            mIvCancel.setVisibility(GONE);
        mTvContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://box.18touch.com/");
                intent.setData(content_url);
                context.startActivity(intent);
            }
        });
        AssetManager am = context.getAssets();
        InputStream logo = null;
        InputStream cancel = null;
        InputStream bg = null;
        try {
            if (theme.equals("light")) {
                textColor = "#333333";

                logo = am.open("st_light_logo.png");
                bg = am.open("st_bg_light.png");

            } else {
                textColor = "#ffffff";
                bg = am.open("st_bg_dark.png");
                logo = am.open("st_dark_logo.png");

            }
            cancel = am.open("st_cancel.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mIvCancel.setBackgroundDrawable(Drawable.createFromStream(cancel, null));
        mIvLogo.setBackgroundDrawable(Drawable.createFromStream(logo, null));
        mTvContent.setTextColor(Color.parseColor(textColor));
        mTvContent.setPadding(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        mTvContent.setText("该版权由手谈汉化组所有详情请前往：box.18touch");


        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                dip2px(60), dip2px(60));
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mIvLogo.setLayoutParams(imageParams);
        mIvLogo.setId(1);

        RelativeLayout.LayoutParams cancelParams = new RelativeLayout.LayoutParams(
                dip2px(20), dip2px(20));
        cancelParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        cancelParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mIvCancel.setLayoutParams(cancelParams);
        mIvCancel.setId(2);


        RelativeLayout.LayoutParams tvContentParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvContentParams.addRule(RelativeLayout.RIGHT_OF, mIvLogo.getId());
        tvContentParams.addRule(RelativeLayout.CENTER_VERTICAL);
        mTvContent.setLayoutParams(tvContentParams);


        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setPadding(dip2px(10), 0, 0, 0);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(mIvLogo);
        RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(dip2px(210), dip2px(80));
        lParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.addView(mTvContent);
        linearLayout.setBackgroundDrawable(Drawable.createFromStream(bg, null));
        linearLayout.setLayoutParams(lParams);


        this.addView(linearLayout);
        this.addView(mIvCancel);

        this.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

    }


    public void setDissmissCallBack(DissmissCallBack dissmissCallBack) {
        this.dissmissCallBack = dissmissCallBack;
    }

    public int dip2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public interface DissmissCallBack {
        void onCallback();
    }

    public void setCancelVisibility(int visibility) {
        mIvCancel.setVisibility(visibility);
    }

    private float x;
    private float y;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - x) > 10 || Math.abs(ev.getY() - y) > 10)
                    return true;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }
}
