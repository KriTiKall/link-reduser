package com.example.link.reduce.model.interfaces;

import com.example.link.reduce.data.entity.ShortEntity;
import com.example.link.reduce.model.ShortLink;

public interface IReduceLink {
    ShortEntity reduce(String link);
}
