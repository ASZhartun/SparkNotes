package com.example.sparknotes;

import java.io.File;

public class AttachItem {
	long id;
	String path;
	File content;
	String type;
	long sparkNoteId;
	public AttachItem(long id, String path, File content, String type, long sparkNoteId) {
		super();
		this.path = path;
		this.content = content;
		this.type = type;
		this.sparkNoteId = sparkNoteId;
	}
	
	public AttachItem(long id, String path, File content, String type) {
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getSparkNoteId() {
		return sparkNoteId;
	}

	public void setSparkNoteId(long sparkNoteId) {
		this.sparkNoteId = sparkNoteId;
	}
	
	
}
