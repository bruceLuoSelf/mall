
package com.wzitech.gamegold.common.tradeorder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataIn" type="{http://tempuri.org/}YxbBizOfferDataIn" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataIn"
})
@XmlRootElement(name = "GetYxbBizOfferList")
public class GetYxbBizOfferList {

    protected YxbBizOfferDataIn dataIn;

    /**
     * Gets the value of the dataIn property.
     * 
     * @return
     *     possible object is
     *     {@link YxbBizOfferDataIn }
     *     
     */
    public YxbBizOfferDataIn getDataIn() {
        return dataIn;
    }

    /**
     * Sets the value of the dataIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link YxbBizOfferDataIn }
     *     
     */
    public void setDataIn(YxbBizOfferDataIn value) {
        this.dataIn = value;
    }

}
