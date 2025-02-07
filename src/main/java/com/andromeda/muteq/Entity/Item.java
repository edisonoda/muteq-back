package com.andromeda.muteq.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "description")
    private String description;

    @Column(name = "year")
    private Integer year;

    public Item() {}
    public Item(String name, String manufacturer, String description, Integer year) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.year = year;
    }
}
