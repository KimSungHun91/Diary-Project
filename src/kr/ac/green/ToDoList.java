package kr.ac.green;

import java.io.Serializable;

public class ToDoList implements Serializable {
	private int year;
	private int month;
	private int day;
	
	private String title;
	private String contents;
	private boolean dday;

	public ToDoList(int year, int month, int day, String title, String contents, boolean dday) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.title = title;
		this.contents = contents;
		this.dday = dday;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}
	
	public boolean isDday(){
		return dday;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public void setDday(boolean dday){
		this.dday = dday;
	}
	public boolean getDday(){
		return dday;
	}
	
	
	
	public String getShortTitle(){
		String str = "";
		if(title.length() > 10){
			str = title.substring(0, 10) + "...";
		} else {
			str = title;
		}
		return str;
	}
	
	
	
	@Override
	public String toString() {
		return "ToDoList [year=" + year + ", month=" + month + ", day=" + day + ", title=" + title + ", contents="
				+ contents + "]";
	}
	
}
