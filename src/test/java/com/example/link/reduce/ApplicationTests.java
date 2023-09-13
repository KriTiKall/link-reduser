package com.example.link.reduce;

import com.example.link.reduce.data.entity.LinkEntity;
import com.example.link.reduce.data.entity.UserEntity;
import com.example.link.reduce.data.repository.LinkRepository;
import com.example.link.reduce.data.repository.ShortRepository;
import com.example.link.reduce.data.repository.StatRepository;
import com.example.link.reduce.model.interfaces.IReduceLink;
import com.example.link.reduce.service.UserService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserService service;
    @Autowired
    private ShortRepository shortRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private StatRepository statRepository;
    @Autowired
    private IReduceLink reducer;

    @Test
    public void saveDataTest() {

        val login = "lid";
        val user = new UserEntity();
        if (!service.existsByUserLogin(login)) {

            user.setLogin(login);
            user.setName("Jeff");
            user.setEmail("Jeff@email.com");
            user.setPassword("pass");
            service.register(user);

        } else {
            user.setLogin(login);
        }


        assertEquals(service.getUserByLogin(login).getLogin(), user.getLogin());
    }

    @Test
    public void shortTest() {
        val value = reducer.reduce("https://translate.yandex.ru/?source_lang=en&target_lang=ru&text=user%20authorize");
        value.setId(shortRepository.save(value).getId());

        val temp = shortRepository.findById(value.getId());

        assertEquals(value.getShortLink(), temp.get().getShortLink());

        shortRepository.deleteById(value.getId());
    }

    @Test
    public void linkTest() {

        assertEquals(0, linkRepository.countByUserId(1l));

        assertEquals(0, linkRepository.findByUserId(1l).size());
    }

    @Test
    public void linkTest1() {
        LinkEntity entity = linkRepository.findByUserIdAndName(1l, "docs.spring.io2");

        assertNotNull(entity);
    }

    @Test
    public void linkDelete() {
        val id = 1l;
        val name = "docs.spring.io4";

        statRepository.deleteByLinkEntity(linkRepository.findByUserIdAndName(id, name));

        linkRepository.deleteByUserIdAndName(id, name);

        assertNull(linkRepository.findByUserIdAndName(id, name));
    }
}
