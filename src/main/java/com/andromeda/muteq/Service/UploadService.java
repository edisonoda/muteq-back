package com.andromeda.muteq.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.andromeda.muteq.Entity.Image;
import com.andromeda.muteq.Repository.ImageRepository;

@Service
public class UploadService {
    @Autowired
    private ImageRepository imageRepository;

    private final Path imageStorageDir;
    
    public UploadService(@Value("C:\\Users\\ediso\\OneDrive\\Área de Trabalho\\images") Path imageStorageDir) {
        this.imageStorageDir = imageStorageDir;
    }

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String path = imageStorageDir + "\\" + file.getOriginalFilename();

        Image image = imageRepository.save(Image.builder()
            .name(file.getOriginalFilename())
            .type(file.getContentType())
            .path(path)
            .build());
        
        if (image != null) {
            file.transferTo(new File(path));
            return path;
        }
        
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Image imageData = imageRepository.findByName(fileName);
        String filePath = imageData.getPath();
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }
}
