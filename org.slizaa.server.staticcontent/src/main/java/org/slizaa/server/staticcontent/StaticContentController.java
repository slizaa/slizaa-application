package org.slizaa.server.staticcontent;

import org.slizaa.server.service.slizaa.internal.SlizaaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class StaticContentController {

    @Autowired
    private SlizaaComponent _component;

    @RequestMapping(value = "/static/**",  method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> index(final HttpServletRequest request) throws IOException {

        final String url = request.getRequestURI();

        ClassPathResource imgFile = new ClassPathResource("icons/class_obj.png", _component.getBackendClassLoader());
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(new InputStreamResource(imgFile.getInputStream()));
    }


//    @RequestMapping(value = "/static/icons/class_obj.png", method = RequestMethod.GET,
//            produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<InputStreamResource> getImage() throws IOException {
//
//        ClassPathResource imgFile = new ClassPathResource("icons/class_obj.png", _component.getBackendClassLoader());
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.IMAGE_PNG)
//                .body(new InputStreamResource(imgFile.getInputStream()));
//    }
}