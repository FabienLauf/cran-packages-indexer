package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contact {
    private String name;
    private String email;

    public Contact() {
    }

    public Contact(String name) {
        this.name = name;
    }

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Lombok adds the getters, setters and toString
}
