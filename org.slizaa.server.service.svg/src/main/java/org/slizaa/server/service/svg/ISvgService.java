package org.slizaa.server.service.svg;

public interface ISvgService {

	String getSvg(String identifier);
	
	String createSvgAndReturnShortKey(String main, String upperLeft, String upperRight, String lowerLeft, String lowerRight);

	String createSvgAndReturnShortKey(String main);
}
