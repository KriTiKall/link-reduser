package com.example.link.reduce.model;

// Store statistics about link

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkStat {

    private Integer number = 0;

    private String name;
    private String site;
    private String link;
    private Long count;
}
