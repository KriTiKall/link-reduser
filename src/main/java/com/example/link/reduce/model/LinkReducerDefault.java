package com.example.link.reduce.model;

import com.example.link.reduce.data.entity.ShortEntity;
import com.example.link.reduce.model.interfaces.IReduceLink;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LinkReducerDefault implements IReduceLink {
    @Override
    public ShortEntity reduce(String link) {
        val ref = new ShortEntity();

        ref.setShortLink(UUID.randomUUID()
                .toString()
                .substring(0, 8));

        return ref;
    }
}
