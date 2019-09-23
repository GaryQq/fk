package com.fengkuang.author.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fengkuang.author.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;


/**
 * @author zhaichong
 * 图片工具类
 */
public class ImageUtil {

    /**
     * 上传图片
     * @param inputStream
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String uploadFile(final InputStream inputStream, final String fileName) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Map<String, Object> params = new HashMap<String, Object>(16);
        String resp = "";
        params.put("get_access", "007");
        params.put("user_id", "110");
        params.put("service_type", "001");
        params.put("service_name", "");
        params.put("storagetime", 0);
        params.put("souce_id", "1111");
        params.put("source", 0);
        params.put("remark", "");
        String str = JSON.toJSONString(params);
        try {
            HttpPost httpPost = new HttpPost(Constants.IMAGE_UPLOAD_URL);
            // 把文件转换成流对象FileBody
            InputStreamBody inputStreamBody = new InputStreamBody(inputStream, fileName);
//	            FileBody bin = new FileBody(file);
            // 以浏览器兼容模式运行，防止文件名乱码。
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("uploadFile", inputStreamBody)// uploadFile对应服务端类的同名属性<File类型>
                    .addTextBody("data", str)
                    //.addPart("uploadFileName", uploadFileName)// uploadFileName对应服务端类的同名属性<String类型>
                    .setCharset(CharsetUtils.get("UTF-8")).build();
            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                // 打印响应状态
//                System.out.println(response.getStatusLine());
                // 获取响应对象
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    Map rtnMap = JSON.parseObject(EntityUtils.toString(resEntity, Charset.forName("UTF-8")), Map.class);
                    resp = Constants.IMAGE_DOMAIN + rtnMap.get("url");
                }
                // 销毁
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
        return resp;
    }

    public static void main(String[] args) {
        try {
            File f = new File("/Users/zhaichong/Desktop/NBA_20190109101716_2_1.gif");
            System.out.println(ImageUtil.uploadFile(new FileInputStream(f), f.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
