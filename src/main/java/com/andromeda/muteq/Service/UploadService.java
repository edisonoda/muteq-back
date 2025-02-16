package com.andromeda.muteq.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import io.micrometer.common.lang.Nullable;

@Service
public class UploadService {
    @Autowired
    private ImageRepository imageRepository;

    private final Path imageStorageDir;

    public UploadService(@Value("media") String imageStorageDir) {
        this.imageStorageDir = FileSystems.getDefault().getPath(imageStorageDir).toAbsolutePath();
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

    private String sanitizeFilename(String name, String filePath) {
        return filePath + "\\" + FilenameUtils.getBaseName(name) + "." + FilenameUtils.getExtension(name);
    }

    private String filenameWithoutExtension(String name, String filePath) {
        return filePath + "\\" + FilenameUtils.getBaseName(name);
    }

    public String uploadImageToFileSystem(MultipartFile file, @Nullable String filePath) throws IOException {
        if (filePath == null)
            filePath = "others";

        String name = sanitizeFilename(file.getOriginalFilename(), filePath);
        final boolean available = imageRepository.findByName(name) == null;

        if (!available)
            name = filenameWithoutExtension(name, filePath) + " (" + getNextNameIncrement(name) + ")"
                    + "." + FilenameUtils.getExtension(name);

        String path =imageStorageDir + "\\" + name;

        imageRepository.save(Image.builder()
            .name(name)
            .type(file.getContentType())
            .path(path)
            .build());
        
        Files.createDirectories(imageStorageDir);
        Files.createDirectories(Paths.get(imageStorageDir.toString(), filePath));
        file.transferTo(new File(path));
        return path;
    }

    public byte[] downloadImageFromFileSystem(String name) throws IOException {
        Image imageData = imageRepository.findByName(name);
        String filePath = imageData.getPath();
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }
}
