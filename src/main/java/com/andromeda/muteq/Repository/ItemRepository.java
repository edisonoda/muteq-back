package com.andromeda.muteq.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.muteq.Entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
