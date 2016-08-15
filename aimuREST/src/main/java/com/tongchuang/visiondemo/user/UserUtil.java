package com.tongchuang.visiondemo.user;

import com.tongchuang.visiondemo.user.dto.UserInfo;

public class UserUtil {
	public static UserInfo getUserInfoByBarcode(String barcode) {
		String[] data=barcode.split(":");
		UserInfo.Role role = UserInfo.Role.valueOf(data[0].toUpperCase());
		int subjectId = Integer.parseInt(data[1]);
		UserInfo userInfo = new UserInfo();
		userInfo.setRole(role);
		userInfo.setSubjectId(subjectId);
		return userInfo;
	}
}
