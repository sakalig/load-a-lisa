package org.bench.loadalisa;

//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

@RestController
public class IndexController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/")
    public String index() {
        return "MonaLisa goes here";
    }

    public String getResourceFilePath(String filename) throws IOException {
        Resource resource = resourceLoader.getResource(filename);
        return resource.getFile().getAbsolutePath();
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

    @GetMapping("/images")
    public ResponseEntity<byte[]> getImages() throws IOException {

        //File imgFile = new File("images/mona_lisa_" + String.valueOf(x) + ".jpg");
        String saveToPath = "";

        // TODO: Make asset accessible from 'resources/images' instead of 'target/classes/images'
        Resource imageResource = new ClassPathResource("images/mona_lisa_1920x1080.jpg");

        if (imageResource.exists()) {
            saveToPath = imageResource.getFile().getParentFile().getAbsolutePath();
            System.out.println("No partitions exist");
            InputStream is = imageResource.getInputStream();
            BufferedImage image = ImageIO.read(is);

            int rows = 4, columns = 4;

            BufferedImage imgs[] = new BufferedImage[16];

            int subimage_Width = image.getWidth() / columns, subimage_Height = image.getHeight() / rows;;

            int current_img = 0;

            for (int i =  0; i < rows; i++) {
                for (int j =  0; j < columns; j++) {
                    imgs[current_img] = new BufferedImage(subimage_Width, subimage_Height, image.getType());
                    Graphics2D img_creator = imgs[current_img].createGraphics();

                    int src_first_x = subimage_Width * j, src_first_y = subimage_Height * i, dst_corner_x = subimage_Width * j + subimage_Width, dst_corner_y = subimage_Height * i + subimage_Height;;

                    img_creator.drawImage(image, 0, 0, subimage_Width, subimage_Height, src_first_x, src_first_y, dst_corner_x, dst_corner_y, null);
                    current_img++;
                }
            }
            System.out.println("Total fragments: " + imgs.length);

            for (int i = 0; i < 16; i++) {
                System.out.println("Writing to: " + saveToPath);
                File outputFile = new File(saveToPath + "/mona_lisa_" + i + ".jpg");
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
                    ImageIO.write(imgs[i], "jpg", ios);
                }
                System.out.println("Image written successfully");
            }
            System.out.println("Image partitions created");
        } else {
            System.out.println("File does not exist");
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg");

        byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/benchmarks")
    public ResponseEntity<byte[]> getBench() throws IOException {
        // TODO: Run latency test (ms)

        // TODO: include multithreading library
        return null;
    }

}
