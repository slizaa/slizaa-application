package org.slizaa.server.graphql.hierarchicalgraph;

import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Node {

    // the node identifier
    private String id;

    // the text of the node
    private String text;

    //
    private transient HGNode hgNode;

    //
    private transient ILabelDefinitionProvider labelDefinitionProvider;

    /**
     */
    public Node(HGNode hgNode) {
        this.hgNode = checkNotNull(hgNode);

        //
        labelDefinitionProvider = Utils.getLabelDefinitionProvider(hgNode);
    }

    /**
     * @return
     */
    public String getId() {
        return hgNode.getIdentifier().toString();
    }

    /**
     * @return
     */
    public String getText() {
        // TODO cache
        return labelDefinitionProvider.getLabelDefinition(hgNode).getText();
    }

    public ImageDescription getImageDescription() {
        ILabelDefinitionProvider.ILabelDefinition labelDefinition = labelDefinitionProvider.getLabelDefinition(hgNode);

        return new ImageDescription(
                labelDefinition.getBaseImagePath(),
                labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.TOP_LEFT),
                labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.TOP_RIGHT),
                labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.BOTTOM_LEFT),
                labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.BOTTOM_RIGHT)
        );
    }

    /**
     * @return
     */
    HGNode getHgNode() {
        return hgNode;
    }
}