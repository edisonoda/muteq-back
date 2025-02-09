package com.andromeda.muteq.Repository;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.muteq.Entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // public Set<Item> findItemsByName(String name, Pageable page);
}
