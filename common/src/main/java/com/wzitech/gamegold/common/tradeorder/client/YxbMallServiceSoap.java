package com.wzitech.gamegold.common.tradeorder.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2014-04-10T22:57:30.670+08:00
 * Generated source version: 2.4.2
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "YxbMallServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface YxbMallServiceSoap {

    @WebResult(name = "GetYxbBizOfferListResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetYxbBizOfferList", targetNamespace = "http://tempuri.org/", className = "com.wzitech.gamegold.common.tradeorder.client.GetYxbBizOfferList")
    @WebMethod(operationName = "GetYxbBizOfferList", action = "http://tempuri.org/GetYxbBizOfferList")
    @ResponseWrapper(localName = "GetYxbBizOfferListResponse", targetNamespace = "http://tempuri.org/", className = "com.wzitech.gamegold.common.tradeorder.client.GetYxbBizOfferListResponse")
    public java.lang.String getYxbBizOfferList(
        @WebParam(name = "dataIn", targetNamespace = "http://tempuri.org/")
        com.wzitech.gamegold.common.tradeorder.client.YxbBizOfferDataIn dataIn
    );
}