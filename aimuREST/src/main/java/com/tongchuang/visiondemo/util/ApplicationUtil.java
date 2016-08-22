package com.tongchuang.visiondemo.util;

import java.util.UUID;

public class ApplicationUtil{
	public static String getEntityID() {
		return UUID.randomUUID().toString();
	}
	
//	public static void main(String[] args) {
//		String u = getEntityID();
//		System.out.println("u="+u+"; size="+u.length());
//	}
}
