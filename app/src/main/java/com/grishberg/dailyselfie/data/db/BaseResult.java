package com.grishberg.dailyselfie.data.db;

/**
 * Created by grishberg on 28.04.16.
 */
public interface BaseResult {
    void addDataReceiveObserver(DataReceiveObserver observer);
    void removeDataReceiveObserver(DataReceiveObserver observer);
    boolean isLoaded();
}
