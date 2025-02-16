package com.andromeda.muteq.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.andromeda.muteq.Service.UploadService;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image) throws IOException {
        String uploadImage = uploadService.uploadImageToFileSystem(image);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping
    public ResponseEntity<byte[]> downloadImage(@RequestParam String fileName) throws IOException {
        byte[] image = uploadService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }
}