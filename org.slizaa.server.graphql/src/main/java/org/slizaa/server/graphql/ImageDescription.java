package org.slizaa.server.graphql;

public class ImageDescription {

    private String baseImagePath;
    private String topLeftImagePath;
    private String topRightImagePath;
    private String bottomLeftImagePath;
    private String bottomRightImagePath;

    public ImageDescription(String baseImagePath, String topLeftImagePath, String topRightImagePath, String bottomLeftImagePath, String bottomRightImagePath) {
        this.baseImagePath = baseImagePath;
        this.topLeftImagePath = topLeftImagePath;
        this.topRightImagePath = topRightImagePath;
        this.bottomLeftImagePath = bottomLeftImagePath;
        this.bottomRightImagePath = bottomRightImagePath;
    }

    public String getTopLeftImagePath() {
        return topLeftImagePath;
    }

    public String getTopRightImagePath() {
        return topRightImagePath;
    }

    public String getBottomLeftImagePath() {
        return bottomLeftImagePath;
    }

    public String getBottomRightImagePath() {
        return bottomRightImagePath;
    }

    public String getBaseImagePath() {
        return baseImagePath;
    }
}

