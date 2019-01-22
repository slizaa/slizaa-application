package org.slizaa.server.service.slizaa.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "slizaa")
public class SlizaaServiceProperties {

    private String databaseRootDirectory;

    public void setDatabaseRootDirectory(String databaseRootDirectory) {
        this.databaseRootDirectory = databaseRootDirectory;
    }

    public String getDatabaseRootDirectory() {
        return databaseRootDirectory;
    }

    public File getDatabaseRootDirectoryAsFile() {

        File result = new File(databaseRootDirectory);
        if (!result.exists()) {
            result.mkdirs();
        }
        // TODO: check dir!
        return result;
    }
}
