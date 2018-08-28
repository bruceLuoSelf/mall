
package com.wzitech.gamegold.common.insurance.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.wzitech.gamegold.common.insurance.dto package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _WebServiceSoapHeader_QNAME = new QName("http://tempuri.org/", "WebServiceSoapHeader");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.wzitech.gamegold.common.insurance.dto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.CreateBQOrder }
     * 
     */
    public CreateBQOrder createCreateBQOrder() {
        return new CreateBQOrder();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.OrderDTO }
     * 
     */
    public OrderDTO createOrderDTO() {
        return new OrderDTO();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.CreateBQOrderResponse }
     * 
     */
    public CreateBQOrderResponse createCreateBQOrderResponse() {
        return new CreateBQOrderResponse();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.BackDTO }
     * 
     */
    public BackDTO createBackDTO() {
        return new BackDTO();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.WebServiceSoapHeader }
     * 
     */
    public WebServiceSoapHeader createWebServiceSoapHeader() {
        return new WebServiceSoapHeader();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.WaitAuditBQ }
     * 
     */
    public WaitAuditBQ createWaitAuditBQ() {
        return new WaitAuditBQ();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.WaitAuditBQResponse }
     * 
     */
    public WaitAuditBQResponse createWaitAuditBQResponse() {
        return new WaitAuditBQResponse();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.QueryBQ }
     * 
     */
    public QueryBQ createQueryBQ() {
        return new QueryBQ();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.QueryBQResponse }
     * 
     */
    public QueryBQResponse createQueryBQResponse() {
        return new QueryBQResponse();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.ModifyTransferTime }
     * 
     */
    public ModifyTransferTime createModifyTransferTime() {
        return new ModifyTransferTime();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.ModifyTransferTimeResponse }
     * 
     */
    public ModifyTransferTimeResponse createModifyTransferTimeResponse() {
        return new ModifyTransferTimeResponse();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.CancerOrder }
     * 
     */
    public CancerOrder createCancerOrder() {
        return new CancerOrder();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.CancerOrderResponse }
     * 
     */
    public CancerOrderResponse createCancerOrderResponse() {
        return new CancerOrderResponse();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.QueryBQOrderByPiccNo }
     * 
     */
    public QueryBQOrderByPiccNo createQueryBQOrderByPiccNo() {
        return new QueryBQOrderByPiccNo();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.QueryBQOrderByPiccNoResponse }
     * 
     */
    public QueryBQOrderByPiccNoResponse createQueryBQOrderByPiccNoResponse() {
        return new QueryBQOrderByPiccNoResponse();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.BackDTOToActivity }
     * 
     */
    public BackDTOToActivity createBackDTOToActivity() {
        return new BackDTOToActivity();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.GetBQOrderByOrderId }
     * 
     */
    public GetBQOrderByOrderId createGetBQOrderByOrderId() {
        return new GetBQOrderByOrderId();
    }

    /**
     * Create an instance of {@link com.wzitech.gamegold.common.insurance.dto.GetBQOrderByOrderIdResponse }
     * 
     */
    public GetBQOrderByOrderIdResponse createGetBQOrderByOrderIdResponse() {
        return new GetBQOrderByOrderIdResponse();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.wzitech.gamegold.common.insurance.dto.WebServiceSoapHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "WebServiceSoapHeader")
    public JAXBElement<WebServiceSoapHeader> createWebServiceSoapHeader(WebServiceSoapHeader value) {
        return new JAXBElement<WebServiceSoapHeader>(_WebServiceSoapHeader_QNAME, WebServiceSoapHeader.class, null, value);
    }

}
