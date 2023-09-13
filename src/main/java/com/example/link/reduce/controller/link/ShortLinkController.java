package com.example.link.reduce.controller.link;

import com.example.link.reduce.controller.link.dto.LinkRequest;
import com.example.link.reduce.model.ShortLink;
import com.example.link.reduce.model.interfaces.IShortLinkService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/")
public class ShortLinkController {

    @Autowired
    private IShortLinkService service;


    @GetMapping("/")
    public ResponseEntity<String> defaultURL() {
        return new ResponseEntity<>("Wrong path...", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/add")
    public ResponseEntity<String> authenticateUser(@RequestBody LinkRequest request, HttpServletRequest requestHTTP) {
        val login = SecurityContextHolder.getContext().getAuthentication().getName();

        String code;
        //todo добавить проверку на одинаковые имена
        if (request.getName().isBlank())
            code = service.createShortLink(request.getLink(), login).getCode();
        else
            code = service.createShortLink(request.getLink(), request.getName(), login).getCode();


        return new ResponseEntity<>(buildShortLink(code, requestHTTP), HttpStatus.OK);
    }

    private String buildShortLink(String code, HttpServletRequest requestHTTP) {
        return new String(
                new StringBuilder("http://")
                        .append(requestHTTP.getLocalAddr())
                        .append(":")
                        .append(requestHTTP.getLocalPort())
                        .append("/")
                        .append(code)
        );
    }


    @GetMapping("/api/links")
    public List<ShortLink> getLinksOfUser(HttpServletRequest request) {
        val login = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return service.getAllLinkOfUser(login).stream()
                .peek(it -> it.setCode(buildShortLink(it.getCode(), request)))
                .toList();
    }

    @GetMapping("/api/link")
    public ShortLink getLinkByName(@RequestParam String name, HttpServletRequest request) {
        val login = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        ShortLink link;

        try {
            link = service.getLinkByName(name, login);
        } catch (NullPointerException ex) {
            return new ShortLink();
        }

        link.setCode(buildShortLink(link.getCode(), request));
        return link;
    }

    @DeleteMapping("/api/link")
    public ResponseEntity<String> deleteLink(@RequestParam String name) {
        val login = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        try {
            service.deleteShortLinkByName(name, login);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (NullPointerException ex) {
            return new ResponseEntity<>("Wrong name", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{code}")
    public RedirectView redirectWithUsingRedirectView(@PathVariable("code") String code, RedirectAttributes attributes) {
        val login = SecurityContextHolder.getContext().getAuthentication().getName();
        val list = service.getAllLink();
        String url = "/";

        for (ShortLink link : list)
            if (link.getCode().equals(code)) {
                service.onclick(link.getName(), login);
                url = link.getLink();
                break;
            }

        return new RedirectView(url);
    }
}
