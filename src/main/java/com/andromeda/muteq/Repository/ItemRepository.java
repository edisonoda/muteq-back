package com.andromeda.muteq.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.muteq.Entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Page<Item> findAllByCategoryId(Long id, Pageable page);
    public Page<Item> findAllBySectionId(Long id, Pageable page);
}
