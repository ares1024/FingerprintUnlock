package com.hd.test.livedata.biometricmanager;

public interface IFingerprintInterface {
    void startAuth(BMMessage message);

    void cancelAuth();
}
