package webserver;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tomcat.jni.Local;

import com.sun.org.apache.bcel.internal.generic.RETURN;

/**
 * 自定义的类加载器
 * @author 李元浩
 *
 */
public class CustomClassLoader extends ClassLoader {
	private static final String DRIVER = "D:\\java";
	
	private static final String FILR_TYPE = ".class";
	
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		byte [] b = loadClassData(name);
		if(b == null){
			return super.loadClass(name);
		}else{
			return defineClass(name, b,0,b.length);
		}		
	}
	
	/**
	 * 通过制定的路径找得到对应的class文件
	 * @return
	 */
	private byte[] loadClassData(String name1){
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(DRIVER+"\\"+name1+FILR_TYPE);
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    int a= 0;
		    while((a = inputStream.read()) != -1){
		    	bos.write(a);
		    }
		    return bos.toByteArray();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}finally {
			try {
				if(inputStream != null)
				inputStream.close();
			} catch (IOException e) {				
			}
		}
	}
	public static void main(String[] args) {
		CustomClassLoader loader = new CustomClassLoader();
		Class class1;
		try {
			class1 = loader.loadClass("User");
			// 通过默认加载器加载的类里面的所有其他的变量都是通过本类加载器加载的
			Object object = class1.newInstance();
			System.out.println(object);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
	}
}
