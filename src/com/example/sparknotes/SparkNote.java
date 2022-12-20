package com.example.sparknotes;

import java.util.ArrayList;
import java.util.Date;

public class SparkNote {
	private long _id;
	private String title;
	private String content;
	private Date initDate;
	private ArrayList<AttachItem> enclosures;

	public SparkNote(long _id, String title, String content, Date initDate, ArrayList<AttachItem> enclosures) {
		super();
		this._id = _id;
		this.title = title;
		this.content = content;
		this.initDate = initDate;
		this.enclosures = enclosures;
	}
	
	public SparkNote(long _id, String title, String content, Date initDate) {
		super();
		this._id = _id;
		this.title = title;
		this.content = content;
		this.initDate = initDate;
		this.enclosures = new ArrayList<AttachItem>();
	}

	public SparkNote(String title, String content, ArrayList<AttachItem> enclosures) {
		super();
		this.title = title;
		this.content = content;
		this.initDate = new Date();
		this.enclosures = enclosures;
	}

	public SparkNote(String title, String content) {
		super();
		this.title = title;
		this.content = content;
		this.initDate = new Date();
		this.enclosures = new ArrayList<AttachItem>();
	}

	public SparkNote() {
		super();
		this.title = "";
		this.content = "";
		this.initDate = new Date();
		this.enclosures = new ArrayList<AttachItem>();
	}
	
	


	public long getId() {
		return _id;
	}

	public void setId(long _id) {
		this._id = _id;
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

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public ArrayList<AttachItem> getEnclosures() {
		return enclosures;
	}

	public void setEnclosures(ArrayList<AttachItem> enclosures) {
		this.enclosures = enclosures;
	}

}
