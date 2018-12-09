package org.slizaa.server.staticcontent;

import com.google.common.io.ByteStreams;
import org.slizaa.server.service.slizaa.internal.SlizaaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class StaticContentController {

    @Autowired
    private SlizaaComponent _component;

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
        headers.setContentType(MediaType.IMAGE_PNG);
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

            //
            ClassPathResource imgFile = new ClassPathResource(path, _component.getCurrentExtensionClassLoader());
            if (imgFile.exists()) {

                //
                try {
                    byte[] targetArray = ByteStreams.toByteArray(imgFile.getInputStream());
                    return targetArray;
                } catch (IOException e) {
                    throw new ResourceNotFoundException(e);
                }
            }

            throw new ResourceNotFoundException("The requested resource couldn't be found.");
        });
    }
}