package org.slizaa.server.service.configuration.impl;

import org.slizaa.server.service.configuration.IConfigurationService;

import java.io.IOException;

public class NullConfigurationService implements IConfigurationService {

    @Override
    public <T> void store(String configurationIdentifier, T configuration) throws IOException {
        // do nothing
    }

    @Override
    public <T> T load(String configurationIdentifier, Class<T> type) throws IOException {
        return null;
    }
}
