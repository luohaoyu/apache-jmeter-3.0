package org.apache.jmeter.functions;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Johnson on 2016/10/13.
 */
public class HTTPrequest {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }

    private void closeHttpClient(CloseableHttpClient client) throws IOException {
        if (client != null){
            client.close();
        }
    }
    public String doPost(String url,String jsonStr){
        String returnStr="初始化值";
        System.out.println("json---->"+jsonStr);
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonStr,"UTF-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            post.addHeader("content-type", "application/json");
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{

                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println( EntityUtils.toString(httpResponse.getEntity()));
                    returnStr= EntityUtils.toString(httpResponse.getEntity());
//                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }

        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                closeHttpClient(httpClient);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return returnStr;
    }
    @Test
    public void test(){
        doPost("http://192.168.237.1/app/service/v110/suggest/submitSuggestApi","{'test':test}");
    }
}
