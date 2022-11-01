package springboot.example.fileuploadanddownload.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@RestController
@RequestMapping("/file")
public class FileController {
    // define a location
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads";

    // Define a method to upload files
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFiles) {
                if (file.getOriginalFilename() != null) {
                    String filename = StringUtils.cleanPath(file.getOriginalFilename());
                    Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
                    copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
                    filenames.add(filename);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            filenames.add(e.getMessage());
        }
        return ResponseEntity.ok().body(filenames);
    }
}
