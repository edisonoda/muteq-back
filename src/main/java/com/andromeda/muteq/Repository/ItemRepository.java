package com.andromeda.muteq.Repository;

import java.util.Set;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.andromeda.muteq.Entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // public Set<Item> findItemsByName(String name, Pageable page);
}
