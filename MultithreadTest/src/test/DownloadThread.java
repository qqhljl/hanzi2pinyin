package test;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadThread extends Thread{

	private int mthreadId;
	private int mthreadStartIndex;
	private int mthreadEndIndex;
	private String mPath;

	/**
	 * @param mthreadId 线程的id
	 * @param mthreadStartIndex 线程下载的开始位置
	 * @param mthreadEndIndex 线程下载的结束位置
	 * @param mPath 下载文件的路径
	 */
	public DownloadThread(int mthreadId, int mthreadStartIndex,
			int mthreadEndIndex, String mPath) {
		super();
		this.mthreadId = mthreadId;
		this.mthreadStartIndex = mthreadStartIndex;
		this.mthreadEndIndex = mthreadEndIndex;
		this.mPath = mPath;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(mPath);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			//重点:请求服务器下载部分文件，要去指定文件开始和结束的位置
			conn.setRequestProperty("Range", "bytes="+mthreadStartIndex+"-"+mthreadEndIndex);
			int code = conn.getResponseCode(); //从服务器请求全部资源成功则200, 如果是返回部分资源则为206
			System.out.println("code"+code);

			InputStream is = conn.getInputStream();//返回资源，由于我们已经设置了range，则这里返回的是对应的区块
			
			RandomAccessFile raf = new RandomAccessFile("test.apk", "rwd");
			//定位文件的pointer的位置
			raf.seek(mthreadStartIndex);
			
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1){
				raf.write(buffer, 0, len);
			}
			is.close();
			raf.close();
			System.out.println("线程"+mthreadId+"下载完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.run();
	} 
}