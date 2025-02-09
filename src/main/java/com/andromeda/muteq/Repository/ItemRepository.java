package com.andromeda.muteq.Repository;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andromeda.muteq.Entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // @Query("select i from item where i.name like %?1")
    // public Set<Item> findItemsByName(String name, Pageable page);
}
