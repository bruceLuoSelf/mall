package com.wzitech.gamegold.common.tradeorder.header;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  响应拦截器
 * @author ztjie
 * 
 */
public  class ServerHeaderOutProcessor extends AbstractSoapInterceptor {
	
	private String requestUid;

	private String requestPassword;
	
	/**
	 * header
	 */
	private final static String HEADER = "YxbMallSoapHeader";
	/**
	 * header 命名空间
	 */
	private final static String HEADER_NS = "http://tempuri.org/";
	/**
	 * 用户、用于权限认证/授权
	 */
	private final static String USERID = "UserId";
	/**
	 * 密码，用于权限验证/授权
	 */
	private final static String PASSWORD = "Password";
	
	
	/**
	 * 创建服务端out header
	 * 
	 */
	private Header createServerOutHeader(String requestUid, String requestPassword){
		//获取服务端ESBHeader对象
		//ServerHeader esbHeader = new ServerHeader();
		
		//生成org.apache.cxf.binding.soap.SoapHeader对象
		QName qName = new QName(HEADER);
		Document doc = DOMUtils.createDocument();
		Element root = doc.createElementNS(HEADER_NS, HEADER);
		
		//if(esbHeader.getUserName() !=null && !"".equals(esbHeader.getUserName())) {
		root.appendChild(createElement(USERID, requestUid, doc));
		root.appendChild(createElement(PASSWORD, requestPassword, doc));
		//}
		SoapHeader header = new SoapHeader(qName, root);
		//serverThreadLocal.remove();
		return header;
	}
	/**
	 * 创建Element
	 * @param key
	 * @param value
	 * @param doc
	 * @return
	 */
	protected static Element createElement(String key, String value, Document doc) {
		Element element = doc.createElementNS("", key);
		element.setTextContent(value);
		return element;
	}
	
	public ServerHeaderOutProcessor(String requestUid, String requestPassword){
		super(Phase.WRITE);
		this.requestUid = requestUid;
		this.requestPassword = requestPassword;
	}
	
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		try {
			List<Header> headers = message.getHeaders();
			Header header = createServerOutHeader(requestUid,requestPassword);
			headers.add(header);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
