package cn.tedu.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ���������Ŀ���
 * 
 * @author ��Ԫ��
 */
public class WebServer {
	// ���������ն���
	private ServerSocket serverSocket;

	// ���ڹ��������̳߳ض���
	private ExecutorService threadPool;

	public WebServer() {
		try {
			System.out.println("����׼��������");
			serverSocket = new ServerSocket(ServerContext.PORT);
			threadPool = Executors.newFixedThreadPool(ServerContext.MATH);
			System.out.println("������׼�����");
		} catch (IOException e) {
			System.out.println("ϵͳ�����쳣");
		}
	}

	// ����������
	public void start() {
		while(true){
	        try {
				Socket socket = serverSocket.accept();
				ClientHandle clientHandle = new ClientHandle(socket);
				threadPool.execute(clientHandle);
			} catch (IOException e) {
				System.out.println("ϵͳ�����쳣");
			}	        
		}
	}

	public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
	}
}
