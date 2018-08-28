
package com.wzitech.gamegold.common.insurance.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ClaimsRangeTypes的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="ClaimsRangeTypes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="All"/&gt;
 *     &lt;enumeration value="Seal"/&gt;
 *     &lt;enumeration value="OverTime"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ClaimsRangeTypes")
@XmlEnum
public enum ClaimsRangeTypes {

    @XmlEnumValue("All")
    ALL("All"),
    @XmlEnumValue("Seal")
    SEAL("Seal"),
    @XmlEnumValue("OverTime")
    OVER_TIME("OverTime");
    private final String value;

    ClaimsRangeTypes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClaimsRangeTypes fromValue(String v) {
        for (ClaimsRangeTypes c: ClaimsRangeTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
