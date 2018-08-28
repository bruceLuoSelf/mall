package com.wzitech.gamegold.common.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Excel工具
 *
 * @author yemq
 */
public class ExcelUtils {

    /**
     * 复制一个Excel模板
     *
     * @param filePath excel模板路径
     * @return
     * @throws Exception
     */
    public static Workbook getCloneWorkBook(String filePath) throws Exception {
        InputStream in = ExcelUtils.class.getResourceAsStream(filePath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = -1;
        while ((len = in.read(buffer)) > -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        Workbook wb = WorkbookFactory.create(is);
        is.close();
        out.close();
        in.close();
        return wb;
    }


}
