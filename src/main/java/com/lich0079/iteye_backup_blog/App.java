package com.lich0079.iteye_backup_blog;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

	private static Set<String> blogURLs = new HashSet<String>();

	private static final String base_url = "http://renxiangzyq.iteye.com";
	
	private static final int DEFAULT_EVENT_LOOP_THREADS = Runtime.getRuntime().availableProcessors() * 2;
	
	private static final ExecutorService pools =Executors.newFixedThreadPool(DEFAULT_EVENT_LOOP_THREADS);
	
	public static void main(String[] args) throws Throwable, IOException {
		Document mainDoc = Jsoup.parse(HttpUtil.getHtmlString(base_url));
		
		int maxPage = getMaxPage(mainDoc);
		
		getAllBlogURLs(maxPage);
		
		System.out.println("blog count:"+blogURLs.size());
		for (String string : blogURLs) {
			saveArticle(string);
		}
		System.out.println(blogURLs.size());
		pools.shutdown();
	}

	private static void getAllBlogURLs(int maxPage) throws InterruptedException, ClientProtocolException, IOException {
//		final CountDownLatch countDown = new CountDownLatch(maxPage);
		for (int i = 1; i <= maxPage; i++) {
			final String pageURL = base_url+"/?page="+i;
//			pools.execute(new Runnable() {
//				public void run() {
//					try {
//						Thread.sleep(50000);//prevent iteye blog your ip
//						Document doc = Jsoup.parse(HttpUtil.getHtmlString(pageURL));
//						Elements newsHeadlines = doc.select("h3 a");
//						for (Element element : newsHeadlines) {
//							blogURLs.add(base_url+element.attr("href"));
//						}
//					} catch (Throwable e) {
//						e.printStackTrace();
//					}finally{
//						countDown.countDown();
//					}111111
//				}
//			});
			Thread.sleep(50000);//prevent iteye blog your ip
			Document doc = Jsoup.parse(HttpUtil.getHtmlString(pageURL));
			Elements newsHeadlines = doc.select("h3 a");
			for (Element element : newsHeadlines) {
				blogURLs.add(base_url+element.attr("href"));
			}
		}
//		countDown.await();
	}
	
	public static int getMaxPage(Document doc){
		int max = 1;
		Elements newsHeadlines = doc.select(".pagination a");
		for (Element element : newsHeadlines) {
			String url = element.attr("href").replace("/?page=", "");
			int page = Integer.parseInt(url);
			if(page>max){
				max=page;
			}
		}
		return max;
	}
	
	private static void saveArticle(String url) throws ClientProtocolException, IOException, InterruptedException{
		Thread.sleep(100000 + new Random(System.currentTimeMillis()).nextInt(10000));//prevent iteye blog your ip
		Document doc = Jsoup.parse(HttpUtil.getHtmlString(url));
		Elements title = doc.select(".blog_title h3 a"); 
		Elements content = doc.select("#blog_content");
		String filename = title.text();
		try {
			FileUtil.writeToFile(filename, content.html());
		} catch (Exception e) {
			filename = url.replace(base_url+"/blog/", "");
			FileUtil.writeToFile(filename, content.html());
		}
//		System.out.println(url);
	}
}
