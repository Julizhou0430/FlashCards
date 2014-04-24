package vike.Download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpDownloader {
	private URL url = null;
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		System.out.println("ok1");
		BufferedReader buffer = null;
		System.out.println("ok2");
		try {
			url = new URL(urlStr);
			System.out.println("ok3");
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			System.out.println("ok4");
			buffer = new BufferedReader(new InputStreamReader(urlConn
					.getInputStream()));
			System.out.println("ok5");
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
				System.out.println("ok6");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public int downFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			
			if (fileUtils.isFileExist(path + fileName)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path,fileName, inputStream);
				if (resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}
}