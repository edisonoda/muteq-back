package com.andromeda.muteq.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

    private Integer getNextNameIncrement(String name) {
        Image img = new Image();
        img.setName(FilenameUtils.getBaseName(name));

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths("content")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        List<Image> images = imageRepository.findAll(Example.of(img, matcher));

        return images.size();
    }

    private String sanitizeFilename(String name) {
        return FilenameUtils.getBaseName(name) + "." + FilenameUtils.getExtension(name);
    }

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String name = sanitizeFilename(file.getOriginalFilename());
        final boolean available = imageRepository.findByName(name) == null;

        if (!available)
            name = FilenameUtils.getBaseName(name) + " (" + getNextNameIncrement(name) + ")"
                    + "." + FilenameUtils.getExtension(name);

        String path = imageStorageDir + "\\" + name;

        Image image = imageRepository.save(Image.builder()
                .name(name)
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
