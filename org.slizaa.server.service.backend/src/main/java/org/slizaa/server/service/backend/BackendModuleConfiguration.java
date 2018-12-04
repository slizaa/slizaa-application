package org.slizaa.server.service.backend;

import org.slizaa.server.service.backend.dao.DefaultSlizaaServerBackendDao;
import org.slizaa.server.service.backend.impl.SlizaaServerBackendImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { DefaultSlizaaServerBackendDao.class, SlizaaServerBackendImpl.class })
public class BackendModuleConfiguration {
}
