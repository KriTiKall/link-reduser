package com.example.link.reduce.model.interfaces;

import com.example.link.reduce.model.dto.ShortLink;

import java.util.List;

public interface IShortLinkService {

    ShortLink createShortLink(String link, String userLogin);

    ShortLink createShortLink(String link, String name, String userLogin);

    List<ShortLink> getAllLinkOfUser(String userLogin);

    ShortLink getLinkByName(String name, String userLogin);

    void deleteShortLinkByName(String name, String userLogin);

    List<ShortLink> getAllLink();

    void onclick(String shortName, String login);
}
