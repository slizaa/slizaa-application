package org.slizaa.server.service.backend;

import org.slizaa.server.service.backend.impl.SlizaaServerBackendImpl;
import org.slizaa.server.service.backend.impl.dao.DefaultSlizaaServerBackendDao;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { DefaultSlizaaServerBackendDao.class, SlizaaServerBackendImpl.class })
public class BackendServiceModuleConfiguration {
}
