package com.grishberg.dailyselfie.common.db;

/**
 * Created by grishberg on 30.04.16.
 */
public interface DataStoreObserver {
    void onDataStored(long id);
}
