package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"id"})
public class CranPackage {

    @Id
    private String id;
    private String name;
    private String version;
    private Date date;
    private String title;
    private String description;
    private List<Contact> authors;
    private List<Contact> maintainers;

    public CranPackage() {
    }

    public CranPackage(String name) {
        this.name = name;
    }

    public CranPackage(String name, String version, Date date, String title, String description, List<Contact> authors, List<Contact> maintainers) {
        this.name = name;
        this.version = version;
        this.date = date;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.maintainers = maintainers;
    }

    // Lombok adds the getters, setters and toString

}
