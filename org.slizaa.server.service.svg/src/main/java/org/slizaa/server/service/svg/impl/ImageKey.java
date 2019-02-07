package org.slizaa.server.service.svg.impl;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImageKey {
	
	public static String longKey(String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight) {

		StringBuilder result = new StringBuilder(checkNotNull(main));

		if (notEmpty(upperLeft) || notEmpty(upperRight) || notEmpty(lowerLeft) || notEmpty(lowerRight)) {
			result.append("?");
		}

		boolean prependAmpersand = append("ul", upperLeft, false, result);
		prependAmpersand = append("ur", upperRight, prependAmpersand, result) || prependAmpersand;
		prependAmpersand = append("ll", lowerLeft, prependAmpersand, result) || prependAmpersand;
		prependAmpersand = append("lr", lowerRight, prependAmpersand, result) || prependAmpersand;

		return result.toString();
	}

	public static String shortKey(String key) {
		byte[] bytes = checkNotNull(key).getBytes();
		long k = 7;
		for (int i = 0; i < bytes.length; i++) {
			k *= 23;
			k += bytes[i];
			k *= 13;
			k %= 1000000009;
		}
		return k + "";
	}

	private static boolean append(String key, String value, boolean prependAmpersand, StringBuilder builder) {
				
		if (notEmpty(value)) {
			if (prependAmpersand) {
				builder.append("&");
			}
			builder.append(key + "=" + value);
			return true;
		}

		return false;
	}

	private static boolean notEmpty(String string) {
		return string != null && !string.isEmpty();
	}
}
