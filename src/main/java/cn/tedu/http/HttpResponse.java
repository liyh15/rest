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
 * 响应对象
 * @author 李元浩
 */
public class HttpResponse {
    
	// 协议类型
	private String protocol = "HTTP/1.1";
	
	// 状态码 
	private int statusCode = 200;
	
	// 状态描述	
	private String statusReson = "OK";
	
	// 需要返回的文件对象
	private File file;
	
	// 响应头的相关信息
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
	 * 响应对象响应用户
	 */
	public void flush(){
		writeStatusLine();
		writeResponseHead();
		if(null != file){
			writeResponseBody();
		}
	}
	
	/**
	 * 响应状态行
	 */
	private void writeStatusLine(){
		String line = protocol+" "+statusCode+" "+statusReson;
		println(line);
	}
	
	
	/**
	 * 编写响应头
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
	 * 输出响应正文
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
     * 响应对象输出一行
     * @param line 输出一行的内容
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
	 * 输出一个回车换行
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
	 * 设置文件
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
