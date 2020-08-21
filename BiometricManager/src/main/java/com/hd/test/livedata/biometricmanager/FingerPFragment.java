package com.hd.test.livedata.biometricmanager;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.Objects;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

public class FingerPFragment extends Fragment implements IFingerprintInterface {
    public MutableLiveData<BMResult> mLiveData;
    private BiometricPrompt mBiometricPrompt;
    private Handler handler = new Handler();

    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void startAuth(BMMessage message) {
        mLiveData = new MutableLiveData<>();
        final BMResult result = new BMResult();
        boolean isCan = false;

        BiometricManager biometricManager = BiometricManager.from(Objects.requireNonNull(getContext()));
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                isCan = true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                result.code = BM.BIOMETRIC_ERROR_NO_HARDWARE;
                result.message = "BIOMETRIC_ERROR_NO_HARDWARE";
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                result.code = BM.BIOMETRIC_ERROR_HW_UNAVAILABLE;
                result.message = "BIOMETRIC_ERROR_HW_UNAVAILABLE";
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                result.code = BM.BIOMETRIC_ERROR_NONE_ENROLLED;
                result.message = "BIOMETRIC_ERROR_NONE_ENROLLED";
                break;
            default:
                break;
        }

        if (!isCan) {//如果 不能使用识别 则返回错误
            mLiveData.postValue(result);
            return;
        }

        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(message.title) //设置大标题
                        .setSubtitle(message.subtitle) // 设置标题下的提示
                        .setNegativeButtonText(message.negativeButtonText) //设置取消按钮
                        .build();

        //需要提供的参数callback
        mBiometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            //各种异常的回调
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                result.code = errorCode;
                result.message = errString.toString();
                mLiveData.postValue(result);
            }

            //认证成功的回调
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult authResult) {
                super.onAuthenticationSucceeded(authResult);
                BiometricPrompt.CryptoObject authenticatedCryptoObject = authResult.getCryptoObject();
                result.code = BM.BIOMETRIC_SUCCESS;
                result.message = "BIOMETRIC_SUCCESS";
                result.authResult = authResult;
                mLiveData.postValue(result);
            }

            //认证失败的回调
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                result.code = BM.BIOMETRIC_AUTHENTICATION_FAILED;
                result.message = "AuthenticationFailed";
                mLiveData.postValue(result);
            }
        });

        // 显示认证对话框
        mBiometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void cancelAuth() {
        if (mBiometricPrompt != null) {
            mBiometricPrompt.cancelAuthentication();
        }
    }
}
