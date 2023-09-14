package com.example.link.reduce.controller.stat;

import com.example.link.reduce.controller.link.dto.LinkRequest;
import com.example.link.reduce.model.LinkStat;
import com.example.link.reduce.model.ShortLink;
import com.example.link.reduce.model.interfaces.IShortLinkService;
import com.example.link.reduce.model.interfaces.IStatService;
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
@RequestMapping("/stat")
public class ShortLinkController {

    @Autowired
    private IStatService service;


    @GetMapping("/top")
    public List<LinkStat> getTopOfLink() {
        val login = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getTopOfLink(login);
    }

}
