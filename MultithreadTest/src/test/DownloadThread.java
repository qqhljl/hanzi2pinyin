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
	 * @param mthreadId �̵߳�id
	 * @param mthreadStartIndex �߳����صĿ�ʼλ��
	 * @param mthreadEndIndex �߳����صĽ���λ��
	 * @param mPath �����ļ���·��
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
			//�ص�:������������ز����ļ���Ҫȥָ���ļ���ʼ�ͽ�����λ��
			conn.setRequestProperty("Range", "bytes="+mthreadStartIndex+"-"+mthreadEndIndex);
			int code = conn.getResponseCode(); //�ӷ���������ȫ����Դ�ɹ���200, ����Ƿ��ز�����Դ��Ϊ206
			System.out.println("code"+code);

			InputStream is = conn.getInputStream();//������Դ�����������Ѿ�������range�������ﷵ�ص��Ƕ�Ӧ������
			
			RandomAccessFile raf = new RandomAccessFile("test.apk", "rwd");
			//��λ�ļ���pointer��λ��
			raf.seek(mthreadStartIndex);
			
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1){
				raf.write(buffer, 0, len);
			}
			is.close();
			raf.close();
			System.out.println("�߳�"+mthreadId+"�������");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.run();
	} 
}