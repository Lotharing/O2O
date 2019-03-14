package top.lothar.o2o.util.wechat;

import java.security.MessageDigest;
import java.util.Arrays;

/**
  * 微信测试号响应工具
 * @author Lothar
 *
 */
public class SignUtil {
	//对应微信上的token，必须要一致
	private static String token = "myo2o";
	/**
	  * 验证微信签名（开发者通Signature对请求校验，get请求来自微信服务器，原路返回echostr(随机字符串)，接入生效）
	 * @param signature	微信加密签名结合token和一下两个参数
	 * @param timestamp	时间戳
	 * @param nonce		随机数
	 * @return
	 * 
	 * 1.三个参数字典排序
	 * 2.拼接并sha1加密
	 * 3.加密后字符传与signature对比，标识请求来源于微信
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[] {token,timestamp,nonce};
		//字典排序
		Arrays.sort(arr);
		//拼接字符
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		//安全工具
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			//进行加密
			byte[] digest = md.digest(content.toString().getBytes());
			 tmpStr = byteToStr(digest);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
	/**
	 * 将字节数组转换为16进制字符串
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}
	/**
	  * 将字节转换为16进制的字符串
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}
}
