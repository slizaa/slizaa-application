package org.slizaa.server.service.svg;

public interface ISvgService {

	String getMergedSvg(String identifer);
	
	String getKey(String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight);
}
