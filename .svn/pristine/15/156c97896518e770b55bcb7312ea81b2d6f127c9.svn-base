package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.RepositoryConstants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.IGameAccountProcessor;
import com.wzitech.gamegold.shorder.business.IPurchaseGameManager;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 上传收货角色
 * Created by 335854 on 2016/3/29.
 */
@Component
public class GameAccountProcessorImpl extends AbstractBusinessObject implements IGameAccountProcessor {
    @Autowired
    IGameAccountManager gameAccountManager;

    @Autowired
    IPurchaseOrderManager purchaseOrderManager;

    @Autowired
    IPurchaseGameManager purchaseGameManager;

    /**
     * 对上传的excel进行处理
     *
     * @param in
     */
    @Override
    @Transactional
    public void process(InputStream in, Integer deliveryType) throws Exception {
        try {
            List<GameAccount> gameAccountList = new ArrayList<GameAccount>();
            Workbook wb = create(in);
            Sheet sheet = wb.getSheetAt(0);

            // 没有数据
            if (sheet.getLastRowNum() == 0) {
                throw new SystemException(ResponseCodes.EmptyUploadFile.getCode(),
                        ResponseCodes.EmptyUploadFile.getMessage());
            }

            // 检查采购单字段数量是否正确
            int fieldCount = getFieldsCount(); // 获取对应游戏的采购单字段数量
            int cellCount = sheet.getRow(0).getLastCellNum(); // 获取第一行的单元格数量
            if (cellCount != fieldCount) {
                throw new SystemException(ResponseCodes.ErrorRepositoryTemplate.getCode(),
                        ResponseCodes.ErrorRepositoryTemplate.getMessage());
            }


            List<PurchaseGame> purchaseGameList = purchaseGameManager.getPublicTradeTypeForUpdateData(CurrentUserContext.getUserLoginAccount());
            // 从第2行开始读取数据，一直读到倒数第2行
            for (int index = 1, count = sheet.getLastRowNum(); index < count + 1; index++) {
                //循环读取每一行
                Row row = sheet.getRow(index);
                if (row == null) continue;

                // 获取一行采购单记录
                GameAccount gameAccount = getGameAccountFromRow(index + 1, row, deliveryType);

                boolean isConfig = false;
                for (PurchaseGame purchaseGame : purchaseGameList) {
                    if (purchaseGame.getGameName().equals(gameAccount.getGameName())) {
                        isConfig = true;
                        break;
                    }
                }

                if (!isConfig) {
                    throw new SystemException(ResponseCodes.NoPurchaseConfig.getCode(), "您未配置【" + gameAccount.getGameName() + "】【游戏币】的收货开关");
                }
                // 将这行采购单记录更新到数据库
                gameAccountManager.addGameAccount(gameAccount);
                gameAccountList.add(gameAccount);
            }
            if (!CollectionUtils.isEmpty(gameAccountList)) {
                purchaseOrderManager.addPurchaseOrderInUpload(gameAccountList, deliveryType);
            }
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

    private Workbook create(InputStream in) throws IOException, InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("您的excel版本目前解析不了");
    }

    /**
     * 根据游戏名称获取该excel模板应有的字段数量
     *
     * @return
     */
    protected int getFieldsCount() {
        return getFields().length;
    }

    /**
     * 根据游戏获取所有的字段
     *
     * @return
     */
    protected String[] getFields() {
//        gameName = StringUtils.trim(gameName);

        return new String[]{
                "游戏名称", "游戏区", "游戏服", "游戏帐号", "游戏密码", "角色名称", "等级", "仓库密码",
                "是否收货角色", "收货数量", "收货单价", "最小收货量", "联系电话"};
    }

    /**
     * 获取一行账号角色数据
     *
     * @param index
     * @param row
     * @return
     */
    protected GameAccount getGameAccountFromRow(int index, Row row, int deliveryType) {
        // 检查采购单字段数量是否正确
        int fieldCount = getFieldsCount(); // 采购单模块应有的字段数量
        int cellCount = row.getLastCellNum();   // 这行的单元格数量
        List<String> fieldValues = new ArrayList<String>(); // 每列的值
        for (int j = 0; j < cellCount && j < fieldCount; j++) {
            // 循环读取每一列
            String data = getStringCellValue(row.getCell(j));
            if (StringUtils.isNotBlank(data)) {
                data = data.trim();
            }
            fieldValues.add(data);
        }
        if (fieldValues.size() != fieldCount) {
            String msg = String.format("第%s行，账号角色字段数量不对，上传的值：%s", index, fieldValues);
            throw new SystemException(ResponseCodes.ErrorRepositoryTemplate.getCode(), msg);
        }

        GameAccount gameAccount = new GameAccount();
        String[] fields = getFields();
        for (int j = 0; j < fieldCount; j++) {
            String field = fields[j]; // 每列的字段
            String data = fieldValues.get(j); // 每列的值

            // 检查字段是否正确
            checkField(index, field, data, deliveryType);

            // 设置采购单字段的值
            if (StringUtils.equals(field, "游戏名称")) {
//                if (!StringUtils.equals(gameName, data)) {
//                    String msg = String.format("第%s行，上传的游戏名称与采购单模板不符", index);
//                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(), msg);
//                }
                gameAccount.setGameName(data.trim());
            } else if (StringUtils.equals(field, "游戏区")) {
                gameAccount.setRegion(data.trim());
            } else if (StringUtils.equals(field, "游戏服")) {
                gameAccount.setServer(data.trim());
            } else if (StringUtils.equals(field, "游戏帐号")) {
                gameAccount.setGameAccount(data.trim());
            } else if (StringUtils.equals(field, "游戏密码")) {
                gameAccount.setGamePwd(data.trim());
            } else if (StringUtils.equals(field, "角色名称")) {
                gameAccount.setRoleName(data.trim());
            } else if (StringUtils.equals(field, "等级")) {
                if (StringUtils.isNotBlank(data)) {
                    BigDecimal bd = new BigDecimal(data.trim());
                    int count = bd.intValue();
                    if (count > RepositoryConstants.LEVEL)
                        count = RepositoryConstants.LEVEL;
                    gameAccount.setLevel(count);

                }
            } else if (StringUtils.equals(field, "仓库密码")) {
                gameAccount.setSecondPwd(data);
            }  else if (StringUtils.equals(field, "二级密码")) {
                gameAccount.setSecondPwd(data);
            } else if (StringUtils.equals(field, "是否收货角色")) {
                if (StringUtils.isNotBlank(data)) {
                    Boolean isShRole = data.trim().equals("是");
                    gameAccount.setIsShRole(isShRole);
                }
            } else if (StringUtils.equals(field, "收货单价")) {
                if (StringUtils.isNotBlank(data)) {
                    BigDecimal price = new BigDecimal(data.trim());
                    price = price.setScale(RepositoryConstants.MAX_DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP);
                    gameAccount.setPrice(price);
                }
            } else if (StringUtils.equals(field, "收货数量")) {
                if (StringUtils.isNotBlank(data)) {
                    BigDecimal bd = new BigDecimal(data.trim());
                    long count = bd.longValue();
                    if (count > RepositoryConstants.MAX_REPOSITORY_COUNT) {
                        count = RepositoryConstants.MAX_REPOSITORY_COUNT;
                    }
                    //判断收货角色，非收货角色制空
                    if(gameAccount.getIsShRole()){
                        gameAccount.setCount(count);
                    }else {
                        gameAccount.setCount(0L);
                    }

                    if (count == 0) {
                        gameAccount.setStatus(GameAccount.S_OFFLINE); //如果收货数量为0，则设置为下架状态
                    }
                }
            } else if (StringUtils.equals(field, "最小收货量")) {
                if (StringUtils.isNotBlank(data)) {
                    BigDecimal bd = new BigDecimal(data.trim());
                    long minCount = bd.longValue();
                    if (minCount > RepositoryConstants.MAX_REPOSITORY_COUNT) {
                        minCount = RepositoryConstants.MAX_REPOSITORY_COUNT;
                    }
                    gameAccount.setMinCount(minCount);
                }

            } else if (StringUtils.equals(field, "联系电话")) {
                gameAccount.setTel(data.trim());
            }
        }

        // 设置卖家信息
        IUser seller = CurrentUserContext.getUser();
        if (seller != null) {
            gameAccount.setBuyerAccount(seller.getLoginAccount());
            gameAccount.setBuyerUid(seller.getUid());
        } else {
            throw new SystemException(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
        }

        gameAccount.setIsPackFull(false);
        gameAccount.setUpdateTime(new Date());

        //组合校验
        if (gameAccount.getCount() > 0 && gameAccount.getPrice().compareTo(BigDecimal.ZERO) == 0) {
            throw new SystemException(ResponseCodes.ShUnitPriceMustGreaterThanZero.getCode(),
                    "第" + index + "行，收货数量大于0，收货单价不能为0");
        }

        return gameAccount;
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

    /**
     * 检查字段值是否符合要求
     *
     * @param index      第几行
     * @param fieldName  字段值
     * @param fieldValue 字段值
     * @return
     */
    protected boolean checkField(int index, String fieldName, String fieldValue, int deliveryType) {
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
        } else if (StringUtils.equals(fieldName, "游戏帐号")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGameAccount.getCode(),
                        "第" + index + "行，游戏帐号不能为空");
            }
        } else if (StringUtils.equals(fieldName, "游戏密码")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyGamePassWord.getCode(),
                        "第" + index + "行，游戏密码不能为空");
            }
        } else if (StringUtils.equals(fieldName, "角色名称")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyShRoleName.getCode(),
                        "第" + index + "行，角色名称不能为空");
            }
        } else if (StringUtils.equals(fieldName, "等级")) {
            if (StringUtils.isNotBlank(fieldValue)) {
                Long level = null;
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    level = bd.longValue();
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，等级：'" + fieldValue + "'错误，请输入整数");
                }
                if (level.longValue() <= 0) {
                    throw new SystemException(ResponseCodes.NoZeroLevelSh.getCode(),
                            "第" + index + "行，等级必须大于0");
                }
            } else {
                throw new SystemException(ResponseCodes.NullLevelSh.getCode(),
                        "第" + index + "行，等级不能为空");
            }
        }
//        else if (StringUtils.equals(fieldName, "仓库密码")) {
//            if (StringUtils.isBlank(fieldValue)) {
//                throw new SystemException(ResponseCodes.EmptyWarehousePwd.getCode(),
//                        "第" + index + "行，仓库密码不能为空");
//            }
//        }
        else if (StringUtils.equals(fieldName, "是否收货角色")) {
            if (StringUtils.isBlank(fieldValue)) {
                throw new SystemException(ResponseCodes.EmptyIsShRole.getCode(),
                        "第" + index + "行，是否收货角色不能为空");
            } else {
                if (!fieldValue.equals("是") && !fieldValue.equals("否")) {
                    throw new SystemException(ResponseCodes.EmptyIsShRole.getCode(),
                            "第" + index + "行，是否收货角色只能填是或否");
                }
            }
        } else if (StringUtils.equals(fieldName, "收货数量")) {
            if (StringUtils.isNotBlank(fieldValue)) {
                Long count = null;  //收货数量
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    count = bd.longValue();
//                    if (deliveryType == ShDeliveryTypeEnum.Robot.getCode() && count % 1000 != 0) {
//                        throw new SystemException(ResponseCodes.IntMultiple.getCode(),
//                                "第" + index + "行，收货数量只能是1000的整数倍");
//                    }
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，收货数量：'" + fieldValue + "'错误，请输入整数");
                }
                if (count.longValue() < 0) {
                    throw new SystemException(ResponseCodes.ShGoldCount.getCode(),
                            "第" + index + "行，收货数量不能小于0");
                }
            } else {
                throw new SystemException(ResponseCodes.NullShCount.getCode(),
                        "第" + index + "行，收货数量不能为空");
            }
        } else if (deliveryType == ShDeliveryTypeEnum.Manual.getCode() && StringUtils.equals(fieldName, "最小收货量")) {
            if (StringUtils.isNotBlank(fieldValue)) {
                Long minCount = null; //最小收货量
                try {
                    BigDecimal bd = new BigDecimal(fieldValue);
                    minCount = bd.longValue();
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，最小收货量：'" + fieldValue + "'错误，请输入整数");
                }
                if (minCount.longValue() < 0) {
                    throw new SystemException(ResponseCodes.ShGoldCount.getCode(),
                            "第" + index + "行，最小收货量不能小于0");
                }
            } else {
                throw new SystemException(ResponseCodes.NullShCount.getCode(),
                        "第" + index + "行，最小收货量不能为空");
            }
        } else if (StringUtils.equals(fieldName, "收货单价")) {
            if (StringUtils.isNotBlank(fieldValue)) {
                BigDecimal price = null;
                try {
                    price = new BigDecimal(fieldValue);
                } catch (NumberFormatException e) {
                    throw new SystemException(ResponseCodes.IllegalArguments.getCode(),
                            "第" + index + "行，发布单价：'" + fieldValue + "'错误，请输入正确的数字");
                }
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    throw new SystemException(ResponseCodes.ShUnitPriceMustGreaterThanZero.getCode(),
                            "第" + index + "行，发布单价不能小于0");
                }
            } else {
                throw new SystemException(ResponseCodes.NullShPrice.getCode(),
                        "第" + index + "行，发布单价不能为空");
            }
        }

        return true;
    }

}
