package org.slizaa.server.staticcontent;

import org.slizaa.server.service.slizaa.internal.SlizaaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class StaticContentController {

    @Autowired
    private SlizaaComponent _component;

    @RequestMapping(value = "/static/**", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> index(HttpServletRequest request) throws IOException {

        String requestPath = request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE ).toString();

        System.out.println(requestPath);

        ClassPathResource imgFile = new ClassPathResource(requestPath.substring("/static/".length()), _component.getBackendClassLoader());

        //
        if (imgFile.exists()) {

            //
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new InputStreamResource(imgFile.getInputStream()));
        }

        // TODO
        throw new RuntimeException();
    }


//    @RequestMapping(value = "{libName:[a-zA-Z\\-]+}-{version:\\d\\.\\d\\.\\d}", method = RequestMethod.GET,
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