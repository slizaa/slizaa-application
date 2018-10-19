package org.slizaa.server.backend.impl;

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
    void configureBackend();

    /**
     *
     */
    void unconfigureBackend();

    /**
     *
     */
    boolean updateBackendConfiguration();
}
