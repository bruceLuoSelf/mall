
package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

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
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class UploadPriceAction extends AbstractFileUploadAction {

	public void setState(String state) {
		this.state = state;
	}

	private static final long serialVersionUID = -2030974717733442846L;

	protected static final Log logger = LogFactory.getLog(UploadPriceAction.class);

	// 封装上传文件域的属性
	private File priceFile;
	
	private String state;

	@Autowired
	IGoodsInfoManager goodsInfoManager;

	@Autowired
	IFileManager fileManager;

	/**
	 * 上传价格信息
	 * 
	 * @return
	 */
	public String uploadPrice() {
		try {
			if (priceFile == null) {
				throw new SystemException(
						ResponseCodes.EmptypriceFile.getCode(),
						ResponseCodes.EmptypriceFile.getMessage());
			}
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(
						new FileInputStream(priceFile));
				ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
				byte[] temp = new byte[1024];
				int size = 0;
				while ((size = in.read(temp)) != -1) {
					out.write(temp, 0, size);
				}
				byte[] content = out.toByteArray();
				goodsInfoManager.mdPriceByExcel(content,state);
			} catch (FileNotFoundException e) {
				logger.debug("上传的文件未找到", e);
				throw new SystemException(
						ResponseCodes.EmptypriceFile.getCode(),
						ResponseCodes.EmptypriceFile.getMessage());
			} catch (IOException e) {
				logger.debug("上传的文件读取失败", e);
				throw new SystemException(
						ResponseCodes.ReadpriceFileError.getCode(),
						ResponseCodes.ReadpriceFileError.getMessage());
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

	public void setPriceFile(File priceFile) {
		this.priceFile = priceFile;
	}

}