package com.tongchuang.visiondemo.common;

import java.util.List;

import com.tongchuang.visiondemo.patient.entity.Patient;

public class ResponseList<T> {

	private List<T>		items;
	private Integer		totalCounts;
	
	public ResponseList(List<T> items) {
		this.items = items;
	}

	
	public List<T> getItems() {
		return items;
	}


	public void setItems(List<T> items) {
		this.items = items;
	}


	public Integer getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(Integer totalCounts) {
		this.totalCounts = totalCounts;
	}
	
	
}
