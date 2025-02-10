package com.andromeda.muteq.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.muteq.Entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    public Image findByName(String name);
    public Image findByPath(String path);
}
