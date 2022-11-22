package com.example.sparknotes;

import android.graphics.Bitmap;

public class ReferencePosition {
	private String title;
	private String content;
	private Bitmap icon;

	public ReferencePosition(String title, String content, Bitmap icon) {
		super();
		this.title = title;
		this.content = content;
		this.icon = icon;
	}
	
	public ReferencePosition(String title, String content) {
		super();
		this.title = title;
		this.content = content;
		this.icon = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

}
