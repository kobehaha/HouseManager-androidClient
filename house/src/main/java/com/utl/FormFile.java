package com.utl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FormFile {
	// 上传文件的数据
	private byte[] data;
	private InputStream inputStream;
	private File file;// 文件
	private String file_name;
	private String parameter_name;// 请求参数类型
	private String contentType = "application/octet-stream";

	public FormFile(String fileName, byte[] data, String paraameterName,
			String contentType) {
		this.data = data;
		this.file_name = fileName;
		this.parameter_name = paraameterName;
		if (contentType != null) {
			this.contentType = contentType;
		}

	}

	public FormFile(String filename, File file, String parameterName,
			String contentType) {
		this.file_name = filename;
		this.file = file;
		this.parameter_name = parameterName;
		try {
			this.inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (contentType != null) {
			this.contentType = contentType;
		}

	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getParameter_name() {
		return parameter_name;
	}

	public void setParameter_name(String parameter_name) {
		this.parameter_name = parameter_name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public File getFile() {
		return file;
	}

	public byte[] getByte() {
		return data;

	}

}
