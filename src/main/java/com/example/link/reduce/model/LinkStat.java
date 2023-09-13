package com.example.link.reduce.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Store statistics about link

public class LinkStat {

    private final ShortLink link;
    private final List<LocalDateTime> clicksTime;

    public LinkStat(ShortLink link) {
        this.link = link;
        clicksTime = new ArrayList<>();
    }

    public void clickOnLink() {
        clicksTime.add(LocalDateTime.now());
    }

    public long getClickCount() {
        return clicksTime.size();
    }
}
