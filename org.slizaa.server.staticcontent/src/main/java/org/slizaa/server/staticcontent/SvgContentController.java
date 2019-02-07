package org.slizaa.server.staticcontent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SvgContentController {

  @Autowired
  private ISlizaaService                    _slizaaService;

  // the resource cache
  private ConcurrentHashMap<String, byte[]> _resourceCache = new ConcurrentHashMap<>();

//	@RequestMapping(value = "/svg/{main}", method = RequestMethod.GET)
//	public ResponseEntity<String> svg(@PathVariable String id,
//			@RequestParam(value = "ul", required = false) String upperLeft,
//			@RequestParam(value = "ur", required = false) String upperRight,
//			@RequestParam(value = "ll", required = false) String lowerLeft,
//			@RequestParam(value = "lr", required = false) String lowerRight) throws IOException {
//
//		System.out.println(main + ": " + upperLeft + " : " + upperRight + " : " + lowerLeft + " : " + lowerRight);
//
//		//
//		HttpHeaders headers = new HttpHeaders();
//		// TODO
//		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
//		// TODO
//		headers.setContentType(MediaType.TEXT_XML);
//		ResponseEntity<String> responseEntity = new ResponseEntity<>("BUMM", headers, HttpStatus.OK);
//		return responseEntity;
//	}

  @RequestMapping(value = "/svg/{shortKey}", method = RequestMethod.GET)
  public ResponseEntity<String> svg(@PathVariable String shortKey) throws IOException {

    //
    HttpHeaders headers = new HttpHeaders();
    // TODO
    headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    // TODO
    headers.setContentType(MediaType.TEXT_XML);
    String svgXml = _slizaaService.getSvgService().getMergedSvg(shortKey);
    ResponseEntity<String> responseEntity = new ResponseEntity<>(svgXml, headers, HttpStatus.OK);
    return responseEntity;
  }
}
