package org.slizaa.server.service.slizaa.internal.graphdatabase;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.util.SocketUtils;

public class SlizaaSocketUtils {

	private static final int MIN_PORT_NUMBER = 1024;
	
	private static final int MAX_PORT_NUMBER = 65535;

	/**
	 * 
	 */
	public static int findAvailableTcpPort() {
		return SocketUtils.findAvailableTcpPort();
	}
	
	/**
	 * Checks to see if a specific port is available.
	 *
	 * @param port the port to check for availability
	 */
	public static boolean available(int port) {
		
	    if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
	        return false;
	    }

	    ServerSocket ss = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
}
