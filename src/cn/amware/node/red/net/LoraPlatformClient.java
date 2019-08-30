package cn.amware.node.red.net;

import com.alibaba.fastjson.JSON;
import javafx.util.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.TreeMap;

public class LoraPlatformClient {

	private static final String SECRET = "3611fa";
	private static final String PLATFORM_URL = "http://182.61.56.51:8096/api";

	private static String generateAccessToken(TreeMap<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet()) {
			if (sb.length() != 0) {
				sb.append("&");
			}
			sb.append(key).append("=").append(params.get(key));
		}
//		System.out.println(sb);
		return NetUtils.makeMd5(sb.toString());
	}

	public String createDevice(String devEui, String appKey, String timeStamp) {
		TreeMap<String, String> params = new TreeMap<>();
		params.put("dev_eui", devEui);
		params.put("timestamp", timeStamp);
		params.put("application_key", appKey);
//		params.put("secret", SECRET);
//		params.put("access_token", generateAccessToken(params));
		params.put("access_token", SECRET);
		String paramsStr = JSON.toJSONString(params);
		System.out.println(paramsStr);

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost post = new HttpPost(PLATFORM_URL + "/devices");
			StringEntity entity = new StringEntity(paramsStr);
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			try (CloseableHttpResponse response = httpClient.execute(post)) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
