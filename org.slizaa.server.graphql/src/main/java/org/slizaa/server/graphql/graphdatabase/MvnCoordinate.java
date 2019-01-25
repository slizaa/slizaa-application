package org.slizaa.server.graphql.graphdatabase;

import org.slizaa.core.mvnresolver.api.IMvnCoordinate;

import static com.google.common.base.Preconditions.checkNotNull;

public class MvnCoordinate {

  private String groupId;

  private String artifactId;

  private String version;

  public MvnCoordinate(IMvnCoordinate mvnCoordinate) {
    this.groupId = checkNotNull(mvnCoordinate).getGroupId();
    this.artifactId = mvnCoordinate.getArtifactId();
    this.version = mvnCoordinate.getVersion();
  }

  public MvnCoordinate(String groupId, String artifactId, String version) {
    this.groupId = checkNotNull(groupId);
    this.artifactId = checkNotNull(artifactId);
    this.version = checkNotNull(version);
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getVersion() {
    return version;
  }
}
