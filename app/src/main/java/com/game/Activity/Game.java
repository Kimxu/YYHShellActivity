package com.game.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.appchina.YYHShellActivity.R;
import com.appchina.utils.STApi;

/**
 *
 * Created by shuuseiyang on 15-4-24.
 */
public class Game extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


    }

    @Override
    protected void onResume() {
        STApi.getInstance(this).show(0,200);
        STApi.getInstance(this).open(100,200);
        super.onResume();
    }

    @Override
    protected void onStop() {
        STApi.getInstance(this).stop();
        super.onStop();

    }
}
