package com.wzitech.gamegold.repository.business;

import com.wzitech.gamegold.common.entity.IUser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.wsdl.Input;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 库存上传处理器接口
 * @author yemq
 */
public interface IRepositoryUploadProcessor {
    /**
     * 对上传的excel进行处理
     * @param gameName 游戏名称
     * @param in 上传的excel文件流
     */
    void process(String gameName, InputStream in,IUser seller) throws IOException, InvalidFormatException;

}
