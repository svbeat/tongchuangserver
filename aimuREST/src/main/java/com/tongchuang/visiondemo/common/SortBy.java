package com.tongchuang.visiondemo.common;

public class SortBy {
	public static enum Direction {ASC, DESC};
	private String sortByField;
	private Direction	direction;
	
	public String getSortByField() {
		return sortByField;
	}
	public void setSortByField(String sortByField) {
		this.sortByField = sortByField;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
}
