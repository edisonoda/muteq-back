package com.andromeda.muteq.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.andromeda.muteq.Service.UploadService;

import io.micrometer.common.lang.Nullable;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image, @Nullable @RequestParam String path) throws IOException {
        HashMap<String, String> res = new HashMap<>();
        String uploadImage = uploadService.uploadImageToFileSystem(image, path);
        res.put("image", uploadImage);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<byte[]> downloadImage(@RequestParam String name) throws IOException {
        byte[] image = uploadService.downloadImageFromFileSystem(name);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }
}