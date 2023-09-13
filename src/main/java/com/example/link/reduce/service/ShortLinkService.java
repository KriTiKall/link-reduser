package com.example.link.reduce.service;

import com.example.link.reduce.data.entity.LinkEntity;
import com.example.link.reduce.data.entity.ShortEntity;
import com.example.link.reduce.data.entity.StatisticEntity;
import com.example.link.reduce.data.repository.LinkRepository;
import com.example.link.reduce.data.repository.ShortRepository;
import com.example.link.reduce.data.repository.StatRepository;
import com.example.link.reduce.data.repository.UserRepository;
import com.example.link.reduce.model.ShortLink;
import com.example.link.reduce.model.interfaces.IReduceLink;
import com.example.link.reduce.model.interfaces.IShortLinkService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ShortLinkService implements IShortLinkService {
    @Autowired
    private IReduceLink reducer;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ShortRepository shortRepository;
    @Autowired
    private StatRepository statRepository;
    @Autowired
    private UserRepository userRepository;

    private final Pattern pattern = Pattern.compile("^(?:https?:\\/\\/)?(?:[^@\\/\\n]+@)?(?:www\\.)?([^:\\/\\n]+)");


    private String getSiteName(String url) {
        val matcher = pattern.matcher(url);
        String result = "";

        while (matcher.find()) {
            result = matcher.group(1);
        }

        return result;
    }

    @Override
    public ShortLink createShortLink(String link, String name, String userLogin) {
        val shortLink = buildLink(link, userLogin);

        shortLink.setName(name);
        linkRepository.save(shortLink);
        return map(shortLink);
    }

    @Override
    public ShortLink createShortLink(String link, String userLogin) {
        val shortLink = buildLink(link, userLogin);

        shortLink.setName(shortLink.getSite() + (linkRepository.countByUserId(shortLink.getUserId()) + 1));
        linkRepository.save(shortLink);

        return map(shortLink);
    }

    private LinkEntity buildLink(String link, String userLogin) {

        val user = userRepository.findByLogin(userLogin);
        val shortLink = new LinkEntity();

        if (user.isEmpty())
            throw new RuntimeException("Invalid user(ShortService.build).");

        shortLink.setShortEntity(createAndSaveShort(link));
        shortLink.setUserId(user.get().getId());
        shortLink.setLink(link);
        shortLink.setSite(getSiteName(link));

        return shortLink;
    }

    private ShortEntity createAndSaveShort(String link) {
        val value = reducer.reduce(link);
        shortRepository.save(value);
        return value;
    }

    @Override
    public List<ShortLink> getAllLinkOfUser(String userLogin) {
        val user = userRepository.findByLogin(userLogin);
        if (user.isEmpty())
            throw new RuntimeException("Invalid user(ShortService.getAllLinkOfUser).");
        return linkRepository.findByUserId(user.get().getId()).stream()
                .map(this::map)
                .toList();
    }

    private ShortLink map(LinkEntity entity) {
        return new ShortLink(
                entity.getName(),
                entity.getSite(),
                entity.getLink(),
                entity.getShortEntity().getShortLink()
        );
    }

    @Override
    public ShortLink getLinkByName(String linkName, String userLogin) {
        return map(findLinkByName(linkName, userLogin));
    }

    private LinkEntity findLinkByName(String linkName, String userLogin) {
        val user = userRepository.findByLogin(userLogin);
        return linkRepository.findByUserIdAndName(
                user.get().getId(),
                linkName
        );
    }

    @Override
    public void deleteShortLinkByName(String name, String userLogin) {
        val user = userRepository.findByLogin(userLogin);
        val link = linkRepository.findByUserIdAndName(user.get().getId(), name);

        statRepository.deleteByLinkEntity(link);
        linkRepository.deleteByUserIdAndName(user.get().getId(), name);
        // todo solve this problem (short don't deleting)
//        shortRepository.deleteById(link.getShortEntity().getId());
    }

    @Override
    public List<ShortLink> getAllLink() {
        return linkRepository.findAll().stream()
                .map(this::map)
                .toList();
    }

    @Override
    public void onclick(String shortName, String login) {
        val stat = new StatisticEntity();

        stat.setClickTime(LocalDateTime.now());
        stat.setLinkEntity(findLinkByName(shortName, login));

        statRepository.save(stat);
    }
}
