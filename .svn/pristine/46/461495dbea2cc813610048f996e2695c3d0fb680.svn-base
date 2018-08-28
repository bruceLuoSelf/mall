package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.RepositoryConstants;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.IRepositoryUploadProcessor;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 库存上传处理器
 *
 * @author yemq
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/19  wubiao           ZW_C_JB_00008 商城增加通货
 */
@Component
public class RepositoryUploadProcessorImpl implements IRepositoryUploadProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryUploadProcessorImpl.class);

    @Autowired
    IRepositoryManager repositoryManager;

    /*************ZW_C_JB_00008_20170519 ADD '库存上传' START**********/
    @Autowired
    private IShGameConfigManager shGameConfigManager;
   /*************ZW_C_JB_00008_20170519 ADD '库存上传' END***********/

    /**
     * 对上传的excel进行处理
     *
     * @param gameName
     * @param in
     */
    @Override
    public void process(String gameName, InputStream in, IUser seller) throws IOException, InvalidFormatException {
        try {
            Workbook wb = create(in);
            Sheet sheet = wb.getSheetAt(0);
            // 没有数据
            if (sheet.getLastRowNum() == 0) {
                throw new SystemException(ResponseCodes.EmptyUploadFile.getCode(),
                        ResponseCodes.EmptyUploadFile.getMessage());
            }
            // 检查库存字段数量是否正确
            int fieldCount = getFieldsCount(gameName); // 获取对应游戏的库存字段数量
            int cellCount = sheet.getRow(0).getLastCellNum(); // 获取第一行的单元格数量
            /**************ZW_C_JB_00008_20170518 ADD '库存上传' START************************************************/
            int total = cellCount-fieldCount;
            if (total!=0&&total!=1) {
                throw new SystemException(ResponseCodes.ErrorRepositoryTemplate.getCode(),
                        ResponseCodes.ErrorRepositoryTemplate.getMessage());
            }
            /**************ZW_C_JB_00008_20170518 ADD '库存上传' END************************************************/
            // 从第2行开始读取数据，一直读到倒数第2行
            Map<String, ShGameConfig> shGameConfigMap = new HashMap();
            repositoryManager.clearAddRepositoryData();
            for (int index = 1, count = sheet.getLastRowNum()-2; index < count; index++) {
                //循环读取每一行
                Row row = sheet.getRow(index);
                if (row == null) continue;
                // 获取一行库存记录
                RepositoryInfo repositoryInfo = getRepositoryFromRow(index+1, row, gameName,seller);
                /***************************ZW_C_JB_00008_20170519 ADD '库存上传' START**************************/
                String key = repositoryInfo.getGameName() + ":" + repositoryInfo.getGoodsTypeName();
                ShGameConfig shGameConfig = shGameConfigMap.get(key);
                if (!shGameConfigMap.containsKey(key)) {
                    shGameConfig = shGameConfigManager.getConfigByGameName(repositoryInfo.getGameName(), repositoryInfo.getGoodsTypeName(), null, true);
                    shGameConfigMap.put(key, shGameConfig);
                }
                if (shGameConfig == null) {
                    String msg = String.format("第%s行，没有找到与当前游戏匹配的商品类目", index);
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(), msg);
                }
                if(!StringUtils.equals(shGameConfig.getUnitName(), repositoryInfo.getMoneyName())){
                    String msg = String.format("第%s行，该商品类型单位必须是%s", index, shGameConfig.getUnitName());
                    if(StringUtils.isBlank(shGameConfig.getUnitName())){
                        msg = String.format("第%s行，该商品类型单位必须为空", index);
                    }
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(), msg);
                }

                repositoryInfo.setGoodsTypeId(shGameConfig.getGoodsTypeId());
                /**************************ZW_C_JB_00008_20170519 ADD '库存上传' START**************************/
                // 将这行库存记录更新到数据库
                repositoryManager.addRepository(repositoryInfo);
            }
            repositoryManager.commitAddRepositoryData();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取一行库存数据
     *
     * @param index
     * @param row
     * @param gameName
     * @return
     */
    protected RepositoryInfo getRepositoryFromRow(int index, Row row, String gameName, IUser seller) {
        // 检查库存字段数量是否正确
        int fieldCount = getFieldsCount(gameName); // 库存模块应有的字段数量
        int cellCount = row.getLastCellNum();   // 这行的单元格数量
        /**************ZW_C_JB_00008_20170519 ADD  START*****************/
        int field_Count = 0;
        if (cellCount > fieldCount) {
            field_Count = getCategoryFieldsCount(gameName);
        } else {
            field_Count = getFieldsCount(gameName);
        }
        /**************ZW_C_JB_00008_20170519 ADD  END*****************/
        List<String> fieldValues = new ArrayList<String>(); // 每列的值

        for (int j = 0; j < cellCount && j < field_Count; j++) {
            // 循环读取每一列
            String data = getStringCellValue(row.getCell(j));
            if (StringUtils.isNotBlank(data)) {
                data = data.trim();
            }
            fieldValues.add(data);
        }

        RepositoryInfo repositoryInfo = new RepositoryInfo();
        String[] fields = null;
        int total = cellCount - fieldCount;
        if (total == 1) {
            //表示的是通货库存文件
            fields = getCategoryFields(gameName);
            //RepositoryInfo repositoryInfo = new RepositoryInfo();
            for (int j = 0; j < field_Count; j++) {
                String field = fields[j]; // 每列的字段
                String data = fieldValues.get(j); // 每列的值
                // 检查字段是否正确
                checkCurrencyField(index, field, data);
                // 设置库存字段的值
                if (StringUtils.equals(field, "游戏名称")) {
                    if (!StringUtils.equals(gameName, data)) {
                        String msg = String.format("第%s行，上传的游戏名称与库存模板不符", index);
                        throw new SystemException(ResponseCodes.IllegalArguments.getCode(), msg);
                    }
                    repositoryInfo.setGameName(data);
                } else if (StringUtils.equals(field, "商品类目")) {
                    repositoryInfo.setGoodsTypeName(data);
                } else if (StringUtils.equals(field, "游戏区")) {
                    repositoryInfo.setRegion(data);
                } else if (StringUtils.equals(field, "游戏服")) {
                    repositoryInfo.setServer(data);
                } else if (StringUtils.equals(field, "游戏阵营")) {
                    repositoryInfo.setGameRace(data);
                } else if (StringUtils.equals(field, "单位")) {
                    repositoryInfo.setMoneyName(data);
                } else if (StringUtils.equals(field, "单价")) {
                    if (StringUtils.isNotBlank(data)) {
                        BigDecimal price = new BigDecimal(data);
                        price = price.setScale(RepositoryConstants.MAX_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP);
                        repositoryInfo.setUnitPrice(price);
                    }
                } else if (StringUtils.equals(field, "库存数量")) {
                    if (StringUtils.isNotBlank(data)) {
                        BigDecimal bd = new BigDecimal(data);
                        long count = bd.longValue();
                        if (count > RepositoryConstants.MAX_REPOSITORY_COUNT)
                            count = RepositoryConstants.MAX_REPOSITORY_COUNT;
                        repositoryInfo.setGoldCount(count);
                    }
                } else if (StringUtils.equals(field, "可销售库存")) {
                    if (StringUtils.isNotBlank(data)) {
                        BigDecimal bd = new BigDecimal(data);
                        long count = bd.longValue();
                        if (count > RepositoryConstants.MAX_REPOSITORY_COUNT)
                            count = RepositoryConstants.MAX_REPOSITORY_COUNT;
                        repositoryInfo.setSellableCount(count);
                    }
                } else if (StringUtils.equals(field, "游戏账号")) {
                    repositoryInfo.setGameAccount(data);
                } else if (StringUtils.equals(field, "游戏密码")) {
                    repositoryInfo.setGamePassWord(data);
                } else if (StringUtils.equals(field, "游戏角色名")) {
                    repositoryInfo.setSellerGameRole(data);
                } else if (StringUtils.equals(field, "二级密码")) {
                    repositoryInfo.setSonGamePassWord(data);
                } else if (StringUtils.equals(field, "仓库密码")) {
                    repositoryInfo.setHousePassword(data);
                }
            }
        } else if (total == 0) {
            //如果相等表示上传游戏币库存
            fields = getFields(gameName);
            for (int j = 0; j < fieldCount; j++) {
                String field = fields[j]; // 每列的字段
                String data = fieldValues.get(j); // 每列的值

                // 检查字段是否正确
                checkField(index, field, data);

                // 设置库存字段的值
                if (StringUtils.equals(field, "游戏名称")) {
                    if (!StringUtils.equals(gameName, data)) {
                        String msg = String.format("第%s行，上传的游戏名称与库存模板不符", index);
                        throw new SystemException(ResponseCodes.IllegalArguments.getCode(), msg);
                    }
                    repositoryInfo.setGameName(data);
                } else if (StringUtils.equals(field, "游戏区")) {
                    repositoryInfo.setRegion(data);
                } else if (StringUtils.equals(field, "游戏服")) {
                    repositoryInfo.setServer(data);
                } else if (StringUtils.equals(field, "游戏阵营")) {
                    repositoryInfo.setGameRace(data);
                } else if (StringUtils.equals(field, "游戏币单位")) {
                    repositoryInfo.setMoneyName(data);
                } else if (StringUtils.equals(field, "单价")) {
                    if (StringUtils.isNotBlank(data)) {
                        BigDecimal price = new BigDecimal(data);
                        price = price.setScale(RepositoryConstants.MAX_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP);
                        repositoryInfo.setUnitPrice(price);
                    }
                } else if (StringUtils.equals(field, "库存数量")) {
                    if (StringUtils.isNotBlank(data)) {
                        BigDecimal bd = new BigDecimal(data);
                        long count = bd.longValue();
                        if (count > RepositoryConstants.MAX_REPOSITORY_COUNT)
                            count = RepositoryConstants.MAX_REPOSITORY_COUNT;
                        repositoryInfo.setGoldCount(count);
                    }
                } else if (StringUtils.equals(field, "可销售库存")) {
                    if (StringUtils.isNotBlank(data)) {
                        BigDecimal bd = new BigDecimal(data);
                        long count = bd.longValue();
                        if (count > RepositoryConstants.MAX_REPOSITORY_COUNT)
                            count = RepositoryConstants.MAX_REPOSITORY_COUNT;
                        repositoryInfo.setSellableCount(count);
                    }
                } else if (StringUtils.equals(field, "游戏账号")) {
                    repositoryInfo.setGameAccount(data);
                } else if (StringUtils.equals(field, "游戏密码")) {
                    repositoryInfo.setGamePassWord(data);
                } else if (StringUtils.equals(field, "游戏角色名")) {
                    repositoryInfo.setSellerGameRole(data);
                } else if (StringUtils.equals(field, "二级密码")) {
                    repositoryInfo.setSonGamePassWord(data);
                } else if (StringUtils.equals(field, "仓库密码")) {
                    repositoryInfo.setHousePassword(data);
                }
                //商品类型为游戏币
                repositoryInfo.setGoodsTypeName(ServicesContants.GOODS_TYPE_GOLD);//ZW_C_JB_00008_20170522 MODIFY
            }
        } else {
            String msg = String.format("第%s行，库存字段数量不对，上传的值：%s", index, fieldValues);
            throw new SystemException(ResponseCodes.ErrorRepositoryTemplate.getCode(), msg);
        }

        // 设置卖家信息
//        IUser seller = CurrentUserContext.getUser();
        if (seller != null) {
            repositoryInfo.setLoginAccount(seller.getLoginAccount());
            repositoryInfo.setAccountUid(seller.getUid());
        } else {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
        }

        repositoryInfo.setIsDeleted(false);
        repositoryInfo.setLastUpdateTime(new Date());
        return repositoryInfo;
    }

    /**
     * 检查字段值是否符合要求
     *
     * @param index      第几行
     * @param fieldName  字段值
     * @param fieldValue 字段值
     * @return
     */
    protected boolean checkField(int index, String fieldName, String fieldValue) {
        if (StringUtils.equals(fieldName, "游戏名称")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                        "第" + index + "行，游戏名不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏区")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyRegion.getCode(),
                        "第" + index + "行，游戏区不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏服")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameServer.getCode(),
                        "第" + index + "行，游戏服不能为空");
            }
        } else if (StringUtils.equals(fieldName, "阵营")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameRace.getCode(),
                        "第" + index + "行，游戏阵营不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏币单位")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyMoneyName.getCode(),
                        "第" + index + "行，游戏币单位不能为空");
            }
        } else if (StringUtils.equals(fieldName, "单价")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyUnitPrice.getCode(),
                        "第" + index + "行，发布单价不能为空");
            }*/
            if (StringUtils.isNotBlank(fieldValue)) {
                BigDecimal price = null;
                try {
                    price = new BigDecimal(fieldValue);
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，单价：'" + fieldValue + "'错误，请输入正确的数字");
                }
                if (BigDecimal.ZERO.compareTo(price) >= 0) {
                    throw new SystemException(ResponseCodes.UnitPriceMustGreaterThanZero.getCode(),
                            "第" + index + "行，单价必须大于0");
                }
            }
        } else if (StringUtils.equals(fieldName, "库存数量")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                        "第" + index + "行，库存数量不能为空");
            }*/
            if (StringUtils.isNotBlank(fieldValue)) {
                Long count = null;
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    count = bd.longValue();
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，库存数量：'" + fieldValue + "'错误，请输入整数");
                }
                if (count.longValue() < 0) {
                    throw new SystemException(ResponseCodes.EmptyRepositoryGold.getCode(),
                            "第" + index + "行，库存数量必须大于0");
                }
            }
        } else if (StringUtils.equals(fieldName, "可销售库存")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                        "第" + index + "行，可销售库存不能为空");
            }*/
            if (StringUtils.isNotBlank(fieldValue)) {
                Long count = null;
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    count = bd.longValue();
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，可销售库存：'" + fieldValue + "'错误，请输入整数");
                }
                if (count.longValue() < 0) {
                    throw new SystemException(ResponseCodes.EmptySellableGoldCount.getCode(),
                            "第" + index + "行，可销售库存必须大于0");
                }
            }
        } else if (StringUtils.equals(fieldName, "游戏账号")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameAccount.getCode(),
                        "第" + index + "行，游戏账号不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏密码")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGamePassWord.getCode(),
                        "第" + index + "行，游戏密码不能为空");
            }*/
        } else if (StringUtils.equals(fieldName, "游戏角色名")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptySellerGameRole.getCode(),
                        "第" + index + "行，游戏角色名不能为空");
            }
        } else if (StringUtils.equals(fieldName, "二级密码")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptySecondaryPwd.getCode(),
                        "第" + index + "行，二级密码不能为空");
            }*/
        } else if (StringUtils.equals(fieldName, "仓库密码")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyWarehousePwd.getCode(),
                        "第" + index + "行，仓库密码不能为空");
            }*/
        }

        return true;
    }

    /**
     * 新增
     * @param index
     * @param fieldName
     * @param fieldValue
     * @return
     */
    protected boolean checkCurrencyField(int index, String fieldName, String fieldValue) {
        if (StringUtils.equals(fieldName, "游戏名称")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                        "第" + index + "行，游戏名不能为空");
            }
        } else if (StringUtils.equals(fieldName, "商品类目")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyCategoryValue.getCode(),
                        "第" + index + "行，商品类目不能为空");
            }
        }else if (StringUtils.equals(fieldName, "游戏区")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyRegion.getCode(),
                        "第" + index + "行，游戏区不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏服")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameServer.getCode(),
                        "第" + index + "行，游戏服不能为空");
            }
        } else if (StringUtils.equals(fieldName, "阵营")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameRace.getCode(),
                        "第" + index + "行，游戏阵营不能为空");
            }
        } else if (StringUtils.equals(fieldName, "单位")) {
//            if (StringUtils.isBlank(fieldValue)) {
//                throw new SystemException(ResponseCodes.EmptyMoneyName.getCode(),
//                        "第" + index + "行，商品单位不能为空");
//            }
        } else if (StringUtils.equals(fieldName, "单价")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyUnitPrice.getCode(),
                        "第" + index + "行，发布单价不能为空");
            }*/
            if (StringUtils.isNotBlank(fieldValue)) {
                BigDecimal price = null;
                try {
                    price = new BigDecimal(fieldValue);
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，单价：'" + fieldValue + "'错误，请输入正确的数字");
                }
                if (BigDecimal.ZERO.compareTo(price) >= 0) {
                    throw new SystemException(ResponseCodes.UnitPriceMustGreaterThanZero.getCode(),
                            "第" + index + "行，单价必须大于0");
                }
            }
        } else if (StringUtils.equals(fieldName, "库存数量")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                        "第" + index + "行，库存数量不能为空");
            }*/
            if (StringUtils.isNotBlank(fieldValue)) {
                Long count = null;
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    count = bd.longValue();
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，库存数量：'" + fieldValue + "'错误，请输入整数");
                }
                if (count.longValue() < 0) {
                    throw new SystemException(ResponseCodes.EmptyRepositoryGold.getCode(),
                            "第" + index + "行，库存数量必须大于0");
                }
            }
        } else if (StringUtils.equals(fieldName, "可销售库存")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                        "第" + index + "行，可销售库存不能为空");
            }*/
            if (StringUtils.isNotBlank(fieldValue)) {
                Long count = null;
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    count = bd.longValue();
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，可销售库存：'" + fieldValue + "'错误，请输入整数");
                }
                if (count.longValue() < 0) {
                    throw new SystemException(ResponseCodes.EmptySellableGoldCount.getCode(),
                            "第" + index + "行，可销售库存必须大于0");
                }
            }
        } else if (StringUtils.equals(fieldName, "游戏账号")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameAccount.getCode(),
                        "第" + index + "行，游戏账号不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏密码")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGamePassWord.getCode(),
                        "第" + index + "行，游戏密码不能为空");
            }*/
        } else if (StringUtils.equals(fieldName, "游戏角色名")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptySellerGameRole.getCode(),
                        "第" + index + "行，游戏角色名不能为空");
            }
        } else if (StringUtils.equals(fieldName, "二级密码")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptySecondaryPwd.getCode(),
                        "第" + index + "行，二级密码不能为空");
            }*/
        } else if (StringUtils.equals(fieldName, "仓库密码")) {
            /*if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyWarehousePwd.getCode(),
                        "第" + index + "行，仓库密码不能为空");
            }*/
        }

        return true;
    }

    /**
     * 根据游戏获取所有的字段
     * @param gameName
     * @return
     */
    protected String[] getFields(String gameName) {
        gameName = StringUtils.trim(gameName);

        if (StringUtils.equals(gameName, "剑灵")
                || StringUtils.equals(gameName, "疾风之刃")
                || StringUtils.equals(gameName, "QQ三国")
                || StringUtils.equals(gameName, "斗战神")
                || StringUtils.equals(gameName, "龙之谷")) {
            return new String[]{
                    "游戏名称", "游戏区", "游戏服", "游戏币单位", "单价", "库存数量", "游戏账号", "游戏密码",
                    "游戏角色名", "二级密码"
            };
        } else if (StringUtils.equals(gameName, "剑侠情缘Ⅲ")) {
            return new String[]{
                    "游戏名称", "游戏区", "游戏服", "游戏币单位", "单价", "库存数量", "游戏账号", "游戏密码",
                    "游戏角色名", "仓库密码"
            };
        } else if (StringUtils.equals(gameName, "地下城与勇士") || StringUtils.equals(gameName, "新天龙八部")) {
            return new String[]{
                    "游戏名称", "游戏区", "游戏服", "游戏币单位", "单价", "库存数量", "可销售库存", "游戏账号",
                    "游戏密码", "游戏角色名", "二级密码"
            };
        } else if (StringUtils.equals(gameName, "魔兽世界(国服)")) {
            return new String[]{
                    "游戏名称", "游戏区", "游戏服", "游戏币单位", "单价", "库存数量", "可销售库存", "游戏账号",
                    "游戏密码", "游戏角色名", "二级密码", "游戏阵营"
            };
        } else {
            return new String[]{
                    "游戏名称", "游戏区", "游戏服", "游戏币单位", "单价", "库存数量", "游戏账号", "游戏密码",
                    "游戏角色名", "二级密码"
            };
        }
    }


    /***
     *
     * @param gameName
     * @return
     */
    protected String[] getCategoryFields(String gameName) {
        gameName = StringUtils.trim(gameName);

        if (StringUtils.equals(gameName, "剑灵")
                || StringUtils.equals(gameName, "疾风之刃")
                || StringUtils.equals(gameName, "QQ三国")
                || StringUtils.equals(gameName, "斗战神")
                || StringUtils.equals(gameName, "龙之谷")) {
            return new String[]{
                    "游戏名称","商品类目", "游戏区", "游戏服", "单位", "单价", "库存数量", "游戏账号", "游戏密码",
                    "游戏角色名", "二级密码"
            };
        } else if (StringUtils.equals(gameName, "剑侠情缘Ⅲ")) {
            return new String[]{
                    "游戏名称","商品类目", "游戏区", "游戏服", "单位", "单价", "库存数量", "游戏账号", "游戏密码",
                    "游戏角色名", "仓库密码"
            };
        } else if (StringUtils.equals(gameName, "地下城与勇士") || StringUtils.equals(gameName, "新天龙八部")) {
            return new String[]{
                    "游戏名称","商品类目", "游戏区", "游戏服", "单位", "单价", "库存数量", "可销售库存", "游戏账号",
                    "游戏密码", "游戏角色名", "二级密码"
            };
        } else if (StringUtils.equals(gameName, "魔兽世界(国服)")) {
            return new String[]{
                    "游戏名称","商品类目", "游戏区", "游戏服", "单位", "单价", "库存数量", "可销售库存", "游戏账号",
                    "游戏密码", "游戏角色名", "二级密码", "游戏阵营"
            };
        } else {
            return new String[]{
                    "游戏名称","商品类目", "游戏区", "游戏服", "单位", "单价", "库存数量", "游戏账号", "游戏密码",
                    "游戏角色名", "二级密码"
            };
        }
    }


    /**
     * 根据游戏名称获取该excel模板应有的字段数量
     *
     * @param gameName
     * @return
     */
    protected int getFieldsCount(String gameName) {
        return getFields(gameName).length;
    }

    /**
     * 获取通货模板长度
     * ZW_C_JB_00008_20170519 ADD
     * @param gameName
     * @return
     */
    protected int getCategoryFieldsCount(String gameName) {
        return getCategoryFields(gameName).length;
    }

    private Workbook create(InputStream in) throws IOException, InvalidFormatException {
        return WorkbookFactory.create(in);
//        if (!in.markSupported()) {
//            in = new PushbackInputStream(in, 8);
//        }

//        if (POIFSFileSystem.hasPOIFSHeader(in)) {
//            return new HSSFWorkbook(in);
//        }
//        if (POIXMLDocument.hasOOXMLHeader(in)) {
//            return new XSSFWorkbook(OPCPackage.open(in));
//        }
//        throw new IllegalArgumentException("您的excel版本目前解析不了");
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }
}
