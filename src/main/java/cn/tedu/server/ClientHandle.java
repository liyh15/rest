package cn.tedu.server;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import javax.servlet.ServletContext;


import cn.tedu.http.HttpContext;
import cn.tedu.http.HttpRequest;
import cn.tedu.http.HttpResponse;
import cn.tedu.servlet.HttpServlet;
/**
 * 一个请求对应的线程 
 * @author 李元浩
 *
 */
public class ClientHandle implements Runnable {
	private Socket socket;

	public ClientHandle(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			String requestUrl = request.getRequestUrl();
			String className = null;
			if ((className = ServerContext.getServletClassName(requestUrl)) != HttpContext.NULL) {
				try {
					// className为本项目下的相对路径
					Class class1 = Class.forName(className);
                    HttpServlet servlet = (HttpServlet) class1.newInstance();
                    servlet.service(request, response);
                    if(ServerContext.GET.equals(request.getMethod())){
                    	// 如果是GET请求
                    	servlet.doGet(request, response);
                    }else if(ServerContext.POST.equals(request.getMethod())){
                    	// 如果是POST请求
                    	servlet.doPost(request, response);
                    }else{
                    	// http一共有八种请求方式，暂时只写这么多
                    }
				} catch (ClassNotFoundException e) {
					System.out.println("反射出现异常");
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else{
				// 没有相关的servlet，开始处理文件响应
				File file = new File("file"+requestUrl);
				// 设置响应对象
				if(file.exists()){
					response.setFile(file);		
				}						
			}
			response.flush();
		} catch (IOException e) {
			System.out.println("系统出现错误3");
		} finally {
			try {
				// 每次请求结束的时候都需要将socket释放，因为同一时刻只能有一个socket连接
				// 如果出现阻塞情况会一直占用网络
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
