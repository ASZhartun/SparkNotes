package com.example.sparknotes;

import java.io.File;

public class AttachItem {
	String path;
	File content;
	public AttachItem(String path, File content) {
		super();
		this.path = path;
		this.content = content;
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
	
	
}
