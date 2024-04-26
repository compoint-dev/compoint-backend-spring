package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageDTO {
    private String name;

    public LanguageDTO(String name) {
        this.name = name;
    }
}
