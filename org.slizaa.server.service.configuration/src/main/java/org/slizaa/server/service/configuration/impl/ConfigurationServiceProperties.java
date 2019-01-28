package org.slizaa.server.service.configuration.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;

@ConfigurationProperties(prefix = "configuration")
public class ConfigurationServiceProperties {

    @NotEmpty
    private String rootDirectory;

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public File getRootDirectoryAsFile() {

        File result = new File(rootDirectory);
        if (!result.exists()) {
            result.mkdirs();
        }
        // TODO: check dir!
        return result;
    }
}
