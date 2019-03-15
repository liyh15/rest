package webserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
public class test {
public static void main(String[] args) throws FileNotFoundException {	
	File file = new File("D:\\LOL_V4.1.1.6_FULL.exe");
	FileInputStream inputStream = new FileInputStream(file);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	long a = System.currentTimeMillis();
	int b = 0;
	try {
	   byte [] d = new byte[3496672];
	   inputStream.read(d);
	} catch (IOException e) {
		e.printStackTrace();
	}
	long c = System.currentTimeMillis();
	System.out.println(a);
	System.out.println(c);
	System.out.println(c-a);
}
}
