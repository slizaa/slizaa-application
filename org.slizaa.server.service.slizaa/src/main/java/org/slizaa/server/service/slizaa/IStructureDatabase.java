package org.slizaa.server.service.slizaa;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

import java.io.IOException;
import java.util.List;

/**
 * <p></p>
 */
public interface IStructureDatabase {

    /**
     * <p></p>
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
     * @param identifier
     * @return
     */
    IMappedSystem createNewMappedSystem(String identifier);

    /**
     *
     * @param identifier
     */
    void disposeMappedSystem(String identifier);

    /**
     *
     * @return
     */
    List<IMappedSystem> getMappedSystems();
}
