package org.slizaa.server.service.svg.impl.fwk;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.extensions.IExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;

@Component
public class DummyBackendService implements IBackendService {

  @Override
  public boolean hasInstalledExtensions() {
    return false;
  }

  @Override
  public List<IExtension> getInstalledExtensions() {
    return null;
  }

  @Override
  public void installExtensions(List<IExtension> extensions) {
  }

  @Override
  public void uninstallAllExtensions() {
  }

  @Override
  public ClassLoader getCurrentExtensionClassLoader() {
    return null;
  }

  @Override
  public byte[] loadResourceFromExtensions(String path) {
    
    File file = new File(System.getProperty("user.dir") + File.separatorChar + "src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar +  path);
    System.out.println(file.getAbsolutePath());
    if (file.exists()) {
      FileSystemResource fileSystemResource = new FileSystemResource(file.getAbsolutePath());
      if (fileSystemResource.exists()) {
        try {
          return ByteStreams.toByteArray(fileSystemResource.getInputStream());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    return null;
  }

}
