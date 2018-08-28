
package com.wzitech.gamegold.common.insurance.dto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>BackDTOToActivity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BackDTOToActivity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Detail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BuyerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BuyerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OrderID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OrderFinishedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="CreatedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="PiccCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OrderPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="IsPicc" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="OrderInsuranceType" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="BqServicePrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="SellerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SellerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BackDTOToActivity", propOrder = {
    "detail",
    "buyerId",
    "buyerName",
    "orderID",
    "orderFinishedDate",
    "createdDate",
    "piccCode",
    "orderPrice",
    "isPicc",
    "orderInsuranceType",
    "bqServicePrice",
    "sellerId",
    "sellerName"
})
public class BackDTOToActivity {

    @XmlElement(name = "Detail")
    protected String detail;
    @XmlElement(name = "BuyerId")
    protected String buyerId;
    @XmlElement(name = "BuyerName")
    protected String buyerName;
    @XmlElement(name = "OrderID")
    protected String orderID;
    @XmlElement(name = "OrderFinishedDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar orderFinishedDate;
    @XmlElement(name = "CreatedDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDate;
    @XmlElement(name = "PiccCode")
    protected String piccCode;
    @XmlElement(name = "OrderPrice", required = true)
    protected BigDecimal orderPrice;
    @XmlElement(name = "IsPicc")
    protected boolean isPicc;
    @XmlElement(name = "OrderInsuranceType")
    protected int orderInsuranceType;
    @XmlElement(name = "BqServicePrice", required = true)
    protected BigDecimal bqServicePrice;
    @XmlElement(name = "SellerId")
    protected String sellerId;
    @XmlElement(name = "SellerName")
    protected String sellerName;

    /**
     * 获取detail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置detail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetail(String value) {
        this.detail = value;
    }

    /**
     * 获取buyerId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * 设置buyerId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerId(String value) {
        this.buyerId = value;
    }

    /**
     * 获取buyerName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * 设置buyerName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerName(String value) {
        this.buyerName = value;
    }

    /**
     * 获取orderID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * 设置orderID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderID(String value) {
        this.orderID = value;
    }

    /**
     * 获取orderFinishedDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOrderFinishedDate() {
        return orderFinishedDate;
    }

    /**
     * 设置orderFinishedDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setOrderFinishedDate(XMLGregorianCalendar value) {
        this.orderFinishedDate = value;
    }

    /**
     * 获取createdDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedDate() {
        return createdDate;
    }

    /**
     * 设置createdDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setCreatedDate(XMLGregorianCalendar value) {
        this.createdDate = value;
    }

    /**
     * 获取piccCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPiccCode() {
        return piccCode;
    }

    /**
     * 设置piccCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPiccCode(String value) {
        this.piccCode = value;
    }

    /**
     * 获取orderPrice属性的值。
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    /**
     * 设置orderPrice属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setOrderPrice(BigDecimal value) {
        this.orderPrice = value;
    }

    /**
     * 获取isPicc属性的值。
     * 
     */
    public boolean isIsPicc() {
        return isPicc;
    }

    /**
     * 设置isPicc属性的值。
     * 
     */
    public void setIsPicc(boolean value) {
        this.isPicc = value;
    }

    /**
     * 获取orderInsuranceType属性的值。
     * 
     */
    public int getOrderInsuranceType() {
        return orderInsuranceType;
    }

    /**
     * 设置orderInsuranceType属性的值。
     * 
     */
    public void setOrderInsuranceType(int value) {
        this.orderInsuranceType = value;
    }

    /**
     * 获取bqServicePrice属性的值。
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public BigDecimal getBqServicePrice() {
        return bqServicePrice;
    }

    /**
     * 设置bqServicePrice属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *     
     */
    public void setBqServicePrice(BigDecimal value) {
        this.bqServicePrice = value;
    }

    /**
     * 获取sellerId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * 设置sellerId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerId(String value) {
        this.sellerId = value;
    }

    /**
     * 获取sellerName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * 设置sellerName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerName(String value) {
        this.sellerName = value;
    }

}
