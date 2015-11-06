package com.game.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.appchina.YYHShellActivity.R;
import com.appchina.utils.ShakeUtils;

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
        ShakeUtils.open(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        ShakeUtils.stop();
        super.onStop();

    }
}
