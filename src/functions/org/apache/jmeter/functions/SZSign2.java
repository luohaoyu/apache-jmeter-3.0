package org.apache.jmeter.functions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.*;
import java.util.Random;

/**
 * Created by Johnson on 2016/10/10.
 */
public class SZSign2 {
    @Test
    public void testCreateSign() throws Exception {
        System.out.println(createSign("{}"));
        System.out.println(signRequest("{}","sign",createSign("{}")));
    }


    public  static String signRequest(String jsonStr, String key, String value){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject.put(key,value);
//        System.out.println(jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }

    public static String createSign(String s) {
        int signNo;
        String signStr;
        String step1str;

        step1str=sort(s);//排序
        step1str=clearJsonStr(step1str);

//        String step1str =sortStr(s);
        signNo = randNo(step1str.length());
        signStr = step1str.substring(step1str.length()/signNo);
        signStr= DigestUtils.md5Hex(signStr.getBytes())+signNo;

        return signStr;
    }

    /**
     * 將 json 按字段名排序，排序后，將逗号、空格、单引号及双引号去除，得到字符串
     */
    public static String sortStr(String s){
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject jsonObjectSorted = new JSONObject();
        String sortedStr = "";
        Object[] tmp = jsonObject.keySet().toArray();
        Arrays.sort(tmp);
//        System.out.println(Arrays.asList(tmp));
        for(Object key:tmp){
            jsonObjectSorted.put(key.toString(),jsonObject.get(key));
        }

        sortedStr = jsonObjectSorted.toJSONString();
        return sortedStr;

    }
    /**
    *随机生成一个数字 randNo ：	randNo 要大于 1 ， 小于 step1str 的长度 ；
     * */
    private static int randNo(int max){
        return new Random().nextInt(max)%(max) + 1 ;
    }
    public static String clearJsonStr(String sortedStr){
        if(sortedStr==null)
            return "";
        sortedStr = sortedStr.replace("'", "");
        sortedStr = sortedStr.replace("\"", "");
        sortedStr = sortedStr.replace(",", "");
        sortedStr = sortedStr.replace(" ", "");

        return sortedStr;

    }

    /**
     * 將 json 按字段名排序，排序后，將逗号、空格、单引号及双引号去除，得到字符串
     */
    public static String sort(String s) {
        List<String> lstr = new ArrayList<String>();
        JSONObject jsonObj= JSON.parseObject(s);
        Iterator<String> iterator = jsonObj.keySet().iterator();
        while (iterator.hasNext()) {
            lstr.add(iterator.next().toString());
        }
        // 调用数组的静态排序方法sort,且不区分大小写
        String[] toBeStored = lstr.toArray(new String[lstr.size()]);
        Arrays.sort(toBeStored, String.CASE_INSENSITIVE_ORDER);
        StringBuffer signStr = new StringBuffer();
        signStr.append("{");
        for (String istr: toBeStored){
                signStr.append(istr + ":" + jsonObj.getString(istr));

        }
        signStr.append("}");
//        String signStr = "{";
//        for (String istr : toBeStored) {
//            signStr = signStr + istr + ":" + jsonObj.getString(istr);
//        }
//        signStr = signStr + "}";
//        System.out.println(signStr);
        return signStr.toString();
    }
    @Test
    public  void testSign() throws Exception {
        String json="{\"platform\":\"ios\",\"token\":\"67e8f768dae0c41a5bef9e81f1365482\",\"appversion\":\"1.1.0\",\"isAnonymous\":\"0\",\"appType\":\"trainingSys\",\"suggestBody\":\"啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊哈吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼\",\"deviceId\":\"B8B4A04D-2A6A-4435-B891-FC4A51B27DA3\",\"suggestType\":\"1\"}";
        SZSign2.signRequest(json,"sign", SZSign2.createSign(json));
        RequestSign.checkSign(json,"sign");
    }
}
