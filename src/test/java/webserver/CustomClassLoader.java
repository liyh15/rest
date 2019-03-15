package webserver;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tomcat.jni.Local;

import com.sun.org.apache.bcel.internal.generic.RETURN;

/**
 * �Զ�����������
 * @author ��Ԫ��
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
	 * ͨ���ƶ���·���ҵõ���Ӧ��class�ļ�
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
			// ͨ��Ĭ�ϼ��������ص�����������������ı�������ͨ��������������ص�
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
