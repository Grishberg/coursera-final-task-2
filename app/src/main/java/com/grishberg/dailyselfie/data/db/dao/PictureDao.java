package com.grishberg.dailyselfie.data.db.dao;

import com.grishberg.dailyselfie.common.db.ListResult;
import com.grishberg.dailyselfie.common.db.SingleResult;
import com.grishberg.dailyselfie.data.model.Pictures;

/**
 * Created by grishberg on 28.04.16.
 */
public interface PictureDao {
    void storePicture(String path);
    ListResult<Pictures> getPictures();

    SingleResult<Pictures> getPicture(long id);
}
