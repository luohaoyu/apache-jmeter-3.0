package org.apache.jmeter.functions;

//import net.minidev.json.JSONObject;
//import net.minidev.json.JSONValue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;


/**
 * Created by Johnson on 2016/10/12.
 */
public class SignRequest extends AbstractFunction {
    private static final List<String> desc = new LinkedList<>();

    private static final String KEY = "__sign"; //$NON-NLS-1$

    private  Object[] jsonStr ;

    private static final Logger log = LoggingManager.getLoggerForClass();


    static {
        desc.add("测试一下啊"); //$NON-NLS-1$
    }
    @Override
    public String execute(SampleResult previousResult, Sampler currentSampler)  {
        String json=((CompoundVariable)jsonStr[0]).execute();
        log.info("传入的json:"+json);
        String signStr;
        String sign = SzSign.createSign(json);
        JMeterVariables jMeterVariables = JMeterContextService.getContext().getVariables();
        jMeterVariables.put("J_SIGN",sign);
        signStr= SzSign.signRequest(json,"sign",sign);
        System.out.println("2签名后的json:"+signStr);

        try {

            boolean isCheckSuc= RequestSign.checkSign(json,"sign");
            System.out.println("检查签名是否正确："+isCheckSuc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signStr ;
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        checkParameterCount(parameters,1);
        jsonStr=parameters.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public String getReferenceKey() {
        return KEY;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }

//单元测试
    @Test
    public void testCreateSign() throws InvalidVariableException {
//        String s= "{\"platform\":\"ios\",\"token\":\"67e8f768dae0c41a5bef9e81f1365482\",\"appversion\":\"1.1.0\",\"isAnonymous\":\"0\",\"appType\":\"trainingSys\",\"suggestBody\":\"哈哈\",\"deviceId\":\"B8B4A04D-2A6A-4435-B891-FC4A51B27DA3\",\"suggestType\":1}";
       String  s="{\"platform\":\"ios\",\"token\":\"67e8f768dae0c41a5bef9e81f1365482\",\n" +
                "\t\"appversion\":\"1.1.0\",\"isAnonymous\":\"0\",\"suggestBody\":\"你好\",\n" +
                "\t\"appType\":\"trainingSys\",\"deviceId\":\"B8B4A04D-2A6A-4435-B891-FC4A51B27DA3\",\"suggestType\":\"2\"}";
        Collection<CompoundVariable> list = new ArrayList<>();
        CompoundVariable cv = new CompoundVariable(s);
        list.add(cv);
        setParameters(list);
        s= execute(null,null);
        try {
//            log.info("------------执行请求------------");
//            String url = "http://192.168.237.1/app/service/v110/suggest/submitSuggestApi";
//            new HTTPrequest().doPost(url,s);
            boolean b=RequestSign.checkSign(s,"sign");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testJMeterSign() {
        String jmeterStr ;
          jmeterStr="{\"platform\":\"ios\",\"token\":\"67e8f768dae0c41a5bef9e81f1365482\",\n" +
                  "\t\"appversion\":\"1.1.0\",\"isAnonymous\":\"0\",\"suggestBody\":\"你好\",\n" +
                  "\t\"appType\":\"trainingSys\",\"deviceId\":\"B8B4A04D-2A6A-4435-B891-FC4A51B27DA3\",\"suggestType\":\"2\"}";
//        jmeterStr= "{\"platform\":\"ios\",\"token\":\"67e8f768dae0c41a5bef9e81f1365482\",\"appversion\":\"1.1.0\",\"isAnonymous\":\"0\",\"appType\":\"trainingSys\",\"suggestBody\":\"测试哦\",\"deviceId\":\"B8B4A04D-2A6A-4435-B891-FC4A51B27DA3\",\"suggestType\":\"2\"}";
        jmeterStr = SZSign2.signRequest(jmeterStr, "sign", SZSign2.createSign(jmeterStr));
        System.out.println(jmeterStr);
        try {
//            new HTTPrequest().doPost("http://192.168.237.1/app/service/v110/suggest/submitSuggestApi",jmeterStr);
            Assert.assertTrue(RequestSign.checkSign(jmeterStr, "sign"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class SzSign {

    public  static String signRequest(String jsonStr, String key, String value){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject.put(key,value);
        return jsonObject.toJSONString();
    }
    public static String createSign(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return createSign(jsonObject);
    }

    public static String createSign(JSONObject jsonObject){
        String sign = "";
        try{
            //排序
            ArrayList<String> keysList = new ArrayList<String>();
            Iterator<String> it = jsonObject.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                keysList.add(key);
            }
            Collections.sort(keysList, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    String s1 = (String)o1;
                    String s2 = (String)o2;
                    return s1.compareTo(s2);
                }
            });
            String jsonStr = "{";
            for(int i=0;i<keysList.size();i++){
                String key = keysList.get(i);
                if(i == 0){
                    jsonStr +=  key +":"+jsonObject.get(key);
                }else{
                    jsonStr +=  ","+key +":"+jsonObject.get(key);
                }
            }
            jsonStr +="}";
            //去掉逗号，单引号，双引号，空格
            jsonStr = jsonStr.replace(",","");
            jsonStr = jsonStr.replace("\'","");
            jsonStr = jsonStr.replace(" ","");
            jsonStr = jsonStr.replace("\n","");
            String step1str = jsonStr.replace("\"","");
            //生成随机数字
            int strLength = step1str.length();
            int randNo = (int)((strLength-2) * Math.random());
            randNo = randNo + 2;
            //生成signStr
            String signStr = step1str.substring(strLength/randNo);
            //生成sign
            sign = DigestUtils.md5Hex(signStr.getBytes())+randNo;

        }catch (Exception e){
            e.printStackTrace();
        }
        return sign;
    }
}

