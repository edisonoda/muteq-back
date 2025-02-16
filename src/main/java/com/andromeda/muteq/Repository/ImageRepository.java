package com.andromeda.muteq.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.muteq.Entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    public Optional<Image> findByName(String name);
}
