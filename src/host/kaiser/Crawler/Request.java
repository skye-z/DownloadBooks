package host.kaiser.Crawler;
import java.io.IOException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
/**
 * 小说下载器请求类
 * @author Kaiser.zsk
 *
 */
public class Request {
	/**
	 * 全局超时设定
	 */
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build(); 
	/**
	 * 发起基于Http协议的Get请求
	 * @param URL 请求地址
	 * @param Code 编码格式
	 * @return 响应信息
	 */
	public static String EstablishHttpGet(String URL,String Code){
		HttpGet httpGet = new HttpGet(URL);
		return EstablishHttpGet(httpGet,Code);  
	}
	/**
	 * 发起基于Https协议的Get请求
	 * @param URL 请求地址
	 * @param Code 编码格式
	 * @return 响应信息
	 */
	public String EstablishHttpsGet(String URL,String Code) {  
		HttpGet httpGet = new HttpGet(URL);
		return EstablishHttpsGet(httpGet,Code);  
	}
	/**
	 * 基于Http协议的Get请求操作
	 * @param httpGet 请求对象
	 * @param Code 编码格式
	 * @return 响应信息
	 */
	private static String EstablishHttpGet(HttpGet httpGet,String Code) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null;  
		try {
			httpClient = HttpClients.createDefault();  
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity,Code);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;
	}
	/**
	 * 基于Https协议的Get请求操作
	 * @param httpGet 请求对象
	 * @param Code 编码格式
	 * @return 响应信息
	 */
	private String EstablishHttpsGet(HttpGet httpGet,String Code) {  
		CloseableHttpClient httpClient = null;  
		CloseableHttpResponse response = null;  
		HttpEntity entity = null;  
		String responseContent = null;  
		try {
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));  
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);  
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();  
			httpGet.setConfig(requestConfig);
			response = httpClient.execute(httpGet);  
			entity = response.getEntity();  
			responseContent = EntityUtils.toString(entity,Code);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {
				if (response != null) {  
					response.close();  
				}  
				if (httpClient != null) {  
					httpClient.close();  
				}  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return responseContent;  
	} 
	@Test
	public void Test(){
		System.out.println(EstablishHttpGet("http://www.mpzw.com/html/119/119817/26260951.html","GBK"));
	}
}
