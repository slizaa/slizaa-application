package org.slizaa.server.service.backend.impl.dao;

import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.extensions.IExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DefaultSlizaaServerBackendDao implements ISlizaaServerBackendDao {

	@Autowired
	private IConfigurationService _configurationService;

	/**
	 * @return
	 */
	@Override
	public List<IExtension> getInstalledExtensions() {
		
		try {
			
			//
			Ent config = _configurationService.load("org.slizaa.server.service.backend", Ent.class);
			
			//
			if (config != null) {
				return config._extensions;
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		//
		return Collections.emptyList();
	}

	/**
	 * @param extensions
	 */
	@Override
	public void saveInstalledExtensions(List<IExtension> extensions) {

		try {
			Ent ent = new Ent();
			ent._extensions = extensions;
			_configurationService.store("org.slizaa.server.service.backend", ent);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
}
