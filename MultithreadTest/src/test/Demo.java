package test;

import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 多线程下载测试类
 *  * @author xbrother
 *
 */
public class Demo {
	public static int sThreadCount = 5;
	public static void main(String[] args) throws Exception {
		// 连接服务器，获取文件大小		
		String path = "http://dla.newhua.com/down/baidugamebox_4141a.apk";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod("GET");
		
		int code = conn.getResponseCode();
		if (code==200){
			//服务器返回内容的长度，本质就是文件的长度
			int fileLength = conn.getContentLength();
			System.out.println("文件长度为:"+fileLength);
			
			//用RandomAccessFile创建一个和要下载文件同样大小的本地临时文件
			RandomAccessFile raf = new RandomAccessFile("test.apk", "rwd");
			raf.setLength(fileLength);
			raf.close();
			
			//这里假设是5个线程
			int blockSize = fileLength / sThreadCount;

			for (int threadId=1;threadId<=sThreadCount;threadId++){
				//线程下载的开始位置
				//开始位置为 (线程id-1) * 区块大小
				//结束位置为 线程id * 区块大小  再-1
				int threadStartIndex = (threadId -1) * blockSize;
				int threadEndIndex = (threadId)* blockSize -1;
				// 如果是最后一个线程，则结束位置要等于最后的地方。
				if (threadId == sThreadCount){
					threadEndIndex = fileLength;
				}
				System.out.println("线程"+threadId+"下载："+threadStartIndex+"到"+threadEndIndex);
				new DownloadThread(threadId, threadStartIndex, threadEndIndex, path).start(); 
			}
						
		} else{
			System.out.println("文件获取异常");
		}

	}

}
