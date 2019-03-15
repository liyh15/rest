package cn.tedu.http;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.PUBLIC_MEMBER;
import cn.tedu.server.ServerContext;
/**
 * ��Ӧ����
 * @author ��Ԫ��
 */
public class HttpResponse {
    
	// Э������
	private String protocol = "HTTP/1.1";
	
	// ״̬�� 
	private int statusCode = 200;
	
	// ״̬����	
	private String statusReson = "OK";
	
	// ��Ҫ���ص��ļ�����
	private File file;
	
	// ��Ӧͷ�������Ϣ
	private Map<String, String> headerMap = new HashMap<String, String>();
	
	private Socket socket;

	private OutputStream outputStream;

	public HttpResponse(Socket socket) {		
		try {
			this.socket = socket;
			this.outputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * ��Ӧ������Ӧ�û�
	 */
	public void flush(){
		writeStatusLine();
		writeResponseHead();
		if(null != file){
			writeResponseBody();
		}
	}
	
	/**
	 * ��Ӧ״̬��
	 */
	private void writeStatusLine(){
		String line = protocol+" "+statusCode+" "+statusReson;
		println(line);
	}
	
	
	/**
	 * ��д��Ӧͷ
	 */
	private void writeResponseHead(){
	    if(headerMap.size()>0){
	    	for(String key : headerMap.keySet()){
				println(key+": "+headerMap.get(key));
			}
	    	writeCrLf();
	    }		
	}
	
	/**
	 * �����Ӧ����
	 */
	private void writeResponseBody(){
		try {
			FileInputStream inputStream = new FileInputStream(file);
			int a = 0;
			while((a = inputStream.read()) != -1){
				outputStream.write(a);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
    /**
     * ��Ӧ�������һ��
     * @param line ���һ�е�����
     */
	private void println(String line){
		try {
			outputStream.write(line.getBytes(HttpContext.UTF8));
			writeCrLf();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���һ���س�����
	 * @throws IOException 
	 */
	private void writeCrLf(){
			try {
				outputStream.write(HttpContext.CR);
				outputStream.write(HttpContext.LF);
			} catch (IOException e) {
				e.printStackTrace();
			}		
	}	   
	
	/**
	 * �����ļ�
	 */
	public void setFile(File file){
		this.file = file;
		this.headerMap.put("Content-Length", file.length()+"");
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		String back = fileName.substring(index+1);
		this.headerMap.put("Content-Type", ServerContext.getFileType(back)+";charset=utf-8");
	}
}
