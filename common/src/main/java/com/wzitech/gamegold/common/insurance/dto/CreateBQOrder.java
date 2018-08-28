
package com.wzitech.gamegold.common.insurance.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="order" type="{http://tempuri.org/}OrderDTO" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "order"
})
@XmlRootElement(name = "CreateBQOrder")
public class CreateBQOrder {

    protected OrderDTO order;

    /**
     * 获取order属性的值。
     * 
     * @return
     *     possible object is
     *     {@link com.wzitech.gamegold.common.insurance.dto.OrderDTO }
     *     
     */
    public OrderDTO getOrder() {
        return order;
    }

    /**
     * 设置order属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link com.wzitech.gamegold.common.insurance.dto.OrderDTO }
     *     
     */
    public void setOrder(OrderDTO value) {
        this.order = value;
    }

}
