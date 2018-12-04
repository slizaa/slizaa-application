package org.slizaa.server.service.extensions;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation of type {@link IExtensionIdentifier}.
 */
public class ExtensionIdentifier implements IExtensionIdentifier {

    /* the symbolic name */
    private String _symbolicName;

    /* the version */
    private Version _version;

    /**
     * Creates a new instance of type {@link IExtensionIdentifier}.
     *
     * @param symbolicName
     * @param version
     */
    public ExtensionIdentifier(String symbolicName, Version version) {
        this._symbolicName = checkNotNull(symbolicName);
        this._version = checkNotNull(version);
    }

    /**
     * Creates a new instance of type {@link IExtensionIdentifier}.
     *
     * @param symbolicName
     * @param version
     */
    public ExtensionIdentifier(String symbolicName, String version) {
        this._symbolicName = checkNotNull(symbolicName);
        this._version = Version.parseVersion(checkNotNull(version));
    }

    @Override
    public String getSymbolicName() {
        return _symbolicName;
    }

    @Override
    public Version getVersion() {
        return _version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IExtensionIdentifier)) return false;
        IExtensionIdentifier that = (IExtensionIdentifier) o;
        return Objects.equals(_symbolicName, that.getSymbolicName()) &&
                Objects.equals(_version, that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_symbolicName, _version);
    }
}
