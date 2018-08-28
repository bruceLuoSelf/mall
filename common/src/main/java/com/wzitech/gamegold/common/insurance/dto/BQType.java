
package com.wzitech.gamegold.common.insurance.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BQType的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="BQType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OTHER"/&gt;
 *     &lt;enumeration value="PICC"/&gt;
 *     &lt;enumeration value="CHINALIFE"/&gt;
 *     &lt;enumeration value="ChinalifeSafe"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "BQType")
@XmlEnum
public enum BQType {

    OTHER("OTHER"),
    PICC("PICC"),
    CHINALIFE("CHINALIFE"),
    @XmlEnumValue("ChinalifeSafe")
    CHINALIFE_SAFE("ChinalifeSafe"),
    /**
     * 账号安全险
     */
    IdSafe("IdSafe"),
    /**
     * 5173卖家安心买
     */
    AnXinBuy("AnXinBuy"),
    /**
     * 5173买家安心买
     */
    BuyerAnXinBuy("BuyerAnXinBuy");

    private final String value;

    BQType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BQType fromValue(String v) {
        for (BQType c: BQType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
