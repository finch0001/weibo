package com.lm.weibo.android.net.entities;

public class FileEntity {
	private String fileName;
	private String fileType;
	private String filePath;
	
	public FileEntity(String fileName, String fileType, String filePath) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public String getFilePath() {
		return filePath;
	}
}
