package com.hd.test.livedata.biometricmanager;


import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

public class BM {

    private static final String TAG = "bm_fragment";

    public static final int BIOMETRIC_SUCCESS = BiometricManager.BIOMETRIC_SUCCESS;
    public static final int BIOMETRIC_ERROR_NO_HARDWARE = BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE;
    public static final int BIOMETRIC_ERROR_HW_UNAVAILABLE = BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;
    public static final int BIOMETRIC_ERROR_NONE_ENROLLED = BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED;
    public static final int BIOMETRIC_AUTHENTICATION_FAILED = -1;

    private FingerPFragment mFragment;

    public BM(AppCompatActivity activity) {
        setFragment(activity.getSupportFragmentManager());
    }

    public BM(Fragment fragment) {
        setFragment(fragment.getChildFragmentManager());
    }

    private void setFragment(FragmentManager fm) {
        if (mFragment == null) {
            synchronized (BM.this) {
                if (mFragment == null) {
                    if (fm.findFragmentByTag(TAG) == null) {
                        mFragment = new FingerPFragment();
                        fm.beginTransaction().add(mFragment, TAG).commitNow();
                    } else {
                        mFragment = (FingerPFragment) fm.findFragmentByTag(TAG);
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public MutableLiveData<BMResult> startAuth(BMMessage message) {
        mFragment.startAuth(message);
        return mFragment.mLiveData;
    }

    public void cancelAuth() {
        mFragment.cancelAuth();
    }
}
