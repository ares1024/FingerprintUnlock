package com.hd.test.livedata.biometricmanager;

import androidx.biometric.BiometricPrompt;

public class BMResult {
    public int code;
    public String message;

    public BiometricPrompt.AuthenticationResult authResult;

    @Override
    public String toString() {
        return "BMResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", authResult=" + authResult +
                '}';
    }
}
