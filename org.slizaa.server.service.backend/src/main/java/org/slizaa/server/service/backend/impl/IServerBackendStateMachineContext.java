package org.slizaa.server.service.backend.impl;

import java.util.List;

import org.slizaa.server.service.extensions.IExtension;

/**
 *
 */
public interface IServerBackendStateMachineContext {

    /**
     * @return
     */
    boolean canConfigureBackend();

    /**
     *
     */
    boolean configureBackend();

    /**
     *
     */
    void unconfigureBackend();

    /**
     *
     */
    boolean updateBackendConfiguration(List<IExtension> extensions);
}
