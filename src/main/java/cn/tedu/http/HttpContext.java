package cn.tedu.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 请求响应对象对应的静态值
 * @author 李元浩
 *
 */
public class HttpContext {
	// 回车对应的ASCALL码
    public static final int CR = 13;
    // 换行对应的ASCALL码
    public static final int LF = 10;
    // 空字符串
    public static final String EMPTY = "";
    //空值
    public static final String NULL = null;
    // UTF-8
    public static final String UTF8 = "UTF-8";
    
    /**
	 * 将字符串进行UTF-8的转换
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String DecodeUtf8(String url) throws UnsupportedEncodingException{
		return URLDecoder.decode(url,HttpContext.UTF8);
	}
}
