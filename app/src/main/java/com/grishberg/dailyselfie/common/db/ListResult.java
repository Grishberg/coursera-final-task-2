package com.grishberg.dailyselfie.common.db;

import com.grishberg.dailyselfie.common.db.BaseResult;

/**
 * Created by grishberg on 28.04.16.
 */
public interface ListResult<T> extends BaseResult {
    T getItem(int index);
    int getCount();
    void release();
}
