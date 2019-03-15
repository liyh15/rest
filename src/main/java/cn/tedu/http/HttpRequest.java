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
 * �������
 * @author ��Ԫ��
 */
public class HttpRequest {
	// requestĬ�ϵ��ַ�������post������Ч
	private String charater = "ISO8859-1";
	// ����ͨ�Ŷ���
	private Socket socket;
	// ���󷽷�
	private String method;
	// ����·��(ȫ·��)
	private String url;
	// ����Э��
	private String prototol;
	// ·��(?������ߵĲ���)
	private String requestUrl;
	// ��Ϣͷ��ֵ��
	private Map<String, String> requestBodyMapping = new HashMap<String, String>();
	// ���������ֵ��
	private Map<String, String> paramMapping = new HashMap<String, String>();
	// request�����Դ��Ĳ���
	private Map<String, Object> paramRequestAttribute = new HashMap<String, Object>();
	// ����������
	private InputStream inputStream;

	public HttpRequest(Socket socket) throws IOException {
		this.socket = socket;
		this.inputStream = socket.getInputStream();
		init();
	}

	/**
	 * ��ʼ���������
	 */
	private void init() {
		readRequstHead();
		readRequestBody();
	}

	/**
	 * ��������ͷ
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
	 * ��һ������url
	 * 
	 * @param url
	 *            ͳһ��Դ��ʶ��(uri,����ϰ��Я��url)
	 */
	private void AnalyseUrl(String url) {
		requestUrl = url;
		if (url.contains("?")) {
			// �ʺ���Ҫ����ת��
			try {
				// ����������ַ���Ҫ����ת�壬�����ַ�Ĭ��������ʮ�����Ƶ�����ʾ%E1%F2%F3
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
	 * ����form��������
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
	 * ������Ϣͷ
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
	 * ������Ϣ���� ����Ϣͷ����Content-length��ʱ��Ż�ִ���������
	 */
	private void readRequestText() {
		  String contentType = requestBodyMapping.get("Content-Type");
		  Integer length = Integer.parseInt(requestBodyMapping.get("Content-Length"));
		  if("application/x-www-form-urlencoded".equals(contentType)){
			  int a = 0;
			  try {
				byte [] b = new byte[length];
				// ���ﲻ��һ��һ���ֽڶ���ӦΪ��������Զ��Ͽ�
				inputStream.read(b);
				String line = HttpContext.DecodeUtf8(new String(b,"ISO8859-1"));	
				AnalyzeFormParam(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	}

	/**
	 * ��ȡŷһ������ ��Ϊhttp������ı���ʽ����һ��һ�еģ�ÿһ����CR LF(���У��س�)�ָ� ��Ϣͷ����Ϣ����֮��������CR,LF�ָ�
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
			System.out.println("ϵͳ��������");
		}
		return null;
	}

	/**
	 * ͨ�����ƻ�ö�Ӧ�ļ�ֵ
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
	 * ��������������
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setAttribute(String name,Object value){
		paramRequestAttribute.put(name, value);
	}
	
	/**
	 * ��ȡ����������
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name){
		return paramRequestAttribute.get(name);
	}
	
	/**
	 * ��ȡrequest����󶨲��������Ƽ���
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
	 * ����post������ַ���
	 */
	public void setCharacterEncoding(String character){
		this.charater = character;
	}
}
