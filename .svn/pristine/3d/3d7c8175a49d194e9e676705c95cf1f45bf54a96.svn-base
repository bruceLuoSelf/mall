package com.wzitech.gamegold.common.typehandler;

import com.wzitech.gamegold.common.enums.GenericEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis泛型枚举类型转换
 *
 * @author yemq
 */
public class GenericEnumTypeHandler<E extends Enum<E> & GenericEnum<E>> extends BaseTypeHandler<GenericEnum> {

    private Class<E> type;
    private E[] enums;

    public GenericEnumTypeHandler(Class<E> type) {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
        this.enums = type.getEnumConstants();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GenericEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getEnumCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (rs.wasNull())
            return null;
        return getEnumByCode(value);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (rs.wasNull())
            return null;
        return getEnumByCode(value);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (cs.wasNull())
            return null;
        return getEnumByCode(value);
    }

    private E getEnumByCode(String code) {
        for (E i : enums) {
            if (i.getEnumCode().equals(code)) {
                return i;
            }
        }
        return null;
    }
}
