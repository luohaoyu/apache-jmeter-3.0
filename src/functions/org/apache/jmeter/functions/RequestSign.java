package org.apache.jmeter.functions;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//import cn.com.bluemoon.training.common.utils.MD5Util;

/**
 * 
 * ClassName:RequestSign<br/>
 * Function: 请求签名类<br/>
 * Date:2016年9月29日上午11:18:40<br/>
 * 
 * @author jiangguqiang
 * @version 1.0
 * @since JDK1.7
 * @see
 */
public class RequestSign {
	
	public static void main(String[] args) throws Exception {
//		String jstr = "{'filter':1,'searchKey':'','currentPage':1,'pageSize':15,'token':'0a4fc559b8b5be9cf89ca76e287af45f','appType':'moonAngel','platform':'ios','sign':'986cf68a468ecb8a3275195e0126b88d12'}";
		String jstr = "{\"test\":\"测试哦哦\",\"fbId\":\"113\",\"sign\":\"c15adeeb095639af64f8cc334e4cd1d69\"}";
		boolean signck = checkSign(jstr, "sign");
		System.out.println(signck);
	}

	/**
	 * 
	 * execute: 获取json字符串对应签名字符串 <br/>
	 * 
	 * @author jiangguqiang
	 * @version 1.0
//	 * @param jsonStr 如： {'name':'lily','age':'18','class':'45'}
	 * @param signNo 随机字符串长度
	 * @param signField 签名字段名
	 * @return
	 * @throws Exception
	 * @since JDK1.7
	 * 
	 */
	public static String getJson2SignStr(JSONObject jsonObj, int signNo, String signField) throws Exception {
		try {
			jsonObj.remove(signField);
			List<String> lstr = new ArrayList<String>();
			Iterator<String> iterator = jsonObj.keySet().iterator();
			while (iterator.hasNext()) {
				lstr.add(iterator.next().toString());
			}
			 // 调用数组的静态排序方法sort,且不区分大小写
			String[] toBeStored = lstr.toArray(new String[lstr.size()]);
			Arrays.sort(toBeStored, String.CASE_INSENSITIVE_ORDER);
			/*Collections.sort(lstr, new Comparator() {
				public int compare(Object a, Object b) {
					char one = ((String) a).charAt(0);
					char two = ((String) b).charAt(0);
					return one - two;
				}
			});*/

			String signStr = "{";
			for (String istr : toBeStored) {
				signStr = signStr + istr + ":" + jsonObj.getString(istr);
			}
			signStr = signStr + "}";
			signStr = signStr.replace("'", "");
			signStr = signStr.replace("\"", "");
			signStr = signStr.replace(",", "");
			signStr = signStr.replace(" ", "");

			signStr = signStr.substring(signStr.length() / signNo);
			System.out.println(">>>"+signStr);
			return signStr;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * execute: 验证签名
	 * 
	 * @author jiangguqiang
	 * @version 1.0
	 * @param jsonStr
//	 * @param signNo
//	 * @param signStr
	 * @return
	 * @throws Exception
	 * @since JDK1.7
	 * 
	 */
	public static boolean checkSign(String jsonStr, String signField) throws Exception {
		try {

			JSONObject jsonObj = JSONObject.parseObject(jsonStr);

			if (!jsonObj.containsKey(signField)) {
				return false;
			}
			String signjostr = jsonObj.getString(signField);
			int signNo = Integer.parseInt(signjostr.substring(32));
			String signStr = getJson2SignStr(jsonObj, signNo, signField);

//			String sign = MD5Util.string2MD5(signStr) + signNo;
			String sign = DigestUtils.md5Hex(signStr) + signNo;
			System.out.println("预期签名:"+sign);
			if (sign.equals(signjostr)) {
				return true;
			}

			return false;
		} catch (Exception e) {
			throw e;
		}
	}

}
