package ru.ibs.gasu.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {
	private List<T> content = new ArrayList<T>();
	private int page;
	private long total;

	public Page() {
		super();
	}

	public Page(org.springframework.data.domain.Page<T> page) {
		this.content.addAll(page.getContent());
		this.page = page.getNumber();
		this.total = page.getTotalElements();
	}

	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	
}

