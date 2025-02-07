package com.andromeda.muteq.DTO;

public class ItemDTO {
    private Long id;

    private String name;
    private String manufacturer;
    private String description;
    private Integer year;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public ItemDTO() {}
    public ItemDTO(String name, String manufacturer, String description, Integer year) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.year = year;
    }
}
