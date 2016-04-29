package com.grishberg.dailyselfie.data.db;

/**
 * Created by grishberg on 28.04.16.
 */
public interface ListResult<T> extends BaseResult {
    T getItem(int index);
    int getCount();
}
