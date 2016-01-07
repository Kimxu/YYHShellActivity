package com.game.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.appchina.YYHShellActivity.R;
import com.sthh.utils.STApi;

/**
 *
 * Created by shuuseiyang on 15-4-24.
 */
public class Game extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        STApi.onCreate(this);

    }

    @Override
    protected void onResume() {
        STApi.onResume(this);
        super.onResume();
    }


    @Override
    protected void onPause() {
        STApi.onPause(this);
        super.onPause();
    }
}
