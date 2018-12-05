package org.slizaa.server.service.backend.impl;

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
    boolean updateBackendConfiguration();
}
