package com.xingjiezheng;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tvData;
    private TextView tvTime;
    int hour = 0;
    int minute = 0;
    int second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        tvData = (TextView) findViewById(R.id.tv_data);

        tvData.setText(getData());
        tvTime = (TextView) findViewById(R.id.tv_time);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startTicks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTicks();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public native String getData();

    private native void startTicks();

    private native void stopTicks();

    /*
    * A function calling from JNI to update current timer
    */
    @Keep
    private void updateTimer() {
        ++second;
        if (second >= 60) {
            ++minute;
            second -= 60;
            if (minute >= 60) {
                ++hour;
                minute -= 60;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String ticks = "" + MainActivity.this.hour + ":" +
                        MainActivity.this.minute + ":" +
                        MainActivity.this.second;
                MainActivity.this.tvTime.setText(ticks);
            }
        });
    }
}
