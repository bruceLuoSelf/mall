package com.wzitech.gamegold.common.insurance.interceptor;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * 用户认证
 * @author yemq
 */
@Component
public class AuthOutInterceptor extends AbstractSoapInterceptor {
    @Value("${insurance.service.userName}")
    private String userName = "5173";
    //private String password = "5173";
    @Value("${insurance.service.password}")
    private String password = "5173//bz@#!";
    private String qname = "http://tempuri.org/";

    public AuthOutInterceptor() {
        super(Phase.WRITE);
    }

    /**
     * Intercepts a message.
     * Interceptors should NOT invoke handleMessage or handleFault
     * on the next interceptor - the interceptor chain will
     * take care of this.
     *
     * @param message
     */
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        QName name = new QName("WebServiceSoapHeader");
        Document doc = DOMUtils.createDocument();

        Element usernameEl = doc.createElement("UserName");
        usernameEl.setTextContent(userName);

        Element passwordEl = doc.createElement("Password");
        passwordEl.setTextContent(password);

        Element root = doc.createElementNS(qname, "WebServiceSoapHeader");
        root.appendChild(usernameEl);
        root.appendChild(passwordEl);

        SoapHeader head = new SoapHeader(name, root);
        List<Header> headers = message.getHeaders();
        headers.add(head);
    }
}
