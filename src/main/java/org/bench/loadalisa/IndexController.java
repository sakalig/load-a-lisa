package org.bench.loadalisa;

//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "MonaLisa goes here";
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage() throws IOException {
        // Load image from classpath (inside resources or the package)
        Resource imageResource = new ClassPathResource("images/mona_lisa_1920x1080.jpg");

        // Read the image as byte array
        byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
    
}
