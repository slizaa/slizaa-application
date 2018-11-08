package org.slizaa.server.service.backend;

import org.slizaa.server.service.backend.dao.DefaultSlizaaServerDao;
import org.slizaa.server.service.backend.impl.SlizaaServerBackendImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { DefaultSlizaaServerDao.class, SlizaaServerBackendImpl.class })
public class BackendModuleConfiguration {
}
