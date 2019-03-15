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
 * һ�������Ӧ���߳� 
 * @author ��Ԫ��
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
					// classNameΪ����Ŀ�µ����·��
					Class class1 = Class.forName(className);
                    HttpServlet servlet = (HttpServlet) class1.newInstance();
                    servlet.service(request, response);
                    if(ServerContext.GET.equals(request.getMethod())){
                    	// �����GET����
                    	servlet.doGet(request, response);
                    }else if(ServerContext.POST.equals(request.getMethod())){
                    	// �����POST����
                    	servlet.doPost(request, response);
                    }else{
                    	// httpһ���а�������ʽ����ʱֻд��ô��
                    }
				} catch (ClassNotFoundException e) {
					System.out.println("��������쳣");
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else{
				// û����ص�servlet����ʼ�����ļ���Ӧ
				File file = new File("file"+requestUrl);
				// ������Ӧ����
				if(file.exists()){
					response.setFile(file);		
				}						
			}
			response.flush();
		} catch (IOException e) {
			System.out.println("ϵͳ���ִ���3");
		} finally {
			try {
				// ÿ�����������ʱ����Ҫ��socket�ͷţ���Ϊͬһʱ��ֻ����һ��socket����
				// ����������������һֱռ������
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
