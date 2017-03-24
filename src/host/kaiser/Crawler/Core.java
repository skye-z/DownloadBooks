package host.kaiser.Crawler;

import java.io.FileWriter;
import java.util.Date;
/**
 * 小说下载器核心类
 * @author Kaiser.zsk
 *
 */
public class Core {
	@org.junit.Test
	public void Test(){
		init(Type.MaoPu,"http://www.mpzw.com/html/113/113279");
	}
	/**
	 * 初始化抓取
	 * @param type 目标
	 * @param url 地址
	 * @return 是否成功
	 */
	public boolean init(Type type,String url){
		if (url==null) {
			return false;
		}else{
			//记录程序开始时间
			long start = System.currentTimeMillis();
			if (type.toString()=="MaoPu") {
				System.out.println("抓取目标-->猫扑");
			}
			String Name = BookName(url,"GBK");
			if (Name!=null) {
				System.out.println("目标名称-->"+Name);
			}
			StringBuffer book = Grab.Start(url,"GBK");
			if (book.length()>0) {
				Download(Name,book.toString());
				//记录结束时间
				long end = System.currentTimeMillis();
				System.out.println("抓取成功-耗时：" + (end - start)/1000 + "秒");
				return true;
			}else{
				System.out.println("===== Error-抓取小说失败 =====");
				return false;
			}
		}
	}
	/**
	 * 获取小说名称
	 * @param url 地址
	 * @param Code 编码
	 * @return 小说名称
	 */
	public String BookName(String url,String Code){
		String Html = Request.EstablishHttpGet(url,Code);
		String name = Analysis.Title(Analysis.ObtainHtmlDome(Html));
		if (name!=null) {
			if (name.indexOf("作者")!=-1) {
				name = name.substring(0,name.indexOf("作者"));
			}
			if (name.indexOf("《")!=-1) {
				name = name.substring(1,name.length()-1);
			}
			return name;
		}else{
			return null;
		}
	}
	/**
	 * 下载小说至本地
	 * @param Name
	 * @param Content
	 */
	public void Download(String Name,String Content){
		if (Name==null) {
			Name=new Date().toString();
		}
		try {
			//创建写入流
			FileWriter FW = new FileWriter("D:\\"+Name+".txt");
			for(int i = 0; i < Content.length(); i++) {
				FW.write(Content.charAt(i));; 
			}
			FW.flush();
			FW.close();
		} catch (Exception e) {
		}
	}
}
