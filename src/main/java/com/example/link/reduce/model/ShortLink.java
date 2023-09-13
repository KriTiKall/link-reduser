package com.example.link.reduce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortLink {

    private String name;
    private String site;
    private String link;
    private String code;

}
