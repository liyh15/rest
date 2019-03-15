package cn.tedu.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.tedu.http.HttpContext;

/**
 * һЩ�������˵ľ�̬������
 * 
 * @author ��Ԫ��
 */
public class ServerContext {
	// GET����
	public static final String GET = "GET";
	// POST����
	public static final String POST = "POST";
	// Ĭ�ϵĶ˿ں�Ϊ8080
	public static final int PORT = 8080;
	// Ĭ�ϲ�����Ϊ150��
	public static final int MATH = 150;
	// Ĭ�Ϸ��ʵĽ���
	public static final List<String> viewList = new ArrayList<String>();
	// �����ļ����Ͷ�Ӧ�ı���
	public static final Map<String, String> fileType = new HashMap<String, String>();
	// servlet��ӳ��map(һ��key��Ӧһ��class�ļ�)
	public static final Map<String, String> servletMapping = new HashMap<String, String>();
	// servlet���ⲿ�ӿ�map(һ��key����Ӧһ��servlet-name)
	public static final Map<String, String> servletUrlMapping = new HashMap<String, String>();

	/**
	 * ��ʼ���ļ�����
	 */
	private static void initFileType() {
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read("conf/web.xml");
			Element element = document.getRootElement();
			List<Element> elements = element.elements("mime-mapping");
			for (Element element2 : elements) {
				String extension = element2.elementText("extension");
				String mimeType = element2.elementText("mime-type");
				fileType.put(extension, mimeType);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ��servlet��ӳ��
	 */
	private static void initServletMapping() {
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read("conf/servlet.xml");
			Element rootElement = document.getRootElement();
			List<Element> elements = rootElement.elements("servlet");
			for (Element element : elements) {
				String name = element.elementText("servlet-name");
				String className = element.elementText("servlet-class");
                servletMapping.put(name, className);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʼ��servlet��Url��ӳ��
	 */
	private static void initServletUrlMapping(){
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read("conf/servlet.xml");
			Element rootElement = document.getRootElement();
			List<Element> elements = rootElement.elements("servlet-mapping");
			for (Element element : elements) {
				String name = element.elementText("servlet-name");
				String urlName = element.elementText("url-pattern");
                servletUrlMapping.put(urlName,name);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ͨ��servlet·�����servlet������
	 * @param servletUrl
	 * @return
	 */
	public static String getServletName(String servletUrl){
		return servletUrlMapping.get(servletUrl);
	}
	
	/**
	 * ͨ��servle���ƻ��servlet��class�ļ���
	 * @param servletName
	 * @return
	 */
	public static String getServletClass(String servletName){
		return servletMapping.get(servletName);
	}
	
	/**
	 *ͨ��ָ����servlet��urlӳ�������ض�Ӧ��class
	 * @return
	 */
	public static String getServletClassName(String servletUrl){
		return servletMapping.get(servletUrlMapping.get(servletUrl));
	}
	/**
	 * ͨ���ļ��ĺ�׺����ļ���ӳ������
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
		return fileType.get(fileName);
	}

	static {
		initFileType();
		initServletMapping();
		initServletUrlMapping();
	}
	   
	public static void main(String[] args) {
       System.out.println(ServerContext.getServletClassName("/hello"));
	}
}
