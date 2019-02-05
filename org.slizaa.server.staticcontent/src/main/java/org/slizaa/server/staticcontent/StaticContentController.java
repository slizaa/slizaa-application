package org.slizaa.server.staticcontent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class StaticContentController {

	@Autowired
	private ISlizaaService _slizaaService;

	// the resource cache
	private ConcurrentHashMap<String, byte[]> _resourceCache = new ConcurrentHashMap<>();

	/**
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/static/**", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImageAsResponseEntity(HttpServletRequest request) throws IOException {

		//
		String requestPath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
		requestPath = requestPath.substring("/static/".length());

		//
		HttpHeaders headers = new HttpHeaders();
		// TODO
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		// TODO
		headers.setContentType(MediaType.APPLICATION_XML);
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(getByte(requestPath), headers, HttpStatus.OK);
		return responseEntity;
	}

	/**
	 *
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private byte[] getByte(String path) throws IOException {

		return _resourceCache.computeIfAbsent(path, p -> {

			byte[] targetArray = _slizaaService.getBackendService().hasInstalledExtensions()
					? _slizaaService.getBackendService().loadResourceFromExtensions(path)
					: null;

			if (targetArray == null) {
				throw new ResourceNotFoundException("The requested resource couldn't be found.");
			}
			return targetArray;
		});
	}
}