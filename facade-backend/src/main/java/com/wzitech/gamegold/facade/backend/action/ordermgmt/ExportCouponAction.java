package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.CouponType;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import com.wzitech.gamegold.facade.backend.excel.ExportExcel;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import com.wzitech.gamegold.order.business.IDiscountCouponManager;
import com.wzitech.gamegold.order.entity.DiscountCoupon;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Scope("prototype")
public class ExportCouponAction extends AbstractAction {
    private DiscountCoupon discountCoupon;

    private Boolean isUsed;

    private InputStream inputStream;

    private int num;

    @Autowired
    IDiscountCouponManager discountCouponManager;

    public String validateUser(){
        UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
        if(user!=null){
            if(!user.getLoginAccount().equals("wangjj@5173.com")&&!user.getLoginAccount().equals("ymq@5173.com"))
            {
                return this.returnError("您没有该权限");
            }
        }
        else{
            return this.returnError("您没有该权限");
        }
        return this.returnSuccess();
    }

    /**
     * 导出优惠券记录
     * @return
     */
    public String exportCoupon()
    {
        List<DiscountCoupon> list= queryDiscountCouponList();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet();

        ExportExcel exportExcel = new ExportExcel(wb, sheet);

        // 创建单元格样式
        HSSFCellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);

        // 创建报表头部
        String headString = "优惠券列表";
        int columnSize = 8;
        exportExcel.createNormalHead(0, headString, columnSize-1);

        // 创建报表列
        String[] columHeader = new String[] { "优惠券码","密码", "金额", "类型", "是否已使用", "订单号", "开始时间", "结束时间" };
        exportExcel.createColumHeader(1, columHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        // 循环创建中间的单元格的各项的值
        if (list != null) {
            int i = 2;
            for (DiscountCoupon discountCoupon : list) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.cteateCell(wb, row, (short) 0,cellstyle,discountCoupon.getCode());
                exportExcel.cteateCell(wb, row, (short) 1,cellstyle,discountCoupon.getPwd());
                String price="";
                if(discountCoupon.getPrice()!=null){
                    price="￥" +discountCoupon.getPrice().toString();
                }
                exportExcel.cteateCell(wb, row, (short) 2,cellstyle,price);
                exportExcel.cteateCell(wb, row, (short) 3,cellstyle, CouponType.getTypeByCode(discountCoupon.getCouponType()).getName());
                exportExcel.cteateCell(wb, row, (short) 4,cellstyle,discountCoupon.getIsUsed()?"已使用":"未使用");
                exportExcel.cteateCell(wb, row, (short) 5,cellstyle,discountCoupon.getOrderId());
                exportExcel.cteateCell(wb, row, (short) 6, cellstyle, format.format(discountCoupon.getStartTime()));
                exportExcel.cteateCell(wb, row, (short) 7, cellstyle,format.format(discountCoupon.getEndTime()));
            }
        }
        String exportPath = WebServerContants.FILES_EXPORT_PATH;
        String path = ServletActionContext.getServletContext().getRealPath(exportPath);
        File file = new File(path);
        file.mkdirs();
        String filePath = path + "/" + UUID.randomUUID().toString() + ".xls";
        exportExcel.outputExcel(filePath);
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.returnSuccess();
    }

    /**
     * 批量生成优惠券
     * @return
     */
    public String generateCoupon()
    {
        try {
            List<DiscountCoupon> list=new ArrayList<DiscountCoupon>();
            List<DiscountCoupon> sheetList=new ArrayList<DiscountCoupon>();
            Integer couponType=discountCoupon.getCouponType();
            Date startTime=discountCoupon.getStartTime();
            Date endTime=discountCoupon.getEndTime();
            Double price= discountCoupon.getPrice();

            Set<String> set=new HashSet<String>();

            while(set.size()<num){
                String time=new Date().getTime()+"";
                String code=getCharAndNumr(1)+time.substring(0,5)+getCharAndNumr(1)+time.substring(5, 8)+getCharAndNumr(1)+time.substring(8)+getCharAndNumr(1);
                if(!set.contains(code)){
                    set.add(code);
                }
            }

            Iterator<String> it= set.iterator();
            for(int i=0;i<set.size();i++){
                DiscountCoupon discountCoupon=new DiscountCoupon();
                discountCoupon.setPrice(price);
                discountCoupon.setCode(it.next());
                discountCoupon.setPwd(getCharAndNumr(6));
                discountCoupon.setCouponType(couponType);
                discountCoupon.setStartTime(startTime);
                discountCoupon.setEndTime(endTime);
                list.add(discountCoupon);
                sheetList.add(discountCoupon);

                if(i!=0&&i%50==0){
                    discountCouponManager.batchInsert(list);
                    list.clear();
                }
            }
            if(list.size()!=0){
                discountCouponManager.batchInsert(list);
            }

            //导出优惠券
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            HSSFWorkbook wb = new HSSFWorkbook();

            HSSFSheet sheet = wb.createSheet();

            ExportExcel exportExcel = new ExportExcel(wb, sheet);

            // 创建单元格样式
            HSSFCellStyle cellStyle = wb.createCellStyle();

            // 指定单元格居中对齐
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            // 指定单元格垂直居中对齐
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            // 指定当单元格内容显示不下时自动换行
            cellStyle.setWrapText(true);

            // 设置单元格字体
            HSSFFont font = wb.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontName("宋体");
            font.setFontHeight((short) 200);
            cellStyle.setFont(font);

            // 创建报表头部
            String headString = "优惠券列表";
            int columnSize = 6;
            exportExcel.createNormalHead(0, headString, columnSize-1);

            // 创建报表列
            String[] columHeader = new String[] { "优惠券码","密码","金额", "类型", "开始时间", "结束时间" };
            exportExcel.createColumHeader(1, columHeader);

            HSSFCellStyle cellstyle = wb.createCellStyle();
            cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

            // 循环创建中间的单元格的各项的值
            if (sheetList != null) {
                int i = 2;
                for (DiscountCoupon discountCoupon : sheetList) {
                    HSSFRow row = sheet.createRow((short) i++);
                    exportExcel.cteateCell(wb, row, (short) 0,cellstyle,discountCoupon.getCode());
                    exportExcel.cteateCell(wb, row, (short) 1,cellstyle,discountCoupon.getPwd());
                    String priceTemp="";
                    if(discountCoupon.getPrice()!=null){
                        priceTemp="￥" +discountCoupon.getPrice().toString();
                    }
                    exportExcel.cteateCell(wb, row, (short) 2,cellstyle,priceTemp);
                    exportExcel.cteateCell(wb, row, (short) 3,cellstyle, CouponType.getTypeByCode(discountCoupon.getCouponType()).getName());
                    exportExcel.cteateCell(wb, row, (short) 4, cellstyle, format.format(discountCoupon.getStartTime()));
                    exportExcel.cteateCell(wb, row, (short) 5, cellstyle,format.format(discountCoupon.getEndTime()));
                }
            }
            String exportPath = WebServerContants.FILES_EXPORT_PATH;
            String path = ServletActionContext.getServletContext().getRealPath(exportPath);
            File file = new File(path);
            file.mkdirs();
            String filePath = path + "/" + UUID.randomUUID().toString() + ".xls";
            exportExcel.outputExcel(filePath);
            try {
                inputStream = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 获取优惠券记录列表
     * @return
     */
    private List<DiscountCoupon> queryDiscountCouponList()
    {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if(discountCoupon.getCouponType()!=0){
            paramMap.put("couponType",discountCoupon.getCouponType());
        }
        if(!StringUtils.isBlank(discountCoupon.getCode())){
            paramMap.put("code",discountCoupon.getCode());
        }
        if(!StringUtils.isBlank(discountCoupon.getOrderId()))
        {
            paramMap.put("orderId",discountCoupon.getOrderId());
        }
        paramMap.put("isUsed",isUsed);
        paramMap.put("startTime", discountCoupon.getStartTime());
        paramMap.put("endTime", WebServerUtil.oneDateLastTime(discountCoupon.getEndTime()));
        List<DiscountCoupon> list = discountCouponManager.queryDiscountCouponList(paramMap, "ID", true);
        return list;
    }

    /**
     * 生成随机数
     * @param length
     * @return
     */
    public  String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public DiscountCoupon getDiscountCoupon() {
        return discountCoupon;
    }

    public void setDiscountCoupon(DiscountCoupon discountCoupon) {
        this.discountCoupon = discountCoupon;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
