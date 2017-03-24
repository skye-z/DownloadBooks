package host.kaiser.Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
/**
 * 小说下载器解析类
 * @author Kaiser.zsk
 *
 */
public class Analysis {
	/**
	 * 解析Html获取文档对象
	 * @param Html Html
	 * @return 文档对象
	 */
	public static Document ObtainHtmlDome(String Html){
		try {
			//将Html字符串转换为Html文档
			Document doc = Jsoup.parseBodyFragment(Html);
			return doc;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 提取章节名称
	 * @param doc 文档对象
	 * @return 章节名称
	 */
	public static String Title(Document doc){
		//解析文档并查找H1元素
		Element dom = doc.select("h1").first();
		//获取H1元素的值
		String title = dom.text();
		//判断是否有前缀
		if (title.indexOf("最新章节")!=-1) {
			//去除前缀
			title = title.substring(title.indexOf("最新章节")+5,title.length());
		}
		//返回标题
		return title.trim();
	}
	/**
	 * 提取章节内容
	 * @param doc 文档对象
	 * @return 章节内容
	 */
	public static String Content(Document doc){
		//解析文档并查找H1元素
		Element dom = doc.select("#clickeye_content").first();
		//将内容解析为字符串
		String content = dom.text();
		//替换换行符
		content = content.replaceAll("     ","\r\n");
		//去除猫扑广告
		content = content.replaceAll("(猫扑中文 www.mpzw.com)","");
		content = content.replaceAll("猫扑中文 www.mpzw.com","");
		content = content.substring(2,content.length()-7);
		//返回内容
		return content.trim();
	}
	@Test
	public void Test(){
		String Html = Request.EstablishHttpGet("http://www.mpzw.com/html/119/119817/","GBK");
		System.out.println(Title(ObtainHtmlDome(Html)));
	}
}
