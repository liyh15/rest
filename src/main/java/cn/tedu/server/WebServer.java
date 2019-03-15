package cn.tedu.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 打开网络服务的开关
 * 
 * @author 李元浩
 */
public class WebServer {
	// 服务器接收对象
	private ServerSocket serverSocket;

	// 用于管理服务的线程池对象
	private ExecutorService threadPool;

	public WebServer() {
		try {
			System.out.println("正在准备服务器");
			serverSocket = new ServerSocket(ServerContext.PORT);
			threadPool = Executors.newFixedThreadPool(ServerContext.MATH);
			System.out.println("服务器准备完毕");
		} catch (IOException e) {
			System.out.println("系统出现异常");
		}
	}

	// 开启服务器
	public void start() {
		while(true){
	        try {
				Socket socket = serverSocket.accept();
				ClientHandle clientHandle = new ClientHandle(socket);
				threadPool.execute(clientHandle);
			} catch (IOException e) {
				System.out.println("系统出现异常");
			}	        
		}
	}

	public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
	}
}
