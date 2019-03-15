package cn.tedu.servlet;
import cn.tedu.http.HttpRequest;
import cn.tedu.http.HttpResponse;
public class HelloServlet extends HttpServlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println(request.getParameter("name"));
		
	}
}
