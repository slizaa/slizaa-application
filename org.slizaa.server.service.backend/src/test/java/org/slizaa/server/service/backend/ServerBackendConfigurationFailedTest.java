package org.slizaa.server.service.backend;

import org.junit.Test;
import org.slizaa.server.service.backend.impl.SlizaaServerBackendImpl;
import org.slizaa.server.service.extensions.Version;
import org.slizaa.server.service.extensions.mvn.MvnBasedExtension;
import org.slizaa.server.service.extensions.mvn.MvnDependency;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerBackendConfigurationFailedTest extends AbstractServerBackendTest {

    //
    private static MvnBasedExtension extension = new MvnBasedExtension("Missing", new Version(1, 0, 0))
            .withDependency(new MvnDependency("NOT_THERE:NOT_THERE:1.0.0-SNAPSHOT"));

    /**
     *
     */
    @Test
    public void test_ServerBackend() {

        //
        extensionService.setExtensions(Collections.singletonList(extension));

        //
        SlizaaServerBackendImpl backend = applicationContext.getBean(SlizaaServerBackendImpl.class);
        assertThat(backend).isNotNull();

        assertThat(backend.isConfigured()).isFalse();

        // try to install
        backend.installExtensions(extensionService.getExtensions());

        //
        assertThat(backend.isConfigured()).isFalse();
    }
}

