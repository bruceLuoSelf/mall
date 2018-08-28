package com.wzitech.gamegold.common.entity;

/**
 * Created by 339928 on 2017/11/24.
 */
public interface IImageRedisDAO {
    /**
     * 获取图片路径
     * @return
     */
    String getImage(String gameId);

    /**
     * 保存路径信息
     */
    void setImageUrl(String gameId,String imageUrl);
}
