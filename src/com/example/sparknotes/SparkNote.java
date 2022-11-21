package com.example.sparknotes;

import java.util.ArrayList;
import java.util.Date;

public class SparkNote {
	private String title;
	private String content;
	private Date initDate;
	private ArrayList<String> enclosures;

	public SparkNote(String title, String content, ArrayList<String> enclosures) {
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
		this.enclosures = new ArrayList<String>();
	}

	public SparkNote() {
		super();
		this.title = "";
		this.content = "";
		this.initDate = new Date();
		this.enclosures = new ArrayList<String>();
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

	public ArrayList<String> getEnclosures() {
		return enclosures;
	}

	public void setEnclosures(ArrayList<String> enclosures) {
		this.enclosures = enclosures;
	}

}
