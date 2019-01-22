package org.slizaa.server.service.slizaa.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "database")
public class SlizaaServiceDatabaseProperties {

    private String rootDirectory;

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public File getDatabaseRootDirectoryAsFile() {

        File result = new File(rootDirectory);
        if (!result.exists()) {
            result.mkdirs();
        }
        
        if (!result.isDirectory()) {
        	throw new RuntimeException(String.format("File '%s' is not a directory.", result.getAbsolutePath()));
        }
        
        // TODO: check dir!
        return result;
    }
}
