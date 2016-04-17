package com.ma.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

import com.utl.FormFile;

/**
 * 表单格式: <FORM METHOD=POST
 * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
 * type="file" name="zip"/> </FORM>
 * 
 * @param path
 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.
 *            iteye.cn或http://192.168.1.101:8083这样的路径测试)
 * @param params
 *            请求参数 key为参数名,value为参数值
 * @param file
 *            上传文件
 */

public class FormInfoService {
	private static final String TAG = "up_load_picture";
	private static final int TIME_OUT = 100 * 1000;
	private static final String CHARSET = "utf-8";
	public static final String SUCCESS = "1";
	public static final String FAILURE = "0";

	public static String post(String path, Map<String, String> params,
			FormFile[] file) {
		String CONTENT_TYPE = "multipart/form-data";
		final String BOUNDARY1 = UUID.randomUUID().toString();
		final String endline = "--" + BOUNDARY1 + "--\r\n";
		int fileDateLength = 0;
		for (FormFile upfile : file) {// 封装数据
			StringBuilder fileBuiler = new StringBuilder();
			fileBuiler.append("--");
			fileBuiler.append(BOUNDARY1);
			fileBuiler.append("\r\n");
			fileBuiler.append("Content-Disposition: form-data;name=\""
					+ upfile.getParameter_name() + "\";filename=\""
					+ upfile.getFile_name() + "\"\r\n");
			fileBuiler.append("Content-Type: " + upfile.getContentType()
					+ "\r\n\r\n");
			fileBuiler.append("\r\n");
			fileDateLength += fileBuiler.length();
			if (upfile.getInputStream() != null) {
				fileDateLength += upfile.getFile().length();// 获取长度
			} else {
				fileDateLength += upfile.getByte().length;// 比特
			}

		}
		// Log.v("文件大小", String.valueOf(fileDateLength));

		StringBuilder textBuilder = new StringBuilder();// 传输文本
		for (Map.Entry<String, String> entry : params.entrySet()) {
			textBuilder.append("--");
			textBuilder.append(BOUNDARY1);
			textBuilder.append("\r\n");
			textBuilder.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n");
			textBuilder.append(entry.getValue());
			textBuilder.append("\r\n");
		}
		// 计算传输给服务器的实体数据总长度
		int dataLength = textBuilder.toString().getBytes().length
				+ fileDateLength + endline.getBytes().length;
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(true);
			connection.setReadTimeout(TIME_OUT);
			connection.setConnectTimeout(TIME_OUT);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("connection", "keep-alive");
			connection.setRequestProperty("Content-Type", CONTENT_TYPE
					+ ";boundary=" + BOUNDARY1);
			connection.setRequestProperty("Charset", CHARSET);
			OutputStream outputStream;
			outputStream = connection.getOutputStream();
			outputStream.write(textBuilder.toString().getBytes());

			for (FormFile uploadFile : file) {
				StringBuilder fileEntity = new StringBuilder();
				fileEntity.append("--");
				fileEntity.append(BOUNDARY1);
				Log.v("上传的个数", String.valueOf(1));

				fileEntity.append("\r\n");
				fileEntity.append("Content-Disposition: form-data;name=\""
						+ uploadFile.getParameter_name() + "\";filename=\""
						+ uploadFile.getFile_name() + "\"\r\n");
				fileEntity.append("Content-Type: "
						+ uploadFile.getContentType() + "\r\n\r\n");
				outputStream.write(fileEntity.toString().getBytes());
				if (uploadFile.getInputStream() != null) {
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = uploadFile.getInputStream().read(buffer, 0,
							1024)) != -1) {
						outputStream.write(buffer, 0, len);
					}
					uploadFile.getInputStream().close();
				} else {
					outputStream.write(uploadFile.getByte(), 0,
							uploadFile.getByte().length);
				}
				outputStream.write("\r\n".getBytes());
				

			}
			outputStream.write(endline.getBytes());
			int response = connection.getResponseCode();
			Log.e(TAG, "response code:" + response);
			if (response == 200) {
				return SUCCESS;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return FAILURE;

	}

	public static String up_load(File file, String url) {
		String Boundary = UUID.randomUUID().toString();// 产生任意字符串
		String PREFIXX = "--";// 协议头
		String LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data";// 表单数据
		URL url2;
		try {
			url2 = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) url2
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(true);
			connection.setReadTimeout(TIME_OUT);
			connection.setConnectTimeout(TIME_OUT);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("connection", "keep-alive");
			connection.setRequestProperty("Content-Type", CONTENT_TYPE
					+ ";boundary=" + Boundary);
			connection.setRequestProperty("Charset", CHARSET);
			if (file != null) {
				OutputStream mFileOutputStream = connection.getOutputStream();
				DataOutputStream mDataOutputStream = new DataOutputStream(
						mFileOutputStream);
				StringBuffer buffer = new StringBuffer();
				buffer.append(PREFIXX);
				buffer.append(Boundary);
				buffer.append(LINE_END);// 设置表单数据分隔符，数据类型
				buffer.append("Content-Disposition: form-data;name="
						+ "\"img\";filename=" + "\"" + file.getName() + "\""
						+ LINE_END);
				Log.v("name", file.getName());
				buffer.append("Content-Type:application/octet-stream;charset="
						+ CHARSET + LINE_END);
				buffer.append(LINE_END);
				mDataOutputStream.write(buffer.toString().getBytes());
				InputStream mInputStream = new FileInputStream(file);// fileinputstream写数据到inputstream
				byte[] byts = new byte[1024];

				while ((mInputStream.read(byts)) != -1) {
					// 将文件转换成字节，保持到dataoutput
					mDataOutputStream.write(byts, 0, 1024);
					Log.v("test", String.valueOf(byts.length));

				}
				mInputStream.close();
				mDataOutputStream.write(LINE_END.getBytes());
				byte[] break_line = (PREFIXX + Boundary + PREFIXX + LINE_END)
						.getBytes();
				Log.v("size", String.valueOf(mDataOutputStream.size()));
				mDataOutputStream.write(break_line);// 分隔符
				;
				mDataOutputStream.flush();
				/**
				 * 
				 * 获取响应
				 * 
				 * */
				int response = connection.getResponseCode();
				Log.e(TAG, "response code:" + response);
				if (response == 200) {
					return SUCCESS;
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}// 连接url，设置属性
		catch (IOException e) {
			e.printStackTrace();
		}

		return FAILURE;

	}
}
