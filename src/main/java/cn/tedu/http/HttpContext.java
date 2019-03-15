package cn.tedu.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * ������Ӧ�����Ӧ�ľ�ֵ̬
 * @author ��Ԫ��
 *
 */
public class HttpContext {
	// �س���Ӧ��ASCALL��
    public static final int CR = 13;
    // ���ж�Ӧ��ASCALL��
    public static final int LF = 10;
    // ���ַ���
    public static final String EMPTY = "";
    //��ֵ
    public static final String NULL = null;
    // UTF-8
    public static final String UTF8 = "UTF-8";
    
    /**
	 * ���ַ�������UTF-8��ת��
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String DecodeUtf8(String url) throws UnsupportedEncodingException{
		return URLDecoder.decode(url,HttpContext.UTF8);
	}
}
