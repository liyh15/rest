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
 * 一些服务器端的静态的配置
 * 
 * @author 李元浩
 */
public class ServerContext {
	// GET方法
	public static final String GET = "GET";
	// POST方法
	public static final String POST = "POST";
	// 默认的端口号为8080
	public static final int PORT = 8080;
	// 默认并发量为150个
	public static final int MATH = 150;
	// 默认访问的界面
	public static final List<String> viewList = new ArrayList<String>();
	// 各个文件类型对应的别名
	public static final Map<String, String> fileType = new HashMap<String, String>();
	// servlet的映射map(一个key对应一个class文件)
	public static final Map<String, String> servletMapping = new HashMap<String, String>();
	// servlet的外部接口map(一个key，对应一个servlet-name)
	public static final Map<String, String> servletUrlMapping = new HashMap<String, String>();

	/**
	 * 初始化文件类型
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
	 * 初始化servlet的映射
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
	 * 初始化servlet的Url的映射
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
	 * 通过servlet路径获得servlet的名称
	 * @param servletUrl
	 * @return
	 */
	public static String getServletName(String servletUrl){
		return servletUrlMapping.get(servletUrl);
	}
	
	/**
	 * 通过servle名称获得servlet的class文件名
	 * @param servletName
	 * @return
	 */
	public static String getServletClass(String servletName){
		return servletMapping.get(servletName);
	}
	
	/**
	 *通过指定的servlet的url映射名返回对应的class
	 * @return
	 */
	public static String getServletClassName(String servletUrl){
		return servletMapping.get(servletUrlMapping.get(servletUrl));
	}
	/**
	 * 通过文件的后缀获得文件的映射类型
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
