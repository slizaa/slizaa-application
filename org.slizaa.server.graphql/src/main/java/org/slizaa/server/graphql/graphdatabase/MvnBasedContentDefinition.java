package org.slizaa.server.graphql.graphdatabase;

import com.google.common.base.Preconditions;

import java.util.List;

public class MvnBasedContentDefinition implements ContentDefinition {

    private List<MvnCoordinate> coordinates;

    public MvnBasedContentDefinition(List<MvnCoordinate> coordinates) {
        this.coordinates = Preconditions.checkNotNull(coordinates);
    }
}
