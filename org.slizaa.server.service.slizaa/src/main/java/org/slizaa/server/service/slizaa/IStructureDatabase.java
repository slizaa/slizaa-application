package org.slizaa.server.service.slizaa;

import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

import java.io.IOException;

public interface IStructureDatabase {

    /**
     *
     * @return
     */
    String getIdentifier();

    /**
     *
     * @param contentDefinitionProvider
     */
    void addContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider);

    /**
     *
     * @return
     */
    boolean hasContentDefinitionProvider();

    /**
     * <p>
     * </p>
     *
     * @throws IOException
     */
    void parseAndStartDatabase() throws IOException;

    /**
     * <p>
     * </p>
     *
     * @return
     */
    HGRootNode getRootNode();
}
