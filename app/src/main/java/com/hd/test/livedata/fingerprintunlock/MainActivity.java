package com.hd.test.livedata.fingerprintunlock;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hd.test.livedata.biometricmanager.BM;
import com.hd.test.livedata.biometricmanager.BMMessage;
import com.hd.test.livedata.biometricmanager.BMResult;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "BM MAIN ACTIVITY";

    private BM mBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBM = new BM(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                BMMessage message = new BMMessage();
                message.title = "测试";
                message.subtitle = "请 按指纹！！";
                message.negativeButtonText = "取消";
                mBM.startAuth(message).observe(MainActivity.this, new Observer<BMResult>() {
                    @Override
                    public void onChanged(BMResult bmResult) {
                        Log.d(TAG, "BMResult: " + bmResult.toString());
                    }
                });
            }
        });
    }
}