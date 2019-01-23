package org.slizaa.server.service.slizaa;

import java.io.IOException;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p></p>
 */
public interface IStructureDatabase extends IHierarchicalGraphContainer {

    /**
     * 
     *
     * @return
     */
    String getIdentifier();

    /**
     *
     * @return
     */
    boolean hasContentDefinitionProvider();

    /**
     *
     * @param contentDefinitionProvider
     */
    void setContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider);

    /**
     * <p>
     * </p>
     *
     * @throws IOException
     */
    void parse(boolean startDatabase) throws IOException;

    /**
     *
     */
    void start();

    /**
     *
     */
    void stop();

    /**
     *
     * @return
     */
    boolean isRunning();

    /**
     *
     */
    void dispose();
}
