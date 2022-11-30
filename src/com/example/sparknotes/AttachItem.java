package com.example.sparknotes;

import java.io.File;

public class AttachItem {
	String path;
	File content;
	String type;
	public AttachItem(String path, File content, String type) {
		super();
		this.path = path;
		this.content = content;
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public File getContent() {
		return content;
	}
	public void setContent(File content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
