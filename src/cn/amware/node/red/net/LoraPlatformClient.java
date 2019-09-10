package cn.amware.node.red.net;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class LoraPlatformClient {

	private static final String SECRET = "3611fa";
	private static final String PLATFORM_URL = "http://182.61.56.51:8096/api";

	private static String generateAccessToken(LinkedHashMap<String, String> params) {
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

	public String createDevice(String devEui, String timeStamp) {
		LinkedHashMap<String, String> allParams = new LinkedHashMap<>();
		TreeMap<String, String> params = new TreeMap<>(Comparator.reverseOrder());
		params.put("dev_eui", devEui);
		params.forEach(allParams::put);
		allParams.put("timestamp", timeStamp);
		allParams.put("secret", SECRET);
		allParams.put("access_token", generateAccessToken(allParams));
		allParams.remove("secret");
		String paramsStr = JSON.toJSONString(allParams);
		System.out.println(paramsStr);

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost post = new HttpPost(PLATFORM_URL + "/create_device");
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
