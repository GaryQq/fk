package com.study.util.permissions;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.study.model.permissions.User;

public class PasswordHelper {
	private static String algorithmName = "md5";
	private static int hashIterations = 2;
	private static PasswordHelper passwordHelper;

	public static void encryptPassword(User user) {
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getUsername()), hashIterations).toHex();
		user.setPassword(newPassword);

	}

	public static void main(String[] args) {
		 passwordHelper = new PasswordHelper();
		 User user = new User();
		 user.setUsername("zhibo");
		 user.setPassword("fengkuang2019");
		 passwordHelper.encryptPassword(user);
		 //  588774e5823b9767b0ef9a179fc80079
		 System.out.println(user);
	}
}
