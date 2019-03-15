package cn.tedu.http;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import com.sun.corba.se.spi.orbutil.fsm.Input;
/**
 * 请求对象
 * @author 李元浩
 */
public class HttpRequest {
	// request默认的字符集，对post请求有效
	private String charater = "ISO8859-1";
	// 网络通信对象
	private Socket socket;
	// 请求方法
	private String method;
	// 请求路径(全路径)
	private String url;
	// 请求协议
	private String prototol;
	// 路径(?符号左边的部分)
	private String requestUrl;
	// 消息头键值对
	private Map<String, String> requestBodyMapping = new HashMap<String, String>();
	// 请求参数键值对
	private Map<String, String> paramMapping = new HashMap<String, String>();
	// request对象自带的参数
	private Map<String, Object> paramRequestAttribute = new HashMap<String, Object>();
	// 请求输入流
	private InputStream inputStream;

	public HttpRequest(Socket socket) throws IOException {
		this.socket = socket;
		this.inputStream = socket.getInputStream();
		init();
	}

	/**
	 * 初始化请求对象
	 */
	private void init() {
		readRequstHead();
		readRequestBody();
	}

	/**
	 * 解析请求头
	 */
	private void readRequstHead() {
		String line = readOneLine(inputStream);
		if (line != null) {
			String[] lines = line.split(" ");
			if (lines.length > 0) {
				method = lines[0];
				url = lines[1];
                AnalyseUrl(url);
				prototol = lines[2];
			}
		}
	}

	/**
	 * 进一步解析url
	 * 
	 * @param url
	 *            统一资源标识符(uri,这里习惯携程url)
	 */
	private void AnalyseUrl(String url) {
		requestUrl = url;
		if (url.contains("?")) {
			// 问号需要进行转义
			try {
				// 如果是中文字符需要进行转义，中文字符默认是三个十六进制的树表示%E1%F2%F3
				url = HttpContext.DecodeUtf8(url);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String[] urls = url.split("\\?");
			if (urls.length > 1) {
				requestUrl = urls[0];  
				String paramStr = urls[1];
				AnalyzeFormParam(paramStr);
			}
		}
	}

	/**
	 * 解析form表单的数据
	 */
	private void AnalyzeFormParam(String paramStr){
		String[] params = paramStr.split("&");
		for (String param : params) {
            String [] kv = param.split("=");
            if(kv.length > 1){
                paramMapping.put(kv[0], kv[1]);
            }
		}
	}
	
	/**
	 * 解析消息头
	 */
	private void readRequestBody() {
        String line = null;
        while(!StringUtils.isEmpty(line = readOneLine(inputStream))){
        	String [] lines = line.split(": ");
        	if(lines.length > 0){
        		requestBodyMapping.put(lines[0], lines[1]);
        	}
        }
        if(requestBodyMapping.containsKey("Content-Length")){
        	readRequestText();
        }
	}

	/**
	 * 解析消息正文 当消息头中有Content-length的时候才会执行这个函数
	 */
	private void readRequestText() {
		  String contentType = requestBodyMapping.get("Content-Type");
		  Integer length = Integer.parseInt(requestBodyMapping.get("Content-Length"));
		  if("application/x-www-form-urlencoded".equals(contentType)){
			  int a = 0;
			  try {
				byte [] b = new byte[length];
				// 这里不能一个一个字节读，应为浏览器会自动断开
				inputStream.read(b);
				String line = HttpContext.DecodeUtf8(new String(b,"ISO8859-1"));	
				AnalyzeFormParam(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	}

	/**
	 * 读取欧一行数据 因为http请求的文本格式都是一行一行的，每一行用CR LF(换行，回车)分隔 消息头和消息正文之间是两组CR,LF分隔
	 * 
	 * @return
	 * @throws IOException
	 */
	private String readOneLine(InputStream inputStream) {
		char a = 0;
		char b = 0;
		int c = 0;
		StringBuilder builder = new StringBuilder();
		try {
			while ((c = inputStream.read()) != -1) {
				if (c != 10) {
					b = a;
					a = (char) c;
					builder.append(a);
				} else {
					return builder.toString().trim();
				}
			}
		} catch (IOException e) {
			System.out.println("系统出现问题");
		}
		return null;
	}

	/**
	 * 通过名称获得对应的键值
	 * @param name
	 */
	public String getParameter(String name) {
		return paramMapping.get(name);
	}
		
	public String getMethod() {
		return method;
	}

	public String getRequestUrl(){
		return requestUrl;
	}
	
	/**
	 * 设置请求对象参数
	 * @param name 参数键
	 * @param value 参数值
	 */
	public void setAttribute(String name,Object value){
		paramRequestAttribute.put(name, value);
	}
	
	/**
	 * 获取请求对象参数
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name){
		return paramRequestAttribute.get(name);
	}
	
	/**
	 * 获取request对象绑定参数的名称集合
	 * @return
	 */
	public Enumeration getAttributeNames(){
		Vector<Object> vector = new Vector<Object>();
		for(String key : paramMapping.keySet()){
			vector.addElement(key);
		}
		Enumeration<Object> en ;
		en = vector.elements();
		return en;
	}
	
	/**
	 * 设置post请求的字符集
	 */
	public void setCharacterEncoding(String character){
		this.charater = character;
	}
}
