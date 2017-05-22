package test;

import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ���߳����ز�����
 *  * @author xbrother
 *
 */
public class Demo {
	public static int sThreadCount = 5;
	public static void main(String[] args) throws Exception {
		// ���ӷ���������ȡ�ļ���С		
		String path = "http://dla.newhua.com/down/baidugamebox_4141a.apk";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod("GET");
		
		int code = conn.getResponseCode();
		if (code==200){
			//�������������ݵĳ��ȣ����ʾ����ļ��ĳ���
			int fileLength = conn.getContentLength();
			System.out.println("�ļ�����Ϊ:"+fileLength);
			
			//��RandomAccessFile����һ����Ҫ�����ļ�ͬ����С�ı�����ʱ�ļ�
			RandomAccessFile raf = new RandomAccessFile("test.apk", "rwd");
			raf.setLength(fileLength);
			raf.close();
			
			//���������5���߳�
			int blockSize = fileLength / sThreadCount;

			for (int threadId=1;threadId<=sThreadCount;threadId++){
				//�߳����صĿ�ʼλ��
				//��ʼλ��Ϊ (�߳�id-1) * �����С
				//����λ��Ϊ �߳�id * �����С  ��-1
				int threadStartIndex = (threadId -1) * blockSize;
				int threadEndIndex = (threadId)* blockSize -1;
				// ��������һ���̣߳������λ��Ҫ�������ĵط���
				if (threadId == sThreadCount){
					threadEndIndex = fileLength;
				}
				System.out.println("�߳�"+threadId+"���أ�"+threadStartIndex+"��"+threadEndIndex);
				new DownloadThread(threadId, threadStartIndex, threadEndIndex, path).start(); 
			}
						
		} else{
			System.out.println("�ļ���ȡ�쳣");
		}

	}

}