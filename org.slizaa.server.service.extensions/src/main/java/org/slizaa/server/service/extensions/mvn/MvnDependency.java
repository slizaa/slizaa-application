package org.slizaa.server.service.extensions.mvn;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class MvnDependency {

    /* - */
    private String _dependency;

    /* - */
    private List<String> _exclusionPatterns;

    /**
     *
     */
    public MvnDependency() {
    }

    /**
     *
     * @param dependency
     */
    public MvnDependency(String dependency) {
        this._dependency = dependency;
    }

    /**
     *
     * @param dependency
     * @param exclusionPatterns
     */
    public MvnDependency(String dependency, String... exclusionPatterns) {
        this._dependency = dependency;
        this._exclusionPatterns = Arrays.asList(exclusionPatterns);
    }

    /**
     *
     * @return
     */
    public String getDependency() {
        return _dependency;
    }

    /**
     *
     * @return
     */
    public List<String> getExclusionPatterns() {
        return _exclusionPatterns;
    }
}
