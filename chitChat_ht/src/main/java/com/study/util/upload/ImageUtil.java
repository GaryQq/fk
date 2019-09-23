package com.study.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

public class ImageUtil {
	 
	public static String uploadFile(InputStream inputStream, String fileName) throws ParseException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		Map<String, Object> params = new HashMap<String, Object>();
		String resp = "";
		params.put("get_access", "009");
		params.put("user_id", "cbht");
		params.put("service_type", "009");
		params.put("service_name", "");
		params.put("storagetime", 0);
		params.put("souce_id", "1");
		params.put("source", "cbht");
		params.put("remark", "");
		String str = new JSONObject(params).toString();
		try {
			String url = "http://upload.fengkuangtiyu.cn/rentPianoUpload";

			HttpPost httpPost = new HttpPost(url);
			// 把文件转换成流对象FileBody
			InputStreamBody inputStreamBody = new InputStreamBody(inputStream, fileName);
			// FileBody bin = new FileBody(file);
			// 以浏览器兼容模式运行，防止文件名乱码。
			HttpEntity reqEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("uploadFile", inputStreamBody)// uploadFile对应服务端类的同名属性<File类型>
					.addTextBody("data", str)
					// .addPart("uploadFileName", uploadFileName)//
					// uploadFileName对应服务端类的同名属性<String类型>
					.setCharset(CharsetUtils.get("UTF-8")).build();
			httpPost.setEntity(reqEntity);

			// 发起请求 并返回请求的响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				// 打印响应状态
				System.out.println(response.getStatusLine());
				// 获取响应对象
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					// 打印响应长度
					System.out.println("Response content length: " + resEntity.getContentLength());
					// 打印响应内容
					String tmp = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
					System.out.println("打印响应内容=" + tmp);
					JSONObject aa = (JSONObject) new JSONObject().parse(tmp);

					// Map rtnMap = new
					// Gson().fromJson(EntityUtils.toString(resEntity,
					// Charset.forName("UTF-8")), Map.class);
					// resp = (String) rtnMap.get("url");
					resp = aa.get("url") + "";
					System.out.println("上传文件地址：" + resp);
				}
				// 销毁
				EntityUtils.consume(resEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			File f = new File("C:\\Users\\Administrator\\Desktop\\国旗\\德国@2x.png");
			ImageUtil.uploadFile(new FileInputStream(f), f.getName());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
