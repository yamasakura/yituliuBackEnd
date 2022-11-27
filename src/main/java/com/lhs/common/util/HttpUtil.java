package com.lhs.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class HttpUtil {

	public static String GetBody(String url) {
		
		HttpClientBuilder builder  = HttpClients.custom();
		
		CloseableHttpClient client = builder.build();

		HttpGet request = new HttpGet(url);

		
		String content = null;
		
		try {
			CloseableHttpResponse response = client.execute(request);
			
			   HttpEntity entity = response.getEntity();
			   
			   content = EntityUtils.toString(entity);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return content;
		
	}




	public static String postBody(String url, String postData)
	{
		HttpsURLConnection conn = null;
		DataOutputStream dos = null;
		BufferedReader br = null;
		String result = null;
		try
		{
			byte[] data = postData.getBytes("UTF-8");

			//创建一个HttpsURLConnection连接
			conn=(HttpsURLConnection) (new URL(url)).openConnection();
			//设置连接超时时间
			conn.setConnectTimeout(80000);
			//post请求必须设置允许输出
			conn.setDoOutput(true);
			//post请求不能使用缓存
			conn.setUseCaches(false);
			//设置post方式请求
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(true);
			// 配置请求Content-Type
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			//开始连接,注意以上的所有设置必须要在connect之前完成
			conn.connect();
			// 发送请求参数,将OutputStream封装成DataOutputStream
			dos = new DataOutputStream(conn.getOutputStream());
			dos.write(data);
			dos.flush();

			//请求成功
			int returnCode = conn.getResponseCode();
			if(returnCode == 200)
			{
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
				StringBuffer strBuf = new StringBuffer();
				String line = null;
				while(true)
				{
					line = br.readLine();
					if (line == null) break;
					strBuf.append(line);
				}
				result = strBuf.toString();
			}
			else {
				System.out.println("HttpsUtil.post,Http return code: " + returnCode + ", postData: " + postData);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try { br.close(); } catch(Exception e) {}
			}
			if (dos != null) {
				try { dos.close(); } catch(Exception e) {}
			}
			if (conn != null) {
				try {conn.disconnect();	} catch (Exception e) {}
			}
		}
		return result;
	}


	
}
