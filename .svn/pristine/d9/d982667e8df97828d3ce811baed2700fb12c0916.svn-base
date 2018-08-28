package com.wzitech.gamegold.facade.backend.action.repository;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.filemgmt.business.IFileManager;
import com.wzitech.gamegold.repository.business.IRepositoryManager;

/**
 * 通货库存上传接口
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  wubiao           ZW_C_JB_00008 商城增加通货
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class UploadRepositoryAction extends AbstractFileUploadAction {

	private static final long serialVersionUID = 2212456161675686700L;
	
	protected static final Log logger = LogFactory.getLog(UploadRepositoryAction.class);

	// 封装上传文件域的属性
	private File repositoryFile;

	//ZW_C_JB_00008_20170512 ADD '商品类目'
	private String goodsTypeName;

	@Autowired
	IRepositoryManager repositoryManager;

	@Autowired
	IFileManager fileManager;

	/**
	 * 上传库存信息
	 * 
	 * @return
	 */
	public String uploadRepository() {
		try {
			if (repositoryFile == null) {
				throw new SystemException(
						ResponseCodes.EmptyRepositoryFile.getCode(),
						ResponseCodes.EmptyRepositoryFile.getMessage());
			}
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(
						new FileInputStream(repositoryFile));
				ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
				byte[] temp = new byte[1024];
				int size = 0;
				while ((size = in.read(temp)) != -1) {
					out.write(temp, 0, size);
				}
				byte[] content = out.toByteArray();
				repositoryManager.batchAddRepository(content,goodsTypeName);//ZW_C_JB_00008_20170513 MODIFY
			} catch (FileNotFoundException e) {
				logger.debug("上传的文件未找到", e);
				throw new SystemException(
						ResponseCodes.EmptyRepositoryFile.getCode(),
						ResponseCodes.EmptyRepositoryFile.getMessage());
			} catch (IOException e) {
				logger.debug("上传的文件读取失败", e);
				throw new SystemException(
						ResponseCodes.ReadRepositoryFileError.getCode(),
						ResponseCodes.ReadRepositoryFileError.getMessage());
			} finally{
				try {
					if(in!=null){
						in.close();						
					}
				} catch (IOException e) {
					logger.debug("上传的文件打开后，关闭失败", e);
				}
			}
			return this.returnSuccess();
		} catch (SystemException e) {
			return this.returnError(e);
		}
	}

	public void setRepositoryFile(File repositoryFile) {
		this.repositoryFile = repositoryFile;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
}