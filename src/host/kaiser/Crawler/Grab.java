package host.kaiser.Crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 小说下载器抓取类
 * @author Kaiser.zsk
 *
 */
public class Grab {
	/**
	 * 开始抓取
	 * @param baseUrl
	 */
	public static StringBuffer Start(String baseUrl,String Code) {
		//创建待遍历Map(链接,是否遍历)
		Map<String, Boolean> OriginalMap = new LinkedHashMap<String, Boolean>();
		//创建基础链接
		String oldLinkHost = baseUrl;
		//添加起始链接
		OriginalMap.put(baseUrl, false);
		//抓取章节链接
		OriginalMap = GrabChapter(oldLinkHost, OriginalMap, Code);
		//输出检查
		StringBuffer book = new StringBuffer();
		OriginalMap.remove(OriginalMap.entrySet().iterator().next().getKey());
		System.out.println("=====共抓取到"+OriginalMap.size()+"章=====");
		for (Map.Entry<String, Boolean> MapGoin : OriginalMap.entrySet()) {
			String Html = Request.EstablishHttpGet(MapGoin.getKey(),Code);
			String Title = Analysis.Title(Analysis.ObtainHtmlDome(Html));
			String Content = Analysis.Content(Analysis.ObtainHtmlDome(Html));
			book.append("\r\n\r\n"+Title+"\r\n\r\n"+Content);
		}
		return book;
	}
	/**
	 * 抓取章节链接
	 * @param oldLinkHost 基础链接
	 * @param OriginalMap 待遍历链接
	 * @param Code 编码
	 * @return 章节链接
	 */
	private static Map<String, Boolean> GrabChapter(String oldLinkHost,Map<String, Boolean> OriginalMap,String Code) {
		//创建存储链接的Map
		Map<String, Boolean> newMap = new LinkedHashMap<String, Boolean>();
		//创建链接载体
		String Old = "";
		//遍历原始链接Map
		for (Map.Entry<String, Boolean> MapGoin : OriginalMap.entrySet()) {
			//输出原始链接
			System.out.println("链接:" + MapGoin.getKey() + "[核对:"+ MapGoin.getValue()+"]");
			//判断是否重复遍历
			if (!MapGoin.getValue()) {
				//获取一条链接
				Old = MapGoin.getKey();
				//发起Get请求
				try {
					//创建URL对象
					URL url = new URL(Old);
					//创建Http连接
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					//设定请求方式
					connection.setRequestMethod("GET");
					//设定超时时间
					connection.setConnectTimeout(2000);
					//设定超时时间
					connection.setReadTimeout(2000);
					//判断Get请求是否成功
					if (connection.getResponseCode() == 200) {
						//创建数据流
						InputStream inputStream = connection.getInputStream();
						//创建缓存
						BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Code));
						//创建链接载体
						String line = "";
						//创建正则对象
						Pattern pattern = Pattern.compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
						//创建正则检测器
						Matcher matcher = null;
						//循环检查链接 为空退出
						while ((line = reader.readLine()) != null) {
							//检查链接
							matcher = pattern.matcher(line);
							//判断是否匹配
							if (matcher.find()) {
								String newLink = matcher.group(1).trim();
								// 丢弃Http和Https开头的独立链接
								if (!newLink.startsWith("http")&&!newLink.startsWith("https")) {
									//判断是否是/开头
									if (newLink.startsWith("/")){
										//直接附加至基础链接
										newLink = oldLinkHost + newLink;
									}else{
										//加/后附加到基础连接
										newLink = oldLinkHost + "/" + newLink;
									}
								}
								//判断尾部是否有/
								if(newLink.endsWith("/")){
									//去除链接尾部的/
									newLink = newLink.substring(0, newLink.length() - 1);
								}
								//去除重复、非Html结尾、index.html和其他网站链接
								if (!newLink.endsWith("index.html")&&newLink.endsWith(".html")&&!OriginalMap.containsKey(newLink)&& !newMap.containsKey(newLink)&& newLink.startsWith(oldLinkHost)) {
									newMap.put(newLink, false);
								}
							}
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				OriginalMap.replace(Old, false, true);
			}
		}
		//有后续链接，继续遍历
		if (!newMap.isEmpty()) {
			OriginalMap.putAll(newMap);
			//Map特性导致不会出现重复键值对
			OriginalMap.putAll(GrabChapter(oldLinkHost, OriginalMap, Code));
		}
		//删除起始链接
		//OriginalMap.remove(OriginalMap.entrySet().iterator().next().getKey());
		//返回结果
		return OriginalMap;
	}
}
