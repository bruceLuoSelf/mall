package com.wzitech.gamegold.common.tradeorder.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2014-04-10T22:57:30.739+08:00
 * Generated source version: 2.4.2
 * 
 */
@WebServiceClient(name = "YxbMallService", 
                  wsdlLocation = "http://tradeservice.5173.com/ServiceForYxbMall/YxbMallService.asmx?wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class YxbMallService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "YxbMallService");
    public final static QName YxbMallServiceSoap = new QName("http://tempuri.org/", "YxbMallServiceSoap");
    public final static QName YxbMallServiceSoap12 = new QName("http://tempuri.org/", "YxbMallServiceSoap12");
    static {
        URL url = null;
        try {
            url = new URL("http://tradeservice.5173.com/ServiceForYxbMall/YxbMallService.asmx?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(YxbMallService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://tradeservice.5173.com/ServiceForYxbMall/YxbMallService.asmx?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public YxbMallService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public YxbMallService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public YxbMallService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns YxbMallServiceSoap
     */
    @WebEndpoint(name = "YxbMallServiceSoap")
    public YxbMallServiceSoap getYxbMallServiceSoap() {
        return super.getPort(YxbMallServiceSoap, YxbMallServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns YxbMallServiceSoap
     */
    @WebEndpoint(name = "YxbMallServiceSoap")
    public YxbMallServiceSoap getYxbMallServiceSoap(WebServiceFeature... features) {
        return super.getPort(YxbMallServiceSoap, YxbMallServiceSoap.class, features);
    }
    /**
     *
     * @return
     *     returns YxbMallServiceSoap
     */
    @WebEndpoint(name = "YxbMallServiceSoap12")
    public YxbMallServiceSoap getYxbMallServiceSoap12() {
        return super.getPort(YxbMallServiceSoap12, YxbMallServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns YxbMallServiceSoap
     */
    @WebEndpoint(name = "YxbMallServiceSoap12")
    public YxbMallServiceSoap getYxbMallServiceSoap12(WebServiceFeature... features) {
        return super.getPort(YxbMallServiceSoap12, YxbMallServiceSoap.class, features);
    }

}
